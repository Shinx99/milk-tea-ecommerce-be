package com.asm.ecommerce.auth.repository;

import com.asm.ecommerce.auth.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Tìm tất cả các token chưa được sử dụng VÀ chưa hết hạn của một user.
     */
    @Query("SELECT t FROM PasswordResetToken t WHERE t.userId = :userId AND t.used = false AND t.expiryDate > :now")
    List<PasswordResetToken> findAllActiveTokensByUserId(
            @Param("userId") UUID userId,
            @Param("now") Instant now
    );
}
