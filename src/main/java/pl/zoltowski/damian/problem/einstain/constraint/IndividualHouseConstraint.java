package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.Constraint;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinVariable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public class IndividualHouseConstraint extends Constraint<EinsteinVariable, EinsteinDomain> {

    public IndividualHouseConstraint(EinsteinVariable... variables) {
        super();
        this.variables.addAll(Arrays.asList(variables));
    }

    @Override
    public boolean satisfied(Map<EinsteinVariable, EinsteinDomain> assigment) {
        HashSet<EinsteinDomain> used = new HashSet<>();

        for(EinsteinVariable v: this.variables) {
            if(assigment.containsKey(v) && used.contains(assigment.get(v))) {
                return false;
            }
            used.add(assigment.get(v));
        }
        return true;
    }


}
