package pl.zoltowski.damian.utils.dataType;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class Graph {

    private Map<Point, List<Point>> adjVertices;

    public Graph() {
        this.adjVertices = new HashMap<>();
    }

    public void addVertex(Point point) {
        adjVertices.putIfAbsent(point, new ArrayList<>());
    }

    public void removeVertex(Point point) {
        adjVertices.values().stream().forEach(e -> e.remove(point));
        adjVertices.remove(point);
    }

    public void addEdge(Point point1, Point point2) {
        adjVertices.get(point1).add(point2);
        adjVertices.get(point2).add(point1);
    }

    public void removeEdge(Point point1, Point point2) {
        List<Point> point1Vertices = adjVertices.get(point1);
        List<Point> point2Vertices = adjVertices.get(point2);
        if (point1Vertices != null)
            point1Vertices.remove(point2);
        if (point2Vertices != null)
            point2Vertices.remove(point1);
    }

    public List<Point> getAdjVertices(Point point) {
        return adjVertices.get(point);
    }

    public boolean containsVertex(Point point) {
        return adjVertices.containsKey(point);
    }

    public List<Point> getKeys() {
        return new ArrayList<>(adjVertices.keySet());
    }

}
