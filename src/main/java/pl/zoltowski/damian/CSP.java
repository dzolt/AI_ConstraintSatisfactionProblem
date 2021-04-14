package pl.zoltowski.damian;


import pl.zoltowski.damian.problem.coloring.MapColoringProblem;
import pl.zoltowski.damian.problem.einstain.EinsteinProblem;
import pl.zoltowski.damian.utils.dataType.DomainHeuristic;
import pl.zoltowski.damian.utils.dataType.VariableHeuristic;


public class CSP {
    public static void main(String[] args){
        MapColoringProblem mcpAC3 = new MapColoringProblem(6, 5, 5, 3,
                                                           false, false,
                                                           VariableHeuristic.NONE, DomainHeuristic.LCV);
        MapColoringProblem mcp = new MapColoringProblem(6, 5, 5, 3,
                                                        false, false,
                                                        VariableHeuristic.NONE, DomainHeuristic.NONE);
        mcp.run();
        mcpAC3.run();


        EinsteinProblem ep = new EinsteinProblem(true, false, VariableHeuristic.NONE, DomainHeuristic.LCV);
        ep.run();

        EinsteinProblem ep2 = new EinsteinProblem(false, false, VariableHeuristic.NONE, DomainHeuristic.NONE);
        ep2.run();



    }


}
