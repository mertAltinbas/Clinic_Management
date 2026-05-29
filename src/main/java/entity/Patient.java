package entity;

import entity.enums.BloodType;
import entity.enums.StatusType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @ElementCollection
    @CollectionTable(name = "patient_phone", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "phone_number")
    private Set<String> phoneNumber = new HashSet<String>();

    @Embedded
    private Address homeAddress;

    @Column(name = "blood_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalNotes> medicalNotes = new ArrayList<>();

    public Patient() {}

    public Patient(String firstName, String lastName, LocalDate dateOfBirth, Set<String> phoneNumber, Address homeAddress, BloodType bloodType) {
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        if (phoneNumber != null) {
            this.phoneNumber.addAll(phoneNumber);
        }
        setHomeAddress(homeAddress);
        setBloodType(bloodType);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Set<String> getPhoneNumber() {
        return Collections.unmodifiableSet(phoneNumber);
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public void setFirstName(String firstName) {
        Objects.requireNonNull(firstName, "Setter firstName must not be null");
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        Objects.requireNonNull(lastName, "Setter lastName must not be null");
        this.lastName = lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        Objects.requireNonNull(dateOfBirth, "Setter dateOfBirth must not be null");
        this.dateOfBirth = dateOfBirth;
    }

    public void addPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            this.phoneNumber.add(phoneNumber);
        }
    }

    public void removePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            this.phoneNumber.remove(phoneNumber);
        }
    }

    public void setHomeAddress(Address homeAddress) {
        Objects.requireNonNull(homeAddress, "Setter homeAddress must not be null");
        this.homeAddress = homeAddress;
    }

    public void setBloodType(BloodType bloodType) {
        Objects.requireNonNull(bloodType, "Setter bloodType must not be null");
        this.bloodType = bloodType;
    }

    public void scheduleAppointment(Doctor doc, LocalDateTime dateTime) {
        Objects.requireNonNull(doc, "Doctor cannot be null");
        Objects.requireNonNull(dateTime, "DateTime cannot be null");

        Appointment newAppointment = new Appointment(dateTime, StatusType.SCHEDULED, doc, this);

        this.addAppointment(newAppointment);
        doc.addAppointment(newAppointment);
    }

    public void scheduleAppointment(Doctor doc, LocalDateTime dateTime, String note) {
        Objects.requireNonNull(doc, "Doctor cannot be null");
        Objects.requireNonNull(dateTime, "DateTime cannot be null");

        Appointment newAppointment = new Appointment(dateTime, StatusType.SCHEDULED, doc, this, note);

        this.addAppointment(newAppointment);
        doc.addAppointment(newAppointment);
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public void addAppointment(Appointment appointment) {
        Objects.requireNonNull(appointment, "Appointment cannot be null");
        if (!this.appointments.contains(appointment)) {
            this.appointments.add(appointment);
            appointment.setPatient(this);
        }
    }

    public void removeAppointment(Appointment appointment) {
        Objects.requireNonNull(appointment, "Appointment cannot be null");
        if (this.appointments.contains(appointment)) {
            this.appointments.remove(appointment);
            appointment.setPatient(null);
        }
    }

    public List<MedicalNotes> getMedicalNotes() {
        return Collections.unmodifiableList(medicalNotes);
    }

    public void addMedicalNote(MedicalNotes note) {
        Objects.requireNonNull(note, "Medical note cannot be null");
        if (!this.medicalNotes.contains(note)) {
            this.medicalNotes.add(note);
            note.setPatient(this);
        }
    }

    public void removeMedicalNote(MedicalNotes note) {
        Objects.requireNonNull(note, "Medical note cannot be null");
        if (this.medicalNotes.contains(note)) {
            this.medicalNotes.remove(note);
            note.setPatient(null);
        }
    }
}
