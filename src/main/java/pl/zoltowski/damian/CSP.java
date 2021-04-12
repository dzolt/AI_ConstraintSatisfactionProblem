package pl.zoltowski.damian;


import pl.zoltowski.damian.problem.coloring.MapColoringProblem;
import pl.zoltowski.damian.problem.coloring.domain.MCPDomain;
import pl.zoltowski.damian.problem.einstain.EinsteinProblem;

import java.util.List;


public class CSP {
    public static void main(String[] args){
        MapColoringProblem mcp = new MapColoringProblem(6, 5, 5, 3);
        mcp.run();

        EinsteinProblem ep = new EinsteinProblem();
        ep.run();

    }

}
