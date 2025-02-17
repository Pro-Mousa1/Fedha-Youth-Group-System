import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

public class AdminBar extends JFrame {
    private JTextField totalRegistrationField;
    private JTextField totalSharesField;
    private JTextField loanInterestField;
    private JTextField fixedDepositInterestField;
    private JTextField amountRetainedField;
    private JTextField adminIdField;
    private JTextField fixedDepositsAmountField;
    private JTextField durationField;
    private JTextField fixedDepositsDateField;
    private JPanel mainPanel;
    private final String adminPassword = "";

    private Connection connection;

    public AdminBar() {
        setTitle("Fedha Youth Group Report");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);

        // Connect to database
        connectToDatabase();

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create menu
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        JMenuItem menuItemSignOut = new JMenuItem("Logout");
        menu.add(menuItemSignOut);
        menuItemSignOut.addActionListener(e -> {
            new LoginAdmin().setVisible(true);
            AdminBar.this.dispose();
        });
        // Sidebar panel for list items with grey background
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(150, getHeight()));
        sidebarPanel.setBackground(new Color(0x127335));

        JLabel lblMembers = createSidebarLabel("Members");
        JLabel lblTypeOfLoans = createSidebarLabel("Type of Loans");
        JLabel lblLoansBorrowed = createSidebarLabel("Loans Debt");
        JLabel lblFixedDeposits = createSidebarLabel("Fixed Deposits");
        JLabel lblGuarantors = createSidebarLabel("Guarantors");
        JLabel lblShares = createSidebarLabel("Shares");
        JLabel lblReports = createSidebarLabel("Reports");
        JLabel lblCompute = createSidebarLabel("Compute");
        JLabel lblPermission = createSidebarLabel("Permission");

        sidebarPanel.add(lblMembers);
        sidebarPanel.add(lblTypeOfLoans);
        sidebarPanel.add(lblLoansBorrowed);
        sidebarPanel.add(lblFixedDeposits);
        sidebarPanel.add(lblGuarantors);
        sidebarPanel.add(lblShares);
        sidebarPanel.add(lblReports);
        sidebarPanel.add(lblCompute);
        sidebarPanel.add(lblPermission);

        mainPanel = new JPanel(new BorderLayout());
        add(sidebarPanel, BorderLayout.WEST);
        mainPanel.setBackground(new Color(0x6CC688));
        add(mainPanel, BorderLayout.CENTER);

        // Action listeners for each label to display data in the main panel
        lblMembers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayMembersList();
            }
        });

        lblTypeOfLoans.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayTypeOfLoansList();
            }
        });

        lblLoansBorrowed.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayLoansDebtList();
            }
        });

        lblFixedDeposits.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayFixedDepositList();
            }
        });

        lblGuarantors.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayGuarantorsList();
            }
        });

        lblShares.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ask the admin to input a memberId
                String memberIdInput = JOptionPane.showInputDialog(mainPanel, "Enter Member ID to check deposits:");
                if (memberIdInput != null && !memberIdInput.isEmpty()) {
                    displayMemberSharesList(Integer.parseInt(memberIdInput));
                }
            }
        });

        lblReports.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ask the admin to input a memberId
                String memberIdInput = JOptionPane.showInputDialog(mainPanel, "Enter Member ID to check other Reports:");
                if (memberIdInput != null && !memberIdInput.isEmpty()) {
                    displayOtherReportsList(memberIdInput);
                }
            }
        });

        lblCompute.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                computeFinanceCalcuation();
            }
        });

        lblPermission.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                permissionAccess();
            }
        });
    }

    private JLabel createSidebarLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setOpaque(true);
                label.setBackground(new Color(0x57E340));
                label.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setOpaque(false);
                label.repaint();
            }
        });
        return label;
    }

    private void displayMembersList() {
        mainPanel.removeAll();
        JTable membersTable = new JTable(new DefaultTableModel(new String[]{"Member ID", "Surname", "Other Names", "Registration Fees"}, 0));
        fetchMembersData(membersTable);

        JScrollPane scrollPane = new JScrollPane(membersTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton btnAddMember = new JButton("Add Member");
        JButton btnEditMember = new JButton("Edit Member");
        JButton btnDeleteMember = new JButton("Delete Member");

        bottomPanel.add(btnAddMember);
        bottomPanel.add(btnEditMember);
        bottomPanel.add(btnDeleteMember);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        btnAddMember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Members membersScreen = new Members();
                membersScreen.setVisible(true);

                membersScreen.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        setVisible(true);
                    }
                });

                // Hide AdminBar while Members is open
                setVisible(false);
            }
        });

        btnEditMember.addActionListener(e -> {
            int selectedRow = membersTable.getSelectedRow();
            if (selectedRow != -1) {
                if (verifyAdminPassword()) {
                    String memberId = membersTable.getValueAt(selectedRow, 0).toString();
                    editMember(memberId);  // Edit the selected member details
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member to edit.");
            }
        });

        btnDeleteMember.addActionListener(e -> {
            int selectedRow = membersTable.getSelectedRow();
            if (selectedRow != -1) {
                if (verifyAdminPassword()) {
                    String memberId = membersTable.getValueAt(selectedRow, 0).toString();
                    deleteMember(memberId);  // Delete the selected member
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member to delete.");
            }
        });

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void displayLoansDebtList() {
        mainPanel.removeAll();
        JTable loansTable = new JTable(new DefaultTableModel(new String[]{"Member ID", "Surname", "Other Names", "Total Loan","Repayment Amount","Loan Balance"}, 0));
        fetchLoansDebtData(loansTable);
        JScrollPane scrollPane = new JScrollPane(loansTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton btnEditLoan = new JButton("Edit Loan");
        JButton btnDeleteLoan = new JButton("Delete Loan");

        bottomPanel.add(btnEditLoan);
        bottomPanel.add(btnDeleteLoan);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        btnEditLoan.addActionListener(e -> {
            int selectedRow = loansTable.getSelectedRow();
            if (selectedRow != -1) {
                if (verifyAdminPassword()) {
                    String memberId = loansTable.getValueAt(selectedRow, 0).toString();
                    editLoan(memberId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a loan to edit.");
            }
        });

        btnDeleteLoan.addActionListener(e -> {
            int selectedRow = loansTable.getSelectedRow();
            if (selectedRow != -1) {
                if (verifyAdminPassword()) {
                    String memberId = loansTable.getValueAt(selectedRow, 0).toString();
                    deleteLoan(memberId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a loan to delete.");
            }
        });

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void displayTypeOfLoansList() {
        mainPanel.removeAll();
        DefaultTableModel model = new DefaultTableModel(new String[]{"Type of Loan", "Interest Rate", "Repayment Period"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        JTable loansTable = new JTable(model);
        FedhaDatabase fetcher = new FedhaDatabase();
        fetcher.fetchTypeOfLoansData(model);
        loansTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                fetcher.updateTypeOfLoan(model, row, column);
            }
        });
        JScrollPane scrollPane = new JScrollPane(loansTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

    }
    private void displayFixedDepositList() {
        mainPanel.removeAll();

        JTable fixedDepositTable = new JTable(new DefaultTableModel(new String[]{"Authentication Number", "Fixed Deposit Amount", "Fixed Deposit Duration", "Fixed Deposit Date","Fixed Deposit Interest"}, 0));
        fetchFixedDepositsData(fixedDepositTable);

        JScrollPane scrollPane = new JScrollPane(fixedDepositTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton btnFixedDeposit = new JButton("Fixed Deposit");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");

        bottomPanel.add(btnFixedDeposit);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        btnFixedDeposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayFixedDeposits();
            }
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = fixedDepositTable.getSelectedRow();
            if (selectedRow != -1) {
                if (verifyAdminPassword()) {
                    String memberId = fixedDepositTable.getValueAt(selectedRow, 0).toString();
                    editFixedDeposit(memberId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a fixed deposit to edit.");
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = fixedDepositTable.getSelectedRow();
            if (selectedRow != -1) {
                if (verifyAdminPassword()) {
                    String memberId = fixedDepositTable.getValueAt(selectedRow, 0).toString();
                    deleteFixedDeposit(memberId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member to delete.");
            }
        });

        mainPanel.revalidate();
        mainPanel.repaint();
    }
    private void displayFixedDeposits() {
        JFrame fixedDepositsFrame = new JFrame("Fixed Deposits");
        fixedDepositsFrame.setSize(550, 380);
        fixedDepositsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fixedDepositsFrame.setLocationRelativeTo(null);

        JPanel fixedDepositsPanel = new JPanel();
        fixedDepositsPanel.setLayout(null);
        fixedDepositsPanel.setBackground(new Color(0x0597E6));
        fixedDepositsFrame.add(fixedDepositsPanel);

        JLabel adminIdLabel = new JLabel("Admin ID:");
        adminIdLabel.setBounds(180, 5, 200, 25);
        adminIdLabel.setForeground(Color.YELLOW);
        fixedDepositsPanel.add(adminIdLabel);
        JTextField adminIdField = new JTextField(15);
        adminIdField.setBounds(180, 30, 300, 25);
        fixedDepositsPanel.add(adminIdField);

        JLabel fixedDepositsAmountLabel = new JLabel("Fixed Deposits Amount:");
        fixedDepositsAmountLabel.setBounds(180, 60, 200, 25);
        fixedDepositsAmountLabel.setForeground(Color.YELLOW);
        fixedDepositsPanel.add(fixedDepositsAmountLabel);
        JTextField fixedDepositsAmountField = new JTextField(15);
        fixedDepositsAmountField.setBounds(180, 85, 300, 25);
        fixedDepositsPanel.add(fixedDepositsAmountField);

        JLabel durationLabel = new JLabel("Duration (years):");
        durationLabel.setBounds(180, 115, 200, 25);
        durationLabel.setForeground(Color.YELLOW);
        fixedDepositsPanel.add(durationLabel);

        JTextField durationField = new JTextField(15);
        durationField.setBounds(180, 140, 300, 25);
        fixedDepositsPanel.add(durationField);

        JLabel fixedDepositInterestLabel = new JLabel("Fixed Deposit Interest:");
        fixedDepositInterestLabel.setBounds(180, 170, 200, 25);
        fixedDepositInterestLabel.setForeground(Color.YELLOW);
        fixedDepositsPanel.add(fixedDepositInterestLabel);
        JTextField fixedDepositInterestField = new JTextField(15);
        fixedDepositInterestField.setBounds(180, 195, 300, 25);
        fixedDepositInterestField.setEditable(false); // Interest is calculated, not entered
        fixedDepositsPanel.add(fixedDepositInterestField);

        JLabel fixedDepositsDateLabel = new JLabel("Fixed Deposits Date:");
        fixedDepositsDateLabel.setBounds(180, 225, 150, 25);
        fixedDepositsDateLabel.setForeground(Color.YELLOW);
        fixedDepositsPanel.add(fixedDepositsDateLabel);
        JTextField fixedDepositsDateField = new JTextField(15);
        fixedDepositsDateField.setBounds(180, 250, 300, 25);
        fixedDepositsDateField.setEditable(false);
        fixedDepositsPanel.add(fixedDepositsDateField);

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        fixedDepositsDateField.setText(formattedDateTime);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(250, 290, 100, 30);
        fixedDepositsPanel.add(btnSubmit);

        // Method to calculate and display interest
        Runnable calculateInterest = () -> {
            try {
                String amountText = fixedDepositsAmountField.getText();
                String durationText = durationField.getText();

                if (!amountText.isEmpty() && !durationText.isEmpty()) {
                    double amount = Double.parseDouble(amountText);
                    int durationInYears = Integer.parseInt(durationText);
                    double interestRatePerMonth = 0.006; // 0.6% per month
                    double totalInterest = amount * interestRatePerMonth * (durationInYears * 12);
                    fixedDepositInterestField.setText(String.format("%.2f", totalInterest));
                } else {
                    fixedDepositInterestField.setText(""); // Clear field if inputs are incomplete
                }
            } catch (NumberFormatException ex) {
                fixedDepositInterestField.setText("Invalid input");
            }
        };

        // Add DocumentListeners to trigger interest calculation dynamically
        fixedDepositsAmountField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateInterest.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateInterest.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateInterest.run();
            }
        });

        durationField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateInterest.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateInterest.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateInterest.run();
            }
        });

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String authenticationNumber = adminIdField.getText();
                String fixedDepositsAmount = fixedDepositsAmountField.getText();
                String fixedDepositsDuration = durationField.getText();
                String fixedDepositsInterest = fixedDepositInterestField.getText();
                String fixedDepositsDate = fixedDepositsDateField.getText();

                if (authenticationNumber.isEmpty() || fixedDepositsAmount.isEmpty() || fixedDepositsDuration.isEmpty()
                        || fixedDepositsInterest.isEmpty() || fixedDepositsDate.isEmpty()) {
                    JOptionPane.showMessageDialog(fixedDepositsFrame, "Please fill all the fields.");
                    return;
                }
                try {
                    if (!FedhaDatabase.isAdminIdExists(authenticationNumber)) {
                        JOptionPane.showMessageDialog(fixedDepositsFrame, "You must be an admin to deposit");
                        return;
                    }

                    if (FedhaDatabase.hasAlreadyFixedDeposited(authenticationNumber, currentDateTime.getYear(), currentDateTime.getMonthValue())) {
                        JOptionPane.showMessageDialog(fixedDepositsFrame, "You can only deposit once per month");
                        return;
                    }
                    FedhaDatabase.insertFixedDeposits(authenticationNumber, fixedDepositsAmount, fixedDepositsDuration, fixedDepositsInterest, fixedDepositsDate);
                    JOptionPane.showMessageDialog(fixedDepositsFrame, "Fixed Deposits successful");
                    fixedDepositsFrame.dispose();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        fixedDepositsFrame.setVisible(true);
    }



    private void displayGuarantorsList() {
        mainPanel.removeAll();

        JTable guarantorTable = new JTable(new DefaultTableModel(new String[]{"Member ID", "Guarantor"}, 0));
        fetchGuarantorsData(guarantorTable);

        JScrollPane scrollPane = new JScrollPane(guarantorTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton btnEditGuarantor = new JButton("Edit Guarantor");
        JButton btnDeleteGuarantor = new JButton("Delete Guarantor");

        bottomPanel.add(btnEditGuarantor);
        bottomPanel.add(btnDeleteGuarantor);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        btnEditGuarantor.addActionListener(e -> {
            int selectedRow = guarantorTable.getSelectedRow();
            if (selectedRow != -1) {
                if (verifyAdminPassword()) {
                    String memberId = guarantorTable.getValueAt(selectedRow, 0).toString();
                    editGuarantor(memberId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a guarantor to edit.");
            }
        });

        btnDeleteGuarantor.addActionListener(e -> {
            int selectedRow = guarantorTable.getSelectedRow();
            if (selectedRow != -1) {
                if (verifyAdminPassword()) {
                    String memberId = guarantorTable.getValueAt(selectedRow, 0).toString();
                    deleteGuarantor(memberId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a guarantor to delete.");
            }
        });

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void displayMemberSharesList(int memberId) {
        mainPanel.removeAll();
        JTable memberSharesTable = new JTable(new DefaultTableModel(new String[]{"Member ID", "Surname", "Other Names", "Deposit Amount", "Deposit Date"}, 0));

        // Pass the memberId to the method that fetches the data
        fetchMemberSharesData(memberSharesTable, memberId);

        JScrollPane scrollPane = new JScrollPane(memberSharesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    private void displayOtherReportsList(String memberId) {
        mainPanel.removeAll();
        JTable otherReportsTable = new JTable(new DefaultTableModel(new String[]{"Member ID","Monthly Repayment", "Loan Limit", "Dividends", "Guaranteed Amount"}, 0));
        fetchOtherReports(otherReportsTable, memberId);
        JScrollPane scrollPane = new JScrollPane(otherReportsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    private void fetchFinancialData() {
        String url = "jdbc:mysql://localhost:3306/fedha_youth_group_schema";
        String user = "root";
        String password = "Mousa@muigai123!";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Query to get the total registration amount
            String registrationQuery = "SELECT SUM(registrationFees) AS totalRegistration FROM members";
            try (PreparedStatement registrationStmt = connection.prepareStatement(registrationQuery);
                 ResultSet registrationRs = registrationStmt.executeQuery()) {
                if (registrationRs.next()) {
                    double totalRegistration = registrationRs.getDouble("totalRegistration");
                    totalRegistrationField.setText(String.format("%.2f", totalRegistration));
                }
            }

            // Query to get the total shares contribution
            String sharesQuery = "SELECT SUM(sharesContribution) AS totalShares FROM deposits";
            try (PreparedStatement sharesStmt = connection.prepareStatement(sharesQuery);
                 ResultSet sharesRs = sharesStmt.executeQuery()) {
                if (sharesRs.next()) {
                    double totalShares = sharesRs.getDouble("totalShares");
                    totalSharesField.setText(String.format("%.2f", totalShares));
                }
            }

            // Query to compute loan interest
            String loanInterestQuery =
                    "SELECT (SUM(loanRepaymentAmount) - SUM(loanAmount)) AS totalLoanInterest FROM loans";
            double totalLoanInterest = 0.0;
            try (PreparedStatement loanInterestStmt = connection.prepareStatement(loanInterestQuery);
                 ResultSet loanInterestRs = loanInterestStmt.executeQuery()) {
                if (loanInterestRs.next()) {
                    totalLoanInterest = loanInterestRs.getDouble("totalLoanInterest");
                    loanInterestField.setText(String.format("%.2f", totalLoanInterest));
                }
            }

            // Query to get the total fixed deposits interest
            String fixedDepositsQuery = "SELECT SUM(fixedDepositsInterest) AS totalFixedDepositsInterest FROM fixedDeposits";
            double totalFixedDepositsInterest = 0.0;
            try (PreparedStatement fixedDepositsStmt = connection.prepareStatement(fixedDepositsQuery);
                 ResultSet fixedDepositsRs = fixedDepositsStmt.executeQuery()) {
                if (fixedDepositsRs.next()) {
                    totalFixedDepositsInterest = fixedDepositsRs.getDouble("totalFixedDepositsInterest");
                    fixedDepositInterestField.setText(String.format("%.2f", totalFixedDepositsInterest));
                }
            }

            // Calculate and display amount retained
            double amountRetained = 0.1 * (totalLoanInterest + totalFixedDepositsInterest);
            amountRetainedField.setText(String.format("%.2f", amountRetained));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching financial data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void computeFinanceCalcuation() {
        JFrame computeFrame = new JFrame("Finance Calculation");
        computeFrame.setSize(600, 400);
        computeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        computeFrame.setLocationRelativeTo(null);

        // Panel with null layout
        JPanel computePanel = new JPanel();
        computePanel.setLayout(null);
        computePanel.setBackground(new Color(0x0597E6));
        computeFrame.add(computePanel);

        JLabel totalRegistrationLabel = new JLabel("Total Registration Amount:");
        totalRegistrationLabel.setBounds(180, 5, 200, 25);
        totalRegistrationLabel.setForeground(Color.YELLOW);
        computePanel.add(totalRegistrationLabel);

        totalRegistrationField = new JTextField(15);
        totalRegistrationField.setBounds(180, 30, 300, 25);
        totalRegistrationField.setEditable(false);
        computePanel.add(totalRegistrationField);

        JLabel totalSharesLabel = new JLabel("Total Shares:");
        totalSharesLabel.setBounds(180, 60, 200, 25);
        totalSharesLabel.setForeground(Color.YELLOW);
        computePanel.add(totalSharesLabel);

        totalSharesField = new JTextField(15);
        totalSharesField.setBounds(180, 85, 300, 25);
        totalSharesField.setEditable(false);
        computePanel.add(totalSharesField);

        // Loan Interest Label and TextField
        JLabel loanInterestLabel = new JLabel("Loan Interest:");
        loanInterestLabel.setBounds(180, 115, 150, 25);
        loanInterestLabel.setForeground(Color.YELLOW);
        computePanel.add(loanInterestLabel);

        loanInterestField = new JTextField(15);
        loanInterestField.setBounds(180, 140, 300, 25);
        loanInterestField.setEditable(false);
        computePanel.add(loanInterestField);

        JLabel fixedDepositsLabel = new JLabel("Fixed Deposits:");
        fixedDepositsLabel.setBounds(180, 170, 150, 25);
        fixedDepositsLabel.setForeground(Color.YELLOW);
        computePanel.add(fixedDepositsLabel);
        fixedDepositInterestField = new JTextField(15);
        fixedDepositInterestField.setBounds(180, 195, 300, 25);
        computePanel.add(fixedDepositInterestField);

        JLabel amountRetainedLabel = new JLabel("Amount Retained:");
        amountRetainedLabel.setBounds(180, 225, 150, 25);
        amountRetainedLabel.setForeground(Color.YELLOW);
        computePanel.add(amountRetainedLabel);
        amountRetainedField = new JTextField(15);
        amountRetainedField.setBounds(180, 250, 300, 25);
        computePanel.add(amountRetainedField);

        // Compute Button
        JButton btnBack = new JButton("BACK");
        btnBack.setBounds(270, 295, 80, 30);
        computePanel.add(btnBack);

        try {
            double registrationAmount = Double.parseDouble(totalRegistrationField.getText());
            double sharesAmount = Double.parseDouble(totalSharesField.getText());
            double fixedDepositInterest = Double.parseDouble(fixedDepositInterestField.getText());
            double amountRetained = Double.parseDouble(amountRetainedField.getText());

            double loanInterest = registrationAmount + sharesAmount + fixedDepositInterest - amountRetained;
            loanInterestField.setText(String.format("%.2f", loanInterest));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"Financial Report");
        }
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminBar.this.setVisible(true);
                computeFrame.setVisible(false);
            }
        });
        fetchFinancialData(); // Fetch and populate data
        computeFrame.setVisible(true);
    }
    private void permissionAccess() {
        // Initialize mainPanel and other UI components
        mainPanel.removeAll();
        JTable permissionAccessTable = new JTable(new DefaultTableModel(
                new String[]{"Member ID", "FullName", "Loan Amount", "Guaranteed Loans",
                        "Exit Reason", "Notice Date", "Exit Date", "Approval"}, 0));
        fetchMembersExit(permissionAccessTable);
        JScrollPane scrollPane = new JScrollPane(permissionAccessTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton btnReject = new JButton("REJECT");
        JButton btnGrant = new JButton("GRANT");

        bottomPanel.add(btnReject);
        bottomPanel.add(btnGrant);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        btnReject.addActionListener(e -> {
            int selectedRow = permissionAccessTable.getSelectedRow();
            if (selectedRow != -1) {
                if (verifyAdminPassword()) {
                    String memberId = permissionAccessTable.getValueAt(selectedRow, 0).toString();

                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to reject this request and delete the member's data?",
                            "Confirm Rejection",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Update status in the table
                        updateApprovalStatus(permissionAccessTable, selectedRow, "Denied");

                        // Update status in the database
                        FedhaDatabase.updateExitApproval(memberId, "Denied");

                        // Delete the member's data from the database
                        if (FedhaDatabase.deleteExitRequest(memberId)) {
                            JOptionPane.showMessageDialog(this, "Member's data deleted successfully.");

                            // Reset auto-increment for idexitRequests column
                            FedhaDatabase.resetAutoIncrement("exitRequests");

                            // Remove row from the table
                            DefaultTableModel model = (DefaultTableModel) permissionAccessTable.getModel();
                            model.removeRow(selectedRow);
                        } else {
                            JOptionPane.showMessageDialog(this, "Error deleting member's data.");
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member to reject.");
            }
        });

        btnGrant.addActionListener(e -> {
            int selectedRow = permissionAccessTable.getSelectedRow();
            if (selectedRow != -1) {
                if (verifyAdminPassword()) {
                    String memberId = permissionAccessTable.getValueAt(selectedRow, 0).toString();
                    updateApprovalStatus(permissionAccessTable, selectedRow, "Granted");
                    FedhaDatabase.updateExitApproval(memberId, "Granted");
                    JOptionPane.showMessageDialog(this, "Approval status updated to 'Granted'.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member to grant.");
            }
        });

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void updateApprovalStatus(JTable table, int row, String approval) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setValueAt(approval, row, 9);
    }
    private boolean verifyAdminPassword() {
        String inputPassword = JOptionPane.showInputDialog(this, "Enter Admin Password:");
        return adminPassword.equals(inputPassword);
    }

    private void editMember(String memberId) {
        try {
            // Fetch current details of the member
            PreparedStatement fetchStatement = connection.prepareStatement("SELECT * FROM members WHERE memberId = ?");
            fetchStatement.setString(1, memberId);
            ResultSet resultSet = fetchStatement.executeQuery();

            if (resultSet.next()) {
                // Populate details in dialog for editing
                String currentSurname = resultSet.getString("surname");
                String currentOtherNames = resultSet.getString("otherNames");
                String currentRegistrationFees = resultSet.getString("registrationFees");

                String newSurname = JOptionPane.showInputDialog(this, "Edit Surname:", currentSurname);
                String newOtherNames = JOptionPane.showInputDialog(this, "Edit Other Names:", currentOtherNames);
                String newRegistrationFees = JOptionPane.showInputDialog(this, "Edit Registration Fees:", currentRegistrationFees);

                if (newSurname != null && newOtherNames != null && newRegistrationFees != null) {
                    // Update the database with new details
                    PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE members SET surname = ?, otherNames = ?, registrationFees = ? WHERE memberId = ?");
                    updateStatement.setString(1, newSurname);
                    updateStatement.setString(2, newOtherNames);
                    updateStatement.setString(3, newRegistrationFees);
                    updateStatement.setString(4, memberId);
                    updateStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Member details updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Update canceled.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Member not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while updating member details.");
        }
    }
    private void deleteMember(String memberId) {
        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this member?",
                "Delete Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                // Delete the member with the specified memberId
                PreparedStatement statement = connection.prepareStatement("DELETE FROM members WHERE memberId = ?");
                statement.setString(1, memberId);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Reset the auto-increment value
                    PreparedStatement resetAutoIncrementStatement = connection.prepareStatement(
                            "ALTER TABLE members AUTO_INCREMENT = (SELECT IFNULL(MAX(memberId), 0) + 1 FROM members)"
                    );
                    resetAutoIncrementStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Member deleted and ID sequence reset successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Member not found or could not be deleted.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while deleting member or resetting ID sequence.");
            }
        }
    }

    private void editLoan(String memberId) {
        try {
            // Fetch current loan details for the member
            PreparedStatement fetchStatement = connection.prepareStatement(
                    "SELECT loans.loanId, loans.loanRepaymentAmount, monthlyRepayment.RepaymentAmount, monthlyRepayment.loanBalance " +
                            "FROM loans " +
                            "JOIN monthlyRepayment ON loans.memberId = monthlyRepayment.memberId " +
                            "WHERE loans.memberId = ?");
            fetchStatement.setString(1, memberId);
            ResultSet resultSet = fetchStatement.executeQuery();

            if (resultSet.next()) {
                String currentLoanRepaymentAmount = resultSet.getString("loanRepaymentAmount");
                String currentRepaymentAmount = resultSet.getString("RepaymentAmount");
                String currentLoanBalance = resultSet.getString("loanBalance");

                // Prompt user for new values
                String newLoanRepaymentAmount = JOptionPane.showInputDialog(this, "Edit Loan Repayment Amount:", currentLoanRepaymentAmount);
                String newRepaymentAmount = JOptionPane.showInputDialog(this, "Edit Monthly Repayment Amount:", currentRepaymentAmount);
                String newLoanBalance = JOptionPane.showInputDialog(this, "Edit Loan Balance:", currentLoanBalance);

                if (newLoanRepaymentAmount != null && newRepaymentAmount != null && newLoanBalance != null) {
                    // Update the loan details in the database
                    PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE loans " +
                                    "JOIN monthlyRepayment ON loans.memberId = monthlyRepayment.memberId " +
                                    "SET loans.loanRepaymentAmount = ?, monthlyRepayment.RepaymentAmount = ?, monthlyRepayment.loanBalance = ? " +
                                    "WHERE loans.memberId = ?");
                    updateStatement.setString(1, newLoanRepaymentAmount);
                    updateStatement.setString(2, newRepaymentAmount);
                    updateStatement.setString(3, newLoanBalance);
                    updateStatement.setString(4, memberId);
                    updateStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Loan details updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Update canceled.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No loan found for this member.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while updating loan details.");
        }
    }

    private void deleteLoan(String memberId) {
        try {
            // Delete the loan for the specified memberId
            PreparedStatement statement = connection.prepareStatement("DELETE FROM loans WHERE memberId = ?");
            statement.setString(1, memberId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Reset the auto-increment value for loanId
                PreparedStatement resetAutoIncrementStatement = connection.prepareStatement(
                        "ALTER TABLE loans AUTO_INCREMENT = (SELECT IFNULL(MAX(loanId), 0) + 1 FROM loans)"
                );
                resetAutoIncrementStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Loan deleted and ID sequence reset successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while deleting loan or resetting ID sequence.");
        }
    }

    private void editFixedDeposit(String authenticationNumber) {
        try {
            // Fetch current fixed deposit details for the given authenticationNumber
            PreparedStatement fetchStatement = connection.prepareStatement(
                    "SELECT fixedDepositsAmount, fixedDepositsDuration, fixedDepositsDate, fixedDepositsInterest " +
                            "FROM fixedDeposits WHERE authenticationNumber = ?");
            fetchStatement.setString(1, authenticationNumber);
            ResultSet resultSet = fetchStatement.executeQuery();

            if (resultSet.next()) {
                double currentAmount = resultSet.getDouble("fixedDepositsAmount");
                String currentDuration = resultSet.getString("fixedDepositsDuration");
                Date currentDate = resultSet.getDate("fixedDepositsDate");
                double currentInterest = resultSet.getDouble("fixedDepositsInterest");

                // Prompt user for new values
                String newAmount = JOptionPane.showInputDialog(this, "Edit Fixed Deposit Amount:", currentAmount);
                String newDuration = JOptionPane.showInputDialog(this, "Edit Fixed Deposit Duration:", currentDuration);
                String newDate = JOptionPane.showInputDialog(this, "Edit Fixed Deposit Date (yyyy-mm-dd):", currentDate);
                String newInterest = JOptionPane.showInputDialog(this, "Edit Fixed Deposit Interest:", currentInterest);

                if (newAmount != null && newDuration != null && newDate != null && newInterest != null) {
                    // Update the fixed deposit details in the database
                    PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE fixedDeposits SET fixedDepositsAmount = ?, fixedDepositsDuration = ?, fixedDepositsDate = ?, fixedDepositsInterest = ? " +
                                    "WHERE authenticationNumber = ?");
                    updateStatement.setDouble(1, Double.parseDouble(newAmount));
                    updateStatement.setString(2, newDuration);
                    updateStatement.setDate(3, Date.valueOf(newDate));
                    updateStatement.setDouble(4, Double.parseDouble(newInterest));
                    updateStatement.setString(5, authenticationNumber);
                    updateStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Fixed Deposit details updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Update canceled.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Fixed Deposit not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while updating Fixed Deposit details.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid input format for date or numeric fields.");
        }
    }

    private void deleteFixedDeposit(String authenticationNumber) {
        try {
            // Delete the fixed deposit for the given authenticationNumber
            PreparedStatement statement = connection.prepareStatement("DELETE FROM fixedDeposits WHERE authenticationNumber = ?");
            statement.setString(1, authenticationNumber);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Optionally reset the auto-increment value if necessary
                PreparedStatement resetAutoIncrementStatement = connection.prepareStatement(
                        "ALTER TABLE fixedDeposits AUTO_INCREMENT = (SELECT IFNULL(MAX(authenticationNumber), 0) + 1 FROM fixedDeposits)"
                );
                resetAutoIncrementStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Fixed Deposit deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Fixed Deposit not found or could not be deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while deleting Fixed Deposit.");
        }
    }
    private void editGuarantor(String memberId) {
        try {
            // Fetch current guarantor details for the given memberId
            PreparedStatement fetchStatement = connection.prepareStatement(
                    "SELECT guarantor FROM guarantors WHERE memberId = ?"
            );
            fetchStatement.setString(1, memberId);
            ResultSet resultSet = fetchStatement.executeQuery();

            if (resultSet.next()) {
                String currentGuarantor = resultSet.getString("guarantor");

                // Prompt user to edit the guarantor
                String newGuarantor = JOptionPane.showInputDialog(
                        this, "Edit Guarantor:", currentGuarantor
                );

                if (newGuarantor != null && !newGuarantor.trim().isEmpty()) {
                    // Update the guarantor in the database
                    PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE guarantors SET guarantor = ? WHERE memberId = ?"
                    );
                    updateStatement.setString(1, newGuarantor);
                    updateStatement.setString(2, memberId);
                    updateStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Guarantor updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Update canceled. Guarantor cannot be empty.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No guarantor found for the specified member ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while updating guarantor details.");
        }
    }

    private void deleteGuarantor(String memberId) {
        try {
            // Delete the guarantor for the given memberId
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM guarantors WHERE memberId = ?"
            );
            statement.setString(1, memberId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Guarantor deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No guarantor found for the specified member ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while deleting guarantor.");
        }
    }


    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fedha_youth_group_schema", "root", "Mousa@muigai123!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed.");
        }
    }

    private void fetchMembersData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM members")) {
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getInt("memberId"),
                        resultSet.getString("surname"),
                        resultSet.getString("otherNames"),
                        resultSet.getDouble("registrationFees")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchLoansDebtData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT members.memberId, members.surname, members.otherNames,monthlyRepayment.RepaymentAmount,SUM(loans.loanRepaymentAmount) AS totalLoan, " +
                     "SUM(monthlyRepayment.loanBalance) AS totalLoanBalance " +
                     "FROM members " +
                     "JOIN loans ON members.memberId = loans.memberId " +
                     "JOIN monthlyRepayment ON monthlyRepayment.memberId = loans.memberId " +
                     "GROUP BY members.memberId, members.surname, members.otherNames,monthlyRepayment.memberId,monthlyRepayment.RepaymentAmount,monthlyRepayment.loanBalance"
             )){
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getInt("memberId"),
                        resultSet.getString("surname"),
                        resultSet.getString("otherNames"),
                        resultSet.getDouble("totalLoan"),
                        resultSet.getDouble("RepaymentAmount"),
                        resultSet.getDouble("totalLoanBalance")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchFixedDepositsData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM fixedDeposits")) {
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getInt("authenticationNumber"),
                        resultSet.getDouble("fixedDepositsAmount"),
                        resultSet.getString("fixedDepositsDuration"),
                        resultSet.getDate("fixedDepositsDate"),
                        resultSet.getDouble("fixedDepositsInterest"),
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchGuarantorsData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT memberId, guarantor FROM loans WHERE guarantor IS NOT NULL")) {
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getInt("memberId"),
                        resultSet.getString("guarantor")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void fetchMemberSharesData(JTable table, int memberId) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        // Modify the query to filter by the provided memberId
        String query = "SELECT deposits.memberId, members.surname, members.otherNames, deposits.sharesContribution, deposits.depositsDate " +
                "FROM deposits JOIN members ON members.memberId = deposits.memberId " +
                "WHERE deposits.memberId = ?"; // Use parameterized query for safety

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fedha_youth_group_schema", "root", "Mousa@muigai123!");
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the memberId parameter
            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    model.addRow(new Object[]{
                            resultSet.getInt("memberId"),
                            resultSet.getString("surname"),
                            resultSet.getString("otherNames"),
                            resultSet.getString("sharesContribution"),
                            resultSet.getString("depositsDate")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void fetchOtherReports(JTable table, String memberId) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        // Check if the member exists in the database
        try {
            if (!FedhaDatabase.isMemberIdExists(memberId)) {
                JOptionPane.showMessageDialog(null, "Member Not Found");
                AdminBar adminBar = new AdminBar();
                adminBar.setVisible(true);
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String query = "SELECT members.memberId, loans.repaymentPeriod, loans.loanLimit, loans.guarantorAmount " +
                "FROM members LEFT JOIN loans ON loans.memberId = members.memberId " +
                "WHERE members.memberId = ?";

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/fedha_youth_group_schema", "root", "Mousa@muigai123!");
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the memberId parameter
            statement.setString(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String memberIdFromDB = resultSet.getString("memberId");
                    String repaymentPeriod = resultSet.getString("repaymentPeriod");
                    double loanLimit = resultSet.getDouble("loanLimit");
                    double guarantorAmount = resultSet.getDouble("guarantorAmount");

                    // Fallback for calculations if needed
                    double dividends = FedhaDatabase.calculateDividends(memberIdFromDB);

                    // Add data to the table
                    model.addRow(new Object[]{
                            memberIdFromDB,
                            repaymentPeriod != null ? repaymentPeriod : "No loan",
                            String.format("%.2f", loanLimit),
                            String.format("%.2f", dividends),
                            guarantorAmount != 0.0 ? String.format("%.2f", guarantorAmount) : "No Guarantor Amount"
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching report");
        }
    }
    private void fetchMembersExit(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM exitRequests")) {
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getInt("memberId"),
                        resultSet.getString("fullName"),
                        resultSet.getDouble("outstandingLoan"),
                        resultSet.getString("guaranteedLoans"),
                        resultSet.getString("reasonForExit"),
                        resultSet.getDate("noticeDate"),
                        resultSet.getDate("noticeDate"),
                        resultSet.getString("approval")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminBar();
            }
        });
    }
}
