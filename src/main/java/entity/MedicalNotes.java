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

    @ManyToMany(mappedBy = "medicalNotes")
    private List<Medication> medications = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    public MedicalNotes() {
    }

    public MedicalNotes(Set<String> diagnosis, String treatment, LocalDate creationDate, Patient patient) {
        if (diagnosis != null) {
            this.diagnosis.addAll(diagnosis);
        }
        setTreatment(treatment);
        setCreationDate(creationDate);
        setPatient(patient);
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

    public List<Medication> getMedications() {
        return Collections.unmodifiableList(medications);
    }

    public void addMedication(Medication medication) {
        Objects.requireNonNull(medication, "Add medication is null");
        if (!medications.contains(medication)) {
            this.medications.add(medication);
            medication.addMedicalNotes(this);
        }
    }

    public void removeMedication(Medication medication) {
        Objects.requireNonNull(medication, "Remove medication is null");
        if (medications.contains(medication)) {
            this.medications.remove(medication);
            medication.removeMedicalNotes(this);
        }
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        if (this.patient == patient) return;
        this.patient = patient;
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
