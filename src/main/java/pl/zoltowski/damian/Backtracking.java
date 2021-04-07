package pl.zoltowski.damian;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Backtracking<V, D> {

    private List<V> variables;
    private Map<V, List<D>> domains;
    private Map<V, List<Constraint<V, D>>> constraints;

    public Backtracking(List<V> variables, Map<V, List<D>> domains) {
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
}
