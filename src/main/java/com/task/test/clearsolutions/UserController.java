package com.task.test.clearsolutions;

import com.task.test.clearsolutions.exception.Status410UserNotExistsException;
import com.task.test.clearsolutions.exception.Status422InvalidDatesOrderException;
import com.task.test.clearsolutions.exception.Status431UserIsUnderAgeException;
import com.task.test.clearsolutions.model.User;
import com.task.test.clearsolutions.model.dto.*;
import com.task.test.clearsolutions.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<User> create(@Valid @RequestBody UserCreateDto createDto)
            throws Status431UserIsUnderAgeException {
        return ResponseEntity.ok(userService.createUser(modelMapper.map(createDto, User.class)));
    }

    @PostMapping("/delete")
    public void delete(@RequestParam("userId") Long userId) throws Status410UserNotExistsException {
        userService.deleteUserById(userId);
    }

    @PutMapping("/edit/all")
    public ResponseEntity<User> updateAllInfo (@RequestBody UpdateUserInfoDto updateDto)
            throws Status410UserNotExistsException {
        return ResponseEntity.ok(userService.updateAll(modelMapper.map(updateDto, User.class)));
    }

    @PutMapping("/edit/name")
    public ResponseEntity<User> updateName (@RequestBody UpdateUserInfoDto updateDto)
        throws Status410UserNotExistsException {
        return ResponseEntity.ok(userService.updateName(modelMapper.map(updateDto, User.class)));
    }

    @PutMapping("/edit/email")
    public ResponseEntity<User> updateEmail (@RequestBody UpdateUserInfoDto updateDto)
            throws Status410UserNotExistsException {
        return ResponseEntity.ok(userService.updateEmail(modelMapper.map(updateDto, User.class)));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/get-all/period")
    public ResponseEntity<List<User>> getAllUsersByDatePeriod(@RequestBody DatePeriodDto dateDto)
            throws Status422InvalidDatesOrderException {
        LocalDate from = dateDto.getDateFrom();
        LocalDate to = dateDto.getDateTo();
        return ResponseEntity.ok(userService.findAllUsersByBirthdatePeriod(from, to));
    }


}
