package com.tienda.web.app.testControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.web.app.models.entity.Brand;
import com.tienda.web.app.services.BrandService;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService service;

    @InjectMocks
    private ObjectMapper objectMapper;

    private List<Brand> brandList;

    @Test
    public void testFindAll() throws Exception {

        this.brandList = new ArrayList<>();

        Brand brand1 = new Brand();
        brand1.setId(1L);
        brand1.setBrandName("Adidas");
        brand1.setPhoto("Foto Prueba".getBytes());

        brandList.add(brand1);

        given(service.finAll()).willReturn(brandList);

        ResultActions response = mockMvc.perform(get("/marca")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandList)));

        for (int i = 0; i<brandList.size(); i++){
            Brand brandToFor = brandList.get(i);
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$["+i+"].id").value(brandToFor.getId()))
                    .andExpect(jsonPath("$["+i+"].brandName").value(brandToFor.getBrandName()))
                    .andExpect(jsonPath("$["+i+"].photoHashCode").value(brandToFor.getPhotoHashCode()));
        }
    }

    @Test
    public void findByIdTest() throws Exception {

        Long brandToFindId = 1L;
        this.brandList = new ArrayList<>();

        Brand brand1 = new Brand();
        brand1.setId(1L);
        brand1.setBrandName("Adidas");
        brand1.setPhoto("Foto de la marca 1".getBytes());

        Brand brand2 = new Brand();
        brand2.setId(2L);
        brand2.setBrandName("Nike");
        brand2.setPhoto("Foto de la marca 2".getBytes());

        brandList.add(brand1);
        brandList.add(brand2);

        Brand brandToFind = new Brand();
        for (int i = 0; i<brandList.size(); i++){
            Brand brandToFor = brandList.get(i);
            if(brandToFor.getId().equals(brandToFindId)){
                brandToFind = brandToFor;
                break;
            }
        }

        given(service.finbyId(anyLong())).willReturn(Optional.of(brandToFind));

        ResultActions response = mockMvc.perform(get("/marca/ver/{id}",brandToFindId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandToFind)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(brandToFind.getId()))
                .andExpect(jsonPath("$.brandName").value(brandToFind.getBrandName()))
                .andExpect(jsonPath("$.photoHashCode").value(brandToFind.getPhotoHashCode()));
    }

    @Test
    public void saveBrandTest() throws Exception {

        Brand brandToSave = new Brand();

        brandToSave.setId(1L);
        brandToSave.setBrandName("Totto");
        brandToSave.setPhoto("Foto marca save".getBytes());

        given(service.save(any(Brand.class))).willReturn(brandToSave);

        ResultActions response = mockMvc.perform(post("/marca/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandToSave)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(brandToSave.getId()))
                .andExpect(jsonPath("$.brandName").value(brandToSave.getBrandName()))
                .andExpect(jsonPath("$.photoHashCode").value(brandToSave.getPhotoHashCode()));
    }

    @Test
    public void editTest() throws Exception{

        Long brandToFindId = 2L;
        this.brandList = new ArrayList<>();

        Brand brand1 = new Brand();
        brand1.setId(1L);
        brand1.setBrandName("Adidas");
        brand1.setPhoto("Foto de la marca 1".getBytes());

        Brand brand2 = new Brand();
        brand2.setId(2L);
        brand2.setBrandName("Nike");
        brand2.setPhoto("Foto de la marca 2".getBytes());

        brandList.add(brand1);
        brandList.add(brand2);

        Brand brandToFind = new Brand();
        for (int i = 0; i<brandList.size(); i++){
            Brand brandToFor = brandList.get(i);
            if(brandToFor.getId().equals(brandToFindId)){
                brandToFind = brandToFor;
                break;
            }
        }

        Brand updatedBrand = new Brand();
        updatedBrand.setId(brandToFind.getId());
        updatedBrand.setBrandName("Totto");
        updatedBrand.setPhoto(brandToFind.getPhoto());

        System.out.println("Antes del update: " + brandList.get(0).getBrandName());
        System.out.println("Antes del update: " + brandList.get(1).getBrandName());

        given(service.finbyId(anyLong())).willReturn(Optional.of(brandToFind));

        given(service.save(any(Brand.class))).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/marca/editar/{id}", brandToFind.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBrand)));

        System.out.println("Despues del update: " + brandList.get(0).getBrandName());
        System.out.println("Despues del update: " + brandList.get(1).getBrandName());

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(updatedBrand.getId()))
                .andExpect(jsonPath("$.brandName").value(updatedBrand.getBrandName()))
                .andExpect(jsonPath("$.photoHashCode").value(updatedBrand.getPhotoHashCode()));
    }

    @Test
    public void deleteBrandTest() throws Exception {
        Long brandToDelete = 1L;

        willDoNothing().given(service).deleteById(brandToDelete);

        ResultActions response = mockMvc.perform(delete("/marca/eliminar/{id}", brandToDelete));

        response.andExpect(status().isNoContent());
    }

    @Test
    public void deleteBrandTest2() throws Exception {
        Long brandToDelete = 2L;

        this.brandList = new ArrayList<>();

        Brand brand1 = new Brand();
        brand1.setId(1L);
        brand1.setBrandName("Adidas");
        brand1.setPhoto("Foto de la marca 1".getBytes());

        Brand brand2 = new Brand();
        brand2.setId(2L);
        brand2.setBrandName("Nike");
        brand2.setPhoto("Foto de la marca 2".getBytes());

        brandList.add(brand1);
        brandList.add(brand2);

        Brand currentBrand = new Brand();

        for (int i = 0; i<brandList.size(); i++){
            Brand brandToFor = brandList.get(i);
            if(brandToFor.getId().equals(brandToDelete)){
                currentBrand = brandToFor;
                break;
            }
        }

        willDoNothing().given(service).deleteById(currentBrand.getId());

        ResultActions response = mockMvc.perform(delete("/marca/eliminar/{id}",brandToDelete)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(currentBrand)));

        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void findMarcaTest() throws Exception {
        String brandToFind = "Nike";

        this.brandList = new ArrayList<>();

        Brand brand1 = new Brand();
        brand1.setId(1L);
        brand1.setBrandName("Adidas");
        brand1.setPhoto("Foto de la marca 1".getBytes());

        Brand brand2 = new Brand();
        brand2.setId(2L);
        brand2.setBrandName("Nike");
        brand2.setPhoto("Foto de la marca 2".getBytes());

        brandList.add(brand1);
        brandList.add(brand2);

        Brand currentBrand = new Brand();

        for (int i = 0; i<brandList.size(); i++){
            Brand brandToFor = brandList.get(i);
            if (brandToFor.getBrandName().equalsIgnoreCase(brandToFind)){
                currentBrand = brandToFor;
                break;
            }
        }

        given(service.findByBrandNameContainingIgnoreCase(anyString())).willReturn(Arrays.asList(currentBrand));

        ResultActions response = mockMvc.perform(post("/marca/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brandName\":\"" + currentBrand + "\"}"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brandName").value(currentBrand.getBrandName()));
    }
}
