package pl.zoltowski.damian;


import pl.zoltowski.damian.problem.coloring.MapColoringProblem;
import pl.zoltowski.damian.problem.einstain.EinsteinProblem;


public class CSP {
    public static void main(String[] args){
        MapColoringProblem mcp = new MapColoringProblem(6, 5, 5, 3);
//        mcp.run();
        mcp.runGeneric();

        EinsteinProblem ep = new EinsteinProblem();

//        ep.run();

    }

}
