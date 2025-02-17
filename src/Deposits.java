import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deposits extends JFrame {
    public Deposits() {
        setTitle("Fedha Deposits");
        setSize(500, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create menu
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        // Create menu items
        JMenuItem menuItemGoBack = new JMenuItem("Back");
        JMenuItem menuItemSignOut = new JMenuItem("Logout");

        // Add menu items to menu
        menu.add(menuItemGoBack);
        menu.add(menuItemSignOut);

        menuItemGoBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement go back functionality here
                new Menubar().setVisible(true);
                Deposits.this.dispose();
            }
        });

        menuItemSignOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement sign out functionality here
                new Login().setVisible(true);
                Deposits.this.dispose();
            }
        });

        // Create a panel with null layout for custom component positioning
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x029334));
        add(panel);

        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdLabel.setBounds(100, 50, 150, 25);
        memberIdLabel.setForeground(Color.YELLOW);
        panel.add(memberIdLabel);
        JTextField memberIdField = new JTextField(15);
        memberIdField.setBounds(100, 75, 300, 25);
        memberIdField.setForeground(Color.BLACK);
        memberIdField.setBackground(Color.WHITE);
        panel.add(memberIdField);

        JLabel sharesContributionLabel = new JLabel("Monthly Shares Contribution Amount:");
        sharesContributionLabel.setBounds(100, 105, 280, 25);
        sharesContributionLabel.setForeground(Color.YELLOW);
        panel.add(sharesContributionLabel);
        JTextField sharesContributionField = new JTextField(15);
        sharesContributionField.setBounds(100, 130, 300, 25);
        sharesContributionField.setForeground(Color.BLACK);
        sharesContributionField.setBackground(Color.WHITE);
        panel.add(sharesContributionField);

        JLabel depositDateLabel = new JLabel("Deposit Date:");
        depositDateLabel.setBounds(100, 160, 150, 25);
        depositDateLabel.setForeground(Color.YELLOW);
        panel.add(depositDateLabel);
        JTextField depositDateField = new JTextField(15);
        depositDateField.setBounds(100, 185, 300, 25);
        depositDateField.setForeground(Color.BLACK);
        depositDateField.setBackground(Color.WHITE);
        panel.add(depositDateField);

        // Get the current date and time from the system and set it to depositDateField
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        depositDateField.setText(formattedDateTime);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(190, 230, 100, 25);
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.setBackground(Color.WHITE);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnSubmit);

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberId = memberIdField.getText();
                String sharesContribution = sharesContributionField.getText();
                String depositsDate = depositDateField.getText();

                // Check for empty fields
                if (memberId.isEmpty() || sharesContribution.isEmpty() || depositsDate.isEmpty()) {
                    JOptionPane.showMessageDialog(Deposits.this, "Please fill all the fields.");
                    return;
                }

                try {
                    // Parse the deposit amount
                    int depositAmount = Integer.parseInt(sharesContribution);

                    // Check for minimum deposit amount
                    if (depositAmount < 500) {
                        JOptionPane.showMessageDialog(Deposits.this, "The minimum amount for monthly deposit is 500.");
                        return;
                    }
                    if (FedhaDatabase.hasAlreadyDeposited(memberId, currentDateTime.getYear(), currentDateTime.getMonthValue())) {
                        JOptionPane.showMessageDialog(Deposits.this, "You can only deposit once per month");
                        Menubar menubar = new Menubar();
                        menubar.setVisible(true);
                        Deposits.this.dispose();
                        return;
                    }
                    // Check if the memberId exists in the database
                    if (!FedhaDatabase.isMemberIdExists(memberId)) {
                        JOptionPane.showMessageDialog(Deposits.this, "You must be a member to deposit");
                        Menubar menubar = new Menubar();
                        menubar.setVisible(true);
                        return;
                    }

                    // Insert the deposit details
                    FedhaDatabase.insertSharesContribution(memberId, sharesContribution, depositsDate);
                    JOptionPane.showMessageDialog(Deposits.this, "Shares Contribution successful");

                    // Show taskbar and close current window
                    Menubar menubar = new Menubar();
                    menubar.setVisible(true);
                    Deposits.this.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Deposits.this, "Please enter a valid numeric shares contribution amount.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Deposits.this, "An error occurred while depositing shares contribution.");
                }
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Deposits();
            }
        });
    }
}
