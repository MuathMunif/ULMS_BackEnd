package seu.ulms.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("API is working!");
    }
}
