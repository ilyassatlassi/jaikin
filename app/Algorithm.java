// ChaikinAlgorithm.java
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Algorithm {
    private static final int MAX_ITERATIONS = 7;
    private static final double Q_RATIO = 0.75;
    private static final double R_RATIO = 0.25;
    
    public static List<List<Point>> generateChaikinSteps(List<Point> controlPoints) {
        List<List<Point>> steps = new ArrayList<>();
        List<Point> currentPoints = new ArrayList<>(controlPoints);
        
        steps.add(new ArrayList<>(currentPoints));
        
        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            currentPoints = applyChaikinIteration(currentPoints);
            steps.add(new ArrayList<>(currentPoints));
        }
        
        return steps;
    }
    
    private static List<Point> applyChaikinIteration(List<Point> points) {
        List<Point> newPoints = new ArrayList<>();
        
        newPoints.add(points.get(0));
        
        for (int i = 0; i < points.size() - 1; i++) {
            Point p0 = points.get(i);
            Point p1 = points.get(i + 1);
            
            Point Q = calculateIntermediatePoint(p0, p1, Q_RATIO);
            Point R = calculateIntermediatePoint(p0, p1, R_RATIO);
            
            newPoints.add(Q);
            newPoints.add(R);
        }
        
        newPoints.add(points.get(points.size() - 1));
        
        return newPoints;
    }
    
    private static Point calculateIntermediatePoint(Point p0, Point p1, double ratio) {
        int x = (int) (ratio * p0.x + (1 - ratio) * p1.x);
        int y = (int) (ratio * p0.y + (1 - ratio) * p1.y);
        return new Point(x, y);
    }
}