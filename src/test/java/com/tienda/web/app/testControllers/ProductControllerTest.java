package com.tienda.web.app.testControllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.web.app.models.entity.Product;
import com.tienda.web.app.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

@SpringBootTest //Carga la aplicacion Spring Boot completa, Permite probar la aplicacion
@AutoConfigureMockMvc //Configura autom. el bean MockMvc, simula solicitudes HTTP
@ExtendWith(MockitoExtension.class) //habilita el soporte de Mockito. simula objetos @MockBean
@TestPropertySource(locations = "classpath:application-test.properties") //Usa la clase referenciada
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc; //Instancia de MockMvc usada para simular peticion HTTP

    @MockBean
    private ProductService service; //Servicio a utiliza para la prueba,
                                    // se usa MockBean para hacer la simulacion

    @Autowired
    private ObjectMapper objectMapper; //Convierte objetos Java en JSON y viceversa,
                                        // se inyecta automaticamente por Spring

    private Product product;
    
    private List<Product> productList;

    @BeforeEach
    public void productTest(){
//        this.product = new Product();
//
//        product.setId(1L);
//        product.setProductName("AllStart");
//        product.setPrice(200000);

        this.productList = new ArrayList<>();

        Product product1 = new Product();
        product1.setId(1L);
        product1.setProductName("SuperStart");
        product1.setPrice(200000);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setProductName("Adizero");
        product2.setPrice(250000);

        productList.add(product1);
        productList.add(product2);
    }

    @Test
    public void testFindALL() throws Exception {
        //given
//        List<Product> productList = Arrays.asList(product);
        given(service.finAll()).willReturn(productList);

        //when
        ResultActions response = mockMvc.perform(get("/producto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productList)));

        //then

        //Ciclo for en donde se incluye el response para asi recorrer la lista y mostrarlos
        for (int i = 0; i < productList.size(); i++) {
            Product currentProduct = productList.get(i);
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(productList.size()))
                    .andExpect(jsonPath("$[" + i + "].id").value(currentProduct.getId().intValue()))
                    .andExpect(jsonPath("$[" + i + "].productName").value(currentProduct.getProductName()))
                    .andExpect(jsonPath("$[" + i + "].price").value(currentProduct.getPrice()));
        }
    }

    @Test
    public void testFindById() throws Exception {
        //given
        Long productId = 1L;
        Product productToFind = null;
        for (Product product : productList){
            if(product.getId().equals(productId)){
                productToFind = product;
                break;
            }
        }
        given(service.finById(anyLong())).willReturn(Optional.ofNullable(productToFind));
        //when

        ResultActions response = mockMvc.perform(get("/producto/ver/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productList)));
        //then

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productToFind.getId()))
                .andExpect(jsonPath("$.productName").value(productToFind.getProductName()))
                .andExpect(jsonPath("$.price").value(productToFind.getPrice()));

    }

    @Test //anotacion para ejecutar el Test, Metodo con la logica del Test
    public void testSaveProduct() throws Exception {
//        given
//        Se crea el producto
        Product product = new Product();

        product.setId(1L);
        product.setProductName("AllStart");
        product.setPrice(200000);

        //given configura el comportamiento del metodo esperardo "save" del servicio
        //se espera que cualquier invocación de service.save devuelva el mismo objeto que se le pasa.
        given(service.save(any(Product.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        //Se utiliza mockMvc para simular una solicitud HTTP, se pasa el objeto como objeto JSON
        ResultActions response = mockMvc.perform(post("/producto/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        //then
        //Se verifica que la respuesta tenga un estado
        //Se usa jsonPath para verificar que el JSON de respuesta contenga los campos/valores esperados
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName", is(product.getProductName())))
                .andExpect(jsonPath("$.price", is(product.getPrice())));
    }

    @Test
    public void testEditProduct() throws Exception {
        //give
        Long productId = 2L;
        Product productToFind =  null;
        for (Product product : productList){
            if(product.getId().equals(productId)){
                productToFind = product;
                break;
            }
        }

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setProductName("Converse");
        updatedProduct.setPrice(100000);

        given(service.finById(anyLong())).willReturn(Optional.ofNullable(productToFind));
        //willAnswer control detallado de un metodo.
        //Expresion lamda invocation, devuelve el primer argumento que se paso en el save "Product"
        //Devuelve el mismo producto pero con su respectiva modificacion
        given(service.save(any(Product.class))).willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/producto/editar/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(updatedProduct.getId()))
                .andExpect(jsonPath("$.productName").value(updatedProduct.getProductName()))
                .andExpect(jsonPath("$.price").value(updatedProduct.getPrice()));
    }

    @Test
    public void testDeleteProduct() throws Exception {

        Long productToDeletedId = 1L;
        //given
        willDoNothing().given(service).deleteById(productToDeletedId);

        //when
        ResultActions response = mockMvc.perform(delete("/producto/eliminar/{id}", productToDeletedId));

        //then
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindProduct () throws Exception {
        //give
        String productName = "Adizero";
        Product productToFind = new Product();

        productToFind.setProductName(productName);
        List<Product> productList = Arrays.asList(productToFind);

        given(service.findByProductNameContainingIgnoreCase(anyString())).willReturn(productList);

        //when
        ResultActions response = mockMvc.perform(post("/producto/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productName\":\"" + productName + "\"}"));

        //then

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value(productName));
    }
}
