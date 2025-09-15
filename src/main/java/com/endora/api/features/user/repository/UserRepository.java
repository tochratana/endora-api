package com.endora.api.features.user.repository;

import com.endora.api.features.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.isDefault = false AND u.createdAt >= :startOfDay")
    long countNonDefaultUsersCreatedToday(LocalDateTime startOfDay);

    @Modifying
    @Query("DELETE FROM User u WHERE u.isDefault = false")
    void deleteNonDefaultUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isDefault = true")
    long countDefaultUsers();
}
