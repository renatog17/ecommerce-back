package com.renato.projects.ecommerce.domain;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	@Column(unique = true, nullable = false)
	private String email;
	private String password;
	private String name;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cliente cliente;
	//Email{
	@Column(nullable = false)
	private boolean verified = false;
	@Column(unique = true)
	private String verificationToken;
	@Column
	private Instant tokenExpiry;
	//}fim email
	//senha{
	@Column(unique = true)
    private String passwordResetToken;
    @Column
    private Instant passwordResetTokenExpiry;
    //}fim senha
	//{ìnicio roles
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles;
	//}fim roles
	public UserDetailsImpl(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.toList();
	}


	@Override
	public String getUsername() {
		
		return this.email;
	}
	@Override
	public String getPassword() {
		return this.password;
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
		return this.isVerified();
	}
}