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

    @Column(name = "employee_code", unique = true, nullable = false)
    private String employeeCode;

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


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "employee_department",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private List<Department> departments =  new ArrayList<Department>();

    public Employee() {
    }

    public Employee(String employeeCode, String name, String middleName, String surname, Set<String> phoneNumber, BigDecimal salary, String shift) {
        setEmployeeCode(employeeCode);
        setName(name);
        setMiddleName(middleName);
        setSurname(surname);
        if (phoneNumber == null || phoneNumber.isEmpty())
            throw new IllegalArgumentException("Employee must have at least one phone number.");
        this.phoneNumber.addAll(phoneNumber);
        setSalary(salary);
        setShift(shift);
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeCode() {
        return employeeCode;
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

    public void setEmployeeCode(String employeeCode) {
        Objects.requireNonNull(employeeCode, "Employee code cannot be null");
        this.employeeCode = employeeCode;
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
            if (this.phoneNumber.size() <= 1 && this.phoneNumber.contains(phoneNumber)) {
                throw new IllegalStateException("Cannot remove the last phone number. Phone number must have at least one.");
            }
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

    public List<Department> getDepartments() {
        return Collections.unmodifiableList(departments);
    }

    public void addDepartment(Department department) {
        Objects.requireNonNull(department, "Department cannot be null");
        if (!this.departments.contains(department)) {
            this.departments.add(department);
            department.addEmployee(this);
        }
    }

    public void removeDepartment(Department department) {
        Objects.requireNonNull(department, "Department cannot be null");
        if (this.departments.contains(department)) {
            this.departments.remove(department);
            department.removeEmployee(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeCode, employee.employeeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeCode);
    }
}
