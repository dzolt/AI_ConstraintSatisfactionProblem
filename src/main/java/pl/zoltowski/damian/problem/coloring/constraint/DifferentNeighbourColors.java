package pl.zoltowski.damian.problem.coloring.constraint;

import pl.zoltowski.damian.utils.dataType.Arc;
import pl.zoltowski.damian.utils.dataType.Constraint;
import pl.zoltowski.damian.problem.coloring.domain.MCPDomain;
import pl.zoltowski.damian.utils.dataType.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DifferentNeighbourColors extends Constraint<Point, MCPDomain> {

    public DifferentNeighbourColors(Point variable1, Point variable2) {
        super();
        this.variables.add(variable1);
        this.variables.add(variable2);
    }

    @Override
    public boolean satisfied(Map<Point, MCPDomain> assigment) {

        if(!containsAllKeys(assigment)) {
            return true;
        }

        return assigment.get(variables.get(0)) != assigment.get(variables.get(1));
    }

    @Override
    public void removeNotSatisfyingValues(Map<Point, List<MCPDomain>> domains, Point variable, MCPDomain assignedValue) {
        for(Point pointVariable: variables) {
            if(!pointVariable.equals(variable)) {
                List<MCPDomain> domain = domains.get(pointVariable);

                domain.remove(assignedValue);
            }
        }
    }

    @Override
    public List<Arc<Point, MCPDomain>> getArcs() {
        List<Arc<Point, MCPDomain>> arcs = new ArrayList<>(2);

        arcs.add(new DifferentNeighbourColorsArc(this.variables.get(0), this.variables.get(1)));
        arcs.add(new DifferentNeighbourColorsArc(this.variables.get(1), this.variables.get(0)));

        return arcs;
    }
}
