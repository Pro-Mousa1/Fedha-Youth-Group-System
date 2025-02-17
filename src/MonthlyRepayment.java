import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MonthlyRepayment extends JFrame {
    public MonthlyRepayment() {
        setTitle("Fedha Monthly Loan Repayment");
        setSize(600, 450);
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
                MonthlyRepayment.this.dispose();
            }
        });

        menuItemSignOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement sign out functionality here
                new Login().setVisible(true);
                MonthlyRepayment.this.dispose();
            }
        });

        // Create a panel with null layout for custom component positioning
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x00B33F));
        add(panel);

        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdLabel.setBounds(150, 20, 150, 25);
        memberIdLabel.setForeground(Color.YELLOW);
        panel.add(memberIdLabel);
        JTextField memberIdField = new JTextField(15);
        memberIdField.setBounds(150, 45, 300, 25);
        memberIdField.setForeground(Color.BLACK);
        memberIdField.setBackground(Color.WHITE);
        panel.add(memberIdField);

        JLabel outstandingLoanLabel = new JLabel("Outstanding Loan:");
        outstandingLoanLabel.setBounds(150, 80, 200, 25);
        outstandingLoanLabel.setForeground(Color.YELLOW);
        panel.add(outstandingLoanLabel);
        JTextField outstandingLoanField = new JTextField(15);
        outstandingLoanField.setBounds(150, 105, 300, 25);
        outstandingLoanField.setEditable(false);
        outstandingLoanField.setForeground(Color.BLACK);
        outstandingLoanField.setBackground(Color.WHITE);
        panel.add(outstandingLoanField);

        JLabel loanRepaymentLabel = new JLabel("Loan Repayment Amount:");
        loanRepaymentLabel.setBounds(150, 140, 250, 25);
        loanRepaymentLabel.setForeground(Color.YELLOW);
        panel.add(loanRepaymentLabel);
        JTextField loanRepaymentField = new JTextField(15);
        loanRepaymentField.setBounds(150, 165, 300, 25);
        loanRepaymentField.setForeground(Color.BLACK);
        loanRepaymentField.setBackground(Color.WHITE);
        panel.add(loanRepaymentField);

        JLabel loanBalanceLabel = new JLabel("Loan Balance:");
        loanBalanceLabel.setBounds(150, 200, 200, 25);
        loanBalanceLabel.setForeground(Color.YELLOW);
        panel.add(loanBalanceLabel);
        JTextField loanBalanceField = new JTextField(15);
        loanBalanceField.setBounds(150, 225, 300, 25);
        loanBalanceField.setEditable(false);
        loanBalanceField.setForeground(Color.BLACK);
        loanBalanceField.setBackground(Color.WHITE);
        panel.add(loanBalanceField);

        JLabel loanRepaymentDateLabel = new JLabel("Loan Repayment Date:");
        loanRepaymentDateLabel.setBounds(150, 260, 200, 25);
        loanRepaymentDateLabel.setForeground(Color.YELLOW);
        panel.add(loanRepaymentDateLabel);
        JTextField loanRepaymentDateField = new JTextField(15);
        loanRepaymentDateField.setBounds(150, 285, 300, 25);
        loanRepaymentDateField.setForeground(Color.BLACK);
        loanRepaymentDateField.setBackground(Color.WHITE);
        panel.add(loanRepaymentDateField);

        // Get the current date and time from the system and set it to loanRepaymentDateField
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        loanRepaymentDateField.setText(formattedDateTime);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(240, 340, 100, 25);
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.setBackground(Color.WHITE);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnSubmit);


        //Add the KeyListener to memberIdField
        memberIdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {  // Check if Enter key is pressed
                    String memberId = memberIdField.getText();
                    if (!memberId.isEmpty()) {
                        try {
                            // Fetch the outstanding loan for the entered Member ID
                            double outstandingLoan = FedhaDatabase.getOutstandingLoan(memberId);

                            // Update the outstanding loan field
                            outstandingLoanField.setText(String.valueOf(outstandingLoan));

                            if (outstandingLoan > 0) {
                                loanBalanceField.setText(String.valueOf(outstandingLoan));
                                loanRepaymentField.setEnabled(true);
                            } else {
                                JOptionPane.showMessageDialog(MonthlyRepayment.this, "No outstanding loan found for this member.");
                                outstandingLoanField.setText("0.00");
                                loanBalanceField.setText("0.00");
                                loanRepaymentField.setEnabled(false);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(MonthlyRepayment.this, "Error fetching loan details.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(MonthlyRepayment.this, "An error occurred while fetching the loan.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(MonthlyRepayment.this, "Please enter a valid Member ID.");
                    }
                }
            }
        });

        // Outstanding loan should be all loans they have not been repaid
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberId = memberIdField.getText();
                String loanRepaymentAmountStr = loanRepaymentField.getText();

                if (memberId.isEmpty() || loanRepaymentAmountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(MonthlyRepayment.this, "Please fill in Member ID and Loan Repayment Amount.");
                    return;
                }
                try {
                    // Check if the memberId exists in the database
                    if (!FedhaDatabase.isMemberIdExists(memberId)) {
                        JOptionPane.showMessageDialog(MonthlyRepayment.this, "You are not eligible for loan repayment.");
                        Menubar menubar = new Menubar();
                        menubar.setVisible(true);
                        return;
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                // Retrieve loan amount, if exists
                Optional<Double> loanBorrowedOpt = FedhaDatabase.getLoanAmount(memberId);
                if (loanBorrowedOpt.isEmpty()) {
                    JOptionPane.showMessageDialog(MonthlyRepayment.this, "No outstanding loan found for this member.");
                    Menubar menubar = new Menubar();
                    menubar.setVisible(true);
                    MonthlyRepayment.this.dispose();
                    return;
                }

                double loanBorrowed = loanBorrowedOpt.get();

                try {
                    double RepaymentAmount = Double.parseDouble(loanRepaymentAmountStr);
                    double newLoanBalance = loanBorrowed - RepaymentAmount;

                    // Check if repayment exceeds loan balance
                    if (newLoanBalance < 0) {
                        JOptionPane.showMessageDialog(MonthlyRepayment.this, "Repayment amount exceeds the outstanding loan balance.");
                        return;
                    }

                    // Display the calculated loan balance in loanBalanceField
                    loanBalanceField.setText(String.valueOf(newLoanBalance));

                    // Record the repayment
                    String loanRepaymentDate = loanRepaymentDateField.getText();
                    FedhaDatabase.insertMonthlyRepayment(memberId, loanBorrowed, RepaymentAmount, newLoanBalance, loanRepaymentDate);
                    // Check if the loan has been fully repaid
                    if (newLoanBalance <= 0) {
                        loanBalanceField.setText("0.00");
                        JOptionPane.showMessageDialog(MonthlyRepayment.this, "Loan fully repaid. No outstanding loan.");
                        loanRepaymentField.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(MonthlyRepayment.this, "Loan repayment successful.");
                        Menubar menubar = new Menubar();
                        menubar.setVisible(true);
                        MonthlyRepayment.this.dispose();
                    }

                    // Introduce a delay before closing current window and opening Taskbar
                    Timer timer = new Timer(15000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            new Menubar().setVisible(true);
                            MonthlyRepayment.this.dispose();
                        }
                    });
                    timer.setRepeats(false); // Execute only once
                    timer.start();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MonthlyRepayment.this, "Invalid input in numeric fields.");
                }catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MonthlyRepayment.this, "An error occurred while repaying the loan.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MonthlyRepayment();
            }
        });
    }
}
