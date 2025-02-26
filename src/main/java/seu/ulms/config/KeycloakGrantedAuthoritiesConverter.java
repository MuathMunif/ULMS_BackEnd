package seu.ulms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>>{

    private static final String CLIENT_ID = "umls";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");

        if (resourceAccess != null && resourceAccess.containsKey(CLIENT_ID)) {
            Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get(CLIENT_ID);
            Object rolesObject = clientRoles.get("roles");

            if (rolesObject instanceof List<?>) {
                List<?> rolesList = (List<?>) rolesObject;
                return rolesList.stream()
                        .filter(role -> role instanceof String) // Ensure elements are strings
                        .map(role -> new SimpleGrantedAuthority(role.toString())) // Keep roles as-is
                        .collect(Collectors.toList());
            }
        }

        return Collections.emptyList(); // Return empty if no roles found
    }
}
