import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MemberInformation extends JFrame {
    private String memberId;
    private JTextField memberIdField;
    private JTextField memberFullNameField;
    private JComboBox<String> dayCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<String> yearCombo;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField registrationFeesField;
    private JTextField activationDateField;
    private JComboBox<String> statusCombo;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MemberInformation(String memberId) {
        this.memberId = memberId;
        setTitle("Member Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // Create the menu bar
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

        menuItemGoBack.addActionListener(e -> {
            new Menubar().setVisible(true);
            MemberInformation.this.dispose();
        });

        menuItemSignOut.addActionListener(e -> {
            new Login().setVisible(true);
            MemberInformation.this.dispose();
        });

        // Create sidebar panel
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBackground(new Color(0xBBBBF3));

        JButton memberInfoButton = new JButton("Member Info");
        memberInfoButton.setBackground(new Color(0xBBBBF3));
        memberInfoButton.setBorder(null);
        JButton loanInfoButton = new JButton("Loan Info");
        loanInfoButton.setBackground(new Color(0xBBBBF3));
        loanInfoButton.setBorder(null);
        JButton depositInfoButton = new JButton("Deposit Info");
        depositInfoButton.setBackground(new Color(0xBBBBF3));
        depositInfoButton.setBorder(null);
        JButton repayInfoButton = new JButton("Loan Repayment Info");
        repayInfoButton.setBackground(new Color(0xBBBBF3));
        repayInfoButton.setBorder(null);
        JButton exitInfoButton = new JButton("Exit Info");
        exitInfoButton.setBackground(new Color(0xBBBBF3));
        exitInfoButton.setBorder(null);

        sidebarPanel.add(memberInfoButton);
        sidebarPanel.add(loanInfoButton);
        sidebarPanel.add(depositInfoButton);
        sidebarPanel.add(repayInfoButton);
        sidebarPanel.add(exitInfoButton);

        // Action listeners to switch between views
        memberInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Member Info");
            }
        });

        loanInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Loan Info");
            }
        });

        depositInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Deposit Info");
            }
        });

        repayInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Loan Repayment Info");
            }
        });

        exitInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Exit Info");
            }
        });

        // Create the content panel with CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Panel for Member Info
        JPanel memberInfoPanel = createMemberInfoPanel();
        contentPanel.add(memberInfoPanel, "Member Info");

        JPanel loanInfoPanel = createLoanInfoPanel(memberId);
        contentPanel.add(loanInfoPanel, "Loan Info");

        JPanel depositInfoPanel = createDepositInfoPanel(memberId);
        contentPanel.add(depositInfoPanel, "Deposit Info");

        JPanel repayInfoPanel = createLoanRepaymentInfoPanel(memberId);
        contentPanel.add(repayInfoPanel, "Loan Repayment Info");

        JPanel exitInfoPanel = createExitInfoPanel(memberId);
        contentPanel.add(exitInfoPanel, "Exit Info");

        // Add sidebar and content panels to frame
        setLayout(new BorderLayout());
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Fetch and display the member data
        try {
            Map<String, String> memberData = FedhaDatabase.getMemberDetails(memberId);
            if (memberData != null) {
                memberIdField.setText(memberId);
                memberFullNameField.setText(memberData.get("fullName"));
                dayCombo.setSelectedItem(memberData.get("day"));
                monthCombo.setSelectedItem(memberData.get("month"));
                yearCombo.setSelectedItem(memberData.get("year"));
                phoneField.setText(memberData.get("phone"));
                emailField.setText(memberData.get("email"));
                registrationFeesField.setText(memberData.get("registrationFees"));
                activationDateField.setText(memberData.get("activationDate"));
                statusCombo.setSelectedItem(memberData.get("status"));
            } else {
                JOptionPane.showMessageDialog(this, "No data found for the provided Member ID.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching member data.");
            ex.printStackTrace();
        }
    }
    // Member Panel
    private JPanel createMemberInfoPanel() {
        JPanel memberPanel = new JPanel();
        memberPanel.setLayout(null);
        memberPanel.setBackground(new Color(0xA4A3AC));

        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdLabel.setBounds(180, 5, 150, 25);
        memberIdLabel.setForeground(Color.YELLOW);
        memberPanel.add(memberIdLabel);

        memberIdField = new JTextField(15);
        memberIdField.setBounds(180, 30, 300, 25);
        memberIdField.setEditable(false);
        memberPanel.add(memberIdField);

        JLabel memberFullNameLabel = new JLabel("Member FullName:");
        memberFullNameLabel.setBounds(180, 60, 200, 25);
        memberFullNameLabel.setForeground(Color.YELLOW);
        memberPanel.add(memberFullNameLabel);

        memberFullNameField = new JTextField(15);
        memberFullNameField.setBounds(180, 85, 300, 25);
        memberFullNameField.setEditable(false);
        memberPanel.add(memberFullNameField);

        JLabel dobLabel = new JLabel("Date of birth:");
        dobLabel.setBounds(180, 115, 150, 25);
        dobLabel.setForeground(Color.YELLOW);
        memberPanel.add(dobLabel);

        dayCombo = new JComboBox<>(createNumberStrings(1, 31));
        dayCombo.setBounds(180, 140, 60, 25);
        memberPanel.add(dayCombo);

        monthCombo = new JComboBox<>(createNumberStrings(1, 12));
        monthCombo.setBounds(250, 140, 60, 25);
        memberPanel.add(monthCombo);

        yearCombo = new JComboBox<>(createNumberStrings(LocalDate.now().getYear() - 99, LocalDate.now().getYear()));
        yearCombo.setBounds(320, 140, 80, 25);
        memberPanel.add(yearCombo);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(180, 170, 150, 25);
        phoneLabel.setForeground(Color.YELLOW);
        memberPanel.add(phoneLabel);

        phoneField = new JTextField(15);
        phoneField.setBounds(180, 195, 300, 25);
        memberPanel.add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(180, 220, 150, 25);
        emailLabel.setForeground(Color.YELLOW);
        memberPanel.add(emailLabel);

        emailField = new JTextField(15);
        emailField.setBounds(180, 245, 300, 25);
        memberPanel.add(emailField);

        JLabel registrationFeesLabel = new JLabel("Registration Fees:");
        registrationFeesLabel.setBounds(180, 270, 150, 25);
        registrationFeesLabel.setForeground(Color.YELLOW);
        memberPanel.add(registrationFeesLabel);

        registrationFeesField = new JTextField(15);
        registrationFeesField.setBounds(180, 295, 300, 25);
        registrationFeesField.setEditable(false);
        memberPanel.add(registrationFeesField);

        JLabel activationDateLabel = new JLabel("Activation Date:");
        activationDateLabel.setBounds(180, 320, 150, 25);
        activationDateLabel.setForeground(Color.YELLOW);
        memberPanel.add(activationDateLabel);

        activationDateField = new JTextField(15);
        activationDateField.setBounds(180, 345, 300, 25);
        activationDateField.setEditable(false);
        memberPanel.add(activationDateField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(180, 370, 150, 25);
        statusLabel.setForeground(Color.YELLOW);
        memberPanel.add(statusLabel);

        statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
        statusCombo.setBounds(180, 395, 100, 25);
        memberPanel.add(statusCombo);

        JLabel updateDateLabel = new JLabel("Update Date:");
        updateDateLabel.setBounds(180, 420, 150, 25);
        updateDateLabel.setForeground(Color.YELLOW);
        memberPanel.add(updateDateLabel);
        JTextField updateDateField = new JTextField(15);
        updateDateField.setBounds(180, 445, 300, 25);
        updateDateField.setForeground(Color.BLACK);
        updateDateField.setBackground(Color.WHITE);
        memberPanel.add(updateDateField);

        // Get the current date and time from the system and set it to activationDateField
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        updateDateField.setText(formattedDateTime);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(290, 500, 100, 25);
        btnUpdate.setForeground(Color.BLACK);
        btnUpdate.setBackground(Color.WHITE);
        memberPanel.add(btnUpdate);

        // Action Listener for the Update Button
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from the input fields and comboBoxes
                String fullName = memberFullNameField.getText();
                String day = (String) dayCombo.getSelectedItem();
                String month = (String) monthCombo.getSelectedItem();
                String year = (String) yearCombo.getSelectedItem();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String status = statusCombo.getSelectedItem().toString();
                String updateDate = updateDateField.getText();

                // Validate the date of birth and calculate age
                LocalDate dob = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                int age = Period.between(dob, LocalDate.now()).getYears();

                // Regular expression to ensure email is in valid Gmail format
                String emailPattern = "^[\\w-\\.]+@gmail\\.com$";

                // Check age limit (18 to 35 years)
                if (age < 18 || age > 35) {
                    JOptionPane.showMessageDialog(MemberInformation.this, "Loan application age limit is 18 to 35 years.");
                    return;
                }

                // Validate that none of the important fields are empty
                if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || status.isEmpty()) {
                    JOptionPane.showMessageDialog(MemberInformation.this, "Please fill all the fields.");
                    return;
                }

                // Validate email format
                if (!email.matches(emailPattern)) {
                    JOptionPane.showMessageDialog(MemberInformation.this, "Please enter a valid Gmail address (e.g., example@gmail.com)");
                    return;
                }
                try {
                    FedhaDatabase.updateMember(memberId, day, month, year, phone, email, status, updateDate);
                    JOptionPane.showMessageDialog(MemberInformation.this, "Member updated successfully.");
                    Menubar menubar = new Menubar();
                    menubar.setVisible(true);
                    MemberInformation.this.dispose();
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(MemberInformation.this, "An error occurred while updating member.");
                    ex.printStackTrace();
                }
            }
        });
        try {
            Map<String, String> memberData = FedhaDatabase.getMemberDetails(memberId);
            if (memberData != null) {
                // Set member details to respective fields
                memberIdField.setText(memberId);
                memberFullNameField.setText(memberData.get("fullName"));

                // Set the day, month, year in respective comboBoxes
                dayCombo.setSelectedItem(memberData.get("day"));
                monthCombo.setSelectedItem(memberData.get("month"));
                yearCombo.setSelectedItem(memberData.get("year"));

                phoneField.setText(memberData.get("phone"));
                emailField.setText(memberData.get("email"));
                registrationFeesField.setText(memberData.get("registrationFees"));
                activationDateField.setText(memberData.get("activationDate"));
                statusCombo.setSelectedItem(memberData.get("status"));
            } else {
                JOptionPane.showMessageDialog(this, "No data found for the provided Member ID.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching member data.");
            ex.printStackTrace();
        }

        setVisible(true);

        return memberPanel;
    }

    // Loan Panel
    private JPanel createLoanInfoPanel(String memberId) {
        JPanel loanPanel = new JPanel();
        String[] columnNames = {"Loan Amount", "Loan Type", "Interest Rate", "Repayment Period",
                "Loan Due", "Guarantor", "Loan Balance", "Loan Date"};

        // Create a table model with the column names
        DefaultTableModel model = new DefaultTableModel(null, columnNames);

        // Create the JTable with the model
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        loanPanel.setLayout(new BoxLayout(loanPanel, BoxLayout.Y_AXIS));
        loanPanel.add(scrollPane);
        FedhaDatabase.getLoanDetails(model, memberId);
        return loanPanel;
    }


    // Deposit Panel
    private JPanel createDepositInfoPanel(String memberId) {
        JPanel depositPanel = new JPanel();
        String[] columnNames = {"MemberId", "Deposit Amount", "Deposit Date"};

        // Create a table model with the column names
        DefaultTableModel model = new DefaultTableModel(null, columnNames);

        // Create the JTable with the model
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        // Set layout and add components to the panel
        depositPanel.setLayout(new BoxLayout(depositPanel, BoxLayout.Y_AXIS));
        depositPanel.add(scrollPane);

        // Pass memberId to getDepositDetails
        FedhaDatabase.getDepositDetails(model, memberId);

        return depositPanel;
    }
    // Loan Repayment Panel
    private JPanel createLoanRepaymentInfoPanel(String memberId) {
        JPanel loanRepayPanel = new JPanel();
        String[] columnNames = {"MemberId", "Loan Repayment Amount", "Repayment Amount", "Loan Balance", "Loan Repayment Date"};

        // Create a table model with the column names
        DefaultTableModel model = new DefaultTableModel(null, columnNames);

        // Create the JTable with the model
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        // Set layout and add components to the panel
        loanRepayPanel.setLayout(new BoxLayout(loanRepayPanel, BoxLayout.Y_AXIS));
        loanRepayPanel.add(scrollPane);

        // Pass the memberId to getLoanRepaymentDetails to fetch the relevant loan repayment details
        FedhaDatabase.getLoanRepaymentDetails(model, memberId);

        return loanRepayPanel;
    }

    // Exit Panel
    private JPanel createExitInfoPanel(String memberId) {
        JPanel exitPanel = new JPanel();
        String[] columnNames = {"FullName", "Outstanding Loan", "Guaranteed Loans",
                "Reason For Exit", "Notice Date", "Exiting Date","Approval"};

        // Create a table model with the column names
        DefaultTableModel model = new DefaultTableModel(null, columnNames);

        // Create the JTable with the model
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        // Set layout and add components to the panel
        exitPanel.setLayout(new BoxLayout(exitPanel, BoxLayout.Y_AXIS));
        exitPanel.add(scrollPane);

        // Call the method to populate the table model
        FedhaDatabase.getExitDetails(model, memberId);

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);

        return exitPanel;
    }

    private String[] createNumberStrings(int start, int end) {
        String[] numbers = new String[end - start + 1];
        for (int i = start; i <= end; i++) {
            numbers[i - start] = String.valueOf(i);
        }
        return numbers;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MemberInformation("");
            }
        });
    }
}

