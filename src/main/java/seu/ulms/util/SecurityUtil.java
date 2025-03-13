package seu.ulms.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    public static String getCurrentUserName() {
        if(SecurityContextHolder.getContext().getAuthentication() == null){
            return "";
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
