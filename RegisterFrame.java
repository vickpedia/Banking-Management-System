import java.sql.*;
import javax.swing.*;

public class RegisterFrame extends JFrame {
    JTextField nameField;
    JPasswordField passField;

    public RegisterFrame() {
        setTitle("Register Account");
        setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        passField = new JPasswordField();
        JButton registerBtn = new JButton("Create");

        nameLabel.setBounds(30, 30, 100, 25);
        nameField.setBounds(140, 30, 150, 25);
        passLabel.setBounds(30, 70, 100, 25);
        passField.setBounds(140, 70, 150, 25);
        registerBtn.setBounds(100, 120, 100, 30);

        add(nameLabel); add(nameField);
        add(passLabel); add(passField);
        add(registerBtn);

        registerBtn.addActionListener(e -> register());

        setSize(350, 220);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    void register() {
        String name = nameField.getText();
        String pass = new String(passField.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO accounts(name, password) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, pass);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int accNo = rs.getInt(1);
                JOptionPane.showMessageDialog(this, "Account created! Your Account No: " + accNo);
                dispose();
                new LoginFrame();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
