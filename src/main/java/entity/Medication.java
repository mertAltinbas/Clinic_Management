package entity;

import entity.enums.MedicationForm;
import entity.enums.MedicationType;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @Column(name = "medication_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MedicationType medicationType;
    @Column(name = "administration_route")
    private String administrationRoute;
    @Column(name = "storage_conditions")
    private String storageConditions;
    @Column(name = "frequency")
    private String frequency;
    @Column(name = "duration_day")
    private Integer durationDay;

    public Medication() {
    }

    public Medication(String name, Set<String> activeIngredient, MedicationForm medicationForm, String dose, MedicationType medicationType, String storageConditions, String administrationRoute) {
        setName(name);
        if (activeIngredient != null) {
            this.activeIngredient.addAll(activeIngredient);
        }
        setMedicationForm(medicationForm);
        setDose(dose);
        setMedicationType(medicationType);
        setAdministrationRoute(administrationRoute);
        setStorageConditions(storageConditions);
    }

    public Medication(String name, Set<String> activeIngredient, MedicationForm medicationForm, String dose, MedicationType medicationType, String frequency, Integer durationDay) {
        setName(name);
        if (activeIngredient != null) {
            this.activeIngredient.addAll(activeIngredient);
        }
        setMedicationForm(medicationForm);
        setDose(dose);
        setMedicationType(medicationType);
        setFrequency(frequency);
        setDurationDay(durationDay);
    }

    public Medication(String name, Set<String> activeIngredient, MedicationForm medicationForm, String dose, MedicationType medicationType, String administrationRoute, String storageConditions, String frequency, Integer durationDay) {
        setName(name);
        if (activeIngredient != null) {
            this.activeIngredient.addAll(activeIngredient);
        }
        setMedicationForm(medicationForm);
        setDose(dose);
        setMedicationType(medicationType);
        setAdministrationRoute(administrationRoute);
        setStorageConditions(storageConditions);
        setFrequency(frequency);
        setDurationDay(durationDay);
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

    public MedicationType getMedicationType() {
        return medicationType;
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

    public void setMedicationType(MedicationType medicationType) {
        Objects.requireNonNull(medicationType, "Setter medicationType must not be null");
        this.medicationType = medicationType;
    }

    public void setAdministrationRoute(String administrationRoute) {
        this.administrationRoute = administrationRoute;
    }

    public void setStorageConditions(String storageConditions) {
        this.storageConditions = storageConditions;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setDurationDay(Integer durationDay) {
        this.durationDay = durationDay;
    }
}
