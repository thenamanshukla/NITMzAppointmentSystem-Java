package ui;

import javax.swing.*;
import java.awt.*;

public class AuthorityDashboard extends JFrame {

    private final String role;

    public AuthorityDashboard(String role) {
        this.role = role;

        setTitle("NIT Mizoram - " + role);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel roleLabel = new JLabel("Logged in as: " + role, JLabel.CENTER);
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        roleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(roleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));
        centerPanel.setBackground(new Color(245, 248, 250));

        JButton viewBtn = new JButton("View Appointments");
        JButton slotBtn = new JButton("Edit Available Slots");

        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        slotBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        centerPanel.add(viewBtn);
        centerPanel.add(slotBtn);
        add(centerPanel, BorderLayout.CENTER);

        viewBtn.addActionListener(e -> new ViewAppointmentsFrame(role));
        slotBtn.addActionListener(e -> new EditSlotsFrame(role));

        setVisible(true);
    }
}
