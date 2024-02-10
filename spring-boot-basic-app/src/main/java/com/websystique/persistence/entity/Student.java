package com.websystique.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "STUDENT")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String major;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id)
                && Objects.equals(name, student.name)
                && Objects.equals(email, student.email)
                && Objects.equals(major, student.major);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, major);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", major='" + major + '\'' +
                '}';
    }
}
