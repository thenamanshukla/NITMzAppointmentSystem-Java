package ui;

import util.AppointmentDAO;
import util.Slot;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EditSlotsFrame extends JFrame {

    public EditSlotsFrame(String role) {
        setTitle("Edit Available Slots - " + role);
        setSize(750, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Date", "Time Slot", "Available"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 2 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 2; // only the checkbox column is editable
            }
        };

        List<Slot> slots = AppointmentDAO.getAllSlots(role);
        for (Slot s : slots) {
            model.addRow(new Object[]{s.date, s.time, s.available});
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);

        // Colour-code rows by availability
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, col);
                Boolean avail = (Boolean) t.getModel().getValueAt(row, 2);
                c.setBackground(Boolean.TRUE.equals(avail)
                        ? new Color(200, 255, 200)
                        : new Color(255, 200, 200));
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save Slot Changes");
        saveBtn.addActionListener(e -> {
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean available = (Boolean) model.getValueAt(i, 2);
                AppointmentDAO.updateSlotAvailability(slots.get(i).id, available);
                slots.get(i).available = available;
            }
            JOptionPane.showMessageDialog(this,
                    "Slot changes saved to database.", "Saved",
                    JOptionPane.INFORMATION_MESSAGE);
            table.repaint();
        });

        JPanel bottom = new JPanel();
        bottom.add(saveBtn);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }
}
