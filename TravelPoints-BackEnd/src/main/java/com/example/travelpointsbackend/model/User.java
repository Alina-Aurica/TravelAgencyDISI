package com.example.travelpointsbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@Data
@Table(name = "users")
public class User implements Serializable, UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", nullable=false)
    private Long userId;
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="email", nullable = false, unique = true)
    private String email;
    @Column(name="password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name="user_type", nullable = false)
    private UserType userType;
    @Column(name="created_at", nullable = false)
    private Timestamp createdAt;
    @Column(name="is_logged", nullable = false)
    private boolean isLogged;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserWishlist userWishlist;
    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    public User() {
        this.createdAt =  new Timestamp(System.currentTimeMillis());
        this.isLogged = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userType.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
