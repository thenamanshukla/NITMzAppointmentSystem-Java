package util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class TextSlipGenerator {

    public static void generateSlip(
            String enrollmentNo,
            String authority,
            String slot,
            String slipId) {

        // Works on Windows, macOS, and Linux
        String desktop = Paths.get(System.getProperty("user.home"), "Desktop").toString();
        String path    = desktop + System.getProperty("file.separator")
                       + "AppointmentSlip_" + slipId + ".txt";

        try (FileWriter fw = new FileWriter(path)) {
            fw.write("NIT MIZORAM\n");
            fw.write("ADMINISTRATION APPOINTMENT SLIP\n\n");
            fw.write("Slip ID       : " + slipId       + "\n");
            fw.write("Enrollment No : " + enrollmentNo + "\n");
            fw.write("Authority     : " + authority    + "\n");
            fw.write("Slot          : " + slot         + "\n\n");
            fw.write("Please report 10 minutes before the scheduled time.\n");
            fw.write("This slip is system generated.\n");
            System.out.println("TXT slip saved to: " + path);
        } catch (IOException e) {
            System.err.println("Failed to write slip: " + e.getMessage());
        }
    }
}
