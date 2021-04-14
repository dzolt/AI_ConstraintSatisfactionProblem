package pl.zoltowski.damian.problem.coloring;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zoltowski.damian.SearchTool;
import pl.zoltowski.damian.problem.Problem;
import pl.zoltowski.damian.problem.coloring.constraint.DifferentNeighbourColors;
import pl.zoltowski.damian.problem.coloring.domain.MCPDomain;
import pl.zoltowski.damian.python.PythonProcessBuilder;
import pl.zoltowski.damian.utils.MCPJsonSerializer;
import pl.zoltowski.damian.utils.SegmentHelper;
import pl.zoltowski.damian.utils.dataType.DomainHeuristic;
import pl.zoltowski.damian.utils.dataType.Graph;
import pl.zoltowski.damian.utils.dataType.VariableHeuristic;
import pl.zoltowski.damian.utils.dataType.Point;
import pl.zoltowski.damian.utils.dataType.Segment;
import pl.zoltowski.damian.utils.dataType.Tuple;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static pl.zoltowski.damian.utils.Util.calculateDistanceBetweenPoints;
import static pl.zoltowski.damian.utils.Util.drawGraph;
import static pl.zoltowski.damian.utils.Util.isPointOutsideBoard;
import static pl.zoltowski.damian.utils.Util.pDistance;

@Data
@NoArgsConstructor
public class MapColoringProblem implements Problem {
    private int width;
    private int height;
    private int vertexesNumber;
    private int maxColourNumber;
    private Graph graph;
    private int[] colors;
    private boolean runForwardChecking;
    private boolean applyAC3;
    private VariableHeuristic variableHeuristic;
    private DomainHeuristic domainHeuristic;

    public MapColoringProblem(int width, int height, int vertexesNumber, int maxColourNumber, boolean runForwardChecking, boolean applyAC3, VariableHeuristic variableHeuristic, DomainHeuristic domainHeuristic) {
        this.width = width;
        this.height = height;
        if (vertexesNumber > (width - 1) * (height - 1)) {
            throw new IllegalArgumentException("Number of vertexes cannot be larger than space available");
        }
        this.vertexesNumber = vertexesNumber;
        this.maxColourNumber = maxColourNumber;
        this.graph = new Graph();
        this.colors = new int[this.vertexesNumber];
        Arrays.fill(this.colors, 0);
        this.runForwardChecking = runForwardChecking;
        this.applyAC3 = applyAC3;
        this.variableHeuristic = variableHeuristic;
        this.domainHeuristic = domainHeuristic;
    }

    public MapColoringProblem(int width, int height, int vertexesNumber, int maxColourNumber, Graph graph, boolean runForwardChecking, boolean applyAC3, VariableHeuristic variableHeuristic, DomainHeuristic domainHeuristic) {
        this.width = width;
        this.height = height;
        if (vertexesNumber > (width - 1) * (height - 1)) {
            throw new IllegalArgumentException("Number of vertexes cannot be larger than space available");
        }
        this.vertexesNumber = vertexesNumber;
        this.maxColourNumber = maxColourNumber;
        this.graph = graph;
        this.colors = new int[this.vertexesNumber];
        Arrays.fill(this.colors, 0);
        this.runForwardChecking = runForwardChecking;
        this.applyAC3 = applyAC3;
        this.variableHeuristic = variableHeuristic;
        this.domainHeuristic = domainHeuristic;
    }

    public void runSpecific() {
        init();
        createConnections();
        int[][] matrix = this.transformGraph();
        if (this.graphColoring(matrix)) {
            try {
                drawGraph(this, new PythonProcessBuilder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void applyConstraints(SearchTool<Point, MCPDomain> searchTool) {
        for (Point vertex : this.graph.getKeys()) {
            for (Point connection : this.graph.getAdjVertices(vertex)) {
                searchTool.addConstraint(new DifferentNeighbourColors(vertex, connection));
            }
        }
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
        //list of variables
        List<Point> variables = this.graph.getKeys();
        //map of domains
        Map<Point, List<MCPDomain>> domains = new HashMap<>();
        for (Point p : variables) {
            //limit to max color number
            domains.put(p, new ArrayList<>(MCPDomain.BLUE.getNDomainValues(this.maxColourNumber)));
        }

        SearchTool<Point, MCPDomain> searchTool = new SearchTool<>(variables, domains, this.variableHeuristic, this.domainHeuristic);
        applyConstraints(searchTool);


        if (this.runForwardChecking) {
            if (this.applyAC3) {
                runForwardCheckAC3(searchTool);
            } else {
                runForwardCheck(searchTool);
            }
        } else {
            if (this.applyAC3) {
                runBacktrackingAC3(searchTool);
            } else {
                runBacktracking(searchTool);
            }
        }
    }

    private void runBacktracking(SearchTool<Point, MCPDomain> searchTool) {
        long startTime = System.nanoTime();
        List<Map<Point, MCPDomain>> result = searchTool.backtrackingSearch();
        long endTime = System.nanoTime();
        long timeTotal = (endTime - startTime);
        printResult(result);
        System.out.println("BACKTRACKING TIME: " + timeTotal + "ns");
    }

    private void runBacktrackingAC3(SearchTool<Point, MCPDomain> searchTool) {
        long startTime = System.nanoTime();
        List<Map<Point, MCPDomain>> result = searchTool.runAC3BackTracking();
        long endTime = System.nanoTime();
        long timeTotal = (endTime - startTime);
        printResult(result);
        System.out.println("BACKTRACKING TIME: " + timeTotal + "ns");
    }

    private void runForwardCheck(SearchTool<Point, MCPDomain> searchTool) {
        long startTime = System.nanoTime();
        List<Map<Point, MCPDomain>> result2 = searchTool.forwardCheckingSearch();
        printResult(result2);
        long endTime = System.nanoTime();
        long timeTotal = (endTime - startTime);
        System.out.println("FORWARD SEARCH TIME: " + timeTotal + "ns");
    }

    private void runForwardCheckAC3(SearchTool<Point, MCPDomain> searchTool) {
        long startTime = System.nanoTime();
        List<Map<Point, MCPDomain>> result2 = searchTool.runAC3ForwardCheck();
        printResult(result2);
        long endTime = System.nanoTime();
        long timeTotal = (endTime - startTime);
        System.out.println("FORWARD SEARCH TIME: " + timeTotal + "ns");
    }

    private boolean isSafeToColor(int vertexIndex, int[][] graphMatrix, int colorToCheck) {
        //check for each edge
        for (int i = 0; i < this.vertexesNumber; i++)
            if (graphMatrix[vertexIndex][i] == 1 && colorToCheck == this.colors[i])
                return false;
        return true;
    }

    private boolean graphColorUtil(int[][] graphMatrix, int vertexIndex) {
        // If all vertices are assigned a color then return true
        if (vertexIndex == this.vertexesNumber)
            return true;

        // Try different colors for vertex V
        for (int i = 1; i <= this.maxColourNumber; i++) {
            // check for assignment safety
            if (isSafeToColor(vertexIndex, graphMatrix, i)) {
                this.colors[vertexIndex] = i;
                // recursion for checking other vertices
                if (graphColorUtil(graphMatrix, vertexIndex + 1)) {
                    return true;
                }
                // if color doesnt lead to solution
                this.colors[vertexIndex] = 0;
            }
        }
        // If no color can be assigned to  vertex
        return false;
    }

    private void printColoringSolution(int color[]) {
        System.out.println("Color schema for vertices are: ");
        for (int i = 0; i < this.vertexesNumber; i++)
            System.out.println(this.graph.getKeys().get(i) + " ---> " + color[i]);
    }

    /**
     * It returns false if the m colors cannot be assigned
     * otherwise return true and
     * print color assignment result to all vertices.
     */
    private boolean graphColoring(int[][] graphMatrix) {
        // Call graphColorUtil() for vertex 0
        if (!graphColorUtil(graphMatrix, 0)) {
            System.out.println("Color schema not possible");
            return false;
        }

        // Print the color schema of vertices
        printColoringSolution(this.colors);
        return true;
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

    public int[][] transformGraph() {
        int[][] graphTransformed = new int[this.vertexesNumber][this.vertexesNumber];
        List<Point> allVertexes = this.graph.getKeys();

        for (int i = 0; i < allVertexes.size(); i++) {
            Point currentPoint = allVertexes.get(i);

            for (int j = 0; j < allVertexes.size(); j++) {
                Point pointToCheck = allVertexes.get(j);

                if (i == j) {
                    graphTransformed[i][j] = 0;
                } else {
                    if (this.graph.getAdjVertices(currentPoint).contains(pointToCheck)) {
                        graphTransformed[i][j] = 1;
                    } else {
                        graphTransformed[i][j] = 0;
                    }
                }
            }
        }
        return graphTransformed;
    }

    private void printResult(List<Map<Point, MCPDomain>> result) {
        if (result.isEmpty()) {
            System.out.println("THIS PROBLEM CANNOT BE SOLVED WITH: " + this.maxColourNumber + " colors!");
            return;
        }
        int i = 0;
        for (Map<Point, MCPDomain> assignment : result) {
            System.out.println("SOLUTION: " + i++);
            System.out.println(assignment);
        }
    }
}
