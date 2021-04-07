package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.Constraint;
import pl.zoltowski.damian.problem.einstain.domain.Domain;
import pl.zoltowski.damian.problem.einstain.domain.Variable;

import java.util.Map;

public class SameHouseConstraint extends Constraint<Variable, Domain> {

    public SameHouseConstraint(Variable variable1, Variable variable2) {
        super();
        this.variables.add(variable1);
        this.variables.add(variable2);
    }

    @Override
    public boolean satisfied(Map<Variable, Domain> assigment) {
        if(!containsAllKeys(assigment)) {
            return true;
        }

        return assigment.get(this.variables.get(0)) == assigment.get(this.variables.get(1));
    }
}
