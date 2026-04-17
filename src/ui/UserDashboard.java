package ui;

import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    public UserDashboard(String username) {
        setTitle("User Dashboard - " + username);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Welcome, " + username, JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(header, BorderLayout.NORTH);

        JButton bookBtn = new JButton("Book Appointment");
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel center = new JPanel();
        center.add(bookBtn);
        add(center, BorderLayout.CENTER);

        bookBtn.addActionListener(e -> new BookAppointmentFrame(username));

        setVisible(true);
    }
}
