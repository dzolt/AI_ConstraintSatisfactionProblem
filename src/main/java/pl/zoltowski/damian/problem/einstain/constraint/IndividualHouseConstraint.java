package pl.zoltowski.damian.problem.einstain.constraint;

import pl.zoltowski.damian.Constraint;
import pl.zoltowski.damian.problem.einstain.domain.Domain;
import pl.zoltowski.damian.problem.einstain.domain.Variable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public class IndividualHouseConstraint extends Constraint<Variable, Domain> {

    public IndividualHouseConstraint(Variable... variables) {
        super();
        this.variables.addAll(Arrays.asList(variables));
    }

    @Override
    public boolean satisfied(Map<Variable, Domain> assigment) {
        HashSet<Domain> used = new HashSet<>();

        for(Variable v: this.variables) {
            if(assigment.containsKey(v) && used.contains(assigment.get(v))) {
                return false;
            }
            used.add(assigment.get(v));
        }
        return true;
    }


}
