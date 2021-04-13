package pl.zoltowski.damian.utils.dataType;

import lombok.Data;

import java.util.List;

@Data
public abstract class Arc<V, D> {
    V variable1;
    V variable2;

    public Arc(V variable1, V variable2) {
        this.variable1 = variable1;
        this.variable2 = variable2;
    }

    public abstract boolean isConstraintSatisfied(D valueToCheck, List<D> domainOfRightVariable);
}
