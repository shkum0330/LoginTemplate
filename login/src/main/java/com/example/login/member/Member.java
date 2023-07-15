package com.example.login.member;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Builder
@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {

    @Id
    @Column(name="id",updatable = false, unique = true, nullable = false)
    private String memberId;
    @Column(updatable = false, unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    public Member(String email, String password) {
        this.memberId= UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.roles=new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
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
