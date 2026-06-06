package gui;

import entity.*;
import entity.enums.ColorCode;
import entity.enums.MedicationForm;
import entity.enums.StatusType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class DoctorDashboard extends JFrame {
    // left panel components
    private JList<Appointment> appointmentJList;
    private DefaultListModel<Appointment> appointmentListModel;

    // right top patient components
    private JLabel patientNameLabel;
    private JLabel patientAgeLabel;
    private JLabel patientBloodTypeLabel;
    private JTextArea pastMedicalNotesTextArea;

    // diagnosis and treatment
    private JTextArea diagnosisTextArea;
    private JTextArea treatmentTextArea;

    // buttons
    private JButton addMedicationButton;
    private JButton issueSickNoteButton;
    private JButton scheduleFollowUpButton;
    private JButton noShowButton;
    private JButton saveCompleteButton;

    private List<MedicationOrder> pendingMedicationOrders = new ArrayList<>();
    private List<Medication> pendingManualMedications = new ArrayList<>();

    public DoctorDashboard() {
        setTitle("Doctor Dashboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
        loadAppointments();
        initListener();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // left panel
        appointmentListModel = new DefaultListModel<>();
        appointmentJList = new JList<>(appointmentListModel);
        JScrollPane scrollAppointmentPane = new JScrollPane(appointmentJList);
        scrollAppointmentPane.setPreferredSize(new Dimension(250, 600));

        add(scrollAppointmentPane, BorderLayout.WEST);

        // right panel
        JPanel rightPanel = new JPanel(new BorderLayout());

        // right panel top
        JPanel topInfoPanel = new JPanel(new BorderLayout());
        JPanel profileDataPanel = new JPanel(new GridLayout(2, 2, 5, 10));
        profileDataPanel.setBorder(BorderFactory.createTitledBorder("Profile Data"));

        patientNameLabel = new JLabel("Patient Name:");
        patientAgeLabel = new JLabel("Patient Age:");
        patientBloodTypeLabel = new JLabel("Patient Blood Type:");

        profileDataPanel.add(patientNameLabel);
        profileDataPanel.add(patientAgeLabel);
        profileDataPanel.add(patientBloodTypeLabel);

        pastMedicalNotesTextArea = new JTextArea(8, 40);
        pastMedicalNotesTextArea.setEditable(false);

        JScrollPane medicalNotesScrollPane = new JScrollPane(pastMedicalNotesTextArea);
        medicalNotesScrollPane.setBorder(BorderFactory.createTitledBorder("Past Medical Notes & Medications"));

        topInfoPanel.add(profileDataPanel, BorderLayout.NORTH);
        topInfoPanel.add(medicalNotesScrollPane, BorderLayout.CENTER);
        rightPanel.add(topInfoPanel, BorderLayout.NORTH);

        // right panel bottom
        JPanel currentConsultationPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        currentConsultationPanel.setBorder(BorderFactory.createTitledBorder("Current Consultation"));

        diagnosisTextArea = new JTextArea(3, 40);
        treatmentTextArea = new JTextArea(3, 40);

        JScrollPane scrollDiagnosis = new JScrollPane(diagnosisTextArea);
        scrollDiagnosis.setBorder(BorderFactory.createTitledBorder("Diagnosis *"));
        JScrollPane scrollTreatment = new JScrollPane(treatmentTextArea);
        scrollTreatment.setBorder(BorderFactory.createTitledBorder("Treatment *"));

        currentConsultationPanel.add(scrollDiagnosis);
        currentConsultationPanel.add(scrollTreatment);
        rightPanel.add(currentConsultationPanel, BorderLayout.CENTER);

        // bottom buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addMedicationButton = new JButton("+ Add Medication");
        issueSickNoteButton = new JButton("+ Issue Sick Note");
        scheduleFollowUpButton = new JButton("+ Schedule Follow Up");
        noShowButton = new JButton("+ No Show");
        saveCompleteButton = new JButton("+ Save Complete");

        buttonPanel.add(addMedicationButton);
        buttonPanel.add(issueSickNoteButton);
        buttonPanel.add(scheduleFollowUpButton);
        buttonPanel.add(Box.createHorizontalStrut(150));
        buttonPanel.add(noShowButton);
        buttonPanel.add(saveCompleteButton);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);
    }

    private void loadAppointments() {
        appointmentListModel.clear();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<StatusType> appointmentStatus = Arrays.asList(StatusType.SCHEDULED, StatusType.RESCHEDULED);
            List<Appointment> appointmentList = session.createQuery("select a from Appointment a join fetch a.patient where a.status IN :status", Appointment.class)
                    .setParameter("status", appointmentStatus)
                    .list();

            for (Appointment appointment : appointmentList) {
                appointmentListModel.addElement(appointment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Appointments could not be loaded: " + e.getMessage());
        }
    }

    private void initListener() {
        // association appointment listener
        appointmentJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Appointment selectedApp = appointmentJList.getSelectedValue();
                if (selectedApp != null) {
                    pendingMedicationOrders.clear();
                    pendingManualMedications.clear();
                    try (Session session = HibernateUtil.getSessionFactory().openSession()) {

                        Appointment activeApp = (Appointment) session.merge(selectedApp);
                        Patient patient = activeApp.getPatient();

                        patientNameLabel.setText("Patient Name: " + patient.getFirstName() + " " + patient.getLastName());
                        patientAgeLabel.setText("Patient Age: " + patient.getAge());
                        patientBloodTypeLabel.setText("Patient Blood Type: " + patient.getBloodType());

                        StringBuilder sb = new StringBuilder();
                        List<MedicalNotes> pastNotes = patient.getMedicalNotes();

                        if (pastNotes.isEmpty()) {
                            sb.append("No past medical notes found.");
                        } else {
                            for (MedicalNotes note : pastNotes) {
                                sb.append("Date: ").append(note.getCreationDate()).append("\n");
                                sb.append("Diagnosis: ").append(String.join(", ", note.getDiagnosis())).append("\n");
                                sb.append("Treatment: ").append(note.getTreatment()).append("\n");

                                sb.append("Medications: ");
                                List<MedicationOrder> orders = note.getMedicationOrders();
                                if (orders.isEmpty()) {
                                    sb.append("None");
                                } else {
                                    for (MedicationOrder order : orders) {
                                        sb.append(order.getMedication().getName())
                                                .append(" (").append(order.getFrequency())
                                                .append(", ").append(order.getDurationDay()).append(" days) ");
                                    }
                                }
                                sb.append("\n-----------------------------------\n");
                            }
                        }
                        pastMedicalNotesTextArea.setText(sb.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Could not load patient details: " + ex.getMessage());
                    }
                }
            }
        });

        // typical button behaviors
        noShowButton.addActionListener(e -> handleNoShow());
        saveCompleteButton.addActionListener(e -> handleSaveComplete());

        addMedicationButton.addActionListener(e -> {
            Appointment selectedApp = appointmentJList.getSelectedValue();
            if (selectedApp == null) {
                JOptionPane.showMessageDialog(this, "Please select an appointment first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            showMedicationDialog();
        });

        issueSickNoteButton.addActionListener(e -> {
            // TODO
        });

        scheduleFollowUpButton.addActionListener(e -> {
            // TODO
        });
    }

    private void handleNoShow() {
        Appointment selectedAppointment = appointmentJList.getSelectedValue();
        if (selectedAppointment == null) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to show.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Mark this patient as No-Show?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();

                // update status of object
                selectedAppointment.updateStatus(StatusType.NO_SHOW);
                session.merge(selectedAppointment);
                transaction.commit();

                // removing element form ui for refresh
                appointmentListModel.removeElement(selectedAppointment);
                clearRightPanel();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void handleSaveComplete() {
        Appointment selectedApp = appointmentJList.getSelectedValue();
        if (selectedApp == null) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.");
            return;
        }

        String diagnosis = diagnosisTextArea.getText().trim();
        String treatment = treatmentTextArea.getText().trim();

        if (diagnosis.isEmpty() || treatment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Diagnosis or Treatment fields cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Appointment activeApp = (Appointment) session.merge(selectedApp);
            Patient patient = activeApp.getPatient();

            Set<String> diagnosisSet = new HashSet<>(Arrays.asList(diagnosis.split(",")));
            MedicalNotes newNote = new MedicalNotes(diagnosisSet, treatment, LocalDate.now(), patient, activeApp);

            patient.addMedicalNote(newNote);
            activeApp.setMedicalNotes(newNote);
            activeApp.updateStatus(StatusType.COMPLETED);

            session.persist(newNote);
            session.merge(activeApp);

            for(Medication manualMed : pendingManualMedications) {
                session.persist(manualMed);
            }

            for (MedicationOrder order : pendingMedicationOrders) {
                Medication managedMed = (Medication) session.merge(order.getMedication());
                order.setMedication(managedMed);

                order.setMedicalNotes(newNote);
                newNote.addMedicationOrder(order);
                managedMed.addMedicationOrder(order);

                session.persist(order);
            }

            transaction.commit();

            JOptionPane.showMessageDialog(this, "Consultation Completed and Saved Successfully!");

            pendingMedicationOrders.clear();
            pendingManualMedications.clear();
            appointmentListModel.removeElement(selectedApp);
            clearRightPanel();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void showMedicationDialog() {
        JDialog medicationDialog = new JDialog(this, "Medication Dialog", true);
        medicationDialog.setSize(400, 300);
        medicationDialog.setLayout(new GridLayout(6, 2, 10, 10));
        medicationDialog.setLocationRelativeTo(this);

        DefaultComboBoxModel<Medication> medicationDefaultComboBoxModel = new DefaultComboBoxModel<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Medication> medications = session.createQuery("from Medication", Medication.class).list();
            for (Medication medication : medications) {
                medicationDefaultComboBoxModel.addElement(medication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JComboBox<Medication> medicationComboBox = new JComboBox<>(medicationDefaultComboBoxModel);
        JCheckBox manuelEntryCheckBox = new JCheckBox("Medication Not Found (Manuel Entry)");
        JTextField manuelMedicationNameField = new JTextField("");
        manuelEntryCheckBox.setEnabled(false);

        JTextField frequencyField = new JTextField("2x1");
        JTextField durationDayField = new JTextField("7");

        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        manuelEntryCheckBox.addActionListener(e -> {
            boolean isManuelEntry = manuelEntryCheckBox.isSelected();
            medicationComboBox.setEnabled(!isManuelEntry);
            manuelMedicationNameField.setEnabled(isManuelEntry);
        });

        medicationDialog.add(new JLabel("Select from Catalog:")); medicationDialog.add(medicationComboBox);
        medicationDialog.add(manuelEntryCheckBox); medicationDialog.add(new JLabel(""));
        medicationDialog.add(new JLabel("Manual Med Name:")); medicationDialog.add(manuelMedicationNameField);
        medicationDialog.add(new JLabel("Frequency:")); medicationDialog.add(frequencyField);
        medicationDialog.add(new JLabel("Duration (Days):")); medicationDialog.add(durationDayField);
        medicationDialog.add(confirmButton); medicationDialog.add(cancelButton);

        cancelButton.addActionListener(e -> dispose());

        confirmButton.addActionListener(e -> {
            int durationDays =  Integer.parseInt(durationDayField.getText().trim());
            Medication selectedMedication = null;

            if (manuelEntryCheckBox.isSelected()) {
                String manualName = manuelMedicationNameField.getText().trim();
                selectedMedication = new Medication(manualName, Set.of("Unknown"), MedicationForm.TABLET, "Unknown", ColorCode.WHITE, true, durationDays);
                pendingManualMedications.add(selectedMedication);
            } else {
                selectedMedication = (Medication) medicationComboBox.getSelectedItem();
            }

            MedicationOrder newOrder = new MedicationOrder(frequencyField.getText().trim(), durationDays);
            newOrder.setMedication(selectedMedication);
            pendingMedicationOrders.add(newOrder);

            JOptionPane.showMessageDialog(medicationDialog, "Medication added to the queue! It will be saved when you Complete Consultation.");
            medicationDialog.dispose();
        });
        medicationDialog.setVisible(true);
    }

    private void clearRightPanel() {
        patientNameLabel.setText("Patient Name: -");
        patientAgeLabel.setText("Patient Age: -");
        patientBloodTypeLabel.setText("Patient Blood Type: -");
        pastMedicalNotesTextArea.setText("");
        diagnosisTextArea.setText("");
        treatmentTextArea.setText("");
    }

}
