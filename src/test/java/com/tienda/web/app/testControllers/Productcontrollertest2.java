package com.tienda.web.app.testControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.web.app.models.entity.Product;
import com.tienda.web.app.services.ProductServiceImplement;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class Productcontrollertest2 {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImplement service;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Product> productList;

    @BeforeEach
    public void productTest (){

        this.productList = new ArrayList<>();

        Product product_1 = new Product();
        product_1.setId(1L);
        product_1.setProductName("SuperStar");
        product_1.setPrice(100000);
        product_1.setPhoto("Mock photo SuperStar".getBytes());


        Product product_2 = new Product();
        product_2.setId(2L);
        product_2.setProductName("Adizero");
        product_2.setPrice(200000);
        product_2.setPhoto("Mock photo Adizero".getBytes());

        productList.add(product_1);
        productList.add(product_2);
    }

    @Test
    public void findAllTest() throws Exception {

        //given
        given(service.finAll()).willReturn(productList);

        //when
        ResultActions response = mockMvc.perform(get("/producto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productList)));

        //Then
        for (int i = 0; i < productList.size(); i++){
            Product currentProduct = productList.get(i);
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$["+i+"].id").value(currentProduct.getId().intValue()))
                    .andExpect(jsonPath("$["+i+"].productName").value(currentProduct.getProductName()))
                    .andExpect(jsonPath("$["+i+"].price").value(currentProduct.getPrice()));
        }
    }

    @Test
    public void findByIdTest() throws Exception {

        Long productId = 1L;
        Product productToFind = new Product();

        //given
        given(service.finById(anyLong())).willReturn(Optional.of(productToFind));

        Optional<Product> result = service.finById(productId);

        //when
        ResultActions response = mockMvc.perform(get("/producto/ver/{id}",productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(result.get().getId())));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(result.get().getId()));
    }

    @Test
    public void TestFindId2() throws Exception {

        Long productId = 1L;
        Product productToFind = new Product();

        for(int i = 0; i < productList.size(); i++){
            Product productToFor = productList.get(i);
            if (productToFor.getId().equals(productId)){
                productToFind = productToFor;
                break;
            }

        }
        given(service.finById(anyLong())).willReturn(Optional.of(productToFind));

        ResultActions response = mockMvc.perform(get("/producto/ver/{id}",productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productToFind)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productToFind.getId().intValue()))
                .andExpect(jsonPath("$.productName").value(productToFind.getProductName()))
                .andExpect(jsonPath("$.price").value(productToFind.getPrice()));
    }

    @Test
    public void findProduct() throws Exception {

            String productFindName = "SuperStar";
            Product productToFind = new Product();
            for (int i = 0; i < productList.size(); i++){
                Product product = productList.get(i);
                if(product.getProductName().equalsIgnoreCase(productFindName)){
                    productToFind = product;
                    break;
                }
            }

        given(service.findByProductNameContainingIgnoreCase(anyString())).willReturn(Arrays.asList(productToFind));

        ResultActions response = mockMvc.perform(post("/producto/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                //La (\) indica al JSON que el elemento va:Ejemplo
                // "productName" : "Aziero"
                .content("{\"productName\":\"" + productFindName + "\"}"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value(productToFind.getProductName()));
    }

    @Test
    public void saveTest() throws Exception{

        Product newProduct = new Product();
        newProduct.setId(3L);
        newProduct.setProductName("Jordan");
        newProduct.setPrice(500000);

        given(service.save(newProduct)).willReturn(newProduct);

        ResultActions response = mockMvc.perform(post("/producto/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newProduct.getId().intValue()))
                .andExpect(jsonPath("$.productName").value(newProduct.getProductName()))
                .andExpect(jsonPath("$.price").value(newProduct.getPrice()));
    }

    @Test
    public void editTest() throws Exception{

        Long productToEdit = 2L;
        Product producttoFind = new Product();
        for(int i = 0; i < productList.size(); i++){
            Product productToFor = productList.get(i);
            if (productToFor.getId().equals(productToEdit)){
                producttoFind = productToFor;
            }
        }

        Product updatedProduct = new Product();
        updatedProduct.setId(producttoFind.getId());
        updatedProduct.setProductName("SuperStar Edit");
        updatedProduct.setPrice(50000);
        updatedProduct.setPhoto(producttoFind.getPhoto());

        //
        System.out.println("Lista antes de la actualizacion: " + productList.get(0).getProductName());
        System.out.println("Lista antes de la actualizacion: " + productList.get(1).getProductName());

        given(service.finById(anyLong())).willReturn(Optional.of(producttoFind));

        given(service.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/producto/editar/{id}", productToEdit)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)));

        //
        System.out.println("Lista despues de la actualizacion: " + productList.get(0).getProductName());
        System.out.println("Lista despues de la actualizacion: " + productList.get(1).getProductName());

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(updatedProduct.getId()))
                .andExpect(jsonPath("$.productName").value(updatedProduct.getProductName()));
    }

    @Test
    public void deleteTest() throws Exception {

        Long productToDeleted = 1L;

        willDoNothing().given(service).deleteById(anyLong());

        ResultActions response = mockMvc.perform(delete("/producto/eliminar/{id}",productToDeleted)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNoContent());
    }

}

