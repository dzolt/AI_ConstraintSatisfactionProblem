package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.utils.dataType.Arc;
import pl.zoltowski.damian.utils.dataType.Constraint;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    @Override
    public void removeNotSatisfyingValues(Map<EinsteinVariable, List<EinsteinDomain>> domains, EinsteinVariable variable, EinsteinDomain assignedValue) {
        EinsteinVariable variableToRemove;

        if(this.variables.get(0).equals(variable)) {
            variableToRemove = this.variables.get(1);
        } else {
            variableToRemove = this.variables.get(0);
        }

        domains.put(variableToRemove, Collections.singletonList(assignedValue));
//        List<EinsteinDomain> domain = domains.get(variableToRemove);
        // or iterator and remove everything except assignedValue
    }

    @Override
    public List<Arc<EinsteinVariable, EinsteinDomain>> getArcs() {
        List<Arc<EinsteinVariable, EinsteinDomain>> arcs = new ArrayList<>(2);

        arcs.add(new SameHouseConstraintArc(this.variables.get(0), this.variables.get(1)));
        arcs.add(new SameHouseConstraintArc(this.variables.get(1), this.variables.get(0)));

        return arcs;
    }
}
