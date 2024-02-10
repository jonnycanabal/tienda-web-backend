package com.tienda.web.app.service.test;

import com.tienda.web.app.models.entity.Role;
import com.tienda.web.app.models.repository.RoleRepository;
import com.tienda.web.app.services.RoleServiceImplement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class RoleServiceTest {

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private RoleServiceImplement service;

    private List<Role> roleList = new ArrayList<>();

    @Test
    public void roleServiceFindAllTest() {

        this.roleList = new ArrayList<>();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_ADMIN");

        roleList.add(role1);
        roleList.add(role2);

        Mockito.when(repository.findAll()).thenReturn(roleList);

        Iterable<Role> currentRole = service.finAll();

        for (int i = 0; i < roleList.size(); i++){
            System.out.println(roleList.get(i).getName());
        }

        for (Role role : currentRole){
            System.out.println(role.getName());
        }

        Assertions.assertEquals(roleList, currentRole);
    }

    @Test
    public void roleFindByIdTest(){

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

        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(currentRole));

        Optional<Role> result = service.findById(currentRole.getId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(currentRole.getId(), roleToFindId);

        System.out.println(result.get().getId());
        System.out.println("Este Id: " + currentRole.getId() + " - Es igual a este Id:" + roleToFindId);
    }

    @Test
    public void roleSaveTest(){

        Role roleToSave = new Role();

        roleToSave.setId(1L);
        roleToSave.setName("ROLE_PRUEBA");

        Mockito.when(repository.save(Mockito.any(Role.class))).thenReturn(roleToSave);

        Role result = service.save(roleToSave);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result, roleToSave);

        System.out.println("Repository: " + result.getName());
        System.out.println("Result: " + roleToSave.getName());
    }

    @Test
    public void roleDeleteTest(){

        this.roleList = new ArrayList<>();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_ADMIN");

        roleList.add(role1);
        roleList.add(role2);

        Long roleToDeleteId = 1L;

        Role currentRole = new Role();

        for (int i = 0; i < roleList.size(); i++){
            Role roleToFor = roleList.get(i);
            if (roleToFor.getId().equals(roleToDeleteId)){
                currentRole = roleToFor;
            }
        }
        //En este caso invocamos primero el metodo del servicio
        service.delateById(currentRole.getId());

        //Despues se verifica que el medoto deleteById fue llamadado, este caso 1 vez
        Mockito.verify(repository, Mockito.times(1)).deleteById(currentRole.getId());

        System.out.println("Se elimino el Role con ID y Nombre: " + currentRole.getId() + " - " + currentRole.getName());
    }

    @Test
    public void roleFindName(){

        this.roleList = new ArrayList<>();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_ADMIN");

        roleList.add(role1);
        roleList.add(role2);

        String roleToFind = "ROLE_USER";

        Role currentRole = new Role();

        for (int i = 0; i < roleList.size(); i++){
            Role roleToFor = roleList.get(i);
            if (roleToFor.getName().equals(roleToFind)){
                currentRole = roleToFor;
            }
        }

        Mockito.when(repository.findByName(Mockito.anyString())).thenReturn(Optional.of(currentRole));

        Optional<Role> result = service.findByName(currentRole.getName());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(currentRole.getName(), roleToFind);

        System.out.println("Repository: " + result.get().getName());
        if (currentRole.getName().equals(roleToFind)){
            System.out.println(currentRole.getName());
        }
    }

}
