import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ProgressBar extends JFrame {
    private Timer timer;
    private double angle = 0;

    public ProgressBar() {
        setTitle("Fedha Youth Group System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        showProgressBarScreen();
    }

    private void showProgressBarScreen() {
        JPanel progressPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL imageUrl = getClass().getResource("Resource/Progress.jpg");
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }

                g.setColor(Color.BLUE);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = 50;
                int numDots = 12;

                for (int i = 0; i < numDots; i++) {
                    double currentAngle = angle + (i * 2 * Math.PI / numDots);
                    int x = (int) (centerX + Math.cos(currentAngle) * radius);
                    int y = (int) (centerY + Math.sin(currentAngle) * radius);

                    g.fillOval(x - 5, y - 5, 10, 10);
                }
            }
        };
        progressPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Fedha Youth Group System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(100, 150, 400, 30);
        progressPanel.add(titleLabel, BorderLayout.CENTER);

        setContentPane(progressPanel);
        setVisible(true);

        startRotatingAnimation();
    }

    private void startRotatingAnimation() {
        timer = new Timer(80, e -> {
            angle += Math.PI / 30;
            repaint();
            if (angle >= 2 * Math.PI) {
                angle = 0;
                showRegisterPage();
                timer.stop();
            }
        });
        timer.start();
    }

    private void showRegisterPage() {
        new Register();
        ProgressBar.this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProgressBar::new);
    }
}

