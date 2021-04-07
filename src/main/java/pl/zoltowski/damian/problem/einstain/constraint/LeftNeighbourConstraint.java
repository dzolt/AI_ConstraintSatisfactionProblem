package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.Constraint;
import pl.zoltowski.damian.problem.einstain.domain.Domain;
import pl.zoltowski.damian.problem.einstain.domain.Variable;

import java.util.Map;

public class LeftNeighbourConstraint extends Constraint<Variable, Domain> {

    public LeftNeighbourConstraint(Variable variable1, Variable variable2) {
        super();
        this.variables.add(variable1);
        this.variables.add(variable2);
    }

    @Override
    public boolean satisfied(Map<Variable, Domain> assigment) {
        if(!containsAllKeys(assigment)) {
            return true;
        }
        return assigment.get(this.variables.get(0)).getNumber() + 1 == assigment.get(this.variables.get(1)).getNumber();
    }
}
