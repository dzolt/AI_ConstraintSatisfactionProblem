package pl.zoltowski.damian.utils.dataType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Constraint<V, D> {
    protected List<V> variables;

    public Constraint() {
        variables = new LinkedList<>();
    }

    protected boolean containsAllKeys(Map<V, D> assigment) {
        for (V variable : this.variables) {
            if (!assigment.containsKey(variable))
                return false;
        }

        return true;
    }

    public abstract boolean satisfied(Map<V, D> assigment);

    public abstract void removeNotSatisfyingValues(Map<V, List<D>> domains, V variable, D value);

    public List<V> getVariables() {
        return variables;
    }

    public abstract List<Arc<V,D>> getArcs();
}
