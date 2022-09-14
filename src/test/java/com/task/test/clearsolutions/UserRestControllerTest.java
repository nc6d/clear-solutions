package com.task.test.clearsolutions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.task.test.clearsolutions.model.User;
import com.task.test.clearsolutions.model.dto.UserCreateDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @PostConstruct
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void postConstruct() {
        this.builder.serializers(new LocalDateSerializer(new DateTimeFormatterBuilder()
                .appendPattern("dd.MM.yyyy").toFormatter()));
    }

    @Test
    public void whenConvertingUserCreateDtoToUser_thenCorrect() {

        ModelMapper modelMapper = new ModelMapper();

        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setFirstName("Test");
        userCreateDto.setLastName("Test");
        userCreateDto.setEmail("test@mail.com");

        User user = modelMapper.map(userCreateDto, User.class);
        assertEquals(userCreateDto.getFirstName(), user.getFirstName());
        assertEquals(userCreateDto.getLastName(), user.getLastName());
        assertEquals(userCreateDto.getEmail(), user.getEmail());
    }

    @Test
    public void shouldCreateNewUser() throws Exception {
        User user = User.builder()
                .id(999L)
                .email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .birthDate(LocalDate.parse("01.01.2001", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    public void shouldReturnUser() throws Exception {
        long id = 999L;
        User user = User.builder()
                .id(id)
                .email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .birthDate(LocalDate.parse("01.01.2001", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.birthDate").value("2001-01-01"))
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundUser() throws Exception {
        long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldReturnListOfUsers() throws Exception {
        List<User> users = new ArrayList<>(
                Arrays.asList(
                        User.builder()
                                .id(1L)
                                .email("test@mail.com")
                                .firstName("Test")
                                .lastName("Test")
                                .birthDate(LocalDate.parse("01.01.2001", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                                .build(),
                        User.builder()
                                .id(2L)
                                .email("test@mail.com")
                                .firstName("Test")
                                .lastName("Test")
                                .birthDate(LocalDate.parse("01.01.2001", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                                .build(),
                        User.builder()
                                .id(3L)
                                .email("test@mail.com")
                                .firstName("Test")
                                .lastName("Test")
                                .birthDate(LocalDate.parse("01.01.2001", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                                .build()));


        when(userRepository.findAll()).thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(users.size()))
                .andDo(print());
    }

    @Test
    public void shouldReturnListOfUsersByDatePeriod() throws Exception {
        List<User> users = new ArrayList<>(
                Arrays.asList(
                        User.builder()
                                .id(1L)
                                .email("test@mail.com")
                                .firstName("Test")
                                .lastName("Test")
                                .birthDate(LocalDate.parse("01.01.2001", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                                .build(),
                        User.builder()
                                .id(2L)
                                .email("test@mail.com")
                                .firstName("Test")
                                .lastName("Test")
                                .birthDate(LocalDate.parse("01.01.2002", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                                .build(),
                        User.builder()
                                .id(3L)
                                .email("test@mail.com")
                                .firstName("Test")
                                .lastName("Test")
                                .birthDate(LocalDate.parse("01.01.2005", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                                .build()));

        List<User> usersFiltered = users.stream()
                .filter(user -> user.getBirthDate().isAfter(
                        LocalDate.parse("01.01.2001", DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                .minusDays(1)) &&
                        user.getBirthDate().isBefore(
                                LocalDate.parse("01.01.2002", DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                        .plusDays(1)))
                .toList();

        when(userRepository.findAll()).thenReturn(usersFiltered);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(usersFiltered.size()))
                .andDo(print());
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        long id = 999L;
        doNothing().when(userRepository).deleteById(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/{id}", id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        long id = 1L;

        User user = User.builder()
                .id(id)
                .email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .address("Test")
                .birthDate(LocalDate.parse("01.01.2000", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .phoneNumber("(123) 456 7899")
                .build();

        User userUpdated = User.builder()
                .id(id)
                .email("test2@mail.com")
                .firstName("Test2")
                .lastName("Test2")
                .address("Test")
                .birthDate(LocalDate.parse("01.01.2001", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .phoneNumber("(123) 456 9999")
                .build();


        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(userUpdated);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/edit/all").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value(userUpdated.getEmail()))
                .andExpect(jsonPath("$.firstName").value(userUpdated.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userUpdated.getLastName()))
                .andExpect(jsonPath("$.address").value(userUpdated.getAddress()))
                .andExpect(jsonPath("$.birthDate").value("2001-01-01"))
                .andExpect(jsonPath("$.phoneNumber").value(userUpdated.getPhoneNumber()))
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundUpdateUser() throws Exception {
        long id = 1L;

        User user = User.builder()
                .id(id)
                .email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .address("Test")
                .birthDate(LocalDate.parse("01.01.2000", DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .phoneNumber("(123) 456 7899")
                .build();


        when(userRepository.findById(id)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/edit/all").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }








}
