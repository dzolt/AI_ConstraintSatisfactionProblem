package pl.zoltowski.damian.problem.coloring.constraint;

import pl.zoltowski.damian.problem.coloring.domain.MCPDomain;
import pl.zoltowski.damian.utils.dataType.Arc;
import pl.zoltowski.damian.utils.dataType.Point;

import java.util.List;

public class DifferentNeighbourColorsArc extends Arc<Point, MCPDomain> {

    public DifferentNeighbourColorsArc(Point variable1, Point variable2) {
        super(variable1, variable2);
    }

    @Override
    public boolean isConstraintSatisfied(MCPDomain valueToCheck, List<MCPDomain> domainOfRightVariable) {
        return domainOfRightVariable.stream().anyMatch(val -> !val.equals(valueToCheck));
    }
}
