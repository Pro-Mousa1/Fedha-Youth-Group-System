import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Exit extends JFrame {
    private DefaultListModel<String> guarantorLoanListModel = new DefaultListModel<>();


    public Exit() {
        setTitle("Fedha Membership Exit");
        setSize(600, 550);
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

        menuItemGoBack.addActionListener(e -> {
            new Menubar().setVisible(true);
            Exit.this.dispose();
        });

        menuItemSignOut.addActionListener(e -> {
            new Login().setVisible(true);
            Exit.this.dispose();
        });

        // Create a panel with null layout for custom component positioning
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x11933E));
        add(panel);

        // Member ID Field
        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdLabel.setBounds(160, 5, 150, 25);
        memberIdLabel.setForeground(Color.YELLOW);
        panel.add(memberIdLabel);
        JTextField memberIdField = new JTextField(15);
        memberIdField.setBounds(160, 30, 300, 25);
        memberIdField.setForeground(Color.BLACK);
        memberIdField.setBackground(Color.WHITE);
        panel.add(memberIdField);

        // Full Name Field
        JLabel memberFullNameLabel = new JLabel("Member FullName:");
        memberFullNameLabel.setBounds(160, 60, 200, 25);
        memberFullNameLabel.setForeground(Color.YELLOW);
        panel.add(memberFullNameLabel);
        JTextField memberFullNameField = new JTextField(15);
        memberFullNameField.setBounds(160, 85, 300, 25);
        memberFullNameField.setForeground(Color.BLACK);
        memberFullNameField.setBackground(Color.WHITE);
        memberFullNameField.setEditable(false);
        panel.add(memberFullNameField);

        // Outstanding Loan Field
        JLabel outstandingLoanLabel = new JLabel("Outstanding Loan:");
        outstandingLoanLabel.setBounds(160, 115, 200, 25);
        outstandingLoanLabel.setForeground(Color.YELLOW);
        panel.add(outstandingLoanLabel);
        JTextField outstandingLoanField = new JTextField(15);
        outstandingLoanField.setBounds(160, 140, 300, 25);
        outstandingLoanField.setForeground(Color.BLACK);
        outstandingLoanField.setBackground(Color.WHITE);
        panel.add(outstandingLoanField);

        // Guaranteed Loans List
        JLabel guarantorLabel = new JLabel("Guaranteed Loans:");
        guarantorLabel.setBounds(160, 170, 150, 25);
        guarantorLabel.setForeground(Color.YELLOW);
        panel.add(guarantorLabel);

        // JList for guaranteed loans
        JList<String> guarantorListBox = new JList<>(guarantorLoanListModel);
        JScrollPane guarantorScrollPane = new JScrollPane(guarantorListBox);
        guarantorScrollPane.setBounds(160, 195, 300, 75);
        panel.add(guarantorScrollPane);

        // Reason for Exit
        JLabel reasonForExitLabel = new JLabel("Reason For Exit:");
        reasonForExitLabel.setBounds(160, 270, 150, 25);
        reasonForExitLabel.setForeground(Color.YELLOW);
        panel.add(reasonForExitLabel);
        String[] reasonForExitOptions = {
                "Financial Hardship",
                "Age limit",
                "Relocation",
                "Retirement",
                "Dissatisfaction with Services",
                "Personal Financial Goals Achieved",
                "Switching to Another SACCO",
                "Interest Rate Concerns",
                "No Longer Needing SACCO Services",
                "Other"
        };
        JComboBox<String> reasonForExitCombo = new JComboBox<>(reasonForExitOptions);
        reasonForExitCombo.setBounds(160, 295, 300, 25);
        reasonForExitCombo.setForeground(Color.BLACK);
        reasonForExitCombo.setBackground(Color.WHITE);
        panel.add(reasonForExitCombo);

        // Notice Date
        JLabel noticeDateLabel = new JLabel("Notice Date:");
        noticeDateLabel.setBounds(160, 325, 150, 25);
        noticeDateLabel.setForeground(Color.YELLOW);
        panel.add(noticeDateLabel);
        JTextField noticeDateField = new JTextField(15);
        noticeDateField.setBounds(160, 350, 300, 25);
        noticeDateField.setForeground(Color.BLACK);
        noticeDateField.setBackground(Color.WHITE);
        panel.add(noticeDateField);

        // Get current date for Notice Date
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        noticeDateField.setText(currentDateTime.format(formatter));

        // Exiting Date
        JLabel exitingDateLabel = new JLabel("Exiting Date:");
        exitingDateLabel.setBounds(160, 380, 150, 25);
        exitingDateLabel.setForeground(Color.YELLOW);
        panel.add(exitingDateLabel);
        JTextField exitingDateField = new JTextField(15);
        exitingDateField.setBounds(160, 405, 300, 25);
        exitingDateField.setForeground(Color.BLACK);
        exitingDateField.setBackground(Color.WHITE);
        panel.add(exitingDateField);

        // Calculate exiting date as 1 month after notice date
        LocalDateTime exitingDate = currentDateTime.plusMonths(1);
        exitingDateField.setText(exitingDate.format(formatter));

        memberIdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String memberId = memberIdField.getText();

                if (!memberId.isEmpty()) {
                    // Fetch full name and outstanding loan
                    String fullName = FedhaDatabase.getMemberFullName(memberId);
                    memberFullNameField.setText(fullName);

                    double outstandingLoan = FedhaDatabase.getTotalLoanAmountForMember(memberId);
                    outstandingLoanField.setText(String.valueOf(outstandingLoan));

                    // Fetch and populate guaranteed loans in the guarantorLoanListModel
                    guarantorLoanListModel.clear(); // Clear previous entries
                    String guaranteedLoansData = FedhaDatabase.getGuaranteedLoans(memberId);

                    if (guaranteedLoansData != null && !guaranteedLoansData.isEmpty()) {
                        String[] guaranteedLoans = guaranteedLoansData.split(";\n");

                        for (String loan : guaranteedLoans) {
                            if (!loan.trim().isEmpty()) {
                                guarantorLoanListModel.addElement(loan.trim());
                            }
                        }
                    }
                } else {
                    // Clear fields if memberId is empty
                    memberFullNameField.setText("");
                    outstandingLoanField.setText("");
                    guarantorLoanListModel.clear();
                }
            }
        });

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(270, 450, 100, 25);
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.setBackground(Color.WHITE);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnSubmit);

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberId = memberIdField.getText();
                String fullName = FedhaDatabase.getMemberFullName(memberId);
                String outstandingLoan = outstandingLoanField.getText();

                // Convert DefaultListModel to ArrayList
                ArrayList<String> guaranteedLoans = new ArrayList<>();
                for (int i = 0; i < guarantorLoanListModel.size(); i++) {
                    guaranteedLoans.add(guarantorLoanListModel.get(i));
                }

                String reasonForExit = reasonForExitCombo.getSelectedItem().toString();
                String noticeDate = noticeDateField.getText();
                String exitingDate = exitingDateField.getText();

                if (memberId.isEmpty() || outstandingLoan.isEmpty() || reasonForExit.isEmpty() || noticeDate.isEmpty() || exitingDate.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                }

                try {
                    // Check if the memberId exists in the database
                    if (!FedhaDatabase.isMemberIdExists(memberId)) {
                        JOptionPane.showMessageDialog(Exit.this, "You must be a member to exit.");
                        Menubar menubar = new Menubar();
                        menubar.setVisible(true);
                        Exit.this.dispose();
                        return;
                    }

                    // Check if the member has already exited
                    if (FedhaDatabase.hasMemberExited(memberId)) {
                        JOptionPane.showMessageDialog(Exit.this, "You have already exited and can no longer be a member.");
                        Menubar menubar = new Menubar();
                        menubar.setVisible(true);
                        Exit.this.dispose();
                        return;
                    }
                    FedhaDatabase.insertExit(memberId, fullName, outstandingLoan, String.valueOf(guaranteedLoans), reasonForExit, noticeDate, exitingDate);
                    JOptionPane.showMessageDialog(Exit.this, "Notice successfully sent for approval");
                    Menubar menubar = new Menubar();
                    menubar.setVisible(true);
                    Exit.this.dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Exit.this, "An error occurred while sending notice.");
                }
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Exit();
            }
        });
    }
}
