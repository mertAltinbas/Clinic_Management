package gui.patient.dialogs;

import entity.Appointment;
import entity.Doctor;
import entity.Patient;
import entity.enums.StatusType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UpdateAppointmentDialog extends JDialog {
    private Patient currentPatient;

    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private List<Appointment> appointmentList;

    private JComboBox<String> dateComboBox;
    private JComboBox<String> timeComboBox;
    private JButton rescheduleButton;
    private JButton cancelAppButton;

    public UpdateAppointmentDialog(JFrame parent, Patient patient) {
        super(parent, "Manage Appointments", true);
        this.currentPatient = patient;
        setSize(750, 450);
        setLocationRelativeTo(parent);

        initUI();
        loadDateTimes();
        loadAppointments();
        initListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // table area
        tableModel = new DefaultTableModel(new String[]{"App ID", "Doctor", "Date", "Time", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        appointmentTable = new JTable(tableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Active Appointments"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // bottom control area
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Action Panel"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("New Date:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        dateComboBox = new JComboBox<>();
        controlPanel.add(dateComboBox, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        controlPanel.add(new JLabel("New Time:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        timeComboBox = new JComboBox<>();
        controlPanel.add(timeComboBox, gbc);

        gbc.gridx = 4; gbc.gridy = 0;
        rescheduleButton = new JButton("Reschedule");
        rescheduleButton.setEnabled(false);
        controlPanel.add(rescheduleButton, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 5;
        cancelAppButton = new JButton("Cancel Selected Appointment");
        cancelAppButton.setForeground(Color.RED);
        cancelAppButton.setEnabled(false);
        JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancelPanel.add(cancelAppButton);
        controlPanel.add(cancelPanel, gbc);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void loadDateTimes() {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 15; i++) {
            dateComboBox.addItem(today.plusDays(i).toString());
        }

        for (int h = 9; h <= 18; h++) {
            timeComboBox.addItem(String.format("%02d:00", h));
            timeComboBox.addItem(String.format("%02d:30", h));
        }
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            appointmentList = session.createQuery(
                            "select a from Appointment a join fetch a.doctor where a.patient.id = :pId and a.status in (:s1, :s2)", Appointment.class)
                    .setParameter("pId", currentPatient.getId())
                    .setParameter("s1", StatusType.SCHEDULED)
                    .setParameter("s2", StatusType.RESCHEDULED)
                    .list();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            for (Appointment app : appointmentList) {
                tableModel.addRow(new Object[]{
                        app.getAppointmentId(),
                        app.getDoctor().getName() + " " + app.getDoctor().getSurname(),
                        app.getDateTime().format(dateFormatter),
                        app.getDateTime().format(timeFormatter),
                        app.getStatus().toString()
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching appointments: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initListeners() {
        appointmentTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = appointmentTable.getSelectedRow() >= 0;
            rescheduleButton.setEnabled(hasSelection);
            cancelAppButton.setEnabled(hasSelection);
        });

        // canceling
        cancelAppButton.addActionListener(e -> {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow < 0) return;

            Appointment selectedApp = appointmentList.get(selectedRow);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to cancel appointment: " + selectedApp.getAppointmentId() + "?",
                    "Confirm Cancellation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction tx = session.beginTransaction();
                    Appointment dbApp = session.get(Appointment.class, selectedApp.getId());
                    dbApp.cancelAppointment(); // Entity icindeki metot
                    session.merge(dbApp);
                    tx.commit();

                    JOptionPane.showMessageDialog(this, "Appointment canceled successfully.");
                    loadAppointments();
                    rescheduleButton.setEnabled(false);
                    cancelAppButton.setEnabled(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error canceling appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // insertin new date time
        rescheduleButton.addActionListener(e -> {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow < 0) return;

            Appointment selectedApp = appointmentList.get(selectedRow);

            String dateStr = (String) dateComboBox.getSelectedItem();
            String timeStr = (String) timeComboBox.getSelectedItem();
            String[] timeParts = timeStr.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int min = Integer.parseInt(timeParts[1]);
            LocalDateTime newDateTime = LocalDate.parse(dateStr).atTime(hour, min);

            if (newDateTime.isBefore(LocalDateTime.now())) {
                JOptionPane.showMessageDialog(this, "You cannot select a past date and time.", "Invalid Time", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Doctor dbDoctor = session.createQuery(
                                "select distinct d from Doctor d left join fetch d.appointments where d.id = :docId", Doctor.class)
                        .setParameter("docId", selectedApp.getDoctor().getId())
                        .uniqueResult();

                if (dbDoctor.checkSchedule(newDateTime)) {
                    Transaction tx = session.beginTransaction();
                    Appointment dbApp = session.get(Appointment.class, selectedApp.getId());

                    dbApp.setDateTime(newDateTime);
                    dbApp.updateStatus(StatusType.RESCHEDULED);

                    session.merge(dbApp);
                    tx.commit();

                    JOptionPane.showMessageDialog(this, "Appointment rescheduled to " + dateStr + " " + timeStr + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAppointments(); // Tabloyu yenile
                    rescheduleButton.setEnabled(false);
                    cancelAppButton.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Conflict! The doctor has another appointment at this time.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error rescheduling appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
