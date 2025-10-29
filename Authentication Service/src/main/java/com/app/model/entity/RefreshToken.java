package com.app.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name = "refreshtoken")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonProperty("id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonProperty("user_id")
    private User user;

    @Column(name = "token", nullable = false, unique = true)
    @JsonProperty("token")
    private String token;

    @Column(name = "expiry", nullable = false)
    @JsonProperty("expiry")
    private Instant expiryDate;

}
