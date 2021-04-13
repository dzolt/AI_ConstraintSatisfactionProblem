package pl.zoltowski.damian.problem.einstain;

import lombok.Data;
import pl.zoltowski.damian.SearchTool;
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
    private boolean runForwardCheck;

    public EinsteinProblem(boolean runForwardCheck) {
        this.solution = new ArrayList<>();
        this.runForwardCheck = runForwardCheck;
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

        SearchTool<EinsteinVariable, EinsteinDomain> searchTool = new SearchTool<>(variables, domains);
        //apply rest of the hints
        applyConstraints(searchTool);

        if(this.runForwardCheck) {
            runForwardCheck(searchTool);
        } else {
            runBacktrack(searchTool);
        }
    }

    private void runBacktrack(SearchTool<EinsteinVariable, EinsteinDomain> searchTool) {
        long startTime = System.nanoTime();
        List<Map<EinsteinVariable, EinsteinDomain>> result = searchTool.backtrackingSearch();
        long endTime = System.nanoTime();
        long timeTotal = endTime - startTime;
        printResult(result);
        System.out.println("BACKTRACKING TIME: " + timeTotal + "ns");
    }

    private void runForwardCheck(SearchTool<EinsteinVariable, EinsteinDomain> searchTool) {
        long startTime = System.nanoTime();
        long endTime = System.nanoTime();
        long timeTotal = endTime - startTime;
        List<Map<EinsteinVariable, EinsteinDomain>> result = searchTool.forwardCheckingSearch();
        printResult(result);
        System.out.println("FORWARD SEARCH TIME: " + timeTotal + "ns");
    }

    private void printResult(List<Map<EinsteinVariable, EinsteinDomain>> result) {
        if (result.isEmpty()) {
            System.out.println("THIS PROBLEM CANNOT BE SOLVED WITH GIVEN CONSTRAINTS");
            return;
        }
        for(Map<EinsteinVariable, EinsteinDomain> assignment: result) {
            if(assignment.containsKey(EinsteinVariable.FISHES)) {
                System.out.println("FISHES ARE IN HOUSE: " + assignment.get(EinsteinVariable.FISHES));
            }
        }
    }

    private void applyConstraints(SearchTool<EinsteinVariable, EinsteinDomain> searchTool) {
        applyIndividualHouseConstraints(searchTool);
        applySameHouseConstraints(searchTool);
        applyLeftNeighbourConstraints(searchTool);
        applyNeighbourHouseConstraints(searchTool);
    }

    private void applyIndividualHouseConstraints(SearchTool<EinsteinVariable, EinsteinDomain> searchTool) {
        searchTool.addConstraint(new IndividualHouseConstraint(EinsteinVariable.NORWEGIAN,
                                                               EinsteinVariable.ENGLISHMAN,
                                                               EinsteinVariable.DANE,
                                                               EinsteinVariable.GERMAN,
                                                               EinsteinVariable.SWEDE));
        searchTool.addConstraint(new IndividualHouseConstraint(EinsteinVariable.LIGHT,
                                                               EinsteinVariable.CIGAR,
                                                               EinsteinVariable.PIPE,
                                                               EinsteinVariable.NO_FILTER,
                                                               EinsteinVariable.MENTHOL));
        searchTool.addConstraint(new IndividualHouseConstraint(EinsteinVariable.TEA,
                                                               EinsteinVariable.MILK,
                                                               EinsteinVariable.WATER,
                                                               EinsteinVariable.BEER,
                                                               EinsteinVariable.COFFEE));
        searchTool.addConstraint(new IndividualHouseConstraint(EinsteinVariable.CATS,
                                                               EinsteinVariable.BIRDS,
                                                               EinsteinVariable.DOGS,
                                                               EinsteinVariable.HORSES,
                                                               EinsteinVariable.FISHES));
        searchTool.addConstraint(new IndividualHouseConstraint(EinsteinVariable.RED,
                                                               EinsteinVariable.GREEN,
                                                               EinsteinVariable.WHITE,
                                                               EinsteinVariable.YELLOW,
                                                               EinsteinVariable.BLUE));

    }

    private void applySameHouseConstraints(SearchTool<EinsteinVariable, EinsteinDomain> searchTool) {
        searchTool.addConstraint(new SameHouseConstraint(EinsteinVariable.ENGLISHMAN,
                                                         EinsteinVariable.RED));
        searchTool.addConstraint(new SameHouseConstraint(EinsteinVariable.DANE,
                                                         EinsteinVariable.TEA));
        searchTool.addConstraint(new SameHouseConstraint(EinsteinVariable.YELLOW,
                                                         EinsteinVariable.CIGAR));
        searchTool.addConstraint(new SameHouseConstraint(EinsteinVariable.GERMAN,
                                                         EinsteinVariable.PIPE));
        searchTool.addConstraint(new SameHouseConstraint(EinsteinVariable.NO_FILTER,
                                                         EinsteinVariable.BIRDS));
        searchTool.addConstraint(new SameHouseConstraint(EinsteinVariable.SWEDE,
                                                         EinsteinVariable.DOGS));
        searchTool.addConstraint(new SameHouseConstraint(EinsteinVariable.MENTHOL,
                                                         EinsteinVariable.BEER));
        searchTool.addConstraint(new SameHouseConstraint(EinsteinVariable.GREEN,
                                                         EinsteinVariable.COFFEE));
    }

    private void applyLeftNeighbourConstraints(SearchTool<EinsteinVariable, EinsteinDomain> searchTool) {
        searchTool.addConstraint(new LeftNeighbourConstraint(EinsteinVariable.GREEN, EinsteinVariable.WHITE));
    }

    private void applyNeighbourHouseConstraints(SearchTool<EinsteinVariable, EinsteinDomain> searchTool) {
        searchTool.addConstraint(new NeighbourHouseConstraint(EinsteinVariable.LIGHT,
                                                              EinsteinVariable.CATS));
        searchTool.addConstraint(new NeighbourHouseConstraint(EinsteinVariable.LIGHT,
                                                              EinsteinVariable.WATER));
        searchTool.addConstraint(new NeighbourHouseConstraint(EinsteinVariable.NORWEGIAN,
                                                              EinsteinVariable.BLUE));
        searchTool.addConstraint(new NeighbourHouseConstraint(EinsteinVariable.HORSES,
                                                              EinsteinVariable.YELLOW));
    }
}
