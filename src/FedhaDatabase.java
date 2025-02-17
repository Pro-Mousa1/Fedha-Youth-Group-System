import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class FedhaDatabase {
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/fedha_youth_group_schema";
    private static final String username = "root";
    private static final String password = "Mousa@muigai123!";

    public static Connection connect() throws SQLException, ClassNotFoundException {
        // Load the MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Establish connection
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
//    public void insertBankData() {
//        String query = "INSERT INTO bank (registrationFees, RepaymentAmount, sharesContribution) " +
//                "SELECT m.registrationFees, mr.monthlyRepayment, d.sharesContribution " +
//                "FROM members m " +
//                "JOIN monthlyRepayment mr ON m.memberId = mr.memberId " +
//                "JOIN deposits d ON m.memberId = d.memberId";
//
//        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            // Execute the update
//            int rowsAffected = stmt.executeUpdate();
//            System.out.println("Data inserted into bank table. Rows affected: " + rowsAffected);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        // Instantiate FedhaDatabase and insert data into the bank table
//        FedhaDatabase database = new FedhaDatabase();
//        database.insertBankData();
//    }
    // Start of Admin Data
    public static void insertAdmin(String adminName, String authenticationNumber, String password) {
        String sql = "INSERT INTO admin (adminName, authenticationNumber, password) VALUES (?, ?, ?)";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set parameters
            pstmt.setString(1, adminName);
            pstmt.setString(2, authenticationNumber);
            pstmt.setString(3, password);

            // Execute the insert command
            pstmt.executeUpdate();
            System.out.println("Admin inserted successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while inserting admin!");
            e.printStackTrace();
        }
    }

    public static boolean checkAdminCredentials(String authenticationNumber, String password) {
        String sql = "SELECT * FROM admin WHERE authenticationNumber = ?";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, authenticationNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean isAdminIdExists(String authenticationNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admin WHERE authenticationNumber = ?";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, authenticationNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public static boolean hasAlreadyFixedDeposited(String authenticationNumber, int year, int month) {
        String sql = "SELECT COUNT(*) FROM fixedDeposits WHERE authenticationNumber = ? AND YEAR(fixedDepositsDate) = ? AND MONTH(fixedDepositsDate) = ?";

        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, authenticationNumber);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    //    // Method to grant/deny exit
    public static void updateExitApproval(String memberId, String approval) {
        String query = "UPDATE exitRequests SET approval = ? WHERE memberId = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl,username,password); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, approval);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean deleteExitRequest(String memberId) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM exitRequests WHERE memberId = ?")) {
            stmt.setString(1, memberId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public static void resetAutoIncrement(String tableName) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    // End of Admin Data

    // Start of User Data
    public static boolean userExists(String username, String email) {
        String sql = "SELECT * FROM users WHERE username = ? OR email = ?";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  // If a result is returned, the user exists
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void insertUser(String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set parameters
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            // Execute the insert command
            pstmt.executeUpdate();
            System.out.println("User inserted successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while inserting user!");
            e.printStackTrace();
        }
    }

    public static boolean checkCredentials(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // End of User Data

    // Start of Member Data
    public static void insertMember(String surname, String otherNames, String day, String month, String year, String phone, String email, String memberId, String registrationFees, String activationDate, String status) {
        String sql = "INSERT INTO members (surname,otherNames,day,month,year,phone,email,memberId,registrationFees,activationDate,status) VALUES (?, ?, ?,?,?,?,?,?,?,?,?)";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set parameters
            pstmt.setString(1, surname);
            pstmt.setString(2, otherNames);
            pstmt.setString(3, day);
            pstmt.setString(4, month);
            pstmt.setString(5, year);
            pstmt.setString(6, phone);
            pstmt.setString(7, email);
            pstmt.setString(8, memberId);
            pstmt.setString(9, registrationFees);
            pstmt.setString(10, activationDate);
            pstmt.setString(11, status);
            // Execute the insert command
            pstmt.executeUpdate();
            System.out.println("Member added successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Member failed");
            e.printStackTrace();
        }
    }
    // End of Member Data

    // Start of Loan Data
    public static void insertLoan(String memberId, double loanAmount, String loanType, String interestRate, String repaymentPeriod, String loanDue, String guarantor, String loanRepaymentAmount,double loanLimit,double guarantorAmount,String loanDate) {
        String sql = "INSERT INTO loans (memberId,loanAmount,loanType,interestRate,repaymentPeriod,loanDue,guarantor,loanRepaymentAmount,loanLimit,guarantorAmount,loanDate) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set parameters
            pstmt.setString(1, memberId);
            pstmt.setDouble(2, loanAmount);
            pstmt.setString(3, loanType);
            pstmt.setString(4, interestRate);
            pstmt.setString(5, repaymentPeriod);
            pstmt.setString(6, loanDue);
            pstmt.setString(7, guarantor);
            pstmt.setString(8, loanRepaymentAmount);
            pstmt.setDouble(9, loanLimit);
            pstmt.setDouble(10, guarantorAmount);
            pstmt.setString(11, loanDate);
            // Execute the insert command
            pstmt.executeUpdate();
            System.out.println("Loan applied successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Loan failed");
            e.printStackTrace();
        }
    }

    public static boolean isMemberIdExists(String memberId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM members WHERE memberId = ?";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static List<String> getAllMembers() {
        List<String> members = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {conn = DriverManager.getConnection(jdbcUrl, username, password);
            // SQL query to get all members (adjust table and column names)
            String sql = "SELECT surname, otherNames FROM members";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            // Add each member's full name to the list
            while (rs.next()) {
                String surname = rs.getString("surname");
                String otherNames = rs.getString("otherNames");
                String fullName = surname + " " + otherNames;
                members.add(fullName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return members;
    }

    public static String getMemberFullName(String memberId) {
        String fullName = "";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = connect();
            String sql = "SELECT surname, otherNames FROM members WHERE memberId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            // Concatenate surname and other names
            if (rs.next()) {
                String surname = rs.getString("surname");
                String otherNames = rs.getString("otherNames");
                fullName = surname + " " + otherNames;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fullName;
    }

    public static double getTotalShares(String memberId) {
        double totalDeposits = FedhaDatabase.getTotalDepositsByMemberId(memberId);
        if (totalDeposits == -1) {
            System.out.println("Member not found or error occurred while fetching deposits.");
            return 0.0;
        }
        return totalDeposits;
    }

    public static double getTotalDepositsByMemberId(String memberId) {
        double totalDeposits = -1;

        try {
            String sql = "SELECT SUM(sharesContribution) FROM deposits WHERE memberId = ?";
            Connection connection = connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalDeposits = rs.getDouble(1); // Get the sum of deposits
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalDeposits;
    }

    public static double getFixedDeposits(String memberId) {
        double sharesContribution = 0.0;
        try (Connection conn = DriverManager.getConnection(jdbcUrl,username,password);
             PreparedStatement stmt = conn.prepareStatement("SELECT sharesContribution FROM deposits WHERE memberId = ?")) {
            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sharesContribution = rs.getDouble("sharesContribution");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sharesContribution;
    }

    public static boolean isEligibleForLoan(String memberId) {
        try {
            String sql = "SELECT COUNT(DISTINCT MONTH(depositsDate)) AS monthsContributed " +
                    "FROM deposits WHERE memberId = ? AND depositsDate >= DATE_SUB(NOW(), INTERVAL 6 MONTH)";
            Connection connection = connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int monthsContributed = rs.getInt("monthsContributed");
                return monthsContributed >= 6;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // End of Loan Data

    //Start of Deposit Data
    public static void insertSharesContribution(String memberId, String sharesContribution, String depositsDate) {
        String sql = "INSERT INTO deposits (memberId, sharesContribution, depositsDate) VALUES (?, ?, ?)";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set parameters
            pstmt.setString(1, memberId);
            pstmt.setString(2, sharesContribution);
            pstmt.setString(3, depositsDate);

            // Execute the insert command
            pstmt.executeUpdate();
            System.out.println("Shares contribution inserted successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while inserting shares contribution!");
            e.printStackTrace();
        }
    }
    // Method to check if the member has already deposited in the given month and year
    public static boolean hasAlreadyDeposited(String memberId, int year, int month) {
        String sql = "SELECT COUNT(*) FROM deposits WHERE memberId = ? AND YEAR(depositsDate) = ? AND MONTH(depositsDate) = ?";

        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, memberId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    //End of Deposit Data

    // Start of Monthly Repayment
    public static void insertMonthlyRepayment(String memberId, double loanBorrowed, double RepaymentAmount, double loanBalance, String loanRepaymentDate) {
        String sql = "INSERT INTO monthlyRepayment (memberId, loanBorrowed, RepaymentAmount, loanBalance, loanRepaymentDate) VALUES (?,?,?,?,?)";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set parameters
            pstmt.setString(1, memberId);
            pstmt.setDouble(2, loanBorrowed);
            pstmt.setDouble(3, RepaymentAmount);
            pstmt.setDouble(4, loanBalance);
            pstmt.setString(5, loanRepaymentDate);

            // Execute the insert command
            pstmt.executeUpdate();
            System.out.println("Loan repayment successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while inserting repaying the loan!");
            e.printStackTrace();
        }
    }

    public static double getTotalLoansByMemberId(String memberId) {
        double totalLoanAmount = 0.0;
        String sql = "SELECT SUM(loanAmount) FROM loans WHERE memberId = ?";

        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalLoanAmount = rs.getDouble(1); // Get the sum of all loans
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return totalLoanAmount;
    }

    public static Optional<Double> getLoanAmount(String memberId) {
        String sql = "SELECT loanRepaymentAmount FROM loans WHERE memberId = ? AND loanRepaymentAmount > 0 LIMIT 1";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getDouble("loanRepaymentAmount"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static double getTotalRepaymentsByMemberId(String memberId) {
        double totalRepaymentAmount = 0.0;
        String sql = "SELECT SUM(RepaymentAmount) FROM monthlyRepayment WHERE memberId = ?";

        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalRepaymentAmount = rs.getDouble(1); // Get the sum of all repayments
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return totalRepaymentAmount;
    }

    public static double getOutstandingLoan(String memberId) throws SQLException {
        double outstandingLoan = 0.0;
        String query = "SELECT loanRepaymentAmount - IFNULL(SUM(monthlyRepayment.RepaymentAmount), 0) AS outstandingLoan " +
                "FROM loans LEFT JOIN monthlyRepayment ON loans.memberId = monthlyRepayment.memberId " +
                "WHERE loans.memberId = ?" +
                "GROUP BY loans.loanRepaymentAmount";

        try (Connection conn = DriverManager.getConnection(jdbcUrl,username,password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                outstandingLoan = rs.getDouble("outstandingLoan");
            }
        }
        return outstandingLoan;
    }

    //End of Monthly Repayment

    // Start of AdminBar Data
    public static void insertFixedDeposits(String authenticationNumber, String fixedDepositsAmount, String fixedDepositsDuration, String fixedDepositsInterest,String fixedDepositsDate) {
        String sql = "INSERT INTO fixedDeposits(authenticationNumber, fixedDepositsAmount,fixedDepositsDuration,fixedDepositsInterest,fixedDepositsDate) VALUES (?, ?, ?,?,?)";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set parameters
            pstmt.setString(1, authenticationNumber);
            pstmt.setString(2, fixedDepositsAmount);
            pstmt.setString(3, fixedDepositsDuration);
            pstmt.setString(4, fixedDepositsInterest);
            pstmt.setString(5,fixedDepositsDate);
            // Execute the insert command
            pstmt.executeUpdate();
            System.out.println("Fixed deposits successfully");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error occured while depositing fixed deposits");
            e.printStackTrace();
        }
    }
    public static void fetchMember(String query, JTable table) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Define table model with column names
            DefaultTableModel model = new DefaultTableModel(new String[]{"Member ID", "Surname", "Other Names", "Registration Fees"}, 0);

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                String memberId = resultSet.getString("memberId");
                String surname = resultSet.getString("surname");
                String otherNames = resultSet.getString("otherNames");
                String registrationFees = resultSet.getString("registrationFees");

                model.addRow(new Object[]{memberId, surname, otherNames, registrationFees});
            }

            // Set model to the table
            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchTypeOfLoansData(DefaultTableModel model) {
        model.setRowCount(0);  // Clear existing rows
        try (Statement statement = connect().createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT typeOfLoans, interestRate, repaymentPeriod FROM typeOfLoans")) {
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getString("typeOfLoans"),
                        resultSet.getString("interestRate"),
                        resultSet.getString("repaymentPeriod")
                });
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateTypeOfLoan(DefaultTableModel model, int row, int column) {
        String typeOfLoan = model.getValueAt(row, 0).toString();
        String columnName = model.getColumnName(column);
        Object newValue = model.getValueAt(row, column);

        String updateQuery = "UPDATE typeOfLoans SET " + columnName + " = ? WHERE typeOfLoans = ?";
        try (PreparedStatement statement = connect().prepareStatement(updateQuery)) {
            statement.setObject(1, newValue);
            statement.setString(2, typeOfLoan);
            statement.executeUpdate();
            System.out.println(columnName + " updated successfully.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void fetchMemberShares(String query, JTable table) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            DefaultTableModel model = new DefaultTableModel(new String[]{"Member ID", "Surname", "Other Names", "Deposit Amount", "Deposit Date"}, 0);
            while (resultSet.next()) {
                String memberId = resultSet.getString("memberId");
                String surname = resultSet.getString("surname");
                String otherNames = resultSet.getString("otherNames");
                String sharesContribution = resultSet.getString("sharesContribution");
                String depositsDate = resultSet.getString("depositsDate");

                model.addRow(new Object[]{memberId, surname, otherNames, sharesContribution,depositsDate});
            }
            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void fetchLoansDebt(String query, JTable table) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Define table model with updated column names
            DefaultTableModel model = new DefaultTableModel(new String[]{"Member ID", "Surname", "Other Names", "Total Loan"}, 0);

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                String memberId = resultSet.getString("memberId");
                String surname = resultSet.getString("surname");
                String otherNames = resultSet.getString("otherNames");
                double totalLoan = resultSet.getDouble("totalLoan");

                model.addRow(new Object[]{memberId, surname, otherNames, totalLoan});
            }

            // Set model to the table
            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLoanDetailsForMember(String memberId, int row, JTable loansTable) {
        DefaultTableModel model = (DefaultTableModel) loansTable.getModel();
        String url = "jdbc:mysql://localhost:3306/fedha_youth_group_schema";
        String username = "root";
        String password = "Mousa@muigai123!";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT loanAmount, loanDate FROM loans WHERE memberId = ?")) {

            statement.setString(1, memberId);
            ResultSet resultSet = statement.executeQuery();

            // Format the details as a row and insert below the clicked row
            while (resultSet.next()) {
                double loanAmount = resultSet.getDouble("loanAmount");
                Date loanDate = resultSet.getDate("loanDate");

                // Insert a new row with details under the selected member row
                model.insertRow(row + 1, new Object[]{"", "", "Loan: " + loanAmount, "Date: " + loanDate});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fetchGuarantor(String query, JTable table) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Define table model with column names
            DefaultTableModel model = new DefaultTableModel(new String[]{"Member ID", "Guarantors'"}, 0);

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                String memberId = resultSet.getString("memberId");
                String guarantor = resultSet.getString("guarantor");

                model.addRow(new Object[]{memberId, guarantor});
            }

            // Set model to the table
            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateMember(String memberId, String surname, String otherNames, String registrationFees) {
        String updateQuery = "UPDATE members SET surname = ?, otherNames = ?, registrationFees = ? WHERE memberId = ?";

        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setString(1, surname);
            pstmt.setString(2, otherNames);
            pstmt.setString(3, registrationFees);
            pstmt.setString(4, memberId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Member details updated successfully.");
            } else {
                System.out.println("No member found with the provided Member ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while updating member details.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public static double calculateDividends(String memberId) {
        double totalDividendPool = 1000;
        double memberShares = 0;
        double totalShares = 0;

        String memberSharesQuery = "SELECT SUM(sharesContribution) AS memberShares " +
                "FROM deposits WHERE memberId = ?";
        String totalSharesQuery = "SELECT SUM(sharesContribution) AS totalShares FROM deposits";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement memberStatement = connection.prepareStatement(memberSharesQuery);
             PreparedStatement totalStatement = connection.prepareStatement(totalSharesQuery)) {

            // Fetch member's total sharesContribution
            memberStatement.setString(1, memberId);
            try (ResultSet resultSet = memberStatement.executeQuery()) {
                if (resultSet.next()) {
                    memberShares = resultSet.getDouble("memberShares");
                }
            }

            // Fetch total sharesContribution from all members
            try (ResultSet resultSet = totalStatement.executeQuery()) {
                if (resultSet.next()) {
                    totalShares = resultSet.getDouble("totalShares");
                }
            }

            // Avoid division by zero
            if (totalShares == 0) {
                return 0;
            }
            // Calculate member's dividend
            return (memberShares / totalShares) * totalDividendPool;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error calculating dividends: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    // End of AdminBar Data

    // Start of Exit Data
    public static void insertExit(String memberId, String fullName, String guaranteedLoans, String outstandingLoan, String reasonForExit, String noticeDate, String exitingDate) {
        String sql = "INSERT INTO exitRequests(memberId, fullName, guaranteedLoans ,outstandingLoan, reasonForExit, noticeDate, exitinGDate) VALUES (?, ?, ?,?, ?, ?, ?)";
        try (Connection connection = connect(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set parameters
            pstmt.setString(1, memberId);
            pstmt.setString(2, fullName);
            pstmt.setString(3, outstandingLoan);
            pstmt.setString(4,guaranteedLoans);
            pstmt.setString(5, reasonForExit);
            pstmt.setString(6, noticeDate);
            pstmt.setString(7, exitingDate);
            // Execute the insert command
            pstmt.executeUpdate();
            System.out.println("Notice successfully sent for approval");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Failed to send notice for approval");
            e.printStackTrace();
        }
    }
    public static double getTotalLoanAmountForMember(String memberId) {
        String url = "jdbc:mysql://localhost:3306/fedha_youth_group_schema";
        String username = "root";
        String password = "Mousa@muigai123!";
        double totalLoanAmount = 0.0;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT loanBalance FROM loans JOIN monthlyRepayment ON monthlyRepayment.memberId = loans.memberId " +
                             "WHERE monthlyRepayment.memberId = ?")) {

            statement.setString(1, memberId);
            ResultSet resultSet = statement.executeQuery();

            // Calculate the total loan amount for the specified memberId
            while (resultSet.next()) {
                double loanAmount = resultSet.getDouble("loanBalance");
                totalLoanAmount += loanAmount;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalLoanAmount;
    }
    // Get guaranteedLoans For Exit
    public static String getGuaranteedLoans(String memberId) {
        StringBuilder guaranteedLoans = new StringBuilder();
        // SQL to fetch loans guaranteed by the member
        String query = "SELECT loans.memberId AS borrowerId, loans.loanRepaymentAmount " +
                "FROM loans " +
                "JOIN members ON CONCAT(members.surname, ' ', members.otherNames) = loans.guarantor " +
                "WHERE members.memberId = ?";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, memberId);
            ResultSet resultSet = statement.executeQuery();
            // Fetch and format the details
            while (resultSet.next()) {
                String borrowerId = resultSet.getString("borrowerId");
                double loanAmount = resultSet.getDouble("loanRepaymentAmount");

                // Append the loan information (borrower's memberId and loan amount)
                guaranteedLoans.append("Borrower ID: ").append(borrowerId)
                        .append(", Loan Amount: ").append(loanAmount)
                        .append(";\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return guaranteedLoans.toString();
    }

    public static boolean hasMemberExited(String memberId) {
        String query = "SELECT COUNT(*) FROM exitRequests WHERE memberId = ?";

        try (Connection connection = connect();PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, memberId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }
    // End of Exit Data

    // Start of MemberInformation Data

     // Fetch member details
    public static Map<String, String> getMemberDetails(String memberId) throws SQLException, ClassNotFoundException {
        String query = "SELECT surname, otherNames,day,month,year,phone, email, registrationFees, activationDate, status FROM members WHERE memberId = ?";
        try (Connection conn = connect();PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, String> memberData = new HashMap<>();
                // Concatenate surname and otherNames to form fullName
                String fullName = rs.getString("surname") + " " + rs.getString("otherNames");
                memberData.put("fullName", fullName);
                memberData.put("day",rs.getString("day"));
                memberData.put("month",rs.getString("month"));
                memberData.put("year",rs.getString("year"));
                memberData.put("phone", rs.getString("phone"));
                memberData.put("email", rs.getString("email"));
                memberData.put("registrationFees", rs.getString("registrationFees"));
                memberData.put("activationDate", rs.getString("activationDate"));
                memberData.put("status", rs.getString("status"));
                return memberData;
            }
        }
        return null;
    }

    // Method to update specific member details
    public static void updateMember(String memberId, String day, String month, String year, String phone, String email, String status, String updateDate) throws SQLException, ClassNotFoundException {
        // Convert day, month, and year into a proper date format for SQL
        LocalDate dob = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        // SQL query to update member information in the members table
        String sql = "UPDATE members SET day = ?, month =?,year =?, phone = ?, email = ?, status = ?, updateDate = ? WHERE memberId = ?";

        try (Connection conn = connect();PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the SQL query
            pstmt.setString(1, day);
            pstmt.setString(2, month);
            pstmt.setString(3, year);
            pstmt.setString(4, phone);
            pstmt.setString(5, email);
            pstmt.setString(6, status);
            pstmt.setString(7,updateDate);
            pstmt.setString(8, memberId);

            // Execute the update
            int rowsUpdated = pstmt.executeUpdate();

            // Optional: Check if the update affected any rows
            if (rowsUpdated == 0) {
                throw new SQLException("Member update failed, no rows affected.");
            }
        }
    }

    public static void getLoanDetails(DefaultTableModel model, String memberId) {
        String query = "SELECT loanRepaymentAmount, loanType, interestRate, repaymentPeriod, " +
                "loanDue, guarantor, monthlyRepayment.loanBalance, loanDate " +
                "FROM loans JOIN monthlyRepayment ON monthlyRepayment.memberId = loans.memberId " +
                "WHERE loans.memberId = ? " +
                "GROUP BY loans.memberId, monthlyRepayment.loanBalance, loans.loanRepaymentAmount, " +
                "loans.loanType, loans.interestRate, loans.repaymentPeriod, loans.loanDue, loans.guarantor, loans.loanDate";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, memberId);  // Set the memberId parameter

            try (ResultSet rs = stmt.executeQuery()) {
                model.setRowCount(0);  // Clear existing rows
                while (rs.next()) {
                    Object[] row = {
                            rs.getDouble("loanRepaymentAmount"),
                            rs.getString("loanType"),
                            rs.getString("interestRate"),
                            rs.getString("repaymentPeriod"),
                            rs.getDate("loanDue"),
                            rs.getString("guarantor"),
                            rs.getDouble("loanBalance"),
                            rs.getDate("loanDate")
                    };
                    model.addRow(row);  // Add row to the model
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getDepositDetails(DefaultTableModel model, String memberId) {
        String query = "SELECT memberId, sharesContribution, depositsDate FROM deposits WHERE memberId = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, memberId);  // Set the memberId in the query

            try (ResultSet rs = stmt.executeQuery()) {
                model.setRowCount(0);  // Clear existing rows
                while (rs.next()) {
                    Object[] row = {
                            rs.getString("memberId"),
                            rs.getString("sharesContribution"),
                            rs.getDate("depositsDate")
                    };
                    model.addRow(row);  // Add row to the model
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getLoanRepaymentDetails(DefaultTableModel model, String memberId) {
        String query = "SELECT monthlyRepayment.memberId, loans.loanRepaymentAmount, RepaymentAmount, loanBalance, loanRepaymentDate " +
                "FROM monthlyRepayment JOIN loans ON loans.memberId = monthlyRepayment.memberId WHERE monthlyRepayment.memberId = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, memberId);  // Set the memberId parameter

            try (ResultSet rs = stmt.executeQuery()) {
                model.setRowCount(0);  // Clear existing rows
                while (rs.next()) {
                    Object[] row = {
                            rs.getString("memberId"),
                            rs.getString("loanRepaymentAmount"),
                            rs.getString("RepaymentAmount"),
                            rs.getDouble("loanBalance"),
                            rs.getDate("loanRepaymentDate")
                    };
                    model.addRow(row);  // Add row to the model
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getExitDetails(DefaultTableModel model, String memberId) {
        String query = "SELECT fullName, outstandingLoan, guaranteedLoans, reasonForExit, " +
                "noticeDate, exitingDate,approval FROM exitRequests WHERE memberId = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, memberId);  // Set the memberId in the query

            try (ResultSet rs = stmt.executeQuery()) {
                model.setRowCount(0);  // Clear existing rows
                while (rs.next()) {
                    Object[] row = {
                            rs.getString("fullName"),
                            rs.getString("outstandingLoan"),
                            rs.getString("guaranteedLoans"),
                            rs.getString("reasonForExit"),
                            rs.getDate("noticeDate"),
                            rs.getDate("exitingDate"),
                            rs.getString("approval")
                    };
                    model.addRow(row);  // Add row to the model
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // End of MemberInformation Data
}
//DELETE FROM `fedha_youth_group_schema`.`deposits` WHERE `iddeposits` = '1';
//DELETE FROM `fedha_youth_group_schema`.`deposits` WHERE `iddeposits` = '2';
//DELETE FROM `fedha_youth_group_schema`.`deposits` WHERE `iddeposits` = '3';
//DELETE FROM `fedha_youth_group_schema`.`deposits` WHERE `iddeposits` = '4';
// truncate table exitRequests;
//ALTER TABLE `fedha_youth_group_schema`.`deposits` DROP COLUMN `iddeposits`;
//ALTER TABLE `fedha_youth_group_schema`.`deposits`
//ADD COLUMN `iddeposits` INT NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;