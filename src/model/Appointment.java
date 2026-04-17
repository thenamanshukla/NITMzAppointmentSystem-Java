package model;

public class Appointment {

    private String enrollmentNo;
    private String authority;
    private String slot;   // formatted as "YYYY-MM-DD | HH:MM - HH:MM"

    public Appointment(String enrollmentNo, String authority, String slot) {
        this.enrollmentNo = enrollmentNo;
        this.authority    = authority;
        this.slot         = slot;
    }

    public String getEnrollmentNo() { return enrollmentNo; }
    public String getAuthority()    { return authority; }
    public String getSlot()         { return slot; }
}
