package com.medbay.controller;

import com.medbay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PutMapping("/activity/{id}")
    public ResponseEntity<Void> changeActivityStatus(@RequestParam String status,
                                                     @RequestParam(required = false) String rejectionReason,
                                                     @PathVariable Long id) {
        return userService.changeActivityStatus(status, id, rejectionReason);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(required = false) Long id) {
        return userService.deleteUser(id);
    }


}
