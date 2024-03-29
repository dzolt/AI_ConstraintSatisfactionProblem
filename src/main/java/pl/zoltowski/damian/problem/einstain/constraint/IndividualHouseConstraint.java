package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.utils.dataType.Arc;
import pl.zoltowski.damian.utils.dataType.Constraint;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

    @Override
    public void removeNotSatisfyingValues(Map<EinsteinVariable, List<EinsteinDomain>> domains, EinsteinVariable variable, EinsteinDomain assignedValue) {
        for(EinsteinVariable constraintVariable: this.variables) {
            if(!constraintVariable.equals(variable)) {
                List<EinsteinDomain> domain = domains.get(constraintVariable);

                domain.remove(assignedValue);
            }
        }
    }

    @Override
    public List<Arc<EinsteinVariable, EinsteinDomain>> getArcs() {
        List<Arc<EinsteinVariable, EinsteinDomain>> arcs = new ArrayList<>();

        for(EinsteinVariable var1: this.variables) {
            for(EinsteinVariable var2: this.variables) {
                if(var1 != var2) {
                    arcs.add(new IndividualHouseConstraintArc(var1, var2));
                }
            }
        }

        return arcs;
    }
}
