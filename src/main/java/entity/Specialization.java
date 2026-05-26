package entity;

import javax.persistence.*;
import java.util.Objects;

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

    public Specialization() {}

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
        Objects.requireNonNull(description,  "Setter description cannot be null");
        this.description = description;
    }
}
