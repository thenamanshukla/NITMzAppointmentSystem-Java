package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Singleton MySQL connection helper.
 *
 * Configuration — set these environment variables before running:
 *
 *   DB_URL      jdbc:mysql://localhost:3306/nit_appointment   (default)
 *   DB_USER     root                                          (default)
 *   DB_PASS     (no default — must be set)
 *
 * Or edit the fallback constants below for quick local testing.
 */
public class DBConnection {

    // ── fallback defaults (override via env vars) ──────────────────────────
    private static final String DEFAULT_URL  =
            "jdbc:mysql://localhost:3306/nit_appointment?useSSL=false&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "";          // set DB_PASS env var

    private static Connection conn;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {

                String url  = env("DB_URL",  DEFAULT_URL);
                String user = env("DB_USER", DEFAULT_USER);
                String pass = env("DB_PASS", DEFAULT_PASS);

                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, pass);
                System.out.println("[DB] Connected to MySQL successfully.");
            }
        } catch (Exception e) {
            System.err.println("[DB] Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /** Read env var, fall back to defaultVal if not set or blank. */
    private static String env(String key, String defaultVal) {
        String val = System.getenv(key);
        return (val != null && !val.isBlank()) ? val : defaultVal;
    }
}
