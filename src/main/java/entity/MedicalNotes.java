package entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    public MedicalNotes() {
    }

    public MedicalNotes(Set<String> diagnosis, String treatment, LocalDate creationDate) {
        if (diagnosis != null) {
            this.diagnosis.addAll(diagnosis);
        }
        setTreatment(treatment);
        setCreationDate(creationDate);
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
}
