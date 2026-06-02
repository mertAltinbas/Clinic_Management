package entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "medication_order")
public class MedicationOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "frequency", nullable = false)
    private String frequency;

    @Column(name = "duration_day", nullable = false)
    private int durationDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_notes_id", nullable = false)
    private MedicalNotes medicalNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    public MedicationOrder() {}

    public MedicationOrder(String frequency, int durationDay) {
        setFrequency(frequency);
        setDurationDay(durationDay);
    }

    public Long getId() {
        return id;
    }

    public String getFrequency() {
        return frequency;
    }

    public int getDurationDay() {
        return durationDay;
    }

    public void setFrequency(String frequency) {
        Objects.requireNonNull(frequency, "Setter frequency is null");
        this.frequency = frequency;
    }

    public void setDurationDay(int durationDay) {
        this.durationDay = durationDay;
    }

    public MedicalNotes getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(MedicalNotes medicalNotes) {
        if (this.medicalNotes == medicalNotes) return;
        this.medicalNotes = medicalNotes;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        if (this.medication == medication) return;
        this.medication = medication;
    }
}
