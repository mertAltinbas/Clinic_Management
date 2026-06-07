package entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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
        BigDecimal totalBonus = BigDecimal.ZERO;
        BigDecimal fee = BigDecimal.valueOf(consultationFee);
        BigDecimal bonusRate = new BigDecimal("0.10");

        for (Appointment app : appointments) {
            if (app.getStatus() == entity.enums.StatusType.COMPLETED) {
                totalBonus = totalBonus.add(fee.multiply(bonusRate));
            }
        }
        return getSalary().add(totalBonus);
    }

    public void scheduleFollowUp(Patient patient, LocalDateTime dateTime, String note) {
        String uniqueAppointmentId = "followup-" + UUID.randomUUID().toString();
        patient.scheduleAppointment(this, uniqueAppointmentId, dateTime, note);
    }

    public boolean checkSchedule(LocalDateTime newStartTime) {
        if (newStartTime == null) return false;

        LocalDateTime newEndTime = newStartTime.plusMinutes(29);

        for (Appointment app : appointments) {
            if (app.getStatus() == entity.enums.StatusType.SCHEDULED || app.getStatus() == entity.enums.StatusType.RESCHEDULED) {

                LocalDateTime existingStartTime = app.getDateTime();
                LocalDateTime existingEndTime = existingStartTime.plusMinutes(29);

                if (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime)) {
                    return false;
                }
            }
        }
        return true; // Çakışma yok, randevu alınabilir
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
