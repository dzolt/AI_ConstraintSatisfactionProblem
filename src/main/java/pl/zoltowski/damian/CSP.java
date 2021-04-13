package pl.zoltowski.damian;


import pl.zoltowski.damian.problem.coloring.MapColoringProblem;
import pl.zoltowski.damian.problem.einstain.EinsteinProblem;


public class CSP {
    public static void main(String[] args){
        MapColoringProblem mcpAC3 = new MapColoringProblem(6, 5, 5, 3, false, true);
        MapColoringProblem mcp = new MapColoringProblem(6, 5, 5, 3, false, false);
        mcp.run();
        mcpAC3.run();


        EinsteinProblem ep = new EinsteinProblem(true, false);
        ep.run();

        EinsteinProblem ep2 = new EinsteinProblem(true, true);
        ep2.run();


    }


}
