package com.peppernoodles.user.domain;

import com.peppernoodles.tag.domain.FoodTag;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A login account.
 *
 * <p>Legacy entity: {@code UserAccount}. That class carried 20 associations,
 * mixed {@code @Column} with {@code @Transient} on the same fields, and had
 * {@code CascadeType.ALL} on nearly every relation. Here the account owns only
 * what genuinely belongs to it — its roles and its two profile rows — and every
 * other relationship is navigated from the other side.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Login identifier, always stored lower-cased — a database CHECK constraint
     * enforces it, so callers must normalise before assigning. See
     * {@code EmailAddress#normalise}.
     */
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /** False until the e-mail address has been verified. */
    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "suspended_at")
    private Instant suspendedAt;

    @Column(name = "suspended_reason")
    private String suspendedReason;

    @Column(name = "email_verified_at")
    private Instant emailVerifiedAt;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();

    /** 興趣標籤 chosen at registration; drives shop and restaurant recommendations. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_food_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<FoodTag> foodTags = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CompanyProfile companyProfile;

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public boolean isSuspended() {
        return suspendedAt != null;
    }

    public boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
        if (profile != null) {
            profile.setUser(this);
        }
    }

    public void setCompanyProfile(CompanyProfile companyProfile) {
        this.companyProfile = companyProfile;
        if (companyProfile != null) {
            companyProfile.setUser(this);
        }
    }

    /** Marks the address verified and opens the account for login. */
    public void markEmailVerified() {
        this.emailVerifiedAt = Instant.now();
        this.enabled = true;
    }

    public void suspend(String reason) {
        this.suspendedAt = Instant.now();
        this.suspendedReason = reason;
        this.enabled = false;
    }

    public void reinstate() {
        this.suspendedAt = null;
        this.suspendedReason = null;
        this.enabled = true;
    }
}
