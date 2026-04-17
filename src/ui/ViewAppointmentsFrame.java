package ui;

import model.Appointment;
import util.AppointmentDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewAppointmentsFrame extends JFrame {

    public ViewAppointmentsFrame(String authority) {
        setTitle("Appointments - " + authority);
        setSize(700, 400);
        setLocationRelativeTo(null);

        String[] columns = {"Enrollment No", "Authority", "Slot"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        List<Appointment> list = AppointmentDAO.getByAuthority(authority);
        for (Appointment a : list) {
            model.addRow(new Object[]{
                a.getEnrollmentNo(),
                a.getAuthority(),
                a.getSlot()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);

        JLabel countLabel = new JLabel(
                "Total: " + list.size() + " appointment(s)", JLabel.RIGHT);
        countLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 8));

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(countLabel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
