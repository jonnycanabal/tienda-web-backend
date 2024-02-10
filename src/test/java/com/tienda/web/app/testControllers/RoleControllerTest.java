package com.tienda.web.app.testControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.web.app.models.entity.Role;
import com.tienda.web.app.services.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService service;

    @InjectMocks
    private ObjectMapper objectMapper;

    private List<Role> roleList;

    @Test
    public void findAllTest() throws Exception {

        this.roleList = new ArrayList<>();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_ADMIN");

        roleList.add(role1);
        roleList.add(role2);

        given(service.finAll()).willReturn(roleList);

        ResultActions response = mockMvc.perform(get("/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleList)));

        for (int i = 0; i<roleList.size(); i ++){
            Role roleToFor = roleList.get(i);
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[" + i + "].id").value(roleToFor.getId()))
                    .andExpect(jsonPath("$[" + i + "].name").value(roleToFor.getName()));
        }
    }

    @Test
    public void FindByIdTest() throws Exception {
        Long roleToFindId = 2L;

        this.roleList = new ArrayList<>();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_ADMIN");

        roleList.add(role1);
        roleList.add(role2);

        Role currentRole = new Role();

        for (int i = 0; i < roleList.size(); i++){
            Role roleToFor = roleList.get(i);
            if(roleToFor.getId().equals(roleToFindId)) {
                currentRole = roleToFor;
                break;
            }
        }

        given(service.findById(anyLong())).willReturn(Optional.of(currentRole));

        ResultActions response = mockMvc.perform(get("/role/ver/{id}",roleToFindId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currentRole)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(currentRole.getId().intValue()))
                .andExpect(jsonPath("$.name").value(currentRole.getName()));
    }

    @Test
    public void roleSaveTest() throws Exception {
        Role roleToSave = new Role();

        roleToSave.setId(3L);
        roleToSave.setName("ROLE_PRUEBA");

        given(service.save(any(Role.class))).willReturn(roleToSave);

        ResultActions response = mockMvc.perform(post("/role/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleToSave)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(roleToSave.getId()))
                .andExpect(jsonPath("$.name").value(roleToSave.getName()));
    }

    @Test
    public void editTest() throws Exception {
        this.roleList = new ArrayList<>();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_ADMIN");

        roleList.add(role1);
        roleList.add(role2);

        Long roleToFindId = 1L;
        Role currentRole = new Role();

        for (int i = 0; i < roleList.size(); i++){
            Role roleToFor = roleList.get(i);
            if(roleToFor.getId().equals(roleToFindId)){
                currentRole = roleToFor;
            }
        }

        Role updatedRole = new Role();
        updatedRole.setId(currentRole.getId());
        updatedRole.setName("ROLE_EDIT");

        for(int i = 0; i<roleList.size(); i++){
            System.out.println("Role Antes del Edit: " + roleList.get(i).getName());
        }

        given(service.findById(anyLong())).willReturn(Optional.of(currentRole));

        given(service.save(any(Role.class))).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/role/editar/{id}", currentRole.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRole)));

        for(int i = 0; i<roleList.size(); i++){
            System.out.println("Role Despues del Edit: " + roleList.get(i).getName());
        }

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(updatedRole.getId()))
                .andExpect(jsonPath("$.name").value(updatedRole.getName()));
    }

    @Test
    public void deleteRoleTest() throws Exception {
        Long roleToDeleteId = 1L;

        willDoNothing().given(service).delateById(roleToDeleteId);

        ResultActions response = mockMvc.perform(delete("/role/eliminar/{id}",roleToDeleteId));

        response.andExpect(status().isNoContent());
    }

    @Test
    public void deleteRoleTest2() throws Exception {
        Long roleToDeleteId = 1L;

        this.roleList = new ArrayList<>();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_ADMIN");

        roleList.add(role1);
        roleList.add(role2);

        Role currentRole = new Role();

        for (int i = 0; i<roleList.size(); i ++){
            Role roletoFor = roleList.get(i);
            if (roletoFor.getId().equals(roleToDeleteId)){
                currentRole = roletoFor;
                break;
            }
        }

        willDoNothing().given(service).delateById(currentRole.getId());

        ResultActions response = mockMvc.perform(delete("/role/eliminar/{id}", roleToDeleteId));

        response.andExpect(status().isNoContent());
    }

    @Test
    public void roleFindTest() throws Exception {

        String roleToFindName = "ROLE_USER";

        this.roleList = new ArrayList<>();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_ADMIN");

        roleList.add(role1);
        roleList.add(role2);

        Role currentRole = new Role();

        for (int i= 0; i<roleList.size(); i++){
            Role roleToFor = roleList.get(i);
            if (roleToFor.getName().equals(roleToFindName)){
                currentRole = roleToFor;
                break;
            }
        }

        given(service.findByName(anyString())).willReturn(Optional.of(currentRole));

        ResultActions response = mockMvc.perform(post("/role/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roleName\":\"" + roleToFindName + "\"}"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(currentRole.getName()));
    }
}
