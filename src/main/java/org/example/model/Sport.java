package org.example.model;

/**
 * Represents a sport that can be played on a turf.
 */
public class Sport {
    private Long id;
    private String name;
    private boolean active;

    public Sport() {}

    // ADD THIS CONSTRUCTOR - It will be used to create a new sport from the admin form
    public Sport(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    // This constructor is used by the DAO when reading from the database
    public Sport(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Sport{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}