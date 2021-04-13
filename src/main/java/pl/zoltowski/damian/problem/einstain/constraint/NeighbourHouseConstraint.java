package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.utils.dataType.Arc;
import pl.zoltowski.damian.utils.dataType.Constraint;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NeighbourHouseConstraint extends Constraint<EinsteinVariable, EinsteinDomain> {

    public NeighbourHouseConstraint(EinsteinVariable variable1, EinsteinVariable variable2){
        super();
        this.variables.add(variable1);
        this.variables.add(variable2);
    }

    @Override
    public boolean satisfied(Map<EinsteinVariable, EinsteinDomain> assigment) {
        if(!containsAllKeys(assigment)) {
            return true;
        }

        return assigment.get(this.variables.get(0)).getNumber() + 1 == assigment.get(this.variables.get(1)).getNumber() ||
               assigment.get(this.variables.get(0)).getNumber() - 1 == assigment.get(this.variables.get(1)).getNumber();
    }

    @Override
    public void removeNotSatisfyingValues(Map<EinsteinVariable, List<EinsteinDomain>> domains, EinsteinVariable variable, EinsteinDomain assignedValue) {
        EinsteinVariable variableToRemove;

        if(this.variables.get(0).equals(variable)) {
            variableToRemove = this.variables.get(1);
        } else {
            variableToRemove = this.variables.get(0);
        }

        List<EinsteinDomain> domain = domains.get(variableToRemove);

        domain.removeIf(d -> d.getNumber() != assignedValue.getNumber() + 1 && d.getNumber() != assignedValue.getNumber() - 1);
    }

    @Override
    public List<Arc<EinsteinVariable, EinsteinDomain>> getArcs() {
        List<Arc<EinsteinVariable, EinsteinDomain>> arcs = new ArrayList<>(2);

        arcs.add(new NeighbourHouseConstraintArc(this.variables.get(0), this.variables.get(1)));
        arcs.add(new NeighbourHouseConstraintArc(this.variables.get(1), this.variables.get(0)));

        return arcs;
    }
}
