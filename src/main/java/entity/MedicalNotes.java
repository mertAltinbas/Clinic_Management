package entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "medical_notes")
public class MedicalNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ElementCollection
    @CollectionTable(name = "medical_notes_diagnosis", joinColumns = @JoinColumn(name = "medical_notes_id"))
    @Column(name = "diagnosis")
    private Set<String> diagnosis = new HashSet<String>();

    @Column(name = "treatment", nullable = false)
    private String treatment;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    // associations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(mappedBy = "medicalNotes", cascade = CascadeType.ALL, orphanRemoval = true)
    private SickNote sickNote;

    @OneToMany(mappedBy = "medicalNotes", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationOrder> medicationOrder = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    public MedicalNotes() {
    }

    public MedicalNotes(Set<String> diagnosis, String treatment, LocalDate creationDate, Patient patient, Appointment appointment) {
        if (diagnosis != null) {
            this.diagnosis.addAll(diagnosis);
        }
        setTreatment(treatment);
        setCreationDate(creationDate);
        setPatient(patient);
        setAppointment(appointment);
    }

    public Long getId() {
        return id;
    }

    public Set<String> getDiagnosis() {
        return Collections.unmodifiableSet(diagnosis);
    }

    public String getTreatment() {
        return treatment;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void addDiagnosis(String diagnosis) {
        if (diagnosis != null && !diagnosis.trim().isEmpty()) {
            this.diagnosis.add(diagnosis);
        }
    }

    public void removeDiagnosis(String diagnosis) {
        if (diagnosis != null && !diagnosis.trim().isEmpty()) {
            this.diagnosis.remove(diagnosis);
        }
    }

    public void setTreatment(String treatment) {
        Objects.requireNonNull(treatment, "Set treatment is null");
        this.treatment = treatment;
    }

    public void setCreationDate(LocalDate creationDate) {
        Objects.requireNonNull(creationDate, "Set creation Date is null");
        this.creationDate = creationDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        if (this.patient == patient) return;
        this.patient = patient;
    }

    public SickNote getSickNote() {
        return sickNote;
    }

    public void setSickNote(SickNote sickNote) {
        if (this.sickNote == sickNote) return;
        this.sickNote = sickNote;
        if (sickNote != null) sickNote.setMedicalNotes(this);
    }

    public List<MedicationOrder> getMedicationOrders() {
        return Collections.unmodifiableList(medicationOrder);
    }

    public void addMedicationOrder(MedicationOrder medicationOrder) {
        Objects.requireNonNull(medicationOrder, "medicationOrder cannot be null");
        if (!this.medicationOrder.contains(medicationOrder)) {
            this.medicationOrder.add(medicationOrder);
            medicationOrder.setMedicalNotes(this);
        }
    }

    public void removeMedicationOrder(MedicationOrder medicationOrder) {
        Objects.requireNonNull(medicationOrder, "Appointment cannot be null");
        if (this.medicationOrder.contains(medicationOrder)) {
            this.medicationOrder.remove(medicationOrder);
            medicationOrder.setMedicalNotes(null);
        }
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        if (this.appointment == appointment) return;
        this.appointment = appointment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicalNotes)) return false;
        MedicalNotes that = (MedicalNotes) o;
        return Objects.equals(treatment, that.treatment)
                && Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(treatment, creationDate);
    }
}
