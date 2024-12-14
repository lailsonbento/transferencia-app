package com.lailsonbento.transferenciaapp.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String document;
    private String email;

    public User(String fullName, String document, String email) {
        this.fullName = fullName;
        this.document = document;
        this.email = email;
    }

}