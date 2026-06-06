package entity;

import entity.enums.ColorCode;
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

    @Column(name = "prescription_category")
    @Enumerated(EnumType.STRING)
    private ColorCode prescriptionCategory;

    @Column(name = "is_otc")
    private boolean isOtc;

    @Column(name = "max_dispense_quantity")
    private int maxDispenseQuantity;

    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationOrder> medicationOrder = new ArrayList<>();

    public Medication() {
    }

    public Medication(String name, Set<String> activeIngredient, MedicationForm medicationForm, String dose, String storageConditions, String administrationRoute) {
        setName(name);
        if (activeIngredient == null || activeIngredient.isEmpty())
            throw new IllegalArgumentException("Medication must have at least one active ingredient.");
        this.activeIngredient.addAll(activeIngredient);
        setMedicationForm(medicationForm);
        setDose(dose);
        this.medicationTypes.add(MedicationType.CLINICAL);
        setAdministrationRoute(administrationRoute);
        setStorageConditions(storageConditions);
    }

    public Medication(String name, Set<String> activeIngredient, MedicationForm medicationForm, String dose, ColorCode prescriptionCategory, boolean isOtc, int maxDispenseQuantity) {
        setName(name);
        if (activeIngredient == null || activeIngredient.isEmpty())
            throw new IllegalArgumentException("Medication must have at least one active ingredient.");
        this.activeIngredient.addAll(activeIngredient);
        setMedicationForm(medicationForm);
        setDose(dose);
        this.medicationTypes.add(MedicationType.PRESCRIBED);
        setPrescriptionCategory(prescriptionCategory);
        setOtc(isOtc);
        setMaxDispenseQuantity(maxDispenseQuantity);
    }

    public Medication(String name, Set<String> activeIngredient, MedicationForm medicationForm, String dose, String administrationRoute, String storageConditions, ColorCode prescriptionCategory, boolean isOtc, int maxDispenseQuantity) {
        setName(name);
        if (activeIngredient == null || activeIngredient.isEmpty())
            throw new IllegalArgumentException("Medication must have at least one active ingredient.");
        this.activeIngredient.addAll(activeIngredient);
        setMedicationForm(medicationForm);
        setDose(dose);
        this.medicationTypes.add(MedicationType.CLINICAL);
        this.medicationTypes.add(MedicationType.PRESCRIBED);
        setAdministrationRoute(administrationRoute);
        setStorageConditions(storageConditions);
        setPrescriptionCategory(prescriptionCategory);
        setOtc(isOtc);
        setMaxDispenseQuantity(maxDispenseQuantity);
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
        if (!this.medicationTypes.contains(MedicationType.CLINICAL)) {
            throw new IllegalStateException("This is not a CLINICAL medication");
        }
        return administrationRoute;
    }

    public String getStorageConditions() {
        if (!this.medicationTypes.contains(MedicationType.CLINICAL)) {
            throw new IllegalStateException("This is not a CLINICAL medication");
        }
        return storageConditions;
    }

    public ColorCode getPrescriptionCategory() {
        if (!this.medicationTypes.contains(MedicationType.PRESCRIBED)) {
            throw new IllegalStateException("This is not a PRESCRIBED medication");
        }
        return prescriptionCategory;
    }

    public boolean isOtc() {
        if (!this.medicationTypes.contains(MedicationType.PRESCRIBED)) {
            throw new IllegalStateException("This is not a PRESCRIBED medication");
        }
        return isOtc;
    }

    public int getMaxDispenseQuantity() {
        if (!this.medicationTypes.contains(MedicationType.PRESCRIBED)) {
            throw new IllegalStateException("This is not a PRESCRIBED medication");
        }
        return maxDispenseQuantity;
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
            if (this.activeIngredient.size() <= 1 && this.activeIngredient.contains(activeIngredient)) {
                throw new IllegalStateException("Cannot remove the last active ingredient. Medication must have at least one.");
            }
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
        this.administrationRoute = administrationRoute;
        medicationTypes.add(MedicationType.CLINICAL);
    }

    public void setStorageConditions(String storageConditions) {
        this.storageConditions = storageConditions;
        medicationTypes.add(MedicationType.CLINICAL);
    }

    public void setOtc(boolean otc) {
        isOtc = otc;
        medicationTypes.add(MedicationType.PRESCRIBED);
    }

    public void setMaxDispenseQuantity(int maxDispenseQuantity) {
        this.maxDispenseQuantity = maxDispenseQuantity;
        medicationTypes.add(MedicationType.PRESCRIBED);
    }

    public void setPrescriptionCategory(ColorCode prescriptionCategory) {
        this.prescriptionCategory = prescriptionCategory;
        medicationTypes.add(MedicationType.PRESCRIBED);
    }

    // associations
    public List<MedicationOrder> getMedicationOrder() {
        return Collections.unmodifiableList(medicationOrder);
    }

    public void addMedicationOrder(MedicationOrder medicationOrder) {
        Objects.requireNonNull(medicationOrder, "Add medicationOrder cannot be null");
        if (!this.medicationOrder.contains(medicationOrder)) {
            this.medicationOrder.add(medicationOrder);
            medicationOrder.setMedication(this);
        }
    }

    public void removeMedicationOrder(MedicationOrder medicationOrder) {
        Objects.requireNonNull(medicationOrder, "remove medicationOrder cannot be null");
        if (this.medicationOrder.contains(medicationOrder)) {
            this.medicationOrder.remove(medicationOrder);
            medicationOrder.setMedication(null);
        }
    }

    @Override
    public String toString() {
        return getName() + " " + getDose();
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
