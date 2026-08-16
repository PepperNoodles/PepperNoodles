package com.peppernoodles.admin.domain;

import com.peppernoodles.user.domain.User;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Who did what in the back office.
 *
 * <p>New in the refactor — the legacy admin suspended accounts and edited
 * restaurants with no record of who acted or why.
 */
@Entity
@Table(name = "admin_audit_log")
@Getter
@Setter
@NoArgsConstructor
public class AdminAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private User actor;

    @Column(nullable = false)
    private String action;

    @Column(name = "target_type", nullable = false)
    private String targetType;

    @Column(name = "target_id", nullable = false)
    private String targetId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String detail;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    public AdminAuditLog(User actor, String action, String targetType, Object targetId, String detail) {
        this.actor = actor;
        this.action = action;
        this.targetType = targetType;
        this.targetId = String.valueOf(targetId);
        this.detail = detail;
    }
}
