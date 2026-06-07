package gui.patient;

import entity.Appointment;
import entity.Doctor;
import entity.Patient;
import entity.Specialization;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class PatientAppointmentDashboard extends JFrame {
    private Patient currentPatient;

    private JComboBox<Specialization> specializationComboBox;
    private JTable doctorTable;
    private DefaultTableModel doctorTableModel;
    private JButton selectDoctorButton;

    private JComboBox<String> dateComboBox;
    private JComboBox<String> timeComboBox;
    private JButton checkAvailabilityButton;

    private JTextField selectedDoctorField;
    private JTextField appointmentTimeField;
    private JTextField appointmentStatusField;
    private JTextField patientNameSurnameField;

    private JTextField feeField;
    private JTextField vatField;
    private JTextField totalField;

    private JButton confirmButton;
    private JButton updateButton;
    private JButton cancelButton;

    private List<Doctor> currentDoctorsList;
    private Doctor selectedDoctor;
    private LocalDateTime selectedDateTime;

    public PatientAppointmentDashboard(Patient loggedInPatient) {
        this.currentPatient = loggedInPatient;
        setTitle("Appointment System - Create an Appointment");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadInitialData();
        initListener();

        if (specializationComboBox.getItemCount() > 0) {
            updateDoctorTable((Specialization) specializationComboBox.getSelectedItem());
        }
    }

    public void loadInitialData() {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 15; i++) {
            dateComboBox.addItem(today.plusDays(i).toString());
        }

        for (int h = 9; h <= 18; h++) {
            timeComboBox.addItem(String.format("%02d:00", h));
            timeComboBox.addItem(String.format("%02d:30", h));
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Specialization> specializationList = session.createQuery("from Specialization", Specialization.class).list();
            for (Specialization specialization : specializationList) {
                specializationComboBox.addItem(specialization);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching specialization data: " + e.getMessage());
        }
    }

    public void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel doctorSelectionPanel = new JPanel(new BorderLayout(5, 5));
        doctorSelectionPanel.setBorder(BorderFactory.createTitledBorder("Doctor Selection"));

        JPanel specPanel = new JPanel(new BorderLayout());
        specPanel.add(new JLabel("Select a specialization: "), BorderLayout.WEST);

        specializationComboBox = new JComboBox<>();
        specializationComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Specialization) {
                    setText(((Specialization) value).getTitle());
                }
                return this;
            }
        });
        specPanel.add(specializationComboBox, BorderLayout.CENTER);

        doctorTableModel = new DefaultTableModel(new String[]{"Doctor Name", "Fee"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        doctorTable = new JTable(doctorTableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(doctorTable);

        selectDoctorButton = new JButton("Select Doctor");
        JPanel selectButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectButtonPanel.add(selectDoctorButton);

        doctorSelectionPanel.add(specPanel, BorderLayout.NORTH);
        doctorSelectionPanel.add(tableScrollPane, BorderLayout.CENTER);
        doctorSelectionPanel.add(selectButtonPanel, BorderLayout.SOUTH);

        JPanel dateTimePanel = new JPanel(new GridBagLayout());
        dateTimePanel.setBorder(BorderFactory.createTitledBorder("Date and Time"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        dateTimePanel.add(new JLabel("Appointment Date:"), gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        dateComboBox = new JComboBox<>();
        dateTimePanel.add(dateComboBox, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        dateTimePanel.add(new JLabel("Appointment Time:"), gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        timeComboBox = new JComboBox<>();
        dateTimePanel.add(timeComboBox, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        checkAvailabilityButton = new JButton("Check Availability");
        JPanel checkBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        checkBtnPanel.add(checkAvailabilityButton);
        dateTimePanel.add(checkBtnPanel, gridBagConstraints);

        topPanel.add(doctorSelectionPanel);
        topPanel.add(dateTimePanel);

        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        middlePanel.setBorder(BorderFactory.createTitledBorder("Confirm and Invoice Details"));

        JPanel summaryPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary of Appointment"));

        selectedDoctorField = new JTextField();
        selectedDoctorField.setEditable(false);
        appointmentTimeField = new JTextField();
        appointmentTimeField.setEditable(false);
        appointmentStatusField = new JTextField("Draft");
        appointmentStatusField.setEditable(false);
        patientNameSurnameField = new JTextField(currentPatient.getFirstName() + " " + currentPatient.getLastName());
        patientNameSurnameField.setEditable(false);

        summaryPanel.add(new JLabel("Selected Doctor:"));
        summaryPanel.add(selectedDoctorField);
        summaryPanel.add(new JLabel("Appointment Time:"));
        summaryPanel.add(appointmentTimeField);
        summaryPanel.add(new JLabel("Appointment Status:"));
        summaryPanel.add(appointmentStatusField);
        summaryPanel.add(new JLabel("Patient:"));
        summaryPanel.add(patientNameSurnameField);

        JPanel invoicePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        invoicePanel.setBorder(BorderFactory.createTitledBorder("Invoice Details"));

        feeField = new JTextField();
        feeField.setEditable(false);
        vatField = new JTextField();
        vatField.setEditable(false);
        totalField = new JTextField();
        totalField.setEditable(false);

        invoicePanel.add(new JLabel("Total Fee:"));
        invoicePanel.add(feeField);
        invoicePanel.add(new JLabel("VAT (20%):"));
        invoicePanel.add(vatField);
        invoicePanel.add(new JLabel("Total:"));
        invoicePanel.add(totalField);

        middlePanel.add(summaryPanel);
        middlePanel.add(invoicePanel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        confirmButton = new JButton("Confirm");
        updateButton = new JButton("Update");
        cancelButton = new JButton("Cancel");

        confirmButton.setEnabled(false);
        updateButton.setEnabled(false);

        actionPanel.add(confirmButton);
        actionPanel.add(updateButton);
        actionPanel.add(cancelButton);

        bottomPanel.add(actionPanel, BorderLayout.CENTER);

        mainContentPanel.add(topPanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(middlePanel);
        mainContentPanel.add(Box.createVerticalStrut(10));
        mainContentPanel.add(bottomPanel);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void updateDoctorTable(Specialization selectedSpec) {
        doctorTableModel.setRowCount(0);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            currentDoctorsList = session.createQuery(
                            "select d from Doctor d where d.specialization.id = :specId", Doctor.class)
                    .setParameter("specId", selectedSpec.getId())
                    .list();

            for (Doctor doc : currentDoctorsList) {
                doctorTableModel.addRow(new Object[]{"Dr. " + doc.getName() + " " + doc.getSurname(), doc.getConsultationFee() + " $"});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initListener() {
        specializationComboBox.addActionListener(e -> {
            Specialization selectedSpec = (Specialization) specializationComboBox.getSelectedItem();
            if (selectedSpec != null) {
                updateDoctorTable(selectedSpec);
                selectedDoctor = null;
                confirmButton.setEnabled(false);
                appointmentTimeField.setText("");
                appointmentStatusField.setText("Draft");
            }
        });

        selectDoctorButton.addActionListener(e -> {
            int selectedRow = doctorTable.getSelectedRow();
            if (selectedRow >= 0 && currentDoctorsList != null) {
                selectedDoctor = currentDoctorsList.get(selectedRow);

                selectedDoctorField.setText("Dr. " + selectedDoctor.getName() + " " + selectedDoctor.getSurname());
                feeField.setText(selectedDoctor.getConsultationFee() + " $");

                double fee = selectedDoctor.getConsultationFee();
                double vat = fee * 0.20;
                vatField.setText(String.format(Locale.US, "%.2f", vat));
                totalField.setText(String.format(Locale.US, "%.2f", fee + vat));

                confirmButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a Doctor from the table.");
            }
        });

        checkAvailabilityButton.addActionListener(e -> {
            if (selectedDoctor == null) {
                JOptionPane.showMessageDialog(this, "Please select a Doctor first.");
                return;
            }

            String dateStr = (String) dateComboBox.getSelectedItem();
            String timeStr = (String) timeComboBox.getSelectedItem();

            String[] timeParts = timeStr.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int min = Integer.parseInt(timeParts[1]);
            selectedDateTime = LocalDate.parse(dateStr).atTime(hour, min);

            if (selectedDateTime.isBefore(LocalDateTime.now())) {
                JOptionPane.showMessageDialog(this, "You cannot select a past date and time.", "Invalid Time", JOptionPane.WARNING_MESSAGE);
                appointmentTimeField.setText("");
                appointmentStatusField.setText("Draft (Invalid Time)");
                confirmButton.setEnabled(false);
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Doctor dbDoctor = session.createQuery(
                                "select distinct d from Doctor d left join fetch d.appointments where d.id = :docId", Doctor.class)
                        .setParameter("docId", selectedDoctor.getId())
                        .uniqueResult();

                if (dbDoctor.checkSchedule(selectedDateTime)) {
                    appointmentTimeField.setText(dateStr + " " + timeStr);
                    appointmentStatusField.setText("Draft (Available)");
                    confirmButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Conflict! The doctor has another appointment at this time.", "Warning", JOptionPane.WARNING_MESSAGE);
                    appointmentTimeField.setText("");
                    appointmentStatusField.setText("Draft (Conflict)");
                    confirmButton.setEnabled(false);
                }
            }
        });

        confirmButton.addActionListener(e -> {
            if (selectedDoctor == null || selectedDateTime == null) {
                JOptionPane.showMessageDialog(this, "Please select a Doctor and check availability.");
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();

                Patient dbPatient = session.get(Patient.class, currentPatient.getId());
                Doctor dbDoctor = session.get(Doctor.class, selectedDoctor.getId());

                String generatedAppId = "APP-" + System.currentTimeMillis();
                dbPatient.scheduleAppointment(dbDoctor, generatedAppId, selectedDateTime);

                Appointment lastAppointmentAdded = dbPatient.getAppointments().get(dbPatient.getAppointments().size() - 1);
                lastAppointmentAdded.generateInvoice();

                session.persist(lastAppointmentAdded);
                session.merge(dbPatient);
                session.merge(dbDoctor);

                transaction.commit();

                appointmentStatusField.setText(lastAppointmentAdded.getStatus().toString());

                JOptionPane.showMessageDialog(this,
                        "Appointment Confirmed!\nInvoice ID: " + lastAppointmentAdded.getInvoice().getInvoiceId(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                confirmButton.setEnabled(false);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }
}