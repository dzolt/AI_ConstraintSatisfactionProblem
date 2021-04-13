package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.utils.dataType.Arc;
import pl.zoltowski.damian.utils.dataType.Constraint;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinVariable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LeftNeighbourConstraint extends Constraint<EinsteinVariable, EinsteinDomain> {

    public LeftNeighbourConstraint(EinsteinVariable variable1, EinsteinVariable variable2) {
        super();
        this.variables.add(variable1);
        this.variables.add(variable2);
    }

    @Override
    public boolean satisfied(Map<EinsteinVariable, EinsteinDomain> assigment) {
        if(!containsAllKeys(assigment)) {
            return true;
        }
        return assigment.get(this.variables.get(0)).getNumber() + 1 == assigment.get(this.variables.get(1)).getNumber();
    }

    @Override
    public void removeNotSatisfyingValues(Map<EinsteinVariable, List<EinsteinDomain>> domains, EinsteinVariable variable, EinsteinDomain assignmentValue) {
        //reduce domain to one element respectively
        EinsteinVariable variableToDelete;
        boolean isLeftVariable;
        int valueToGetNextOrPreviousDomainValue = 0;
        if(this.variables.get(0).equals(variable)) {
            isLeftVariable = true;
            variableToDelete = this.variables.get(1);
        } else {
            isLeftVariable = false;
            variableToDelete = this.variables.get(0);
        }

        List<EinsteinDomain> domain = domains.get(variableToDelete);
        //iterate over domain and delete every domain variable except one that suits
        Iterator<EinsteinDomain> it = domain.iterator();

        if(isLeftVariable) {
            valueToGetNextOrPreviousDomainValue = 1;
        } else {
            valueToGetNextOrPreviousDomainValue = -1;
        }

        while(it.hasNext()) {
            EinsteinDomain next = it.next();
            if(next.getNumber() != assignmentValue.getNumber() + valueToGetNextOrPreviousDomainValue) {
                it.remove();
            }
        }
    }

    @Override
    public List<Arc<EinsteinVariable, EinsteinDomain>> getArcs() {
        List<Arc<EinsteinVariable, EinsteinDomain>> arcs = new ArrayList<>(2);

        arcs.add(new LeftNeighbourConstraintArc(this.variables.get(0), this.variables.get(1)));
        arcs.add(new RightNeighbourConstraintArc(this.variables.get(1), this.variables.get(0)));

        return arcs;
    }
}
