# NIT Mizoram Administration Appointment System

A Java Swing desktop application for booking and managing appointments with administrative authorities at NIT Mizoram.

---

## Features

- **Student login** — book appointments with any administrative authority
- **Authority login** — view booked appointments, manage available slots
- **MySQL-backed** — all data persists across sessions
- **Appointment slip** — generates a printable TXT slip on the Desktop
- **Role-based access** — separate dashboards for students and authorities

---

## Supported Authorities

| Username | Dashboard Role |
|---|---|
| `director` | Director |
| `registrar` | Registrar |
| `dean` | Dean (all sub-categories) |
| `admin` | Admin Section |
| `finance` | Finance Section |
| `erp` | ERP Section |

Student accounts follow the pattern: `BT<YY><BRANCH><NNN>` (e.g. `BT23CS001`)

---

## Project Structure

```
NITAppointmentSystem/
├── database/
│   └── schema.sql          ← Run this first to create DB + seed data
├── lib/
│   ├── openpdf-1.3.30.jar
│   └── mysql-connector-j-*.jar   ← Download separately (see below)
├── src/
│   ├── App.java
│   ├── model/
│   │   ├── Appointment.java
│   │   └── User.java
│   ├── ui/
│   │   ├── LoginFrame.java
│   │   ├── UserDashboard.java
│   │   ├── AuthorityDashboard.java
│   │   ├── BookAppointmentFrame.java
│   │   ├── ViewAppointmentsFrame.java
│   │   ├── EditSlotsFrame.java
│   │   └── AppointmentSlipFrame.java
│   ├── util/
│   │   ├── DBConnection.java
│   │   ├── AppointmentDAO.java
│   │   ├── Slot.java
│   │   ├── SlotStore.java
│   │   └── TextSlipGenerator.java
│   └── resources/
│       ├── nit_logo.png
│       └── nit_building.jpg
├── .env.example
├── .gitignore
└── README.md
```

---

## Prerequisites

| Tool | Version |
|---|---|
| Java JDK | 11 or higher |
| MySQL | 8.0 or higher |
| MySQL Connector/J | 8.x (JDBC driver) |

---

## Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/NITAppointmentSystem.git
cd NITAppointmentSystem
```

### 2. Set up the database

```bash
mysql -u root -p < database/schema.sql
```

This creates the `nit_appointment` database with all tables and seed user accounts.

### 3. Download the JDBC driver

Download **MySQL Connector/J** from:  
https://dev.mysql.com/downloads/connector/j/

Place the `.jar` file inside the `lib/` folder:
```
lib/mysql-connector-j-8.x.x.jar
```

### 4. Configure database credentials

Copy `.env.example` to `.env` and fill in your MySQL password:

```bash
cp .env.example .env
```

Edit `.env`:
```
DB_URL=jdbc:mysql://localhost:3306/nit_appointment?useSSL=false&serverTimezone=UTC
DB_USER=root
DB_PASS=your_mysql_password
```

Then set these as environment variables before running:

**Windows (Command Prompt):**
```cmd
set DB_USER=root
set DB_PASS=your_password
```

**macOS / Linux:**
```bash
export DB_USER=root
export DB_PASS=your_password
```

### 5. Compile

```bash
javac -cp "lib/*" -d bin src/App.java src/model/*.java src/ui/*.java src/util/*.java
```

Also copy resources into bin:
```bash
cp -r src/resources bin/
```

### 6. Run

```bash
java -cp "bin;lib/*" App          # Windows
java -cp "bin:lib/*" App          # macOS / Linux
```

---

## Default Login Credentials

> ⚠️ Change these passwords in the database before deploying.

| Role | Username | Password |
|---|---|---|
| Director | `director` | `admin123` |
| Registrar | `registrar` | `admin123` |
| Dean | `dean` | `admin123` |
| Admin | `admin` | `admin123` |
| Finance | `finance` | `admin123` |
| ERP | `erp` | `admin123` |
| Student | `BT23CS001` | `student123` |

---

## Database Schema

```
users          — login credentials and roles
slots          — available appointment time windows per authority
appointments   — confirmed bookings (linked to slots)
```

Full schema: [`database/schema.sql`](database/schema.sql)

---

## Tech Stack

- **Java** (Swing GUI)
- **MySQL** (persistence)
- **JDBC** (database connectivity)
- **OpenPDF** (PDF generation library, included in lib/)

---

## Contributing

1. Fork the repo
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push and open a Pull Request

---

## License

This project was developed as part of coursework at NIT Mizoram.
