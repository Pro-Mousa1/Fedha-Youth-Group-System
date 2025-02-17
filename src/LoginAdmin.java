import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginAdmin extends JFrame {
    public LoginAdmin() {
        setTitle("Fedha Login Admin");
        setSize(500,350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x0C701B));
        add(panel);

        JLabel AuthenticationNumberLabel = new JLabel("Authentication Number:");
        AuthenticationNumberLabel.setBounds(10, 120, 200, 25);
        AuthenticationNumberLabel.setForeground(Color.BLACK);
        panel.add(AuthenticationNumberLabel);
        JTextField authenticationNumberField = new JTextField(15);
        authenticationNumberField.setBounds(190, 120, 200, 25);
        authenticationNumberField.setForeground(Color.BLACK);
        authenticationNumberField.setBackground(new Color(0x0EEC54));
        panel.add(authenticationNumberField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 160, 150, 25);
        passwordLabel.setForeground(Color.BLACK);
        panel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBounds(190, 160, 200, 25);
        passwordField.setForeground(Color.BLACK);
        passwordField.setBackground(new Color(0x0EEC54));
        panel.add(passwordField);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(230, 200, 100, 30);
        btnLogin.setBackground(new Color(0x0EEC54));
        panel.add(btnLogin);

        JLabel registerLabel = new JLabel("No account? Sign up");
        registerLabel.setBounds(210, 260, 230, 25);
        registerLabel.setForeground(Color.BLACK);
        panel.add(registerLabel);

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterAdmin().setVisible(true);
                LoginAdmin.this.dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registerLabel.setText("<html><u>No account? Sign up</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerLabel.setText("No account? Sign up");
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredAuthenticationNumber = authenticationNumberField.getText();
                String enteredPassword = new String(passwordField.getPassword());

                if (enteredAuthenticationNumber.isEmpty() || enteredPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields.");
                } else {
                    boolean isValidAdmin= FedhaDatabase.checkAdminCredentials(enteredAuthenticationNumber, enteredPassword);
                    if (isValidAdmin) {
                        JOptionPane.showMessageDialog(LoginAdmin.this, "Admin Login successful!");
                        AdminBar reports = new AdminBar();
                        reports.setVisible(true);
                        LoginAdmin.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginAdmin.this, "Incorrect authentication Number or password.");
                    }
                }
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginAdmin();
            }
        });
    }
}
