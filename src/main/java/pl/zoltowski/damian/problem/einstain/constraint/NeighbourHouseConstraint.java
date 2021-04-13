package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.Constraint;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinVariable;

import java.util.Iterator;
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

        Iterator<EinsteinDomain> it = domain.iterator();

        while(it.hasNext()) {
            EinsteinDomain d = it.next();
            if(d.getNumber() != assignedValue.getNumber() + 1 && d.getNumber() != assignedValue.getNumber() - 1) {
                it.remove();
            }
        }
    }
}
