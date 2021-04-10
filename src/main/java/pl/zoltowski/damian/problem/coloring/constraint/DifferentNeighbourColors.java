package pl.zoltowski.damian.problem.coloring.constraint;

import pl.zoltowski.damian.Constraint;
import pl.zoltowski.damian.problem.coloring.domain.MCPDomain;
import pl.zoltowski.damian.utils.dataType.Point;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DifferentNeighbourColors extends Constraint<Point, MCPDomain> {

    public DifferentNeighbourColors(List<Point> points) {
        super();
        this.variables.addAll(points);
    }

    @Override
    public boolean satisfied(Map<Point, MCPDomain> assigment) {
        HashSet<MCPDomain> used = new HashSet<>();

        for(Point v: this.variables) {
            if(assigment.containsKey(v) && used.contains(assigment.get(v))) {
                return false;
            }
            used.add(assigment.get(v));
        }
        return true;
    }
}
