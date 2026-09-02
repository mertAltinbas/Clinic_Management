package entity;

import entity.enums.StatusType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id", nullable = false, unique = true)
    private String appointmentId;

    @Column(name = "date", nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusType status;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(mappedBy = "appointment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Invoice invoice;

    @OneToOne(mappedBy = "appointment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private MedicalNotes medicalNotes;

    public Appointment() {
    }

    public Appointment(String appointmentId, LocalDateTime dateTime, StatusType status, Doctor doctor, Patient patient) {
        setAppointmentId(appointmentId);
        setDateTime(dateTime);
        setStatus(status);
        setDoctor(doctor);
        setPatient(patient);
    }

    public Appointment(String appointmentId, LocalDateTime dateTime, StatusType status, Doctor doctor, Patient patient, String note) {
        setAppointmentId(appointmentId);
        setDateTime(dateTime);
        setStatus(status);
        setDoctor(doctor);
        setPatient(patient);
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public StatusType getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setDateTime(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime, "setter date cannot be null");
        this.dateTime = dateTime;
    }

    public void setStatus(StatusType status) {
        Objects.requireNonNull(status, "setter status cannot be null");
        this.status = status;
    }

    public void setNote(String note) {
        Objects.requireNonNull(note, "setter note cannot be null");
        this.note = note;
    }

    public void cancelAppointment() {
        this.status = StatusType.CANCELED;
    }

    public void updateStatus(StatusType nStatus) {
        this.status = nStatus;
    }

    public void generateInvoice() {
        if (this.doctor == null) throw new IllegalStateException("There should be one doctor at least.");

        BigDecimal fee = BigDecimal.valueOf(this.doctor.getConsultationFee());
        Float vatRate = 0.20f;

        BigDecimal vatMultiplier = BigDecimal.ONE.add(BigDecimal.valueOf(vatRate));
        BigDecimal totalAmt = fee.multiply(vatMultiplier);

        String generatedInvoiceId = "INV-" + this.appointmentId;

        Invoice newInvoice = new Invoice(generatedInvoiceId, totalAmt, LocalDate.now(), false, vatRate, this);
        this.setInvoice(newInvoice);
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        if (this.doctor == doctor) return;
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        if (this.patient == patient) return;
        this.patient = patient;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        if (this.invoice == invoice) return;
        this.invoice = invoice;
        if (invoice != null) {
            invoice.setAppointment(this);
        }
    }

    public MedicalNotes getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(MedicalNotes medicalNotes) {
        if (this.medicalNotes == medicalNotes) return;
        this.medicalNotes = medicalNotes;
        if (medicalNotes != null) {
            medicalNotes.setAppointment(this);
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String pName = (patient != null) ? patient.getFirstName() + " " + patient.getLastName() : "Unknown";
        String statusStr = status.name().charAt(0) + status.name().substring(1).toLowerCase();
        return dateTime.format(formatter) + " - " + pName + " (" + statusStr + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(getAppointmentId(), that.getAppointmentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }
}
