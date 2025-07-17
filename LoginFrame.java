import java.sql.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    JTextField accField;
    JPasswordField passField;

    public LoginFrame() {
        setTitle("Bank Login");
        setLayout(null);

        JLabel label1 = new JLabel("Account No:");
        accField = new JTextField();
        JLabel label2 = new JLabel("Password:");
        passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        label1.setBounds(30, 30, 100, 25);
        accField.setBounds(140, 30, 150, 25);
        label2.setBounds(30, 70, 100, 25);
        passField.setBounds(140, 70, 150, 25);
        loginBtn.setBounds(40, 110, 100, 30);
        registerBtn.setBounds(160, 110, 100, 30);

        add(label1); add(accField);
        add(label2); add(passField);
        add(loginBtn); add(registerBtn);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        setSize(350, 220);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    void login() {
        int accNo = Integer.parseInt(accField.getText());
        String pass = new String(passField.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM accounts WHERE account_number=? AND password=?");
            ps.setInt(1, accNo);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new DashboardFrame(accNo, rs.getString("name"));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
