import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class Members extends JFrame {
    public Members() {
        setTitle("Fedha Members");
        setSize(702, 600);
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
                Members.this.dispose();
            }
        });

        menuItemSignOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement sign out functionality here
                new Login().setVisible(true);
                Members.this.dispose();
            }
        });


        // Create a panel with null layout for custom component positioning
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0x125729));
        add(panel);

        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setBounds(180, 5, 150, 25);
        surnameLabel.setForeground(Color.YELLOW);
        panel.add(surnameLabel);
        JTextField surnameField = new JTextField(15);
        surnameField.setBounds(180, 30, 300, 25);
        surnameField.setForeground(Color.BLACK);
        surnameField.setBackground(Color.WHITE);
        panel.add(surnameField);

        JLabel otherNamesLabel = new JLabel("Other Names:");
        otherNamesLabel.setBounds(180, 60, 150, 25);
        otherNamesLabel.setForeground(Color.YELLOW);
        panel.add(otherNamesLabel);
        JTextField otherNamesField = new JTextField(15);
        otherNamesField.setBounds(180, 85, 300, 25);
        otherNamesField.setForeground(Color.BLACK);
        otherNamesField.setBackground(Color.WHITE);
        panel.add(otherNamesField);

        JLabel dobLabel = new JLabel("Date of birth:");
        dobLabel.setBounds(180, 115, 150, 25);
        dobLabel.setForeground(Color.YELLOW);
        panel.add(dobLabel);

        // Day, Month, Year ComboBoxes
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) days[i - 1] = String.valueOf(i);
        JComboBox<String> dayCombo = new JComboBox<>(days);
        dayCombo.setBounds(180, 140, 60, 25);
        dayCombo.setForeground(Color.BLACK);
        dayCombo.setBackground(Color.WHITE);
        panel.add(dayCombo);

        String[] months = new String[12];
        for (int i = 1; i <= 12; i++) months[i - 1] = String.valueOf(i);
        JComboBox<String> monthCombo = new JComboBox<>(months);
        monthCombo.setBounds(250, 140, 60, 25);
        monthCombo.setForeground(Color.BLACK);
        monthCombo.setBackground(Color.WHITE);
        panel.add(monthCombo);

        String[] years = new String[100];
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 100; i++) years[i] = String.valueOf(currentYear - i);
        JComboBox<String> yearCombo = new JComboBox<>(years);
        yearCombo.setBounds(320, 140, 80, 25);
        yearCombo.setForeground(Color.BLACK);
        yearCombo.setBackground(Color.WHITE);
        panel.add(yearCombo);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(180, 170, 150, 25);
        phoneLabel.setForeground(Color.YELLOW);
        panel.add(phoneLabel);
        JTextField phoneField = new JTextField(15);
        phoneField.setBounds(180, 195, 300, 25);
        phoneField.setForeground(Color.BLACK);
        phoneField.setBackground(Color.WHITE);
        panel.add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(180, 220, 150, 25);
        emailLabel.setForeground(Color.YELLOW);
        panel.add(emailLabel);
        JTextField emailField = new JTextField(15);
        emailField.setBounds(180, 245, 300, 25);
        emailField.setForeground(Color.BLACK);
        emailField.setBackground(Color.WHITE);
        panel.add(emailField);

        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdLabel.setBounds(180, 270, 150, 25);
        memberIdLabel.setForeground(Color.YELLOW);
        panel.add(memberIdLabel);
        JTextField memberIdField = new JTextField(15);
        memberIdField.setBounds(180, 295, 300, 25);
        memberIdField.setForeground(Color.BLACK);
        memberIdField.setBackground(Color.WHITE);
        memberIdField.setEditable(false);
        panel.add(memberIdField);

        JLabel registrationFeesLabel = new JLabel("Registration Fees:");
        registrationFeesLabel.setBounds(180, 320, 150, 25);
        registrationFeesLabel.setForeground(Color.YELLOW);
        panel.add(registrationFeesLabel);
        JTextField registrationFeesField = new JTextField(15);
        registrationFeesField.setBounds(180, 345, 300, 25);
        registrationFeesField.setForeground(Color.BLACK);
        registrationFeesField.setBackground(Color.WHITE);
        panel.add(registrationFeesField);

        JLabel activationDateLabel = new JLabel("Activation Date:");
        activationDateLabel.setBounds(180, 370, 150, 25);
        activationDateLabel.setForeground(Color.YELLOW);
        panel.add(activationDateLabel);
        JTextField activationDateField = new JTextField(15);
        activationDateField.setBounds(180, 395, 300, 25);
        activationDateField.setForeground(Color.BLACK);
        activationDateField.setBackground(Color.WHITE);
        panel.add(activationDateField);

        // Get the current date and time from the system and set it to activationDateField
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        activationDateField.setText(formattedDateTime);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(180, 425, 150, 25);
        statusLabel.setForeground(Color.YELLOW);
        panel.add(statusLabel);
        String[] statusOptions = {"Active", "Inactive"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
        statusCombo.setBounds(180, 450, 100, 25);
        statusCombo.setForeground(Color.BLACK);
        statusCombo.setBackground(Color.WHITE);
        panel.add(statusCombo);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(290, 500, 100, 25);
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.setBackground(Color.WHITE);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnSubmit);

        // Generate a unique Member ID using the current timestamp and set it in the field
        String generatedMemberId = "" + Instant.now().getEpochSecond(); // Generate ID using timestamp
        memberIdField.setText(generatedMemberId);
        JOptionPane.showMessageDialog(Members.this,"Member ID: " + generatedMemberId);

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String surname = surnameField.getText();
                String otherNames = otherNamesField.getText();
                String day = (String) dayCombo.getSelectedItem();
                String month = (String) monthCombo.getSelectedItem();
                String year = (String) yearCombo.getSelectedItem();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String memberId = memberIdField.getText();
                String registrationFees = registrationFeesField.getText();
                String activationDate = activationDateField.getText();
                String status = statusCombo.getSelectedItem().toString();

                // Convert date of birth to LocalDate and calculate age
                LocalDate dob = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                int age = Period.between(dob, LocalDate.now()).getYears();

                // Regular expression to ensure the email ends with @gmail.com
                String emailPattern = "^[\\w-\\.]+@gmail\\.com$";

                // Check age limit
                if (age < 18 || age > 35) {
                    JOptionPane.showMessageDialog(Members.this, "Loan application age limit is 18 to 35 years.");
                    return;  // Stop further processing if age is out of bounds
                }

                // Check for empty fields
                if (surname.isEmpty() || otherNames.isEmpty() || day.isEmpty() || month.isEmpty() || year.isEmpty() || phone.isEmpty() || email.isEmpty() || memberId.isEmpty() || registrationFees.isEmpty() || activationDate.isEmpty() || status.isEmpty()) {
                    JOptionPane.showMessageDialog(Members.this, "Please fill all the fields.");
                    return;
                }
                if (!email.matches(emailPattern)) {
                    JOptionPane.showMessageDialog(Members.this, "Please enter a valid Gmail address (e.g., example@gmail.com)");
                    return;
                }

                try {
                    FedhaDatabase.insertMember(surname, otherNames, day, month, year, phone, email, memberId, registrationFees,activationDate,status);
                    JOptionPane.showMessageDialog(Members.this, "Member added successfully");
                    Menubar menubar = new Menubar();
                    menubar.setVisible(true);
                    Members.this.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Members.this, "An error occurred while adding member.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Members();
            }
        });
    }
}
