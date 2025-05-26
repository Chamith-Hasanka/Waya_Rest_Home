import javax.swing.*;

public class RegistrationForm extends JFrame {
    private JTextField usernameField, phoneField, dobField, emailField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegistrationForm() {
        setTitle("User Registration");
        setLayout(null);
        setSize(350, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // UI components
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 80, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 20, 180, 25);
        add(usernameField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(20, 60, 80, 25);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(120, 60, 180, 25);
        add(phoneField);

        JLabel dobLabel = new JLabel("DOB:");
        dobLabel.setBounds(20, 100, 80, 25);
        add(dobLabel);

        dobField = new JTextField();
        dobField.setBounds(120, 100, 180, 25);
        add(dobField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 140, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(120, 140, 180, 25);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 180, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 180, 180, 25);
        add(passwordField);

        registerButton = new JButton("Register");
        registerButton.setBounds(120, 220, 100, 30);
        add(registerButton);

        registerButton.addActionListener(e -> registerUser());

        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String phone = phoneField.getText();
        String dob = dobField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Simple validation
        if (username.isEmpty() || phone.isEmpty() || dob.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Invalid email.");
            return;
        }
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Invalid phone number.");
            return;
        }

        User user = new User(username, phone, dob, email, password);
        boolean success = Database.saveUser(user);

        if (success) {
            JOptionPane.showMessageDialog(this, "User registered successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error saving user.");
        }
    }
}
