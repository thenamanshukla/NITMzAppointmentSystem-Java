package ui;

import util.TextSlipGenerator;

import javax.swing.*;
import java.awt.*;

public class AppointmentSlipFrame extends JFrame {

    public AppointmentSlipFrame(
            String enrollmentNo,
            String authority,
            String slot,
            String slipId) {

        setTitle("Appointment Slip");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea slip = new JTextArea();
        slip.setEditable(false);
        slip.setFont(new Font("Monospaced", Font.PLAIN, 14));
        slip.setText(
            "NIT MIZORAM\n" +
            "ADMINISTRATION APPOINTMENT SLIP\n\n" +
            "Slip ID       : " + slipId       + "\n" +
            "Enrollment No : " + enrollmentNo + "\n" +
            "Authority     : " + authority    + "\n" +
            "Slot          : " + slot         + "\n\n" +
            "Please report 10 minutes before the scheduled time.\n" +
            "This slip is system generated."
        );

        add(new JScrollPane(slip), BorderLayout.CENTER);

        JButton txtBtn = new JButton("Export as TXT");
        txtBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtBtn.addActionListener(e -> {
            TextSlipGenerator.generateSlip(enrollmentNo, authority, slot, slipId);
            JOptionPane.showMessageDialog(this,
                    "Text slip saved to Desktop.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel bottom = new JPanel();
        bottom.add(txtBtn);
        add(bottom, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        toFront();
        requestFocus();
    }
}
