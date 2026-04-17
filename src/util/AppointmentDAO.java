package util;

import model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-Access Object for appointments and slots.
 * Replaces the old in-memory AppointmentStore + SlotStore.
 */
public class AppointmentDAO {

    // ─────────────────────────────────────────────────────────────────────
    //  APPOINTMENTS
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Save a new appointment and mark its slot as unavailable.
     *
     * @return the generated slip_id, or null on failure
     */
    public static String saveAppointment(String enrollmentNo,
                                         String authority,
                                         int    slotId,
                                         String slipId) {
        String sql = "INSERT INTO appointments " +
                     "(slip_id, enrollment_no, authority, slot_id) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, slipId);
            ps.setString(2, enrollmentNo);
            ps.setString(3, authority);
            ps.setInt   (4, slotId);
            ps.executeUpdate();

            // Mark slot as taken
            markSlotUnavailable(slotId);
            return slipId;

        } catch (SQLException e) {
            System.err.println("[DAO] saveAppointment error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fetch all appointments for a given authority.
     * "Dean" matches all Dean sub-categories.
     */
    public static List<Appointment> getByAuthority(String authority) {
        List<Appointment> list = new ArrayList<>();

        String sql = authority.equals("Dean")
                ? "SELECT a.enrollment_no, a.authority, " +
                  "       CONCAT(s.slot_date,' | ',s.slot_time) AS slot " +
                  "FROM appointments a " +
                  "JOIN slots s ON s.id = a.slot_id " +
                  "WHERE a.authority LIKE 'Dean%'"
                : "SELECT a.enrollment_no, a.authority, " +
                  "       CONCAT(s.slot_date,' | ',s.slot_time) AS slot " +
                  "FROM appointments a " +
                  "JOIN slots s ON s.id = a.slot_id " +
                  "WHERE a.authority = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (!authority.equals("Dean")) ps.setString(1, authority);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Appointment(
                        rs.getString("enrollment_no"),
                        rs.getString("authority"),
                        rs.getString("slot")));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] getByAuthority error: " + e.getMessage());
        }
        return list;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  SLOTS
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Fetch available slots for an authority.
     * If none exist, seed default slots for the next 7 days.
     */
    public static List<Slot> getAvailableSlots(String authority) {
        ensureSlotsExist(authority);
        List<Slot> result = new ArrayList<>();

        String sql = "SELECT id, slot_date, slot_time " +
                     "FROM slots " +
                     "WHERE authority = ? AND available = TRUE " +
                     "ORDER BY slot_date, slot_time";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, authority);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Slot s = new Slot(rs.getString("slot_date"),
                                  rs.getString("slot_time"));
                s.id        = rs.getInt("id");
                s.available = true;
                result.add(s);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] getAvailableSlots error: " + e.getMessage());
        }
        return result;
    }

    /** Fetch ALL slots (available + booked) for the Edit Slots screen. */
    public static List<Slot> getAllSlots(String authority) {
        ensureSlotsExist(authority);
        List<Slot> result = new ArrayList<>();

        String sql = "SELECT id, slot_date, slot_time, available " +
                     "FROM slots WHERE authority = ? " +
                     "ORDER BY slot_date, slot_time";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, authority);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Slot s = new Slot(rs.getString("slot_date"),
                                  rs.getString("slot_time"));
                s.id        = rs.getInt("id");
                s.available = rs.getBoolean("available");
                result.add(s);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] getAllSlots error: " + e.getMessage());
        }
        return result;
    }

    /** Toggle a slot's availability in the DB. */
    public static void updateSlotAvailability(int slotId, boolean available) {
        String sql = "UPDATE slots SET available = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setInt    (2, slotId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DAO] updateSlotAvailability error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  AUTH
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Validate a login attempt against the DB.
     *
     * @return "authority" | "student" | null (invalid)
     */
    public static String validateLogin(String username, String password) {
        String sql = "SELECT role FROM users " +
                     "WHERE username = ? AND password = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);          // hash comparison in production
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("role");

        } catch (SQLException e) {
            System.err.println("[DAO] validateLogin error: " + e.getMessage());
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────

    private static void markSlotUnavailable(int slotId) {
        updateSlotAvailability(slotId, false);
    }

    /** Seed 7 days × 4 time-slots if this authority has no rows yet. */
    private static void ensureSlotsExist(String authority) {
        String check = "SELECT COUNT(*) FROM slots WHERE authority = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(check)) {

            ps.setString(1, authority);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return;  // already seeded

        } catch (SQLException e) {
            System.err.println("[DAO] ensureSlotsExist check error: " + e.getMessage());
            return;
        }

        // Seed
        String insert = "INSERT IGNORE INTO slots (authority, slot_date, slot_time) " +
                        "VALUES (?, ?, ?)";
        String[] times = {"10:00 - 10:15", "10:15 - 10:30",
                          "11:00 - 11:15", "11:15 - 11:30"};

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(insert)) {

            java.time.LocalDate today = java.time.LocalDate.now();
            for (int i = 0; i < 7; i++) {
                String date = today.plusDays(i).toString();
                for (String t : times) {
                    ps.setString(1, authority);
                    ps.setString(2, date);
                    ps.setString(3, t);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            System.out.println("[DAO] Seeded default slots for: " + authority);

        } catch (SQLException e) {
            System.err.println("[DAO] ensureSlotsExist seed error: " + e.getMessage());
        }
    }
}
