package entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "surname", nullable = false)
    private String surname;

    @ElementCollection
    @CollectionTable(name = "employee_phone", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "phone_number")
    private Set<String> phoneNumber = new HashSet<String>();

    @Column(name = "salary", nullable = false)
    private BigDecimal salary;

    @Column(name = "shift", nullable = false)
    private String shift;

    public Employee() {
    }

    public Employee(String name, String middleName, String surname, Set<String> phoneNumber, BigDecimal salary, String shift) {
        setName(name);
        setMiddleName(middleName);
        setSurname(surname);
        if (phoneNumber != null) {
            this.phoneNumber.addAll(phoneNumber);
        }
        setSalary(salary);
        setShift(shift);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getMiddleName() {
        return (middleName == null || middleName.isEmpty()) ? Optional.empty() : Optional.of(middleName);
    }

    public String getSurname() {
        return surname;
    }

    public Set<String> getPhoneNumber() {
        return Collections.unmodifiableSet(phoneNumber);
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public String getShift() {
        return shift;
    }

    public void setName(String name) {
        Objects.requireNonNull(name, "setter name cannot be null");
        this.name = name;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setSurname(String surname) {
        Objects.requireNonNull(surname, "setter surname cannot be null");
        this.surname = surname;
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

    public void setSalary(BigDecimal salary) {
        Objects.requireNonNull(salary, "setter salary cannot be null");
        this.salary = salary;
    }

    public void setShift(String shift) {
        Objects.requireNonNull(shift, "setter shift cannot be null");
        this.shift = shift;
    }

    public abstract BigDecimal calculateBonus();
}
