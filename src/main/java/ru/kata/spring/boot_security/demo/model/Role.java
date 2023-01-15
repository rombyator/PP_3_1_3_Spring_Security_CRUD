package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public static Role adminRole() {
        return new Role("admin");
    }

    public static Role userRole() {
        return new Role("user");
    }

    public Role() {
    }

    public Role(String name) {
        setName(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        name = name.toUpperCase();
        if (name.startsWith("ROLE_")) {
            this.name = name;
        } else {
            this.name = "ROLE_" + name;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }

    @Override
    public String toString() {
        return "Role{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        Role other = (Role) obj;

        return Objects.equals(name, other.name);
    }
}
