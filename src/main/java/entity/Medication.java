package entity;

import entity.enums.MedicationForm;
import entity.enums.MedicationType;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "medication")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "medication_active_ingredient", joinColumns = @JoinColumn(name = "medication_id"))
    @Column(name = "active_ingredient", nullable = false)
    private Set<String> activeIngredient = new HashSet<>();

    @Column(name = "medication_form", nullable = false)
    @Enumerated(EnumType.STRING)
    private MedicationForm medicationForm;

    @Column(name = "dose", nullable = false)
    private String dose;

    @ElementCollection(targetClass = MedicationType.class)
    @CollectionTable(name = "medication_type_mapping", joinColumns = @JoinColumn(name = "medication_id"))
    @Column(name = "medication_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<MedicationType> medicationTypes = new HashSet<>();

    @Column(name = "administration_route")
    private String administrationRoute;

    @Column(name = "storage_conditions")
    private String storageConditions;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "duration_day")
    private Integer durationDay;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "medication_medical_notes",
            joinColumns = @JoinColumn(name = "medication_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_notes_id")
    )
    private List<MedicalNotes> medicalNotes = new ArrayList<MedicalNotes>();

    public Medication() {
    }

    public Medication(String name, Set<String> activeIngredient, MedicationForm medicationForm, String dose, String storageConditions, String administrationRoute) {
        setName(name);
        if (activeIngredient != null) {
            this.activeIngredient.addAll(activeIngredient);
        }
        setMedicationForm(medicationForm);
        setDose(dose);
        setAdministrationRoute(administrationRoute);
        setStorageConditions(storageConditions);
        this.medicationTypes.add(MedicationType.CLINICAL);
    }

    public Medication(String name, Set<String> activeIngredient, MedicationForm medicationForm, String dose, String frequency, Integer durationDay) {
        setName(name);
        if (activeIngredient != null) {
            this.activeIngredient.addAll(activeIngredient);
        }
        setMedicationForm(medicationForm);
        setDose(dose);
        setFrequency(frequency);
        setDurationDay(durationDay);
        this.medicationTypes.add(MedicationType.PRESCRIBED);
    }

    public Medication(String name, Set<String> activeIngredient, MedicationForm medicationForm, String dose, String administrationRoute, String storageConditions, String frequency, Integer durationDay) {
        setName(name);
        if (activeIngredient != null) {
            this.activeIngredient.addAll(activeIngredient);
        }
        setMedicationForm(medicationForm);
        setDose(dose);
        setAdministrationRoute(administrationRoute);
        setStorageConditions(storageConditions);
        setFrequency(frequency);
        setDurationDay(durationDay);
        this.medicationTypes.add(MedicationType.CLINICAL);
        this.medicationTypes.add(MedicationType.PRESCRIBED);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getActiveIngredient() {
        return Collections.unmodifiableSet(activeIngredient);
    }

    public MedicationForm getMedicationForm() {
        return medicationForm;
    }

    public String getDose() {
        return dose;
    }

    public Set<MedicationType> getMedicationTypes() {
        return Collections.unmodifiableSet(medicationTypes);
    }

    public String getAdministrationRoute() {
        return administrationRoute;
    }

    public String getStorageConditions() {
        return storageConditions;
    }

    public String getFrequency() {
        return frequency;
    }

    public Integer getDurationDay() {
        return durationDay;
    }

    public void setName(String name) {
        Objects.requireNonNull(name, "Setter Name cannot be null");
        this.name = name;
    }

    public void addActiveIngredient(String activeIngredient) {
        if (activeIngredient != null && !activeIngredient.trim().isEmpty()) {
            this.activeIngredient.add(activeIngredient);
        }
    }

    public void removeActiveIngredient(String activeIngredient) {
        if (activeIngredient != null && !activeIngredient.trim().isEmpty()) {
            this.activeIngredient.remove(activeIngredient);
        }
    }

    public void setMedicationForm(MedicationForm medicationForm) {
        Objects.requireNonNull(medicationForm, "Setter medicationForm must not be null");
        this.medicationForm = medicationForm;
    }

    public void setDose(String dose) {
        Objects.requireNonNull(dose, "Setter dose must not be null");
        this.dose = dose;
    }

    public void addMedicationType(MedicationType type) {
        if (type != null) {
            this.medicationTypes.add(type);
        }
    }

    public void setAdministrationRoute(String administrationRoute) {
        if (!this.medicationTypes.contains(MedicationType.CLINICAL)) {
            throw new IllegalStateException("This is not a CLINICAL medication");
        }
        this.administrationRoute = administrationRoute;
    }

    public void setStorageConditions(String storageConditions) {
        if (!this.medicationTypes.contains(MedicationType.CLINICAL)) {
            throw new IllegalStateException("This is not a CLINICAL medication");
        }
        this.storageConditions = storageConditions;
    }

    public void setFrequency(String frequency) {
        if (!this.medicationTypes.contains(MedicationType.PRESCRIBED)) {
            throw new IllegalStateException("This is not a PRESCRIBED medication");
        }
        this.frequency = frequency;
    }

    public void setDurationDay(Integer durationDay) {
        if (!this.medicationTypes.contains(MedicationType.PRESCRIBED)) {
            throw new IllegalStateException("This is not a PRESCRIBED medication");
        }
        this.durationDay = durationDay;
    }

    public List<MedicalNotes> getMedicalNotes() {
        return Collections.unmodifiableList(medicalNotes);
    }

    public void addMedicalNotes(MedicalNotes medicalNotes) {
        Objects.requireNonNull(medicalNotes, "Add medication cannot be null");
        if (!this.medicalNotes.contains(medicalNotes)) {
            this.medicalNotes.add(medicalNotes);
            medicalNotes.addMedication(this);
        }
    }

    public void removeMedicalNotes(MedicalNotes medicalNotes) {
        Objects.requireNonNull(medicalNotes, "remove medication cannot be null");
        if (this.medicalNotes.contains(medicalNotes)) {
            this.medicalNotes.remove(medicalNotes);
            medicalNotes.removeMedication(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medication)) return false;
        Medication medication = (Medication) o;
        return Objects.equals(name, medication.name)
                && Objects.equals(medicationForm, medication.medicationForm)
                && Objects.equals(dose, medication.dose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, medicationForm, dose);
    }
}
