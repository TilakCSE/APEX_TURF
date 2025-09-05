package org.example.model;

/**
 * Represents a turf facility where users can book time slots.
 */
public class Turf {
    private Long id;
    private String name;
    private String location;
    private boolean active;

    public Turf() {}

    public Turf(Long id, String name, String location, boolean active) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Turf{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", active=" + active +
                '}';
    }
}
