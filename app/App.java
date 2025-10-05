
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class App extends JPanel {
    // Constants
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    private static final int POINT_RADIUS = 4;
    private static final int DRAG_THRESHOLD = 10;
    private static final int ANIMATION_DELAY = 1000;
    
    // State variables
    private final List<Point> controlPoints = new ArrayList<>();
    private final List<List<Point>> algorithmSteps = new ArrayList<>();
    private int currentStep = 0;
    private boolean isAnimating = false;
    private boolean hasError = false;
    private Point selectedPoint = null;
    
    // Components
    private Timer animationTimer;
    
    public App() {
        setupPanel();
        setupEventListeners();
    }
    
    private void setupPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
    }
    
    private void setupEventListeners() {
        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
        addKeyListener(new KeyboardHandler());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        drawErrorMessages(g2d);
        drawControlPoints(g2d);
        drawCurrentStep(g2d);
    }
    
    private void drawErrorMessages(Graphics2D g2d) {
        if (hasError) {
            g2d.setColor(Color.RED);
            g2d.drawString("You need at least 2 points to start animation.", 20, 20);
        }
    }
    
    private void drawControlPoints(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        for (Point point : controlPoints) {
            drawPoint(g2d, point);
        }
    }
    
    private void drawCurrentStep(Graphics2D g2d) {
        if (controlPoints.isEmpty()) return;
        
        g2d.setStroke(new BasicStroke(2));
        
        List<Point> pointsToDraw = getPointsForCurrentStep();
        drawLines(g2d, pointsToDraw);
    }
    
    private List<Point> getPointsForCurrentStep() {
        return algorithmSteps.isEmpty() ? controlPoints : algorithmSteps.get(currentStep);
    }
    
    private void drawLines(Graphics2D g2d, List<Point> points) {
        if (points.size() < 2) return;
        
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
    
    private void drawPoint(Graphics2D g2d, Point point) {
        int x = point.x - POINT_RADIUS;
        int y = point.y - POINT_RADIUS;
        int diameter = POINT_RADIUS * 2;
        g2d.drawOval(x, y, diameter, diameter);
    }
    
    private void startAnimation() {
        if (controlPoints.size() < 2) {
            hasError = true;
            repaint();
            return;
        }
        
        initializeAnimation();
        setupAnimationTimer();
    }
    
    private void initializeAnimation() {
        hasError = false;
        algorithmSteps.clear();
        algorithmSteps.addAll(Algorithm.generateChaikinSteps(controlPoints));
        isAnimating = true;
        currentStep = 0;
    }
    
    private void setupAnimationTimer() {
        stopAnimationTimer();
        
        animationTimer = new Timer(ANIMATION_DELAY, e -> {
            currentStep = (currentStep + 1) % algorithmSteps.size();
            repaint();
        });
        animationTimer.start();
    }
    
    private void stopAnimationTimer() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }
    
    private void resetApplication() {
        controlPoints.clear();
        algorithmSteps.clear();
        currentStep = 0;
        isAnimating = false;
        hasError = false;
        stopAnimationTimer();
        repaint();
    }
    
    // Event Handler Classes
    private class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && !isAnimating) {
                handlePointSelection(e.getPoint());
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            selectedPoint = null;
        }
        
        private void handlePointSelection(Point clickPoint) {
            selectedPoint = findPointNearClick(clickPoint);
            if (selectedPoint == null) {
                controlPoints.add(new Point(clickPoint));
                repaint();
            }
        }
        
        private Point findPointNearClick(Point clickPoint) {
            for (Point point : controlPoints) {
                if (point.distance(clickPoint) <= DRAG_THRESHOLD) {
                    return point;
                }
            }
            return null;
        }
    }
    
    private class MouseMotionHandler extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectedPoint != null) {
                selectedPoint.setLocation(e.getPoint());
                repaint();
            }
        }
    }
    
    private class KeyboardHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ENTER -> startAnimation();
                case KeyEvent.VK_SPACE -> resetApplication();
                case KeyEvent.VK_ESCAPE -> System.exit(0);
            }
        }
    }
    
    public List<Point> getControlPoints() { return new ArrayList<>(controlPoints); }
    public List<List<Point>> getAlgorithmSteps() { return new ArrayList<>(algorithmSteps); }
    public int getCurrentStep() { return currentStep; }
    public boolean isAnimating() { return isAnimating; }
}