package entity;

import javax.persistence.*;
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

    public Department() {}

    public Department(String departmentName, int floor, String code) {
        setDepartmentName(departmentName);
        setFloor(floor);
        setCode(code);
    }

    public Long getId() { return id; }

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
}
