package entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "doctor")
public class Doctor extends Employee {
    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(name = "consultation_fee", nullable = false)
    private float consultationFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    public Doctor() {
    }

    public Doctor(String employeeCode, String name, String middleName, String surname, Set<String> phoneNumber, BigDecimal salary, String shift, String licenseNumber, float consultationFee, Specialization specialization) {
        super(employeeCode, name, middleName, surname, phoneNumber, salary, shift);
        setLicenseNumber(licenseNumber);
        setConsultationFee(consultationFee);
        setSpecialization(specialization);
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public float getConsultationFee() {
        return consultationFee;
    }

    public void setLicenseNumber(String licenseNumber) {
        Objects.requireNonNull(licenseNumber, "Setter licenseNumber cannot be null");
        this.licenseNumber = licenseNumber;
    }

    public void setConsultationFee(float consultationFee) {
        this.consultationFee = consultationFee;
    }

    @Override
    public BigDecimal calculateBonus() {
        /* TODO */
        return new BigDecimal("1");
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        if (this.specialization == specialization) return;
        this.specialization = specialization;
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public void addAppointment(Appointment appointment) {
        Objects.requireNonNull(appointment, "Appointment cannot be null");
        if (!this.appointments.contains(appointment)) {
            this.appointments.add(appointment);
            appointment.setDoctor(this);
        }
    }

    public void removeAppointment(Appointment appointment) {
        Objects.requireNonNull(appointment, "Appointment cannot be null");
        if (this.appointments.contains(appointment)) {
            this.appointments.remove(appointment);
            appointment.setDoctor(null);
        }
    }
}
