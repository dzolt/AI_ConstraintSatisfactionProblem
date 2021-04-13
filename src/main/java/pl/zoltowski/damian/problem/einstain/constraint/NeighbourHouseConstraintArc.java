package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinVariable;
import pl.zoltowski.damian.utils.dataType.Arc;

import java.util.List;

public class NeighbourHouseConstraintArc extends Arc<EinsteinVariable, EinsteinDomain> {
    public NeighbourHouseConstraintArc(EinsteinVariable variable1, EinsteinVariable variable2) {
        super(variable1, variable2);
    }

    @Override
    public boolean isConstraintSatisfied(EinsteinDomain valueToCheck, List<EinsteinDomain> domainOfRightVariable) {
        return domainOfRightVariable
          .stream()
          .map(EinsteinDomain::getNumber)
          .anyMatch(number -> number == valueToCheck.getNumber() + 1 || number == valueToCheck.getNumber() - 1);
    }
}
