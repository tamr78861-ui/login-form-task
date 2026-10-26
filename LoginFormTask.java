import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class User {
    private final String username;
    private final String password;
    private final String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}

class WelcomePage extends Frame {
    WelcomePage(User user) {
        super("Welcome");
        setLayout(new GridLayout(2, 1, 8, 8));
        add(new Label("Hello, " + user.getUsername(), Label.CENTER));
        add(new Label("Role: " + user.getRole(), Label.CENTER));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(320, 150);
        setLocationRelativeTo(null);
    }
}

public class LoginFormTask extends Frame {
    private final List<User> users = new ArrayList<>();
    private int failedAttempts = 0;

    public LoginFormTask() {
        super("Login");
        loadUsers();

        setLayout(new GridBagLayout());
        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));
        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        Button cancel = new Button("Cancel");
        Button login = new Button("Login");
        buttons.add(cancel);
        buttons.add(login);
        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        login.addActionListener(e -> {
            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                showMessage("Please enter both username and password.");
                return;
            }

            User user = findUser(enteredUsername);
            if (user != null
                    && user.getPassword().equals(enteredPassword)
                    && user.getRole().equals(role.getSelectedItem())) {
                new WelcomePage(user).setVisible(true);
                dispose();
            } else {
                failedAttempts++;
                if (failedAttempts >= 3) {
                    login.setEnabled(false);
                    showMessage("Login disabled after 3 incorrect attempts.");
                } else {
                    showMessage("Incorrect username, password, or role. Attempts remaining: "
                            + (3 - failedAttempts));
                }
            }
        });

        cancel.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Login", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(new Label(message, Label.CENTER), BorderLayout.CENTER);
        Button ok = new Button("OK");
        ok.addActionListener(e -> dialog.dispose());
        Panel buttonPanel = new Panel();
        buttonPanel.add(ok);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setSize(350, 120);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadUsers() {
        users.add(new User("student1", "stu123", "Student"));
        users.add(new User("ta1", "ta123", "TA"));
        users.add(new User("prof1", "prof123", "Professor"));
    }

    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new LoginFormTask().setVisible(true));
    }
}

