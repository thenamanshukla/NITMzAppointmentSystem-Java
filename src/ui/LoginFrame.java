package ui;

import util.AppointmentDAO;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    JTextField    usernameField;
    JPasswordField passwordField;

    // Authority usernames → display role mapping
    private static final java.util.Map<String, String> AUTHORITY_DISPLAY =
        new java.util.LinkedHashMap<>();

    static {
        AUTHORITY_DISPLAY.put("director",  "Director");
        AUTHORITY_DISPLAY.put("registrar", "Registrar");
        AUTHORITY_DISPLAY.put("admin",     "Admin Section");
        AUTHORITY_DISPLAY.put("finance",   "Finance Section");
        AUTHORITY_DISPLAY.put("erp",       "ERP Section");
        AUTHORITY_DISPLAY.put("dean",      "Dean");
    }

    public LoginFrame() {
        setTitle("NIT Mizoram - Appointment System");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(new BackgroundPanel());
        setLayout(null);

        // ── Logo ──────────────────────────────────────────────────────────
        ImageIcon logoIcon = new ImageIcon(
                getClass().getResource("/resources/nit_logo.png"));
        Image logoImg = logoIcon.getImage().getScaledInstance(
                110, 110, Image.SCALE_SMOOTH);

        JPanel logoPanel = new JPanel();
        logoPanel.setBounds(40, 40, 130, 130);
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setOpaque(true);
        logoPanel.add(new JLabel(new ImageIcon(logoImg)));
        add(logoPanel);

        // ── Title ─────────────────────────────────────────────────────────
        JLabel title = new JLabel("NIT Mizoram Administration Appointment System");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(200, 40, 520, 30);
        add(title);

        // ── Login form ────────────────────────────────────────────────────
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(250, 150, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(350, 150, 200, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(250, 190, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(350, 190, 200, 25);
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(350, 240, 100, 30);
        add(loginBtn);

        loginBtn.addActionListener(e -> authenticate());
        // Allow Enter key on password field
        passwordField.addActionListener(e -> authenticate());

        setVisible(true);
    }

    // ── Authentication (DB-backed) ────────────────────────────────────────
    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username and password.",
                    "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String role = AppointmentDAO.validateLogin(username, password);

        if ("authority".equals(role)) {
            String displayRole = AUTHORITY_DISPLAY.getOrDefault(
                    username.toLowerCase(), username);
            openAuthorityDashboard(displayRole);

        } else if ("student".equals(role)) {
            new UserDashboard(username);
            dispose();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid Username or Password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAuthorityDashboard(String role) {
        new AuthorityDashboard(role);
        dispose();
    }

    // ── Background panel ─────────────────────────────────────────────────
    class BackgroundPanel extends JPanel {
        Image bg;

        public BackgroundPanel() {
            bg = new ImageIcon(
                    getClass().getResource("/resources/nit_building.jpg")
            ).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.35f));
            g2d.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}
