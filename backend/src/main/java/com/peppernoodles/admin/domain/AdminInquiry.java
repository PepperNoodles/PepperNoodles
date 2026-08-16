package com.peppernoodles.admin.domain;

import com.peppernoodles.user.domain.User;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 聯絡我們 enquiry. Legacy entity: {@code RearMessageBox}. */
@Entity
@Table(name = "admin_inquiries")
@Getter
@Setter
@NoArgsConstructor
public class AdminInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Null when submitted by a logged-out visitor. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(nullable = false)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.OPEN;

    @Column(name = "resolution_note")
    private String resolutionNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_user_id")
    private User resolvedBy;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    public void resolve(User admin, String note) {
        this.status = Status.RESOLVED;
        this.resolvedBy = admin;
        this.resolutionNote = note;
        this.resolvedAt = Instant.now();
    }

    /** OPEN / RESOLVED. Legacy used a boolean column literally named "condition". */
    public enum Status {
        OPEN,
        RESOLVED
    }
}
