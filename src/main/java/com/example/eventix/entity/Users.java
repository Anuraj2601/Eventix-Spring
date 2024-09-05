package com.example.eventix.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String regNo;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private boolean active;
    private String otp;
    private LocalDateTime otpGeneratedTime;
    private String bio;

    @Column(columnDefinition = "varchar(255) default 'student' ")
    private String role;

    /*private String imageFileName;
    private String imageContentType;
    @Lob
    @Column(name = "imagedata", length = 1000)
    private byte[] imageData;*/
    private String photoUrl;

    @ToString.Exclude
    @OneToOne(mappedBy = "users")
    @JsonManagedReference
    private Clubs clubs;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
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

//    @OneToMany(mappedBy = "published_user")
//    @JsonManagedReference
//    private Set<Post> postsList;
}
