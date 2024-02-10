package com.tienda.web.app.testControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.web.app.models.entity.Product;
import com.tienda.web.app.models.entity.User;
import com.tienda.web.app.services.UserService;
import org.checkerframework.checker.units.qual.m;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ObjectMapper objectMapper;



    private List<User> userList;

    @Test
    public void findAllTest() throws Exception {

        this.userList = new ArrayList<>();

        User user1 = new User();

        user1.setId(1L);
        user1.setFirtsName("Diego");
        user1.setMiddleName("");
        user1.setLastName("Briñez");
        user1.setSeconLastName("");
        user1.setPhoneNumber("+57 3183695874");
        user1.setEmail("pumba@gmail.com");
        user1.setUsername("pumba");
        user1.setPassword("12345");
        user1.setEnabled(true);
        user1.setAdmin(true);

        userList.add(user1);

        given(service.finAll()).willReturn(userList);

        ResultActions response = mockMvc.perform(get("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userList)));

        for (int i = 0; i<userList.size(); i++){
            User currentUser = userList.get(i);
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$["+ i +"].id").value(currentUser.getId()))
                    .andExpect(jsonPath("$["+ i +"].firtsName").value(currentUser.getFirtsName()))
                    .andExpect(jsonPath("$["+ i +"].middleName").value(currentUser.getMiddleName()))
                    .andExpect(jsonPath("$["+ i +"].lastName").value(currentUser.getLastName()))
                    .andExpect(jsonPath("$["+ i +"].seconLastName").value(currentUser.getSeconLastName()))
                    .andExpect(jsonPath("$["+ i +"].phoneNumber").value(currentUser.getPhoneNumber()))
                    .andExpect(jsonPath("$["+ i +"].email").value(currentUser.getEmail()))
                    .andExpect(jsonPath("$["+ i +"].username").value(currentUser.getUsername()));
        }
    }

    @Test
    public void findByIdTest() throws Exception {

        Long userToFindId = 1L;

        this.userList = new ArrayList<>();

        User user1 = new User();
        user1.setId(1L);
        user1.setFirtsName("Diego");
        user1.setMiddleName("");
        user1.setLastName("Briñez");
        user1.setSeconLastName("");
        user1.setPhoneNumber("+57 3183695874");
        user1.setEmail("pumba@gmail.com");
        user1.setUsername("pumba");
        user1.setPassword("12345");
        user1.setEnabled(true);
        user1.setAdmin(true);

        userList.add(user1);

        User userToFind = new User();

        for (int i = 0; i < userList.size(); i++){
            User userToFor = userList.get(i);
            if(userToFor.getId().equals(userToFindId)){
                userToFind = userToFor;
                break;
            }
        }

        given(service.findById(anyLong())).willReturn(Optional.of(userToFind));

        ResultActions response = mockMvc.perform(get("/usuario/ver/{id}", userToFindId)
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(userToFind)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userToFind.getId()));

    }

    @Test
    public void userSaveTest() throws Exception {

        User user = new User();

        user.setId(1L);
        user.setFirtsName("Diego");
        user.setMiddleName("");
        user.setLastName("Briñez");
        user.setSeconLastName("");
        user.setPhoneNumber("+57 3183695874");
        user.setEmail("pumba@gmail.com");
        user.setUsername("pumba");
//        user.setPassword(passwordEncoder.encode("12345"));
        user.setPassword("12345"); //Hay que quitar @JsonProperty(access = Access.WRITE_ONLY) en Entity para la prueba
        user.setEnabled(true);
        user.setAdmin(false);

//        given(service.save(any(User.class))).will(invocation -> {
//            User userArgument = invocation.getArgument(0);
//            String encodedPassword = passwordEncoder.encode(userArgument.getPassword());
//            userArgument.setPassword(encodedPassword);
//            return userArgument;
//        });

        given(service.save(any(User.class))).willReturn(user);

        System.out.println("Cuerpo de la solicitud: " + objectMapper.writeValueAsString(user));

        ResultActions response = mockMvc.perform(post("/usuario/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firtsName").value(user.getFirtsName()))
                .andExpect(jsonPath("$.middleName").value(user.getMiddleName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.seconLastName").value(user.getSeconLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));
    }

    @Test
    public void saveAdminTest() throws Exception {

        User user = new User();

        user.setId(1L);
        user.setFirtsName("Diego");
        user.setMiddleName("");
        user.setLastName("Briñez");
        user.setSeconLastName("");
        user.setPhoneNumber("+57 3183695874");
        user.setEmail("pumba@gmail.com");
        user.setUsername("pumba");
        user.setPassword("12345"); //Hay que quitar @JsonProperty(access = Access.WRITE_ONLY) en Entity para la prueba
        user.setEnabled(true);
        user.setAdmin(true);

        given(service.save(any(User.class))).willReturn(user);

        ResultActions response = mockMvc.perform(post("/usuario/crear/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firtsName").value(user.getFirtsName()))
                .andExpect(jsonPath("$.middleName").value(user.getMiddleName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.seconLastName").value(user.getSeconLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));
    }

    @Test
    public void editUserTest() throws Exception {

        Long userToEditId = 1L;

        this.userList = new ArrayList<>();

        User user = new User();
        user.setId(1L);
        user.setFirtsName("Diego");
        user.setMiddleName("");
        user.setLastName("Briñez");
        user.setSeconLastName("");
        user.setPhoneNumber("+57 3183695874");
        user.setEmail("pumba@gmail.com");
        user.setUsername("pumba");
        user.setPassword("12345");
        user.setEnabled(true);
        user.setAdmin(false);

        userList.add(user);
        User currentUser = new User();

        for (int i = 0; i < userList.size(); i++){
            User userToFor = userList.get(i);
            if (userToFor.getId().equals(userToEditId)){
                currentUser = userToFor;
            }
        }

        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setFirtsName("Diego2");
        updatedUser.setMiddleName("");
        updatedUser.setLastName("Briñez2");
        updatedUser.setSeconLastName("");
        updatedUser.setPhoneNumber("+57 3183695874");
        updatedUser.setEmail("pumba2@gmail.com");
        updatedUser.setUsername("pumba");
        updatedUser.setPassword("12345");
        updatedUser.setEnabled(true);
        updatedUser.setAdmin(false);

        for (int i = 0; i < userList.size(); i++){
            System.out.println("Antes del editar: " + userList.get(i).getFirtsName());
            System.out.println("Antes del editar: " + userList.get(i).getLastName());
            System.out.println("Antes del editar: " + userList.get(i).getEmail());
        }

        given(service.findById(anyLong())).willReturn(Optional.of(currentUser));

        given(service.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/usuario/editar/{1}", userToEditId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));

        for (int i = 0; i < userList.size(); i++){
            System.out.println("Despues del editar: " + userList.get(i).getFirtsName());
            System.out.println("Despues del editar: " + userList.get(i).getLastName());
            System.out.println("Despues del editar: " + userList.get(i).getEmail());
        }

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.firtsName").value(updatedUser.getFirtsName()))
                .andExpect(jsonPath("$.middleName").value(updatedUser.getMiddleName()))
                .andExpect(jsonPath("$.lastName").value(updatedUser.getLastName()))
                .andExpect(jsonPath("$.seconLastName").value(updatedUser.getSeconLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(updatedUser.getPhoneNumber()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.username").value(updatedUser.getUsername()))
                .andExpect(jsonPath("$.password").value(updatedUser.getPassword()));
    }

    @Test
    public void deleteUser() throws Exception {
        Long userToDeleteId = 1L;

        willDoNothing().given(service).deleteById(anyLong());

        ResultActions response = mockMvc.perform(delete("/usuario/eliminar/{id}", userToDeleteId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound());
    }
}
