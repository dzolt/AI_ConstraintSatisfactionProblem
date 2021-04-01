package pl.zoltowski.damian;


import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import pl.zoltowski.damian.problem.coloring.MapColoringProblem;
import pl.zoltowski.damian.python.PythonProcessBuilder;
import pl.zoltowski.damian.utils.dataType.Point;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static pl.zoltowski.damian.utils.Util.pDistance;

public class CSP {
    public static void main(String[] args) throws IOException {
        PythonProcessBuilder ppb = new PythonProcessBuilder();
        MapColoringProblem mcp = new MapColoringProblem(6, 5, 10, 5);

        mcp.run();

        drawGraph(mcp, ppb);


    }

    private static void drawGraph(MapColoringProblem mcp, PythonProcessBuilder ppb) throws IOException {
        for (Point p : mcp.getGraph().getKeys()) {
            System.out.println(p);
        }
        mcp.saveProblemToJson("graph.json");
        ppb.generateGraphImageToFile("graph.json", "graph.png");
        System.out.println(mcp.getGraph());
        Graph<Point, DefaultEdge> g =
                new SimpleGraph<>(DefaultEdge.class);
        for (Point p : mcp.getGraph().getKeys()) {
            g.addVertex(p);
        }
        for (Point p : mcp.getGraph().getKeys()) {
            for (Point edge : mcp.getGraph().getAdjVertices(p)) {
                g.addEdge(p, edge);
            }
        }
        JGraphXAdapter<Point, DefaultEdge> graphAdapter =
                new JGraphXAdapter<Point, DefaultEdge>(g);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/main/resources/graph.png");
        ImageIO.write(image, "PNG", imgFile);
    }
}
