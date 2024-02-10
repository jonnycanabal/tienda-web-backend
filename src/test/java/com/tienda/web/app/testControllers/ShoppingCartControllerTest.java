package com.tienda.web.app.testControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.web.app.models.entity.Product;
import com.tienda.web.app.models.entity.ShoppingCart;
import com.tienda.web.app.models.entity.User;
import com.tienda.web.app.services.ShoppingCartService;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService service;

    @InjectMocks
    private ObjectMapper objectMapper;

    private List<ShoppingCart> shoppingCartList;

    @Test
    public void FindAll() throws Exception {

        this.shoppingCartList = new ArrayList<>();

        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.setId(1L);

        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.setId(2L);

        shoppingCartList.add(shoppingCart1);
        shoppingCartList.add(shoppingCart2);

        given(service.finAll()).willReturn(shoppingCartList);

        ResultActions response = mockMvc.perform(get("/carrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shoppingCartList)));

        for (int i = 0; i < shoppingCartList.size(); i++){
            ShoppingCart curretShoppingCart = shoppingCartList.get(i);
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$["+i+"].id").value(curretShoppingCart.getId()));
            }
    }

    @Test
    public void findById() throws Exception{
        Long shoppingCartToFindId = 1L;

        this.shoppingCartList = new ArrayList<>();

        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.setId(1L);

        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.setId(2L);

        shoppingCartList.add(shoppingCart1);
        shoppingCartList.add(shoppingCart2);

        ShoppingCart currentShoppingCart = new ShoppingCart();

        for (int i = 0; i < shoppingCartList.size(); i++){
            ShoppingCart shoppingCartToFor = shoppingCartList.get(i);
            if (shoppingCartToFor.getId().equals(shoppingCartToFindId)){
                currentShoppingCart = shoppingCartToFor;
                break;
            }
        }

        given(service.finById(anyLong())).willReturn(Optional.of(currentShoppingCart));

        ResultActions response = mockMvc.perform(get("/carrito/ver/{id}", shoppingCartToFindId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currentShoppingCart)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(currentShoppingCart.getId()));
    }

    @Test
    public void saveTest() throws Exception{

        User user = new User();
        user.setId(1L);
        user.setFirtsName("Diego");
        user.setMiddleName("");
        user.setLastName("Briñez");
        user.setSeconLastName("");
        user.setPhoneNumber("+57 3185984575");
        user.setEmail("pumba@gmail.com");
        user.setUsername("pumba");
        user.setPassword("12345");

        Product product = new Product();
        product.setId(1L);
        product.setProductName("SuperStar");
        product.setPrice(250000);
        product.setPhoto("Foto Producto".getBytes());

        ShoppingCart shoppingCartToSave = new ShoppingCart();
        shoppingCartToSave.setId(1L);
        shoppingCartToSave.setUser(user);
        shoppingCartToSave.getProducts().add(product);

        given(service.save(any(ShoppingCart.class))).willReturn(shoppingCartToSave);

        ResultActions response = mockMvc.perform(post("/carrito/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shoppingCartToSave)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(shoppingCartToSave.getId()));
    }

    @Test
    public void editTest() throws Exception {
        Long shoppingCartToEditId = 1L;

        User user1 = new User();
        user1.setId(1L);
        user1.setFirtsName("Diego");
        user1.setMiddleName("");
        user1.setLastName("Briñez");
        user1.setSeconLastName("");
        user1.setPhoneNumber("+57 3185984575");
        user1.setEmail("pumba@gmail.com");
        user1.setUsername("pumba");
        user1.setPassword("12345");

        User user2 = new User();
        user2.setId(1L);
        user2.setFirtsName("Diego2");
        user2.setMiddleName("");
        user2.setLastName("Briñez2");
        user2.setSeconLastName("");
        user2.setPhoneNumber("+57 3185984575");
        user2.setEmail("pumba2@gmail.com");
        user2.setUsername("pumba2");
        user2.setPassword("12345");

        Product product1 = new Product();
        product1.setId(1L);
        product1.setProductName("SuperStar");
        product1.setPrice(250000);
        product1.setPhoto("Foto Producto".getBytes());

        Product product2 = new Product();
        product2.setId(2L);
        product2.setProductName("Adizero");
        product2.setPrice(100000);
        product2.setPhoto("Foto Producto".getBytes());

        this.shoppingCartList = new ArrayList<>();

        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.setId(1L);

        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.setId(2L);

        shoppingCart1.setUser(user1);
        shoppingCart1.getProducts().add(product1);

        shoppingCart2.setUser(user2);
        shoppingCart2.getProducts().add(product2);

        shoppingCartList.add(shoppingCart1);
        shoppingCartList.add(shoppingCart2);

        ShoppingCart currentShoppingCart = new ShoppingCart();

        for(int i = 0; i < shoppingCartList.size(); i++){
            ShoppingCart shoppingCartToFor = shoppingCartList.get(i);
            if (shoppingCartToFor.getId().equals(shoppingCartToEditId)){
                currentShoppingCart = shoppingCartToFor;
            }
        }

        ShoppingCart updatedShoppingCart = new ShoppingCart();
        updatedShoppingCart.setId(currentShoppingCart.getId());
        updatedShoppingCart.getProducts().add(product1);
        updatedShoppingCart.getProducts().add(product2);
        updatedShoppingCart.setUser(currentShoppingCart.getUser());

        for (int i = 0; i < shoppingCartList.size(); i++){
            System.out.println("Antes del Edit: " + shoppingCartList.get(i).getId());
            System.out.println("Antes del Edit: " + shoppingCartList.get(i).getUser());
            System.out.println("Antes del Edit: " + shoppingCartList.get(i).getProducts());
        }

        given(service.finById(anyLong())).willReturn(Optional.of(currentShoppingCart));

        given(service.save(any(ShoppingCart.class))).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/carrito/editar/{id}", shoppingCartToEditId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedShoppingCart)));

        for (int i = 0; i < shoppingCartList.size(); i++){
            System.out.println("Despues del edit: " + shoppingCartList.get(i).getId());
            System.out.println("Despues del edit: " + shoppingCartList.get(i).getUser());
            System.out.println("Despues del edit: " + shoppingCartList.get(i).getProducts());
        }

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(updatedShoppingCart.getId()));
    }

    @Test
    public void deleteTest() throws Exception {
        Long shoppingCartToDeleteId = 1L;

        this.shoppingCartList = new ArrayList<>();

        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.setId(1L);

        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.setId(2L);

        shoppingCartList.add(shoppingCart1);
        shoppingCartList.add(shoppingCart2);

        ShoppingCart currentShoppingCart = new ShoppingCart();

        for (int i = 0; i < shoppingCartList.size(); i++){
            ShoppingCart shoppingCartToFor = shoppingCartList.get(i);
            if (shoppingCartToFor.getId().equals(shoppingCartToDeleteId)){
                currentShoppingCart = shoppingCartToFor;
                break;
            }
        }

        // Agrega logs antes de la eliminación
        System.out.println("Antes de la eliminación: " + shoppingCartList);

        willDoNothing().given(service).deleteById(anyLong());

        ResultActions response = mockMvc.perform(delete("/carrito/eliminar/{id}", currentShoppingCart.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //se usa el remove para eliminar el carrito de la lista
        shoppingCartList.remove(currentShoppingCart);

        // Agrega logs después de la eliminación
        System.out.println("Después de la eliminación: " + shoppingCartList);

        if (service.finById(shoppingCartToDeleteId).isPresent()) {
            response.andExpect(status().isNoContent());
        } else {
            response.andExpect(status().isNotFound());
        }
    }
}
