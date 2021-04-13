package pl.zoltowski.damian;

import pl.zoltowski.damian.utils.dataType.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SearchTool<V, D> {

    private List<V> variables;
    private Map<V, List<D>> domains;
    private Map<V, List<Constraint<V, D>>> constraints;

    public SearchTool(List<V> variables, Map<V, List<D>> domains) {
        this.variables = variables;
        this.domains = domains;
        this.constraints = new HashMap<>();

        for (V variable : variables) {
            this.constraints.put(variable, new LinkedList<>());

            if(!domains.containsKey(variable))
                throw new IllegalArgumentException("Each variable has to have domain");
        }
    }

    public void addConstraint(Constraint<V, D> constraint) {
        for (V variable : constraint.getVariables()) {
            if(!this.variables.contains(variable))
                throw new IllegalArgumentException("Variable form constraint not in Problem");
            else
                this.constraints.get(variable).add(constraint);
        }
    }

    private boolean consistent(V variable, Map<V, D> assigment) {
        for (Constraint<V, D> constraint : this.constraints.get(variable)) {
            if(!constraint.satisfied(assigment))
                return false;
        }

        return true;
    }

    private V getFirstUnassigned(Map<V, D> assigment) {
        for (V variable : this.variables) {
            if(!assigment.containsKey(variable))
                return variable;
        }

        return null;
    }

    public List<Map<V, D>> backtrackingSearch() {
        return backtrackingSearch(new HashMap<>());
    }

    private List<Map<V, D>> backtrackingSearch(Map<V, D> assigment) {

        List<Map<V, D>> results = new LinkedList<>();

        if(assigment.size() == variables.size()) {
            results.add(assigment);
            return results;
        }

        V unassigned = getFirstUnassigned(assigment);

        for (D value : this.domains.get(unassigned)) {

            Map<V, D> assigment_copy = new HashMap<>(assigment);
            assigment_copy.put(unassigned, value);

            if(consistent(unassigned, assigment_copy)) {
                List<Map<V, D>> result = backtrackingSearch(assigment_copy);

                if(!result.isEmpty())
                    results.addAll(result);
            }
        }

        return results;
    }

    public List<Map<V,D>> forwardCheckingSearch() {
        return forwardCheckingSearch(new HashMap<>(), copyDomains(this.domains));
    }

    private List<Map<V,D>> forwardCheckingSearch(Map<V,D> assignment, Map<V, List<D>> domains) {
        List<Map<V, D>> results = new LinkedList<>();

        if(assignment.size() == variables.size()) {
            results.add(assignment);
            return results;
        }

        V unassignedVariable = getFirstUnassigned(assignment);

        for(D possibleAssignment: domains.get(unassignedVariable)) {
            Map<V, D> assignmentCopy = new HashMap<>(assignment);
            assignmentCopy.put(unassignedVariable, possibleAssignment);

            Map<V, List<D>> domainsCopy = copyDomains(domains);
            domainsCopy.put(unassignedVariable, new ArrayList<>(Collections.singletonList(possibleAssignment)));
            pruneDomains(domainsCopy, unassignedVariable, possibleAssignment);

            if(!domainWipeOut(domains)) {
                List<Map<V,D>>  result = forwardCheckingSearch(assignmentCopy, domainsCopy);
                if(!result.isEmpty()) {
                    results.addAll(result);
                }
            }
        }
        return results;
    }

    private Map<V, List<D>> copyDomains(Map<V, List<D>> domains) {
        Map<V, List<D>> newDomains = new HashMap<>();

        for(Map.Entry<V,List<D>> entry: domains.entrySet()) {
            newDomains.put(entry.getKey(), new ArrayList<>(entry.getValue()));

        }
        return newDomains;
    }

    private boolean domainWipeOut(Map<V, List<D>> domainsCopy) {
        for(List<D> domain: domainsCopy.values()) {
            if(domain.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void pruneDomains(Map<V, List<D>> domains, V variable, D assignedValue) {
        for(Constraint<V, D> constraint: constraints.get(variable)) {
            constraint.removeNotSatisfyingValues(domains, variable, assignedValue);
        }
    }
}
