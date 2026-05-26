package entity;

import entity.enums.StatusType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusType status;

    public Appointment() {}

    public Appointment(LocalDate date, StatusType status) {
        setDate(date);
        setStatus(status);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setDate(LocalDate date) {
        Objects.requireNonNull(date, "setter date cannot be null");
        this.date = date;
    }

    public void setStatus(StatusType status) {
        Objects.requireNonNull(status, "setter status cannot be null");
        this.status = status;
    }

    public void cancelAppointment() {
        this.status = StatusType.CANCELED;
    }

    public void updateStatus(StatusType nStatus) {
        this.status = nStatus;
    }
}
