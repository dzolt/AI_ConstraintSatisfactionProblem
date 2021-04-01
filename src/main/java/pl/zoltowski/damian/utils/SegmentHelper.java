package pl.zoltowski.damian.utils;

import pl.zoltowski.damian.utils.dataType.Orientation;
import pl.zoltowski.damian.utils.dataType.Point;
import pl.zoltowski.damian.utils.dataType.Segment;

public class SegmentHelper {

    public boolean onLine(Segment s, Point p) {
        if(		p.getX() <= Math.max(s.getStart().getX(), s.getEnd().getX()) &&
                p.getX() >= Math.min(s.getStart().getX(), s.getEnd().getX()) &&
                p.getY() <= Math.max(s.getStart().getY(), s.getEnd().getY()) &&
                p.getY() >= Math.min(s.getStart().getY(), s.getEnd().getY()))
            return true;

        return false;
    }

    private Orientation orientation(Point p, Point q, Point r) {

        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if(val == 0)
            return Orientation.COLINEAR;
        else if(val > 0)
            return Orientation.CLOKWISE;
        else
            return Orientation.COUNTERCLOCKWISE;
    }

    public boolean doIntersect(Segment s1, Segment s2)
    {
        Orientation o1 = orientation(s1.getStart(), s1.getEnd(), s2.getStart());
        Orientation o2 = orientation(s1.getStart(), s1.getEnd(), s2.getEnd());
        Orientation o3 = orientation(s2.getStart(), s2.getEnd(), s1.getStart());
        Orientation o4 = orientation(s2.getStart(), s2.getEnd(), s1.getEnd());

        if((s1.getStart().equals(s2.getStart()) || s1.getEnd().equals(s2.getEnd())) && !sameDirection(s1, s2))
            return false;

        if((s1.getStart().equals(s2.getEnd()) || s1.getEnd().equals(s2.getStart())) && !oppositeDirection(s1, s2))
            return false;

        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == Orientation.COLINEAR && onLine(s1, s2.getStart())) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == Orientation.COLINEAR && onLine(s1, s2.getEnd())) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == Orientation.COLINEAR && onLine(s2, s1.getStart())) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == Orientation.COLINEAR && onLine(s2, s1.getEnd())) return true;

        return false; // Doesn't fall in any of the above cases
    }

    private boolean sameDirection(Segment s1, Segment s2) {
        double xChange1 = s1.getStart().getX() - s1.getEnd().getX();
        double yChange1 = s1.getStart().getY() - s1.getEnd().getY();

        double xChange2 = s2.getStart().getX() - s2.getEnd().getX();
        double yChange2 = s2.getStart().getY() - s2.getEnd().getY();

        if(Math.abs(yChange1 * xChange2) == Math.abs(yChange2 * xChange1) &&
                Math.signum(xChange1) == Math.signum(xChange2) &&
                Math.signum(yChange1) == Math.signum(yChange2))
            return true;

        return false;
    }

    private boolean oppositeDirection(Segment s1, Segment s2) {
        double xChange1 = s1.getStart().getX() - s1.getEnd().getX();
        double yChange1 = s1.getStart().getY() - s1.getEnd().getY();

        double xChange2 = s2.getStart().getX() - s2.getEnd().getX();
        double yChange2 = s2.getStart().getY() - s2.getEnd().getY();

        if(Math.abs(yChange1 * xChange2) == Math.abs(yChange2 * xChange1) &&
                Math.signum(xChange1) == -1.0 * Math.signum(xChange2) &&
                Math.signum(yChange1) == -1.0 * Math.signum(yChange2))
            return true;

        return false;
    }
}