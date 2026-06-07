package gui;

import entity.*;
import entity.enums.ColorCode;
import entity.enums.MedicationForm;
import entity.enums.StatusType;
import gui.dialogs.FollowUpDialog;
import gui.dialogs.MedicationDialog;
import gui.dialogs.SickNoteDialog;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // temp variables for saving db
    private List<MedicationOrder> pendingMedicationOrders = new ArrayList<>();
    private List<Medication> pendingManualMedications = new ArrayList<>();
    private SickNote pendingSickNote = null;
    private LocalDateTime pendingFollowUpDate = null;

    private Doctor currentDoctor;

    public DoctorDashboard(Doctor loggedInDoctor) {
        this.currentDoctor = loggedInDoctor;
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
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

            List<StatusType> appointmentStatus = Arrays.asList(StatusType.SCHEDULED, StatusType.RESCHEDULED);

            List<Appointment> appointmentList = session.createQuery(
                            "select a from Appointment a join fetch a.patient " +
                                    "where a.status IN :status " +
                                    "and a.dateTime >= :startOfDay and a.dateTime < :endOfDay " +
                                    "and a.doctor = :currentDoc", Appointment.class)
                    .setParameter("status", appointmentStatus)
                    .setParameter("startOfDay", startOfDay)
                    .setParameter("endOfDay", endOfDay)
                    .setParameter("currentDoc", currentDoctor)
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
                    pendingSickNote = null;
                    pendingFollowUpDate = null;
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
            if (appointmentJList.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(this, "Please select an appointment first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            MedicationDialog dialog = new MedicationDialog(this);
            dialog.setVisible(true);

            MedicationDialog.MedicationResult result = dialog.getResult();
            if (result != null) {
                pendingMedicationOrders.add(result.order);
                if (result.manualMedication != null) {
                    pendingManualMedications.add(result.manualMedication);
                }
                JOptionPane.showMessageDialog(this, "Medication added to the queue!");
            }
        });

        issueSickNoteButton.addActionListener(e -> {
            if (appointmentJList.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(this, "Please select an appointment first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (pendingSickNote != null) {
                JOptionPane.showMessageDialog(this, "There is already a sick note in queue.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            SickNoteDialog dialog = new SickNoteDialog(this);
            dialog.setVisible(true);

            if (dialog.getSickNote() != null) {
                pendingSickNote = dialog.getSickNote();
                JOptionPane.showMessageDialog(this, "Sick note added to the queue!");
            }
        });

        scheduleFollowUpButton.addActionListener(e -> {
            Appointment selectedApp = appointmentJList.getSelectedValue();
            if (selectedApp == null) {
                JOptionPane.showMessageDialog(this, "Please select an appointment first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            FollowUpDialog dialog = new FollowUpDialog(this);
            dialog.setVisible(true);

            if (dialog.getFollowUpDate() != null) {
                LocalDateTime chosenDate = dialog.getFollowUpDate();

                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Doctor doc = (Doctor) session.merge(currentDoctor);

                    if (!doc.checkSchedule(chosenDate)) {
                        JOptionPane.showMessageDialog(this, "Doctor has another appointment in this date and time.", "Conflict", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    pendingFollowUpDate = chosenDate;
                    JOptionPane.showMessageDialog(this, "Follow-up appointment added to the queue!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void handleNoShow() {
        Appointment selectedAppointment = appointmentJList.getSelectedValue();
        if (selectedAppointment == null) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.", "Warning", JOptionPane.WARNING_MESSAGE);
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

            if (pendingManualMedications != null && !pendingManualMedications.isEmpty()) {
                for (Medication manualMed : pendingManualMedications) {
                    session.persist(manualMed);
                }
            }

            if (pendingMedicationOrders != null && !pendingMedicationOrders.isEmpty()) {
                for (MedicationOrder pendingOrder : pendingMedicationOrders) {
                    Medication managedMed = (Medication) session.merge(pendingOrder.getMedication());

                    newNote.addPrescription(managedMed, pendingOrder.getFrequency(), pendingOrder.getDurationDay());
                }
            }

            session.persist(newNote);
            session.merge(activeApp);

            if (pendingSickNote != null) {
                newNote.setSickNote(pendingSickNote);
                session.persist(pendingSickNote);
            }

            if (pendingFollowUpDate != null) {
                String generatedAppId = "APP-FU-" + System.currentTimeMillis();

                Appointment followUpApp = new Appointment(generatedAppId, pendingFollowUpDate, StatusType.SCHEDULED, activeApp.getDoctor(), patient, "Follow-up scheduled after consultation.");
                activeApp.getDoctor().addAppointment(followUpApp);
                patient.addAppointment(followUpApp);

                session.persist(followUpApp);
                if (pendingFollowUpDate.toLocalDate().equals(LocalDate.now())) {
                    appointmentListModel.addElement(followUpApp);
                }
            }

            transaction.commit();

            JOptionPane.showMessageDialog(this, "Consultation Completed and Saved Successfully!");

            if (pendingMedicationOrders != null) pendingMedicationOrders.clear();
            if (pendingManualMedications != null) pendingManualMedications.clear();
            pendingSickNote = null;
            pendingFollowUpDate = null;

            appointmentListModel.removeElement(selectedApp);
            clearRightPanel();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
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
