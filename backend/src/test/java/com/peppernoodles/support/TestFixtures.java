package com.peppernoodles.support;

import com.peppernoodles.user.domain.RoleName;
import com.peppernoodles.user.domain.User;
import com.peppernoodles.user.domain.UserProfile;
import com.peppernoodles.user.repository.RoleRepository;
import com.peppernoodles.user.repository.UserRepository;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Builds the minimum persistent state a test needs. */
@Component
public class TestFixtures {

    public static final String PASSWORD = "Password123!";

    private static final AtomicLong COUNTER = new AtomicLong();

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;

    public TestFixtures(UserRepository users, RoleRepository roles, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
    }

    public User enabledUser(RoleName role) {
        String email = "test%d@example.com".formatted(COUNTER.incrementAndGet());
        User user = new User(email, passwordEncoder.encode(PASSWORD));
        user.setEnabled(true);
        user.markEmailVerified();
        user.addRole(roles.require(role));

        UserProfile profile = new UserProfile();
        profile.setRealName("Test User");
        profile.setNickname("tester" + COUNTER.get());
        user.setProfile(profile);

        return users.save(user);
    }
}
