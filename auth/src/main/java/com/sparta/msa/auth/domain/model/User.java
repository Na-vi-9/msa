package com.sparta.msa.auth.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "p_user")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String slackId;

    @Column(nullable = false)
    private UserRoleEnum role;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

    @Length(max = 10)
    private String deletedBy;

    private boolean isDeleted;


    @Builder
    public User(String username, String password, String name, String email, String slackId, UserRoleEnum role, String createdBy) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.slackId = slackId;
        this.role = role;
        this.isDeleted = false;
        super.setCreatedBy(createdBy);
    }
}