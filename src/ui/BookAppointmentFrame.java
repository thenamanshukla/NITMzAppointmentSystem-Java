package ui;

import util.AppointmentDAO;
import util.Slot;

import javax.swing.*;
import java.util.List;
import java.util.UUID;

public class BookAppointmentFrame extends JFrame {

    private final String enrollmentNo;

    public BookAppointmentFrame(String enrollmentNo) {
        this.enrollmentNo = enrollmentNo;

        setTitle("Book Appointment");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel authLabel = new JLabel("Authority:");
        authLabel.setBounds(100, 60, 120, 25);
        add(authLabel);

        String[] authorities = {
            "Director",
            "Registrar",
            "Dean (Academics)",
            "Dean (R&C)",
            "Dean (Student Welfare)",
            "Dean (Administration)",
            "Dean (Planning & Development)",
            "Admin Section",
            "Finance Section",
            "ERP Section"
        };

        JComboBox<String> authorityBox = new JComboBox<>(authorities);
        authorityBox.setBounds(240, 60, 220, 25);
        add(authorityBox);

        JLabel slotLabel = new JLabel("Available Slots:");
        slotLabel.setBounds(100, 110, 120, 25);
        add(slotLabel);

        JComboBox<String> slotBox = new JComboBox<>();
        slotBox.setBounds(240, 110, 220, 25);
        add(slotBox);

        // Keep track of currently loaded slots (for id lookup)
        final List<Slot>[] currentSlots = new List[]{null};

        // Populate slots when authority changes
        Runnable loadSlots = () -> {
            slotBox.removeAllItems();
            String authority = (String) authorityBox.getSelectedItem();
            List<Slot> slots = AppointmentDAO.getAvailableSlots(authority);
            currentSlots[0] = slots;
            for (Slot s : slots) {
                slotBox.addItem(s.date + " | " + s.time);
            }
            if (slots.isEmpty()) {
                slotBox.addItem("No slots available");
            }
        };

        authorityBox.addActionListener(e -> loadSlots.run());
        loadSlots.run(); // initial load

        JButton confirmBtn = new JButton("Confirm Appointment");
        confirmBtn.setBounds(180, 180, 200, 35);
        add(confirmBtn);

        confirmBtn.addActionListener(e -> {
            int idx = slotBox.getSelectedIndex();
            List<Slot> slots = currentSlots[0];

            if (slots == null || slots.isEmpty() || idx < 0 || idx >= slots.size()) {
                JOptionPane.showMessageDialog(this,
                        "Please select a valid slot.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String authority    = (String) authorityBox.getSelectedItem();
            Slot   selectedSlot = slots.get(idx);
            String slipId       = "APT-" + UUID.randomUUID()
                                                .toString()
                                                .substring(0, 8)
                                                .toUpperCase();

            String result = AppointmentDAO.saveAppointment(
                    enrollmentNo, authority, selectedSlot.id, slipId);

            if (result != null) {
                new AppointmentSlipFrame(
                        enrollmentNo, authority,
                        selectedSlot.date + " | " + selectedSlot.time,
                        slipId);
                loadSlots.run(); // refresh — booked slot disappears
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to book appointment. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
