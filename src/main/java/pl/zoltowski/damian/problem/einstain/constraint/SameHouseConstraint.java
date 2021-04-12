package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.Constraint;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinVariable;

import java.util.Map;

public class SameHouseConstraint extends Constraint<EinsteinVariable, EinsteinDomain> {

    public SameHouseConstraint(EinsteinVariable variable1, EinsteinVariable variable2) {
        super();
        this.variables.add(variable1);
        this.variables.add(variable2);
    }

    @Override
    public boolean satisfied(Map<EinsteinVariable, EinsteinDomain> assigment) {
        if(!containsAllKeys(assigment)) {
            return true;
        }

        return assigment.get(this.variables.get(0)) == assigment.get(this.variables.get(1));
    }
}
