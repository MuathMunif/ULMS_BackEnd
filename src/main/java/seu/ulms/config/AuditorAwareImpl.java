package seu.ulms.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // In a real application, you would fetch the currently logged-in user from SecurityContext
        // For demonstration purposes, we'll use a hardcoded value
        return Optional.of("system-user"); // Replace with your actual logic
    }
}
