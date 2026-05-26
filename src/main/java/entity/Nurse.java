package entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "nurse")
public class Nurse extends Employee {
    @ElementCollection
    @CollectionTable(name = "nurse_certification", joinColumns = @JoinColumn(name = "employee_id"))
    private Set<String> certification = new HashSet<String>();

    @Column(name = "assigned_ward", nullable = false)
    private String assignedWard;

    @Column(name = "is_scrub_nurse", nullable = false)
    private Boolean isScrubNurse;

    public Nurse() {
    }

    public Nurse(String employeeCode, String name, String middleName, String surname, Set<String> phoneNumber, BigDecimal salary, String shift, Set<String> certification, String assignedWard, boolean isScrubNurse) {
        super(employeeCode, name, middleName, surname, phoneNumber, salary, shift);
        if (certification != null) {
            this.certification.addAll(certification);
        }
        setAssignedWard(assignedWard);
        setScrubNurse(isScrubNurse);
    }

    public Set<String> getCertification() {
        return Collections.unmodifiableSet(certification);
    }

    public String getAssignedWard() {
        return assignedWard;
    }

    public Boolean isScrubNurse() {
        return isScrubNurse;
    }

    public void setScrubNurse(Boolean scrubNurse) {
        Objects.requireNonNull(scrubNurse, "Setter scrubNurse cannot be null");
        isScrubNurse = scrubNurse;
    }

    public void setAssignedWard(String assignedWard) {
        Objects.requireNonNull(assignedWard, "Setter assignedWard is null");
        this.assignedWard = assignedWard;
    }

    public void addCertification(String certification) {
        if (certification != null && !certification.trim().isEmpty()) {
            this.certification.add(certification);
        }
    }

    public void removeCertification(String certification) {
        if (certification != null && !certification.trim().isEmpty()) {
            this.certification.remove(certification);
        }
    }

    @Override
    public BigDecimal calculateBonus() {
        if (isScrubNurse) {
            BigDecimal bonusRate = new BigDecimal("1.25");
            return getSalary().multiply(bonusRate);
        } else return getSalary();
    }
}
