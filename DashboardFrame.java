import java.sql.*;
import javax.swing.*;

public class DashboardFrame extends JFrame {
    int accNo;
    JLabel welcomeLabel;

    public DashboardFrame(int accNo, String name) {
        this.accNo = accNo;

        setTitle("Dashboard - " + name);
        setLayout(null);

        welcomeLabel = new JLabel("Welcome, " + name + " (Acc: " + accNo + ")");
        JButton balanceBtn = new JButton("Balance");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton logoutBtn = new JButton("Logout");

        welcomeLabel.setBounds(30, 20, 300, 25);
        balanceBtn.setBounds(30, 60, 100, 30);
        depositBtn.setBounds(150, 60, 100, 30);
        withdrawBtn.setBounds(30, 110, 100, 30);
        logoutBtn.setBounds(150, 110, 100, 30);

        add(welcomeLabel);
        add(balanceBtn); add(depositBtn); add(withdrawBtn); add(logoutBtn);

        balanceBtn.addActionListener(e -> showBalance());
        depositBtn.addActionListener(e -> transaction("DEPOSIT"));
        withdrawBtn.addActionListener(e -> transaction("WITHDRAW"));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setSize(320, 220);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    void showBalance() {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT balance FROM accounts WHERE account_number=?");
            ps.setInt(1, accNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double bal = rs.getDouble("balance");
                JOptionPane.showMessageDialog(this, "Your balance: ₹" + bal);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void transaction(String type) {
        String amtStr = JOptionPane.showInputDialog(this, "Enter amount:");
        if (amtStr == null) return;

        try {
            double amount = Double.parseDouble(amtStr);
            try (Connection conn = DBConnection.getConnection()) {
                if (type.equals("WITHDRAW")) {
                    PreparedStatement ps = conn.prepareStatement("SELECT balance FROM accounts WHERE account_number=?");
                    ps.setInt(1, accNo);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next() && rs.getDouble("balance") < amount) {
                        JOptionPane.showMessageDialog(this, "Insufficient balance!");
                        return;
                    }
                    ps = conn.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE account_number=?");
                    ps.setDouble(1, amount);
                    ps.setInt(2, accNo);
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_number=?");
                    ps.setDouble(1, amount);
                    ps.setInt(2, accNo);
                    ps.executeUpdate();
                }

                PreparedStatement ps = conn.prepareStatement("INSERT INTO transactions(account_number, type, amount) VALUES (?, ?, ?)");
                ps.setInt(1, accNo);
                ps.setString(2, type);
                ps.setDouble(3, amount);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, type + " successful!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }
}
