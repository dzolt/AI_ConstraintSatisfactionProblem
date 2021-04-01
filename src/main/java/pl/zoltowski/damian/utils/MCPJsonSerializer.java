package pl.zoltowski.damian.utils;

import com.google.gson.*;
import pl.zoltowski.damian.problem.coloring.MapColoringProblem;

import pl.zoltowski.damian.utils.dataType.Point;

import java.lang.reflect.Type;

public class MCPJsonSerializer implements JsonSerializer<MapColoringProblem> {

    @Override
    public JsonElement serialize(MapColoringProblem src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonMCP = new JsonObject();
        JsonArray x_values = new JsonArray();
        JsonArray y_values = new JsonArray();
        JsonArray connectedPoints = new JsonArray();

        for(Point p: src.getGraph().getKeys()) {
            x_values.add(p.getX());
            y_values.add(p.getY());
            JsonArray connectedPointsOfCurrentPoint = new JsonArray();
            for(Point edge: src.getGraph().getAdjVertices(p)) {
                JsonArray connectedPointCoordinates = new JsonArray();
                connectedPointCoordinates.add(edge.getX());
                connectedPointCoordinates.add(edge.getY());
                connectedPointsOfCurrentPoint.add(connectedPointCoordinates);
            }
            connectedPoints.add(connectedPointsOfCurrentPoint);
        }

        jsonMCP.addProperty("x_size", src.getWidth());
        jsonMCP.addProperty("y_size", src.getWidth());
        jsonMCP.add("x_values", x_values);
        jsonMCP.add("y_values", y_values);
        jsonMCP.add("connectedPoints", connectedPoints);
        return jsonMCP;
    }
}
