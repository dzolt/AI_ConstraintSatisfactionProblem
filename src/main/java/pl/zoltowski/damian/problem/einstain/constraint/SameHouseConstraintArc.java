package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinVariable;
import pl.zoltowski.damian.utils.dataType.Arc;

import java.util.List;

public class SameHouseConstraintArc extends Arc<EinsteinVariable, EinsteinDomain> {
    public SameHouseConstraintArc(EinsteinVariable variable1, EinsteinVariable variable2) {
        super(variable1, variable2);
    }

    @Override
    public boolean isConstraintSatisfied(EinsteinDomain valueToCheck, List<EinsteinDomain> domainOfRightVariable) {
        return domainOfRightVariable.contains(valueToCheck);
    }
}
