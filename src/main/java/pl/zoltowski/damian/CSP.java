package pl.zoltowski.damian;


import pl.zoltowski.damian.problem.coloring.MapColoringProblem;
import pl.zoltowski.damian.python.PythonProcessBuilder;

import java.io.IOException;

public class CSP {
    public static void main(String[] args) throws IOException {
        PythonProcessBuilder ppb = new PythonProcessBuilder();
        MapColoringProblem mcp = new MapColoringProblem(6, 5, 5, 3);

        mcp.run();



    }

}
