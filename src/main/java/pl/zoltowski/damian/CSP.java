package pl.zoltowski.damian;


import pl.zoltowski.damian.problem.einstain.EinsteinProblem;
import pl.zoltowski.damian.problem.einstain.domain.EinsteinDomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CSP {
    public static void main(String[] args){
//        MapColoringProblem mcp = new MapColoringProblem(6, 5, 5, 3, false);
//        mcp.run();



        EinsteinProblem ep = new EinsteinProblem(false);
        ep.run();

        EinsteinProblem ep2 = new EinsteinProblem(true);
        ep2.run();


    }


}
