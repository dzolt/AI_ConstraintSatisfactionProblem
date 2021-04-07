package pl.zoltowski.damian.problem.einstain;

import lombok.Data;
import pl.zoltowski.damian.Backtracking;
import pl.zoltowski.damian.problem.Problem;
import pl.zoltowski.damian.problem.einstain.constraint.IndividualHouseConstraint;
import pl.zoltowski.damian.problem.einstain.constraint.LeftNeighbourConstraint;
import pl.zoltowski.damian.problem.einstain.constraint.NeighbourHouseConstraint;
import pl.zoltowski.damian.problem.einstain.constraint.SameHouseConstraint;
import pl.zoltowski.damian.problem.einstain.domain.Domain;
import pl.zoltowski.damian.problem.einstain.domain.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EinsteinProblem implements Problem {
    private List<List<Integer>> solution;

    public EinsteinProblem() {
        this.solution = new ArrayList<>();
    }

    @Override
    public void run() {
        //create list of variables
        List<Variable> variables = new ArrayList<>(Arrays.asList(Variable.values()));
        //create map of domains
        Map<Variable, List<Domain>> domains = new HashMap<>();
        for(Variable v: variables) {
            domains.put(v, new ArrayList<>(Arrays.asList(Domain.values())));
        }
        //apply hint Norwegian lives in 1st house
        domains.put(Variable.NORWEGIAN, new ArrayList<>(Collections.singletonList(Domain.FIRST)));
        //apply hint middle house owner drinks milk
        domains.put(Variable.MILK, new ArrayList<>(Collections.singletonList(Domain.THIRD)));

        Backtracking<Variable, Domain> backtracking = new Backtracking<>(variables, domains);
        //apply rest of the hints
        applyConstraints(backtracking);

        List<Map<Variable, Domain>> result = backtracking.backtrackingSearch();

        printResult(result);

    }

    private void printResult(List<Map<Variable, Domain>> result) {
        for(Map<Variable, Domain> assignment: result) {
            if(assignment.containsKey(Variable.FISHES)) {
                System.out.println("FISHES ARE IN HOUSE: " + assignment.get(Variable.FISHES));
            }
        }
    }

    private void applyConstraints(Backtracking<Variable, Domain> backtracking) {
        applyIndividualHouseConstraints(backtracking);
        applySameHouseConstraints(backtracking);
        applyLeftNeighbourConstraints(backtracking);
        applyNeighbourHouseConstraints(backtracking);
    }

    private void applyIndividualHouseConstraints(Backtracking<Variable, Domain> backtracking) {
        backtracking.addConstraint(new IndividualHouseConstraint(Variable.NORWEGIAN,
                                                                 Variable.ENGLISHMAN,
                                                                 Variable.DANE,
                                                                 Variable.GERMAN,
                                                                 Variable.SWEDE));
        backtracking.addConstraint(new IndividualHouseConstraint(Variable.LIGHT,
                                                                 Variable.CIGAR,
                                                                 Variable.PIPE,
                                                                 Variable.NO_FILTER,
                                                                 Variable.MENTHOL));
        backtracking.addConstraint(new IndividualHouseConstraint(Variable.TEA,
                                                                 Variable.MILK,
                                                                 Variable.WATER,
                                                                 Variable.BEER,
                                                                 Variable.COFFEE));
        backtracking.addConstraint(new IndividualHouseConstraint(Variable.CATS,
                                                                 Variable.BIRDS,
                                                                 Variable.DOGS,
                                                                 Variable.HORSES,
                                                                 Variable.FISHES));
        backtracking.addConstraint(new IndividualHouseConstraint(Variable.RED,
                                                                 Variable.GREEN,
                                                                 Variable.WHITE,
                                                                 Variable.YELLOW,
                                                                 Variable.BLUE));

    }

    private void applySameHouseConstraints(Backtracking<Variable, Domain> backtracking) {
        backtracking.addConstraint(new SameHouseConstraint(Variable.ENGLISHMAN,
                                                           Variable.RED));
        backtracking.addConstraint(new SameHouseConstraint(Variable.DANE,
                                                           Variable.TEA));
        backtracking.addConstraint(new SameHouseConstraint(Variable.YELLOW,
                                                           Variable.CIGAR));
        backtracking.addConstraint(new SameHouseConstraint(Variable.GERMAN,
                                                           Variable.PIPE));
        backtracking.addConstraint(new SameHouseConstraint(Variable.NO_FILTER,
                                                           Variable.BIRDS));
        backtracking.addConstraint(new SameHouseConstraint(Variable.SWEDE,
                                                           Variable.DOGS));
        backtracking.addConstraint(new SameHouseConstraint(Variable.MENTHOL,
                                                           Variable.BEER));
        backtracking.addConstraint(new SameHouseConstraint(Variable.GREEN,
                                                           Variable.COFFEE));
    }

    private void applyLeftNeighbourConstraints(Backtracking<Variable, Domain> backtracking) {
        backtracking.addConstraint(new LeftNeighbourConstraint(Variable.GREEN, Variable.WHITE));
    }

    private void applyNeighbourHouseConstraints(Backtracking<Variable, Domain> backtracking) {
        backtracking.addConstraint(new NeighbourHouseConstraint(Variable.LIGHT,
                                                                Variable.CATS));
        backtracking.addConstraint(new NeighbourHouseConstraint(Variable.LIGHT,
                                                                Variable.WATER));
        backtracking.addConstraint(new NeighbourHouseConstraint(Variable.NORWEGIAN,
                                                                Variable.BLUE));
        backtracking.addConstraint(new NeighbourHouseConstraint(Variable.HORSES,
                                                                Variable.YELLOW));
    }
}
