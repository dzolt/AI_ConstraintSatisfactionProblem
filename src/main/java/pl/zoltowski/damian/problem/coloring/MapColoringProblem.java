package pl.zoltowski.damian.problem.coloring;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zoltowski.damian.utils.MCPJsonSerializer;
import pl.zoltowski.damian.utils.SegmentHelper;
import pl.zoltowski.damian.utils.dataType.*;
import pl.zoltowski.damian.problem.Problem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

import static pl.zoltowski.damian.utils.Util.*;

@Data
@NoArgsConstructor
public class MapColoringProblem implements Problem {
    private int width;
    private int height;
    private int vertexesNumber;
    private int maxColourNumber;
    private Graph graph;

    public MapColoringProblem(int width, int height, int vertexesNumber, int maxColourNumber) {
        this.width = width;
        this.height = height;
        if (vertexesNumber > (width - 1) * (height - 1)) {
            throw new IllegalArgumentException("Number of vertexes cannot be larger than space available");
        }
        this.vertexesNumber = vertexesNumber;
        this.maxColourNumber = maxColourNumber;
        this.graph = new Graph();
    }

    public MapColoringProblem(int width, int height, int vertexesNumber, int maxColourNumber, Graph graph) {
        this.width = width;
        this.height = height;
        if (vertexesNumber > (width - 1) * (height - 1)) {
            throw new IllegalArgumentException("Number of vertexes cannot be larger than space available");
        }
        this.vertexesNumber = vertexesNumber;
        this.maxColourNumber = maxColourNumber;
        this.graph = graph;
    }

    public void init() {
        int generatedPoints = 0;
        Random r = new Random();
        while (generatedPoints < this.vertexesNumber) {
            int x = r.nextInt(width) + 1;
            int y = r.nextInt(height) + 1;
            Point vertex = new Point(x, y);
            if (!graph.containsVertex(vertex) && !isPointOutsideBoard(width, height, vertex)) {
                graph.addVertex(vertex);
                generatedPoints++;
            }
        }
    }

    public void createConnections() {
        Map<Point, List<Point>> distances = getDistances();
        List<Tuple<Point, List<Point>>> pointsToTraverse = new ArrayList<>();
        for (Point p : distances.keySet()) {
            pointsToTraverse.add(new Tuple<>(p, distances.get(p)));
        }

        while (areThereStillPointsToConnect(pointsToTraverse)) {
            //for each point get first closest if not intersecting with any other
            for (Tuple<Point, List<Point>> pointAndDistances : pointsToTraverse) {
                if (!pointAndDistances.getSecond().isEmpty()) {
                    if (!isIntersecting(pointAndDistances.getFirst(), pointAndDistances.getSecond().get(0))) {
                        //if not intersecting add edge to the graph
                        this.graph.addEdge(pointAndDistances.getFirst(), pointAndDistances.getSecond().get(0));
                        distances.get(pointAndDistances.getSecond().get(0)).remove(pointAndDistances.getFirst());
                        //remove point from closes proximity array
                        pointAndDistances.getSecond().remove(0);

                    } else {
                        distances.get(pointAndDistances.getSecond().get(0)).remove(pointAndDistances.getFirst());
                        //remove point from closes proximity array
                        pointAndDistances.getSecond().remove(0);
                    }
                }
            }
        }
    }

    private boolean areThereStillPointsToConnect(List<Tuple<Point, List<Point>>> pointsToTraverse) {
        for (Tuple<Point, List<Point>> point : pointsToTraverse) {
            if (!point.getSecond().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean isIntersecting(Point startingPoint, Point endingPoint) {
        SegmentHelper sh = new SegmentHelper();
        for (Point vertex : graph.getKeys()) {
            for (Point connection : graph.getAdjVertices(vertex)) {
                if ((!vertex.isSame(startingPoint) && !connection.isSame(endingPoint)) || (!vertex.isSame(endingPoint) && !connection.isSame(startingPoint))) {
                    //check if intersection point isn't start or end point and is in bound of segment
                    if (sh.doIntersect(new Segment(startingPoint, endingPoint), new Segment(vertex, connection)) ||
                            isLineIntersectingOtherVertexes(startingPoint, endingPoint, graph.getKeys())
                    ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLineIntersectingOtherVertexes(Point startingPoint, Point endingPoint, List<Point> otherVertexes) {
        for (Point otherVertex : otherVertexes) {
            if (!startingPoint.isSame(otherVertex) && !endingPoint.isSame(otherVertex)) {
                //check if the other vertex is inside the given line
                double otherVertexXMaxRange = Math.max(startingPoint.getX(), endingPoint.getX());
                double otherVertexXMinRange = Math.min(startingPoint.getX(), endingPoint.getX());
                double otherVertexYMaxRange = Math.max(startingPoint.getY(), endingPoint.getY());
                double otherVertexYMinRange = Math.min(startingPoint.getY(), endingPoint.getY());
                if (otherVertex.getX() <= otherVertexXMaxRange && otherVertex.getX() >= otherVertexXMinRange &&
                        otherVertex.getY() <= otherVertexYMaxRange && otherVertex.getY() >= otherVertexYMinRange && pDistance(startingPoint, endingPoint, otherVertex) == 0.0) {
                    System.out.println("LINE " + startingPoint + ", " + endingPoint + " IS INTERSECTING WITH OTHER VERTEX " + otherVertex);
                    return true;
                }
            }
        }
        return false;
    }

    private Map<Point, List<Point>> getDistances() {
        Map<Point, List<Point>> distances = new HashMap<>();

        for (Point p : this.graph.getKeys()) {
            List<Tuple<Point, Double>> distancesToPoint = new ArrayList<>();
            for (Point p2 : this.graph.getKeys()) {
                if (p != p2) {
                    Tuple<Point, Double> pointAndDistance = new Tuple<>(new Point(p2), calculateDistanceBetweenPoints(p, p2));
                    distancesToPoint.add(pointAndDistance);
                }
            }
            distancesToPoint.sort(Comparator.comparing(Tuple::getSecond));
            distances.putIfAbsent(new Point(p), distancesToPoint.stream().map(tuple -> tuple.getFirst()).collect(Collectors.toList()));
        }
        return distances;
    }

    @Override
    public void run() {
        init();
        createConnections();
    }


    private void jsonify(String fileName) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonSerializer<MapColoringProblem> serializer = new MCPJsonSerializer();
        gsonBuilder.registerTypeAdapter(MapColoringProblem.class, serializer);
        String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\zoltowski\\damian\\python\\data\\graph\\" + fileName;
        try (Writer writer = new FileWriter(filePath)) {
            gsonBuilder.create().toJson(this, writer);
        }
    }

    public void saveProblemToJson(String fileName) {
        try {
            this.jsonify(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
