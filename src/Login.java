import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame{
    public Login(){
        setTitle("Fedha Login System");
        setSize(500,350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Create a panel with null layout for custom component positioning
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x267516));
        add(panel);

        JLabel titleLabel = new JLabel("Welcome To Fedha Youth Group System");
        titleLabel.setBounds(70, 50, 370, 25);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(40, 120, 150, 25);
        usernameLabel.setForeground(Color.BLACK);
        panel.add(usernameLabel);
        JTextField usernameField = new JTextField(15);
        usernameField.setBounds(160, 120, 200, 25);
        usernameField.setForeground(Color.BLACK);
        usernameField.setBackground(new Color(0x00F19C));
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 160, 150, 25);
        passwordLabel.setForeground(Color.BLACK);
        panel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBounds(160, 160, 200, 25);
        passwordField.setForeground(Color.BLACK);
        passwordField.setBackground(new Color(0x00F19C));
        panel.add(passwordField);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(210, 200, 100, 30);
        btnLogin.setBackground(new Color(0x00F19C));
        panel.add(btnLogin);

        JLabel registerLabel = new JLabel("No account? Sign up");
        registerLabel.setBounds(190, 260, 230, 25);
        registerLabel.setForeground(Color.BLACK);
        panel.add(registerLabel);

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Register().setVisible(true);
                Login.this.dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Optional: underline the text when the mouse hovers over it
                registerLabel.setText("<html><u>No account? Sign up</u></html>");
                registerLabel.setForeground(new Color(0x00FBF3));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Remove underline when the mouse exits
                registerLabel.setText("No account? Sign Up");
                registerLabel.setForeground(Color.BLACK);
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                String enteredPassword = new String(passwordField.getPassword());

                if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields.");
                } else {
                    // Always check the database for valid credentials
                    boolean isValidUser = FedhaDatabase.checkCredentials(enteredUsername, enteredPassword);

                    if (isValidUser) {
                        JOptionPane.showMessageDialog(Login.this, "Login successful!");
                        // Proceed to the dashboard or main application
                        Menubar menubar = new Menubar();
                        menubar.setVisible(true);
                        Login.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(Login.this, "Incorrect username or password. Please try again.");
                    }
                }
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login();
            }
        });
    }
}
