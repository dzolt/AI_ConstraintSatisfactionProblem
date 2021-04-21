package pl.zoltowski.damian;

import pl.zoltowski.damian.utils.dataType.Arc;
import pl.zoltowski.damian.utils.dataType.Constraint;
import pl.zoltowski.damian.utils.dataType.DomainHeuristic;
import pl.zoltowski.damian.utils.dataType.Tuple;
import pl.zoltowski.damian.utils.dataType.VariableHeuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class SearchTool<V, D> {

    private List<V> variables;
    private Map<V, List<D>> domains;
    private Map<V, List<Constraint<V, D>>> constraints;
    private VariableHeuristic variableHeuristic;
    private DomainHeuristic domainHeuristic;
    private int numberOfVisitedVertexes;

    public SearchTool(List<V> variables, Map<V, List<D>> domains, VariableHeuristic variableHeuristic, DomainHeuristic domainHeuristic) {
        this.variables = variables;
        this.domains = domains;
        this.constraints = new HashMap<>();

        for (V variable : variables) {
            this.constraints.put(variable, new LinkedList<>());

            if (!domains.containsKey(variable))
                throw new IllegalArgumentException("Each variable has to have domain");
        }
        this.variableHeuristic = variableHeuristic;
        this.domainHeuristic = domainHeuristic;
        this.numberOfVisitedVertexes = 0;
    }

    public void addConstraint(Constraint<V, D> constraint) {
        for (V variable : constraint.getVariables()) {
            if (!this.variables.contains(variable))
                throw new IllegalArgumentException("Variable form constraint not in Problem");
            else
                this.constraints.get(variable).add(constraint);
        }
    }

    private boolean consistent(V variable, Map<V, D> assigment) {
        for (Constraint<V, D> constraint : this.constraints.get(variable)) {
            if (!constraint.satisfied(assigment))
                return false;
        }
        return true;
    }

    private V getFirstUnassigned(Map<V, D> assigment) {
        for (V variable : this.variables) {
            if (!assigment.containsKey(variable))
                return variable;
        }
        return null;
    }

    private V getFirstUnassignedWithLeastDomain(Map<V, D> assigment) {
        V variableWithLeastDomain = null;
        for(V variable: this.variables) {
            if(!assigment.containsKey(variable)) {
                if(variableWithLeastDomain == null) {
                    variableWithLeastDomain = variable;
                }
                if(this.domains.get(variable).size() < this.domains.get(variableWithLeastDomain).size()) {
                    variableWithLeastDomain = variable;
                }
            }

        }
        return variableWithLeastDomain;
    }

    public List<Map<V, D>> backtrackingSearch() {
        return backtrackingSearch(new HashMap<>());
    }

    private List<Map<V, D>> backtrackingSearch(Map<V, D> assigment) {

        List<Map<V, D>> results = new LinkedList<>();

        if (assigment.size() == variables.size()) {
            results.add(assigment);
            return results;
        }

        V unassigned;

        if (this.variableHeuristic == VariableHeuristic.MRV) {
            unassigned = getFirstUnassignedWithLeastDomain(assigment);
        } else {
            unassigned = getFirstUnassigned(assigment);
        }

        for (D value : orderDomainValues(unassigned, this.domains.get(unassigned), assigment)) {
            this.numberOfVisitedVertexes++;
            Map<V, D> assigment_copy = new HashMap<>(assigment);
            assigment_copy.put(unassigned, value);

            if (consistent(unassigned, assigment_copy)) {
                List<Map<V, D>> result = backtrackingSearch(assigment_copy);

                if (!result.isEmpty())
                    results.addAll(result);
            }
        }

        return results;
    }


    private Iterable<? extends D> orderDomainValues(V variable, List<D> domains, Map<V, D> assigment) {
        List<D> sortedDomain = new ArrayList<>();
        List<Tuple<D,Integer>> conflicts = new ArrayList<>();

        if (this.domainHeuristic == DomainHeuristic.NONE) {
            return domains;
        }
        else if(this.domainHeuristic == DomainHeuristic.REVERSE) {
            Collections.reverse(domains);
            return domains;
        } else {
            for(D value: domains) {
                conflicts.add(new Tuple<>(value, calculateConflicts(variable, value, assigment)));
            }
            conflicts.sort(Comparator.comparing(Tuple::getSecond));
            for(Tuple<D, Integer> domainValue: conflicts) {
                sortedDomain.add(domainValue.getFirst());
            }
            return sortedDomain;
        }
    }

    private Integer calculateConflicts(V variable, D value, Map<V,D> assigment) {
        Integer numberOfConflicts = 0;
        Map<V, D> assigmentCopy = new HashMap<>(assigment);
        assigmentCopy.put(variable, value);
        for(Constraint<V, D> constraint: this.constraints.get(variable)) {
            if(!constraint.satisfied(assigmentCopy)) {
                numberOfConflicts++;
            }
        }
        assigmentCopy.remove(variable);
        return numberOfConflicts;
    }

    private List<Map<V, D>> forwardCheckingSearch(Map<V, D> assignment, Map<V, List<D>> domains) {
        List<Map<V, D>> results = new LinkedList<>();

        if (assignment.size() == variables.size()) {
            results.add(assignment);
            return results;
        }

        V unassignedVariable = getFirstUnassigned(assignment);

        for (D possibleAssignment : domains.get(unassignedVariable)) {
            this.numberOfVisitedVertexes++;
            Map<V, D> assignmentCopy = new HashMap<>(assignment);
            assignmentCopy.put(unassignedVariable, possibleAssignment);

            Map<V, List<D>> domainsCopy = copyDomains(domains);
            domainsCopy.put(unassignedVariable, new ArrayList<>(Collections.singletonList(possibleAssignment)));
            pruneDomains(domainsCopy, unassignedVariable, possibleAssignment);

            if (!domainWipeOut(domains)) {
                List<Map<V, D>> result = forwardCheckingSearch(assignmentCopy, domainsCopy);
                if (!result.isEmpty()) {
                    results.addAll(result);
                }
            }
        }
        return results;
    }

    public List<Map<V, D>> forwardCheckingSearch() {
        return forwardCheckingSearch(new HashMap<>(), copyDomains(this.domains));
    }

    public Map<V, List<D>> copyDomains(Map<V, List<D>> domains) {
        Map<V, List<D>> newDomains = new HashMap<>();

        for (Map.Entry<V, List<D>> entry : domains.entrySet()) {
            newDomains.put(entry.getKey(), new ArrayList<>(entry.getValue()));

        }
        return newDomains;
    }

    private boolean domainWipeOut(Map<V, List<D>> domainsCopy) {
        for (List<D> domain : domainsCopy.values()) {
            if (domain.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void pruneDomains(Map<V, List<D>> domains, V variable, D assignedValue) {
        for (Constraint<V, D> constraint : constraints.get(variable)) {
            constraint.removeNotSatisfyingValues(domains, variable, assignedValue);
        }
    }

    public List<Map<V, D>> runAC3BackTracking() {
        this.ac3();
        return this.backtrackingSearch();
    }

    public List<Map<V, D>> runAC3ForwardCheck() {
        this.ac3();
        return this.forwardCheckingSearch();
    }

    public void ac3() {
        Map<V, Set<Arc<V, D>>> allArcs = new HashMap<>();

        for (V variable : this.variables) {
            allArcs.put(variable, new HashSet<>());
        }

        Queue<Arc<V, D>> queue = new LinkedList<>();

        for (Map.Entry<V, List<Constraint<V, D>>> entry : this.constraints.entrySet()) {
            for (Constraint<V, D> constraint : entry.getValue()) {
                for (Arc<V, D> arc : constraint.getArcs()) {
                    if (allArcs.get(arc.getVariable2()).add(arc)) {
                        queue.add(arc);
                    }
                }
            }
        }

        while (!queue.isEmpty()) {
            Arc<V, D> currentArc = queue.poll();
            boolean domainChanged = false;

            Iterator<D> domainIterator = this.domains.get(currentArc.getVariable1()).iterator();

            while (domainIterator.hasNext()) {
                D value = domainIterator.next();
                if (!currentArc.isConstraintSatisfied(value, this.domains.get(currentArc.getVariable2()))) {
                    domainIterator.remove();
                    domainChanged = true;
                }
            }

            if (domainChanged) {
                for (Arc<V, D> arc : allArcs.get(currentArc.getVariable1())) {
                    if (!queue.contains(arc)) {
                        queue.add(arc);
                    }
                }
            }
        }
    }

    public int getNumberOfVisitedVertexes() {
        return this.numberOfVisitedVertexes;
    }
}
