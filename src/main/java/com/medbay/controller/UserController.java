package com.medbay.controller;

import com.medbay.domain.User;
import com.medbay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PutMapping("/activity/{id}")
    public ResponseEntity<Void> changeActivityStatus(@RequestParam String status,
                                                     @PathVariable Long id) {
        return userService.changeActivityStatus(status, id);
    }

//    @GetMapping
//    public ResponseEntity<List<User>> getRegistrationsRequests() {
//        return userService.getRegistrationRequests();
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        return userService.deleteUser(id);¸¸
//    }
//

}
