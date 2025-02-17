import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class Menubar extends JFrame {
    public Menubar() {
        setTitle("Fedha Menubar");
        setSize(711, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
            new Login().setVisible(true);
            Menubar.this.dispose();
        });

        menuItemSignOut.addActionListener(e -> {
            new Login().setVisible(true);
            Menubar.this.dispose();
        });

        // Create a main panel with BorderLayout
//        new Color(0x0800FF)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0xBE044E39));
        add(mainPanel);

        // Sidebar panel for labels
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(0xCDD2D6));
        sidebar.setPreferredSize(new Dimension(150, getHeight()));
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Create labels for each "button" with spacing
        sidebar.add(createSidebarLabel("Member"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing

        sidebar.add(createSidebarLabel("Loan"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        sidebar.add(createSidebarLabel("Deposits"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        sidebar.add(createSidebarLabel("Monthly Repayments"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        sidebar.add(createSidebarLabel("Information"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        sidebar.add(createSidebarLabel("Exit"));

        // Add listeners to labels
        sidebar.getComponent(0).addMouseListener(new LabelMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Members members = new Members();
                members.setVisible(true);
                Menubar.this.dispose();
            }
        });

        sidebar.getComponent(2).addMouseListener(new LabelMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Loan loan = new Loan();
                loan.setVisible(true);
                Menubar.this.dispose();
            }
        });

        sidebar.getComponent(4).addMouseListener(new LabelMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Deposits deposits = new Deposits();
                deposits.setVisible(true);
                Menubar.this.dispose();
            }
        });

        sidebar.getComponent(6).addMouseListener(new LabelMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MonthlyRepayment monthlyRepayment = new MonthlyRepayment();
                monthlyRepayment.setVisible(true);
                Menubar.this.dispose();
            }
        });

        sidebar.getComponent(8).addMouseListener(new LabelMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String memberId = JOptionPane.showInputDialog("Please enter your Member ID:");
                if (memberId != null && !memberId.trim().isEmpty()) {
                    try {
                        if (FedhaDatabase.isMemberIdExists(memberId)) {
                            MemberInformation memberInformation = new MemberInformation(memberId);
                            memberInformation.setVisible(true);
                            Menubar.this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "You must be a member to view your information.");
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        sidebar.getComponent(10).addMouseListener(new LabelMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Exit exit = new Exit();
                exit.setVisible(true);
                Menubar.this.dispose();
            }
        });

        setVisible(true);
    }

    private JLabel createSidebarLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(150, 40));
        label.setOpaque(true);
        label.setBackground(new Color(0xCDD2D6));
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private abstract static class LabelMouseListener extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            ((JLabel) e.getSource()).setBackground(new Color(0xAAB0B5));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ((JLabel) e.getSource()).setBackground(new Color(0xCDD2D6));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Menubar();
            }
        });
    }
}

