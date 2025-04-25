package seu.ulms.services.keycloak;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import seu.ulms.dto.user.UserDto;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String REALM;

    //  إنشاء مستخدم جديد باستخدام UserDTO
    public ResponseEntity<String> createUser(UserDto userDTO) {
        try {
            RealmResource realmResource = keycloak.realm(REALM);

            //  نبحث في Keycloak باستخدام email ككلمة مفتاحية
            List<UserRepresentation> existingUsers = realmResource.users().search(userDTO.getEmail(), true);

            //  نتأكد إن فيه مستخدم عنده نفس الإيميل بالضبط
            for (UserRepresentation user : existingUsers) {
                if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(userDTO.getEmail())) {
                    return ResponseEntity.badRequest().body("The user with this email already exists.");
                }
            }

            //  إنشاء المستخدم الجديد
            UserRepresentation newUser = new UserRepresentation();
            newUser.setUsername(userDTO.getUsername());
            newUser.setEmail(userDTO.getEmail());
            newUser.setEnabled(true);
            newUser.setEmailVerified(false);

            realmResource.users().create(newUser);
            return ResponseEntity.status(201).body("User created successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create user: " + e.getMessage());
        }
    }

    // جلب معلومات مستخدم معين
    public ResponseEntity<?> getUser(String username) {
        List<UserRepresentation> users = keycloak.realm(REALM).users().search(username);
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users.get(0));
        }
        return ResponseEntity.status(404).body("User not found!");
    }

    // حذف مستخدم معين
    public ResponseEntity<String> deleteUser(String username) {
        List<UserRepresentation> users = keycloak.realm(REALM).users().search(username);
        if (!users.isEmpty()) {
            String userId = users.get(0).getId();
            keycloak.realm(REALM).users().get(userId).remove();
            return ResponseEntity.ok("User deleted successfully!");
        }
        return ResponseEntity.status(404).body("User not found!");
    }

    // تعيين دور (`Role`) للمستخدم
    public ResponseEntity<String> assignRoleToUser(String username, String roleName) {
        try {
            RealmResource realmResource = keycloak.realm(REALM);
            List<UserRepresentation> users = realmResource.users().search(username);

            if (users.isEmpty()) {
                return ResponseEntity.status(404).body("User not found!");
            }

            String userId = users.get(0).getId();
            UserResource userResource = realmResource.users().get(userId);

            RolesResource rolesResource = realmResource.roles();
            RoleResource roleResource = rolesResource.get(roleName);
            RoleRepresentation roleRepresentation = roleResource.toRepresentation();

            userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));

            return ResponseEntity.ok("Role assigned successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to assign role: " + e.getMessage());
        }
    }

    //  إرسال رابط لإعادة تعيين كلمة المرور
    public ResponseEntity<String> triggerResetPasswordEmail(String username) {
        try {
            List<UserRepresentation> users = keycloak.realm(REALM).users().search(username);
            if (users.isEmpty()) {
                return ResponseEntity.status(404).body("User not found!");
            }

            String userId = users.get(0).getId();
            UserResource userResource = keycloak.realm(REALM).users().get(userId);

            userResource.executeActionsEmail(Collections.singletonList("UPDATE_PASSWORD"));

            return ResponseEntity.ok("Password reset email sent successfully to " + username);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending reset password email: " + e.getMessage());
        }
    }

    //  إرسال رابط لتفعيل البريد الإلكتروني
    public ResponseEntity<String> triggerVerifyEmail(String username) {
        try {
            List<UserRepresentation> users = keycloak.realm(REALM).users().search(username);
            if (users.isEmpty()) {
                return ResponseEntity.status(404).body("User not found!");
            }

            String userId = users.get(0).getId();
            UserResource userResource = keycloak.realm(REALM).users().get(userId);

            userResource.executeActionsEmail(Collections.singletonList("VERIFY_EMAIL"));

            return ResponseEntity.ok("Verification email sent successfully to " + username);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending verification email: " + e.getMessage());
        }
    }

    // إنشاء كلمة مرور للمستخدم
    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        return passwordCred;
    }

// to save user data from keycloak to User DTO
    public UserDto getUserByUsername(String username) {
        List<UserRepresentation> users = keycloak.realm(REALM).users().search(username);

        if (users.isEmpty()) {
            return null;
        }

        UserRepresentation user = users.get(0);
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFullName((user.getFirstName() != null ? user.getFirstName() : "")
                + " " +
                (user.getLastName() != null ? user.getLastName() : ""));

        return userDto;
    }


    // test keycloak admin client
    public boolean isConnected() {
        try {
            List<UserRepresentation> users = keycloak.realm(REALM).users().list();
            System.out.println("✅ Total users in Keycloak: " + users.size());
            return true;
        } catch (Exception e) {
            System.out.println(" Failed to connect to Keycloak: " + e.getMessage());
            return false;
        }
    }
}