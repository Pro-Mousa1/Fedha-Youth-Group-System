import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class Register extends JFrame {
    public Register(){
        setTitle("Fedha Registration System");
        setSize(550,360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create menu
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        JMenuItem menuItemAdmin = new JMenuItem("Admin");
        menu.add(menuItemAdmin);

        menuItemAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginAdmin().setVisible(true);
                Register.this.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x009116));
        add(panel);

        JLabel titleLabel = new JLabel("Register To Fedha Youth Group System");
        titleLabel.setBounds(120, 20, 370, 25);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 60, 150, 25);
        usernameLabel.setForeground(Color.BLACK);
        panel.add(usernameLabel);
        JTextField usernameField = new JTextField(15);
        usernameField.setBounds(180, 60, 200, 25);
        usernameField.setForeground(Color.BLACK);
        usernameField.setBackground(new Color(0x00F19C));
        panel.add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 100, 150, 25);
        emailLabel.setForeground(Color.BLACK);
        panel.add(emailLabel);
        JTextField emailField = new JTextField(15);
        emailField.setBounds(180, 100, 200, 25);
        emailField.setForeground(Color.BLACK);
        emailField.setBackground(new Color(0x00F19C));
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 140, 150, 25);
        passwordLabel.setForeground(Color.BLACK);
        panel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBounds(180, 140, 200, 25);
        passwordField.setForeground(Color.BLACK);
        passwordField.setBackground(new Color(0x00F19C));
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(20, 180, 150, 25);
        confirmPasswordLabel.setForeground(Color.BLACK);
        panel.add(confirmPasswordLabel);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setBounds(180, 180, 200, 25);
        confirmPasswordField.setForeground(Color.BLACK);
        confirmPasswordField.setBackground(new Color(0x00F19C));
        panel.add(confirmPasswordField);

        JButton btnRegister = new JButton("REGISTER");
        btnRegister.setBounds(225, 220, 100, 25);
        btnRegister.setBackground(new Color(0x00F19C));
        panel.add(btnRegister);

        JLabel loginLabel = new JLabel("Already have an account? Sign In");
        loginLabel.setBounds(170, 270, 250, 25);
        loginLabel.setForeground(Color.BLACK);
        panel.add(loginLabel);

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Login().setVisible(true);
                Register.this.dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLabel.setText("<html><u>Already have an account? Sign In</u></html>");
                loginLabel.setForeground(new Color(0x00FBF3));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLabel.setText("Already have an account? Sign In");
                loginLabel.setForeground(Color.BLACK);
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Regular expression to ensure the email ends with @gmail.com
                String emailPattern = "^[\\w-\\.]+@gmail\\.com$";

                if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(Register.this, "Please fill all the fields.");
                } else {
                    // Check if email matches the @gmail.com pattern
                    if (!email.matches(emailPattern)) {
                        JOptionPane.showMessageDialog(Register.this, "Please enter a valid Gmail address.(example@gmail.com)");
                        return;  // Return early if email is invalid
                    }
                    // Check if password and confirm password match
                    if (!password.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(Register.this, "Passwords do not match. Please try again.");
                        return;  // Return early if passwords do not match
                    }
                    try {
                        // Check if the username or email already exists
                        if (FedhaDatabase.userExists(username, email)) {
                            JOptionPane.showMessageDialog(Register.this, "Username or email already exists. Please use a different one.");
                        } else {
                            // Insert the user into the database
                            FedhaDatabase.insertUser(username, email, password);
                            JOptionPane.showMessageDialog(Register.this, "Registration successful!");

                            // Open the login window after successful registration
                            Login login = new Login();
                            login.setVisible(true);
                            Register.this.dispose();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Register.this, "An error occurred while registering.");
                    }
                }
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Register();
            }
        });
    }
}