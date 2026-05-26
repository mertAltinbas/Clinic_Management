package entity;

import entity.enums.BloodType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
}
