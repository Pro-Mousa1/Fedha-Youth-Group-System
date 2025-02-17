import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
public class Loan extends JFrame {
    private List<String> guarantorList = new ArrayList<>();
    private DefaultListModel<String> guarantorListModel = new DefaultListModel<>();

    public Loan() {
        setTitle("Fedha Loan Application");
        setSize(702,750);
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
                Loan.this.dispose();
            }
        });

        menuItemSignOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement sign out functionality here
                new Login().setVisible(true);
                Loan.this.dispose();
            }
        });

        // Create a panel with null layout for custom component positioning
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x016E27));
        add(panel);

        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdLabel.setBounds(180, 5, 150, 25);
        memberIdLabel.setForeground(Color.YELLOW);
        panel.add(memberIdLabel);
        JTextField memberIdField = new JTextField(15);
        memberIdField.setBounds(180, 30, 300, 25);
        memberIdField.setForeground(Color.BLACK);
        memberIdField.setBackground(Color.WHITE);
        panel.add(memberIdField);

        JLabel loanAmountLabel = new JLabel("Loan Amount:");
        loanAmountLabel.setBounds(180, 60, 150, 25);
        loanAmountLabel.setForeground(Color.YELLOW);
        panel.add(loanAmountLabel);
        JTextField loanAmountField = new JTextField(15);
        loanAmountField.setBounds(180, 85, 300, 25);
        loanAmountField.setForeground(Color.BLACK);
        loanAmountField.setBackground(Color.WHITE);
        panel.add(loanAmountField);

        JLabel loanTypeLabel = new JLabel("Loan Type:");
        loanTypeLabel.setBounds(180, 115, 150, 25);
        loanTypeLabel.setForeground(Color.YELLOW);
        panel.add(loanTypeLabel);
        String[] loanTypeOptions = {"Emergency Loan", "Short Loan", "Normal Loan", "Development Loan"};
        JComboBox<String> loanTypeCombo = new JComboBox<>(loanTypeOptions);
        loanTypeCombo.setBounds(180, 140, 300, 25);
        loanTypeCombo.setForeground(Color.BLACK);
        loanTypeCombo.setBackground(Color.WHITE);
        panel.add(loanTypeCombo);

        JLabel interestRateLabel = new JLabel("Interest Rate:");
        interestRateLabel.setBounds(180, 170, 150, 25);
        interestRateLabel.setForeground(Color.YELLOW);
        panel.add(interestRateLabel);
        JTextField interestRateField = new JTextField(15);
        interestRateField.setBounds(180, 195, 300, 25);
        interestRateField.setEditable(false);
        panel.add(interestRateField);

        JLabel repaymentPeriodLabel = new JLabel("Repayment Period:");
        repaymentPeriodLabel.setBounds(180, 225, 150, 25);
        repaymentPeriodLabel.setForeground(Color.YELLOW);
        panel.add(repaymentPeriodLabel);
        JTextField repaymentPeriodField = new JTextField(15);
        repaymentPeriodField.setBounds(180, 250, 300, 25);
        repaymentPeriodField.setEditable(false);
        panel.add(repaymentPeriodField);

        JLabel loanDueLabel = new JLabel("Loan Due:");
        loanDueLabel.setBounds(180, 280, 150, 25);
        loanDueLabel.setForeground(Color.YELLOW);
        panel.add(loanDueLabel);
        JTextField loanDueField = new JTextField(15);
        loanDueField.setBounds(180, 305, 300, 25);
        loanDueField.setEditable(false);
        loanDueField.setForeground(Color.BLACK);
        loanDueField.setBackground(Color.WHITE);
        panel.add(loanDueField);

        JLabel guarantorLabel = new JLabel("Guarantors:");
        guarantorLabel.setBounds(180, 330, 150, 25);
        guarantorLabel.setForeground(Color.YELLOW);
        panel.add(guarantorLabel);

        // JList to show selected guarantors in ascending order
        JList<String> guarantorListBox = new JList<>(guarantorListModel);
        JScrollPane guarantorScrollPane = new JScrollPane(guarantorListBox);
        guarantorScrollPane.setBounds(180, 355, 300, 75);
        panel.add(guarantorScrollPane);

        // Add MouseListener for the guarantor list to show a remove option
        guarantorListBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Single click
                    int index = guarantorListBox.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String selectedGuarantor = guarantorListModel.get(index);
                        int response = JOptionPane.showConfirmDialog(
                                Loan.this,
                                "Do you want to remove " + selectedGuarantor + " from the guarantor list?",
                                "Remove Guarantor",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (response == JOptionPane.YES_OPTION) {
                            guarantorList.remove(selectedGuarantor);
                            guarantorListModel.remove(index);
                        }
                    }
                }
            }
        });

        // Button to open guarantor selection dialog
        JButton selectGuarantorButton = new JButton("Select Guarantors");
        selectGuarantorButton.setBounds(490, 380, 180, 25);
        selectGuarantorButton.setForeground(Color.BLACK);
        selectGuarantorButton.setBackground(Color.WHITE);
        selectGuarantorButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(selectGuarantorButton);

        JLabel loanRepaymentAmountLabel = new JLabel("Loan Repayment Amount:");
        loanRepaymentAmountLabel.setBounds(180, 430, 250, 25);
        loanRepaymentAmountLabel.setForeground(Color.YELLOW);
        panel.add(loanRepaymentAmountLabel);
        JTextField loanRepaymentAmountField = new JTextField(15);
        loanRepaymentAmountField.setBounds(180, 455, 300, 25);
        loanRepaymentAmountField.setEditable(false);
        loanRepaymentAmountField.setForeground(Color.BLACK);
        loanRepaymentAmountField.setBackground(Color.WHITE);
        panel.add(loanRepaymentAmountField);

        JLabel loanLimitLabel = new JLabel("Loan Limit:");
        loanLimitLabel.setBounds(180, 480, 250, 25);
        loanLimitLabel.setForeground(Color.YELLOW);
        panel.add(loanLimitLabel);
        JTextField loanLimitField = new JTextField(15);
        loanLimitField.setBounds(180, 505, 300, 25);
        loanLimitField.setEditable(false);
        loanLimitField.setForeground(Color.BLACK);
        loanLimitField.setBackground(Color.WHITE);
        panel.add(loanLimitField);

        JLabel guaranteedAmountLabel = new JLabel("Guaranteed Amount:");
        guaranteedAmountLabel.setBounds(180, 530, 250, 25);
        guaranteedAmountLabel.setForeground(Color.YELLOW);
        panel.add(guaranteedAmountLabel);
        JTextField guaranteedAmountField = new JTextField(15);
        guaranteedAmountField.setBounds(180, 555, 300, 25);
        guaranteedAmountField.setEditable(false);
        guaranteedAmountField.setForeground(Color.BLACK);
        guaranteedAmountField.setBackground(Color.WHITE);
        panel.add(guaranteedAmountField);

        JLabel loanDateLabel = new JLabel("Loan Application Date:");
        loanDateLabel.setBounds(180, 580, 250, 25);
        loanDateLabel.setForeground(Color.YELLOW);
        panel.add(loanDateLabel);
        JTextField loanDateField = new JTextField(15);
        loanDateField.setBounds(180, 605, 300, 25);
        loanDateField.setEditable(false);
        loanDateField.setForeground(Color.BLACK);
        loanDateField.setBackground(Color.WHITE);
        panel.add(loanDateField);

        // Get the current date and time from the system
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        loanDateField.setText(formattedDateTime);

        // Button ActionListener to open the selection dialog
        selectGuarantorButton.addActionListener(e -> {
            String memberId = memberIdField.getText();
            String memberFullName = FedhaDatabase.getMemberFullName(memberId);
            List<String> members = FedhaDatabase.getAllMembers();

            // Remove the loan applicant and already selected guarantors from the list of possible guarantors
            members.removeIf(member -> member.equalsIgnoreCase(memberFullName) || guarantorList.contains(member));
            String[] memberArray = members.toArray(new String[0]);

            // Create a JList for multiple selections
            JList<String> memberList = new JList<>(memberArray);
            memberList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            // Show a dialog with the JList
            int result = JOptionPane.showConfirmDialog(Loan.this, new JScrollPane(memberList),
                    "Select Guarantors", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                // Get selected members
                List<String> selectedMembers = memberList.getSelectedValuesList();
                // Add selected members to the guarantor list and remove them from the available list
                guarantorList.addAll(selectedMembers);
                // Sort the guarantor list
                Collections.sort(guarantorList);

                // Update the JList and ensure previous selections are retained
                guarantorListModel.clear();
                for (String member : guarantorList) {
                    guarantorListModel.addElement(member);
                }
                // Refresh available members by removing selected guarantors
                for (String selectedMember : selectedMembers) {
                    members.remove(selectedMember);
                }
            }
        });


        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(290, 650, 100, 25);
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.setBackground(Color.WHITE);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnSubmit);

        // Add ActionListener to loanTypeCombo
        loanTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLoanType = loanTypeCombo.getSelectedItem().toString();

                // Automatically set interest rate and repayment period based on loan type
                if (selectedLoanType.equals("Emergency Loan")) {
                    interestRateField.setText("0.3%");
                    repaymentPeriodField.setText("1 year");
                } else if (selectedLoanType.equals("Short Loan")) {
                    interestRateField.setText("0.6%");
                    repaymentPeriodField.setText("2 years");
                } else if (selectedLoanType.equals("Normal Loan")) {
                    interestRateField.setText("1.0%");
                    repaymentPeriodField.setText("3 years");
                } else if (selectedLoanType.equals("Development Loan")) {
                    interestRateField.setText("1.4%");
                    repaymentPeriodField.setText("4 years");
                }

                // Calculate the loan repayment amount based on simple interest
//                try {
//                    double loanAmount = Double.parseDouble(loanAmountField.getText());
//                    double interestRate = Double.parseDouble(interestRateField.getText().replace("%", "")) / 100;
//                    int repaymentPeriod = Integer.parseInt(repaymentPeriodField.getText().replace(" years", "").replace(" year", ""));
//
//                    // Simple interest formula: Interest = Principal * Rate * Time
//                    double interest = loanAmount * interestRate * repaymentPeriod;
//                    double totalRepayment = loanAmount + interest;
//
//                    // Display the calculated total repayment amount
//                    loanRepaymentAmountField.setText(String.format("%.2f", totalRepayment));
//                } catch (NumberFormatException ex) {
//                    JOptionPane.showMessageDialog(Loan.this, "Please enter a valid loan amount.");
//                }
                try {
                    // Parse user input
                    double loanAmount = Double.parseDouble(loanAmountField.getText());
                    double interestRate = Double.parseDouble(interestRateField.getText().replace("%", "")) / 100;
                    int repaymentPeriod = Integer.parseInt(repaymentPeriodField.getText().replace(" years", "").replace(" year", ""));
                    int timesCompounded = 4; // Assuming quarterly compounding

                    // Compound Interest calculation
                    double totalRepayment = loanAmount * Math.pow((1 + interestRate / timesCompounded), timesCompounded * repaymentPeriod);

                    // Display the calculated total repayment amount
                    loanRepaymentAmountField.setText(String.format("%.2f", totalRepayment));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Loan.this, "Please enter valid inputs for loan amount, interest rate, and repayment period.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Loan.this, "An unexpected error occurred: " + ex.getMessage());
                }
                // Calculate the loan due date based on the selected repayment period
                LocalDateTime loanDateTime = LocalDateTime.now();
                int yearsToAdd = 0;
                if(repaymentPeriodField.getText().equals("1 year")) {
                    yearsToAdd = 1;
                } else if (repaymentPeriodField.getText().equals("2 years")) {
                    yearsToAdd = 2;
                } else if (repaymentPeriodField.getText().equals("3 years")) {
                    yearsToAdd = 3;
                } else if (repaymentPeriodField.getText().equals("4 years")) {
                    yearsToAdd = 4;
                }
                // Calculate the due date by adding the selected years
                LocalDateTime dueDate = loanDateTime.plusYears(yearsToAdd);
                String formattedDueDate = dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                loanDueField.setText(formattedDueDate);
            }
        });

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberId = memberIdField.getText();
                double loanAmount;
                double loanLimit;
                double guarantorAmount;
                try {
                    loanAmount = Double.parseDouble(loanAmountField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Loan.this, "Please enter a valid numeric amount.");
                    return;
                }
                String loanType = loanTypeCombo.getSelectedItem().toString();
                String interestRate = interestRateField.getText();
                String repaymentPeriod = repaymentPeriodField.getText();
                String loanDue = loanDueField.getText();
                String guarantors = String.join(", ", guarantorList);
                String loanRepaymentAmount = loanRepaymentAmountField.getText();
                String loanDate = loanDateField.getText();

                // Check for empty fields
                if (memberId.isEmpty() || loanAmountField.getText().isEmpty() || guarantorList.isEmpty() ||
                        loanDate.isEmpty() || loanDue.isEmpty() || loanRepaymentAmount.isEmpty()) {
                    JOptionPane.showMessageDialog(Loan.this, "Please fill all fields and select guarantors.");
                    return;
                }

                // Check if member is eligible for the loan
                if (!FedhaDatabase.isEligibleForLoan(memberId)) {
                    JOptionPane.showMessageDialog(Loan.this, "You are not eligible for a loan. " +
                            "You must contribute for at least six consecutive months.");
                    return;
                }

                // Retrieve the borrower's total shares
                double borrowerShares = FedhaDatabase.getTotalShares(memberId);

                // Determine the maximum allowed loan amount based on loan type
                double maxLoanAmount = 0;
                switch (loanType) {
                    case "Emergency Loan":
                        maxLoanAmount = borrowerShares;
                        break;
                    case "Short Loan":
                        maxLoanAmount = borrowerShares * 2;
                        break;
                    case "Normal Loan":
                        maxLoanAmount = borrowerShares * 3;
                        break;
                    case "Development Loan":
                        maxLoanAmount = borrowerShares * 5;
                        break;
                    default:
                        JOptionPane.showMessageDialog(Loan.this, "Invalid loan type selected.");
                        return;
                }
                // Display the calculated loan limit
                loanLimitField.setText(String.format("%.2f", maxLoanAmount));

                // Check if the requested loan amount exceeds the maximum allowed for the loan type
                if (loanAmount > maxLoanAmount) {
                    JOptionPane.showMessageDialog(Loan.this, "Loan amount exceeds the maximum allowed for this loan type. " +
                            "The maximum loan amount is: " + maxLoanAmount);
                    return;
                }

                // Calculate the required guarantor amount: Loan amount minus the borrower’s shares
                double requiredGuarantorAmount = loanAmount - borrowerShares;

                // Calculate total deposits of guarantors from their fixed deposits in the database
                double totalGuarantorDeposits = 0;
                for (String guarantor : guarantorList) {
                    // Assuming getFixedDeposits method exists in FedhaDatabase to fetch each guarantor's fixed deposit
                    totalGuarantorDeposits += FedhaDatabase.getFixedDeposits(guarantor);
                }

                // Display the total guarantor deposits
                guaranteedAmountField.setText(String.format("%.2f", totalGuarantorDeposits));

                // Check if the combined guarantors' deposits meet the required amount
                if (totalGuarantorDeposits < requiredGuarantorAmount) {
                    JOptionPane.showMessageDialog(Loan.this, "Guarantors' total deposits are insufficient to guarantee this loan.");
                    return;
                }

                try {
                    // Check if the memberId exists in the database
                    if (!FedhaDatabase.isMemberIdExists(memberId)) {
                        JOptionPane.showMessageDialog(Loan.this, "You must be a member to apply for a loan.");
                        Members members = new Members();
                        members.setVisible(true);
                        return;
                    }

                    // Insert the loan details
                    FedhaDatabase.insertLoan(memberId, loanAmount, loanType, interestRate, repaymentPeriod, loanDue, guarantors, loanRepaymentAmount, maxLoanAmount, totalGuarantorDeposits, loanDate);
                    JOptionPane.showMessageDialog(Loan.this, "Loan successfully applied");
                    Menubar menubar = new Menubar();
                    menubar.setVisible(true);
                    Loan.this.dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Loan.this, "An error occurred while applying for the loan.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Loan();
            }
        });
    }
}
