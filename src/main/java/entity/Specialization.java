package entity;

import javax.persistence.*;
import javax.print.Doc;
import java.util.*;

@Entity
@Table(name = "specialization")
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "specialization", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapKey(name = "licenseNumber")
    private Map<String, Doctor> doctorsMap = new HashMap<>();

    public Specialization() {
    }

    public Specialization(String title, String description) {
        setTitle(title);
        setDescription(description);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        Objects.requireNonNull(title, "Setter title cannot be null");
        this.title = title;
    }

    public void setDescription(String description) {
        Objects.requireNonNull(description, "Setter description cannot be null");
        this.description = description;
    }

    public Map<String, Doctor> getDoctorsMap() {
        return Collections.unmodifiableMap(doctorsMap);
    }

    public void addDoctor(Doctor doctor) {
        Objects.requireNonNull(doctor, "Doctor cannot be null");
        if (!doctorsMap.containsKey(doctor.getLicenseNumber())) {
            this.doctorsMap.put(doctor.getLicenseNumber(), doctor);
            doctor.setSpecialization(this);
        }
    }

    public void removeDoctor(Doctor doctor) {
        Objects.requireNonNull(doctor, "Doctor cannot be null");
        if (doctorsMap.containsKey(doctor.getLicenseNumber())) {
            this.doctorsMap.remove(doctor.getLicenseNumber());
            doctor.setSpecialization(null);
        }
    }

    public Doctor findDoctor(String licenseNum) {
        return doctorsMap.get(licenseNum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Specialization)) return false;
        Specialization that = (Specialization) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
