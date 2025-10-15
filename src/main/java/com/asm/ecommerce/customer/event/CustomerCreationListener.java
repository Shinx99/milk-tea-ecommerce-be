package com.asm.ecommerce.customer.event;

import com.asm.ecommerce.customer.domain.CustomerModel;
import com.asm.ecommerce.customer.repository.CustomerRepository;
import com.asm.ecommerce.shared.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.time.Instant;
import java.util.UUID;

/**
 * Listens to UserRegisteredEvent and creates Customer profile
 * Runs asynchronously - does not block user registration
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerCreationListener {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    @Async
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // Wait for commit
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("Received UserRegisteredEvent for userId: {}", event.getUserId());

        try {
            // 1. Create Customer from event data
            UUID customerId = UUID.randomUUID();
            CustomerModel customer = CustomerModel.builder()
                    .id(customerId)
                    .phone(event.getPhone())
                    .fullname(event.getFullname())
                    .isActive(false) // Inactive until email verification
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            CustomerModel savedCustomer = customerRepository.save(customer);
            log.info("Customer profile created successfully: customerId={}, userId={}", 
                     savedCustomer.getId(), event.getUserId());

            // 2. Update User.customer_id via internal API
            updateUserCustomerId(event.getUserId(), savedCustomer.getId());

            log.info("Successfully linked customer {} to user {}", 
                     savedCustomer.getId(), event.getUserId());

        } catch (Exception e) {
            log.error("Failed to create customer for userId: {}", event.getUserId(), e);
            // Could publish CustomerCreationFailedEvent here for compensation
            throw e; // Retry will happen due to @Retryable
        }
    }

    /**
     * Calls Auth service internal API to update user.customer_id
     */
    private void updateUserCustomerId(UUID userId, UUID customerId) {
        try {
            String authServiceUrl = "http://localhost:8080/api/internal/users/" 
                                    + userId + "/customer";
            
            UpdateCustomerIdRequest request = new UpdateCustomerIdRequest(customerId);
            
            restTemplate.put(authServiceUrl, request);
            
            log.debug("Updated user {} with customer_id {}", userId, customerId);
            
        } catch (Exception e) {
            log.error("Failed to update user.customer_id via REST API for userId: {}", 
                      userId, e);
            throw new RuntimeException("Failed to link customer to user", e);
        }
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    private static class UpdateCustomerIdRequest {
        private UUID customerId;
    }
}
