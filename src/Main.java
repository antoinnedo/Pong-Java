import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Use invokeLater to ensure GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}