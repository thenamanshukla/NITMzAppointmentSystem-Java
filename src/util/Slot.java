package util;

public class Slot {
    public int     id;          // database primary key (0 if not yet persisted)
    public String  date;
    public String  time;
    public boolean available;

    public Slot(String date, String time) {
        this.date      = date;
        this.time      = time;
        this.available = true;
    }

    @Override
    public String toString() {
        return date + " | " + time;
    }
}
