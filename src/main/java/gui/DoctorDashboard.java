package gui;

import entity.Appointment;
import entity.MedicalNotes;
import entity.MedicationOrder;
import entity.Patient;
import entity.enums.StatusType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public DoctorDashboard() {
        setTitle("Doctor Dashboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setLocationByPlatform(true);

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
            List<Appointment> appointmentList = session.createQuery("select a from Appointment a join fetch a.patient where a.status = :status", Appointment.class)
                    .setParameter("status", StatusType.SCHEDULED)
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
                    // LazyInitializationException almamak için yeni bir session açıp nesnemizi aktifleştiriyoruz
                    try (Session session = HibernateUtil.getSessionFactory().openSession()) {

                        Appointment activeApp = (Appointment) session.merge(selectedApp);
                        Patient patient = activeApp.getPatient(); // SQL sorgusu yok, ilişkiyle gidiyoruz

                        patientNameLabel.setText("Name: " + patient.getFirstName() + " " + patient.getLastName());
                        patientAgeLabel.setText("Age: " + patient.getAge());
                        patientBloodTypeLabel.setText("Blood Type: " + patient.getBloodType());

                        StringBuilder sb = new StringBuilder();
                        List<entity.MedicalNotes> pastNotes = patient.getMedicalNotes();

                        if (pastNotes.isEmpty()) {
                            sb.append("No past medical notes found.");
                        } else {
                            for (entity.MedicalNotes note : pastNotes) {
                                sb.append("Date: ").append(note.getCreationDate()).append("\n");
                                sb.append("Diagnosis: ").append(String.join(", ", note.getDiagnosis())).append("\n");
                                sb.append("Treatment: ").append(note.getTreatment()).append("\n");

                                sb.append("Medications: ");
                                List<entity.MedicationOrder> orders = note.getMedicationOrders();
                                if (orders.isEmpty()) {
                                    sb.append("None");
                                } else {
                                    for (entity.MedicationOrder order : orders) {
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
            // TODO
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
            JOptionPane.showMessageDialog(this, "Diagnosis and Treatment fields cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Patient patient = selectedApp.getPatient();

            Set<String> diagnosisSet = new HashSet<>(Arrays.asList(diagnosis.split(",")));
            MedicalNotes newNote = new MedicalNotes(diagnosisSet, treatment, LocalDate.now(), patient, selectedApp);

            patient.addMedicalNote(newNote);
            selectedApp.setMedicalNotes(newNote);
            selectedApp.updateStatus(StatusType.COMPLETED);

            session.persist(newNote);
            session.merge(selectedApp);

            transaction.commit();

            JOptionPane.showMessageDialog(this, "Saved Successfully");
            appointmentListModel.removeElement(selectedApp);
            clearRightPanel();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void clearRightPanel() {
        patientNameLabel.setText("Name: -");
        patientAgeLabel.setText("Age: -");
        patientBloodTypeLabel.setText("Blood Type: -");
        pastMedicalNotesTextArea.setText("");
        diagnosisTextArea.setText("");
        treatmentTextArea.setText("");
    }

}
