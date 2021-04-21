package pl.zoltowski.damian;


import pl.zoltowski.damian.problem.coloring.MapColoringProblem;
import pl.zoltowski.damian.problem.einstain.EinsteinProblem;
import pl.zoltowski.damian.utils.dataType.DomainHeuristic;
import pl.zoltowski.damian.utils.dataType.VariableHeuristic;

import java.util.ArrayList;
import java.util.List;


public class CSP {
    public static void main(String[] args) {
        List<Integer> vertexes = new ArrayList<>();
        vertexes.add(2);
        vertexes.add(4);
        vertexes.add(8);
        vertexes.add(9);
        vertexes.add(10);
        vertexes.add(12);

        for(Integer v: vertexes) {
            System.out.println("NUMBER OF VERTEXES: " + v);
            runMCP(false, v, VariableHeuristic.NONE, DomainHeuristic.NONE, "BACKTRACKING");
            runMCP(true, v, VariableHeuristic.NONE, DomainHeuristic.NONE, "FORWARD CHECK");
            System.out.println();
        }


    }

    private static void runEP(boolean fc, VariableHeuristic vh, DomainHeuristic dh, String message) {
        EinsteinProblem ep = new EinsteinProblem(fc,
                                                 false,
                                                 vh, dh);
        System.out.println(message);
        ep.run();
        System.out.println(ep.getNumberOfVisitedVertexes());
    }

    private static void runMCP(boolean fc, int vertexNumber, VariableHeuristic vh, DomainHeuristic dh, String message) {
        MapColoringProblem mcp = new MapColoringProblem(6, 5,
                                                        vertexNumber,
                                                        5,
                                                        fc,
                                                        false,
                                                        vh, dh);
        System.out.println(message);
        mcp.run();
        System.out.println(mcp.getNumberOfVisitedVertexes());
    }

}
