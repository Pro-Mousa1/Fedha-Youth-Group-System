import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;

public class RegisterAdmin extends JFrame {
    public RegisterAdmin() {
        setTitle("Fedha Register Admin");
        setSize(550,350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        JMenuItem menuItemAdmin = new JMenuItem("User");
        menu.add(menuItemAdmin);

        menuItemAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Register().setVisible(true);
                RegisterAdmin.this.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(13, 165, 2, 255));
        add(panel);

        JLabel AdminNameLabel = new JLabel("AdminName:");
        AdminNameLabel.setBounds(10, 20, 150, 25);
        AdminNameLabel.setForeground(Color.BLACK);
        panel.add(AdminNameLabel);
        JTextField AdminNameField = new JTextField(15);
        AdminNameField.setBounds(180, 20, 200, 25);
        AdminNameField.setForeground(Color.BLACK);
        AdminNameField.setBackground(new Color(0x0EEC9E));
        panel.add(AdminNameField);

        JLabel authenticationNumberLabel = new JLabel("Authentication Number:");
        authenticationNumberLabel.setBounds(10, 60, 200, 25);
        authenticationNumberLabel.setForeground(Color.BLACK);
        panel.add(authenticationNumberLabel);
        JTextField authenticationNumberField = new JTextField(15);
        authenticationNumberField.setBounds(180, 60, 200, 25);
        authenticationNumberField.setForeground(Color.BLACK);
        authenticationNumberField.setEnabled(false);
        authenticationNumberField.setBackground(new Color(0x0EEC9E));
        panel.add(authenticationNumberField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 100, 150, 25);
        passwordLabel.setForeground(Color.BLACK);
        panel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBounds(180, 100, 200, 25);
        passwordField.setForeground(Color.BLACK);
        passwordField.setBackground(new Color(0x0EEC9E));
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(10, 140, 150, 25);
        confirmPasswordLabel.setForeground(Color.BLACK);
        panel.add(confirmPasswordLabel);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setBounds(180, 140, 200, 25);
        confirmPasswordField.setForeground(Color.BLACK);
        confirmPasswordField.setBackground(new Color(0x0EEC9E));
        panel.add(confirmPasswordField);

        JButton btnRegister = new JButton("REGISTER");
        btnRegister.setBounds(225, 190, 100, 25);
        btnRegister.setBackground(new Color(0x0EEC9E));
        panel.add(btnRegister);

        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabel.setBounds(190, 250, 230, 25);
        loginLabel.setForeground(Color.BLACK);
        panel.add(loginLabel);

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginAdmin().setVisible(true);
                RegisterAdmin.this.dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLabel.setText("<html><u>Already have an account?</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLabel.setText("Already have an account?");
            }
        });

        // Authentication Number
        String generatedAuthenticationNumber = "" + Instant.now().getEpochSecond();
        authenticationNumberField.setText(generatedAuthenticationNumber);
        JOptionPane.showMessageDialog(RegisterAdmin.this,"Authentication Number: " + generatedAuthenticationNumber);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adminName = AdminNameField.getText();
                String authenticationNumber = authenticationNumberField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());


                if (authenticationNumber.isEmpty() || adminName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterAdmin.this, "Please fill all the fields.");
                } else {
                    if (!password.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(RegisterAdmin.this, "Counter-check the passwords.");
                        return;
                    }
                    try {
                        FedhaDatabase.insertAdmin(adminName, authenticationNumber, password);
                        JOptionPane.showMessageDialog(RegisterAdmin.this, "Admin Registration successful!");
                        LoginAdmin loginAdmin = new LoginAdmin();
                        loginAdmin.setVisible(true);
                        RegisterAdmin.this.dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(RegisterAdmin.this, "An error occurred while registering.");
                    }
                }
            }
        });

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterAdmin();
            }
        });
    }
}
