import javax.swing.JFrame;
import javax.swing.JPanel;

public class Jaikin extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chaikin");
        App app = new App();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(app);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}