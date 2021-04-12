package pl.zoltowski.damian.problem.einstain;

import lombok.Data;
import pl.zoltowski.damian.Backtracking;
import pl.zoltowski.damian.problem.Problem;
import pl.zoltowski.damian.problem.einstain.constraint.IndividualHouseConstraint;
import pl.zoltowski.damian.problem.einstain.constraint.LeftNeighbourConstraint;
import pl.zoltowski.damian.problem.einstain.constraint.NeighbourHouseConstraint;
import pl.zoltowski.damian.problem.einstain.constraint.SameHouseConstraint;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinVariable;

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
        List<EinsteinVariable> variables = new ArrayList<>(Arrays.asList(EinsteinVariable.values()));
        //create map of domains
        Map<EinsteinVariable, List<EinsteinDomain>> domains = new HashMap<>();
        for(EinsteinVariable v: variables) {
            domains.put(v, new ArrayList<>(Arrays.asList(EinsteinDomain.values())));
        }
        //apply hint Norwegian lives in 1st house
        domains.put(EinsteinVariable.NORWEGIAN, new ArrayList<>(Collections.singletonList(EinsteinDomain.FIRST)));
        //apply hint middle house owner drinks milk
        domains.put(EinsteinVariable.MILK, new ArrayList<>(Collections.singletonList(EinsteinDomain.THIRD)));

        Backtracking<EinsteinVariable, EinsteinDomain> backtracking = new Backtracking<>(variables, domains);
        //apply rest of the hints
        applyConstraints(backtracking);

        List<Map<EinsteinVariable, EinsteinDomain>> result = backtracking.backtrackingSearch();

        printResult(result);

    }

    private void printResult(List<Map<EinsteinVariable, EinsteinDomain>> result) {
        for(Map<EinsteinVariable, EinsteinDomain> assignment: result) {
            if(assignment.containsKey(EinsteinVariable.FISHES)) {
                System.out.println("FISHES ARE IN HOUSE: " + assignment.get(EinsteinVariable.FISHES));
            }
        }
    }

    private void applyConstraints(Backtracking<EinsteinVariable, EinsteinDomain> backtracking) {
        applyIndividualHouseConstraints(backtracking);
        applySameHouseConstraints(backtracking);
        applyLeftNeighbourConstraints(backtracking);
        applyNeighbourHouseConstraints(backtracking);
    }

    private void applyIndividualHouseConstraints(Backtracking<EinsteinVariable, EinsteinDomain> backtracking) {
        backtracking.addConstraint(new IndividualHouseConstraint(EinsteinVariable.NORWEGIAN,
                                                                 EinsteinVariable.ENGLISHMAN,
                                                                 EinsteinVariable.DANE,
                                                                 EinsteinVariable.GERMAN,
                                                                 EinsteinVariable.SWEDE));
        backtracking.addConstraint(new IndividualHouseConstraint(EinsteinVariable.LIGHT,
                                                                 EinsteinVariable.CIGAR,
                                                                 EinsteinVariable.PIPE,
                                                                 EinsteinVariable.NO_FILTER,
                                                                 EinsteinVariable.MENTHOL));
        backtracking.addConstraint(new IndividualHouseConstraint(EinsteinVariable.TEA,
                                                                 EinsteinVariable.MILK,
                                                                 EinsteinVariable.WATER,
                                                                 EinsteinVariable.BEER,
                                                                 EinsteinVariable.COFFEE));
        backtracking.addConstraint(new IndividualHouseConstraint(EinsteinVariable.CATS,
                                                                 EinsteinVariable.BIRDS,
                                                                 EinsteinVariable.DOGS,
                                                                 EinsteinVariable.HORSES,
                                                                 EinsteinVariable.FISHES));
        backtracking.addConstraint(new IndividualHouseConstraint(EinsteinVariable.RED,
                                                                 EinsteinVariable.GREEN,
                                                                 EinsteinVariable.WHITE,
                                                                 EinsteinVariable.YELLOW,
                                                                 EinsteinVariable.BLUE));

    }

    private void applySameHouseConstraints(Backtracking<EinsteinVariable, EinsteinDomain> backtracking) {
        backtracking.addConstraint(new SameHouseConstraint(EinsteinVariable.ENGLISHMAN,
                                                           EinsteinVariable.RED));
        backtracking.addConstraint(new SameHouseConstraint(EinsteinVariable.DANE,
                                                           EinsteinVariable.TEA));
        backtracking.addConstraint(new SameHouseConstraint(EinsteinVariable.YELLOW,
                                                           EinsteinVariable.CIGAR));
        backtracking.addConstraint(new SameHouseConstraint(EinsteinVariable.GERMAN,
                                                           EinsteinVariable.PIPE));
        backtracking.addConstraint(new SameHouseConstraint(EinsteinVariable.NO_FILTER,
                                                           EinsteinVariable.BIRDS));
        backtracking.addConstraint(new SameHouseConstraint(EinsteinVariable.SWEDE,
                                                           EinsteinVariable.DOGS));
        backtracking.addConstraint(new SameHouseConstraint(EinsteinVariable.MENTHOL,
                                                           EinsteinVariable.BEER));
        backtracking.addConstraint(new SameHouseConstraint(EinsteinVariable.GREEN,
                                                           EinsteinVariable.COFFEE));
    }

    private void applyLeftNeighbourConstraints(Backtracking<EinsteinVariable, EinsteinDomain> backtracking) {
        backtracking.addConstraint(new LeftNeighbourConstraint(EinsteinVariable.GREEN, EinsteinVariable.WHITE));
    }

    private void applyNeighbourHouseConstraints(Backtracking<EinsteinVariable, EinsteinDomain> backtracking) {
        backtracking.addConstraint(new NeighbourHouseConstraint(EinsteinVariable.LIGHT,
                                                                EinsteinVariable.CATS));
        backtracking.addConstraint(new NeighbourHouseConstraint(EinsteinVariable.LIGHT,
                                                                EinsteinVariable.WATER));
        backtracking.addConstraint(new NeighbourHouseConstraint(EinsteinVariable.NORWEGIAN,
                                                                EinsteinVariable.BLUE));
        backtracking.addConstraint(new NeighbourHouseConstraint(EinsteinVariable.HORSES,
                                                                EinsteinVariable.YELLOW));
    }
}
