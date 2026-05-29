package entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_id", nullable = false, unique = true)
    private String invoiceId;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;

    @Column(name = "vat_rate", nullable = false)
    private Float vatRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    public Invoice() {}

    public Invoice(String invoiceId, BigDecimal totalAmount, LocalDate issueDate, Boolean isPaid, Float vatRate,  Appointment appointment) {
        setInvoiceId(invoiceId);
        setTotalAmount(totalAmount);
        setIssueDate(issueDate);
        setPaid(isPaid);
        setVatRate(vatRate);
        setAppointment(appointment);
    }

    public Long getId() {
        return id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public Float getVatRate() {
        return vatRate;
    }

    public void setInvoiceId(String invoiceId) {
        Objects.requireNonNull(invoiceId, "Setter invoiceId cannot be null");
        this.invoiceId = invoiceId;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        Objects.requireNonNull(totalAmount, "Setter totalAmount cannot be null");
        this.totalAmount = totalAmount;
    }

    public void setIssueDate(LocalDate issueDate) {
        Objects.requireNonNull(issueDate, "Setter issueDate cannot be null");
        this.issueDate = issueDate;
    }

    public void setPaid(Boolean paid) {
        Objects.requireNonNull(paid, "Setter paid cannot be null");
        isPaid = paid;
    }

    public void setVatRate(Float vatRate) {
        Objects.requireNonNull(vatRate, "Setter vatRate cannot be null");
        this.vatRate = vatRate;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        if (this.appointment == appointment) return;
        this.appointment = appointment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        Invoice that = (Invoice) o;
        return Objects.equals(getInvoiceId(), that.getInvoiceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId());
    }
}
