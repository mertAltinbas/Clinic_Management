package entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sick_note")
public class SickNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_id", unique = true, nullable = false)
    private String documentId;

    @Column(name = "rest_days", nullable = false)
    private int restDays;

    @Column(name = "issue_day", nullable = false)
    private LocalDateTime issueDay;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_notes_id", nullable = false, unique = true)
    private MedicalNotes medicalNotes;

    public SickNote() {
    }

    public SickNote(String documentId, int restDays, LocalDateTime issueDay) {
        setDocumentId(documentId);
        setRestDays(restDays);
        setIssueDay(issueDay);
    }

    public Long getId() {
        return id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public int getRestDays() {
        return restDays;
    }

    public LocalDateTime getIssueDay() {
        return issueDay;
    }

    public void setDocumentId(String documentId) {
        Objects.requireNonNull(documentId, "Setter documentId is null");
        this.documentId = documentId;
    }

    public void setRestDays(int restDays) {
        this.restDays = restDays;
    }

    public void setIssueDay(LocalDateTime issueDay) {
        Objects.requireNonNull(issueDay, "Setter issueDay is null");
        this.issueDay = issueDay;
    }

    public MedicalNotes getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(MedicalNotes medicalNotes) {
        if (this.medicalNotes == medicalNotes) return;
        this.medicalNotes = medicalNotes;
        medicalNotes.setSickNote(this);
    }
}
