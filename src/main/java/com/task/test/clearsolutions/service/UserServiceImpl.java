package com.task.test.clearsolutions.service;

import com.task.test.clearsolutions.UserRepository;
import com.task.test.clearsolutions.exception.Status409UserAlreadyRegisteredException;
import com.task.test.clearsolutions.exception.Status410UserNotExistsException;
import com.task.test.clearsolutions.exception.Status422InvalidDatesOrderException;
import com.task.test.clearsolutions.exception.Status431UserIsUnderAgeException;
import com.task.test.clearsolutions.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${user.age.required}")
    private Integer REQUIRED_AGE;

    @Override
    public User createUser(User user) throws Status409UserAlreadyRegisteredException, Status431UserIsUnderAgeException {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(finalUser -> {
                    throw new Status409UserAlreadyRegisteredException(
                            "User " + user.getEmail() + " is already exists. Try another one!");
                });

        return saveUser(user);

    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new Status410UserNotExistsException("User not found"));
    }


    public User updateAll(User user) throws Status410UserNotExistsException {
        return userRepository.findById(user.getId())
                .map(finalUser -> {
                    log.info("User {} updated his bio", finalUser.getEmail());

                    finalUser.setFirstName(user.getFirstName());
                    finalUser.setLastName(user.getLastName());
                    finalUser.setEmail(user.getEmail());
                    finalUser.setAddress(user.getAddress());
                    finalUser.setBirthDate(user.getBirthDate());
                    finalUser.setPhoneNumber(user.getPhoneNumber());


                    return userRepository.save(finalUser);
                })
                .orElseThrow(() -> new Status410UserNotExistsException(
                        String.format("User %s not exists", user.getEmail())));

    }

    public User updateName(User user) throws Status410UserNotExistsException {
        return userRepository.findById(user.getId())
                .map(finalUser -> {
                    log.info("User {} updated his name", finalUser.getEmail());
                    finalUser.setFirstName(user.getFirstName());
                    finalUser.setLastName(user.getLastName());

                    return userRepository.save(finalUser);


                }).orElseThrow(() -> new Status410UserNotExistsException(
                        String.format("User %s not exists", user.getEmail())));

    }

    public User updateEmail(User user) throws Status410UserNotExistsException {
        return userRepository.findById(user.getId())
                .map(finalUser -> {
                    log.info("User {} updated his email", finalUser.getEmail());
                    finalUser.setEmail(user.getEmail());

                    return userRepository.save(finalUser);


                }).orElseThrow(() -> new Status410UserNotExistsException(
                        String.format("User %s not exists", user.getEmail())));

    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.findById(userId).ifPresent(
                user -> userRepository.deleteById(userId));
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public List<User> findAllUsersByBirthdatePeriod(LocalDate startDate, LocalDate endDate)
            throws Status422InvalidDatesOrderException {
        if (startDate.isBefore(endDate)) {
            return findAllUsers()
                    .stream()
                    .filter(user -> user.getBirthDate().isAfter(startDate.minusDays(1)) &&
                            user.getBirthDate().isBefore(endDate.plusDays(1)))
                    .collect(Collectors.toList());
        }
        throw new Status422InvalidDatesOrderException("Start date is greater than end date");

    }

    private boolean checkIfUserIsMature(LocalDate birthDate) throws Status410UserNotExistsException {
        return Period.between(birthDate, LocalDate.now()).getYears() >= REQUIRED_AGE;
    }

    private User saveUser(User user) throws Status431UserIsUnderAgeException {
        if (checkIfUserIsMature(user.getBirthDate())) {
            return userRepository.save(user);
        }
        throw new Status431UserIsUnderAgeException("FORBIDDEN: User " + user.getEmail() + " is under age.");
    }
}
