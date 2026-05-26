package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "floor", nullable = false)
    private int floor;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "code", nullable = false)
    private String code;

    @ManyToMany(mappedBy = "departments")
    private List<Employee> employees = new ArrayList<>();

    public Department() {
    }

    public Department(String departmentName, int floor, String code) {
        setDepartmentName(departmentName);
        setFloor(floor);
        setCode(code);
    }

    public Long getId() {
        return id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public int getFloor() {
        return floor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        Objects.requireNonNull(code, "setter code cannot be null");
        this.code = code;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setDepartmentName(String departmentName) {
        Objects.requireNonNull(departmentName, "setter departmentName cannot be null");
        this.departmentName = departmentName;
    }

    public List<Employee> getEmployees() {
        return Collections.unmodifiableList(employees);
    }

    public void addEmployee(Employee employee) {
        Objects.requireNonNull(employee, "Employee cannot be null");
        if (!this.employees.contains(employee)) {
            this.employees.add(employee);
            employee.addDepartment(this);
        }
    }

    public void removeEmployee(Employee employee) {
        Objects.requireNonNull(employee, "Employee cannot be null");
        if (this.employees.contains(employee)) {
            this.employees.remove(employee);
            employee.removeDepartment(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
