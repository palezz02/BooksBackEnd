package com.betacom.books.be.categoryTest;

import com.betacom.books.be.controller.CategoryController;
import com.betacom.books.be.dto.CategoryDTO;
import com.betacom.books.be.requests.CategoryReq;
import com.betacom.books.be.services.interfaces.ICategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
    }

    private CategoryReq createCategoryReq(Integer id, String name) {
        return new CategoryReq(id, name);
    }

    private CategoryDTO createCategoryDTO(Integer id, String name) {
        return CategoryDTO.builder().id(id).name(name).build();
    }

    // --- CREATE Endpoint Tests ---

    @Test
    void create_Success_ReturnsRcTrue() throws Exception {
        // If ICategoryService.create() returns a CategoryDTO
        CategoryReq req = createCategoryReq(null, "Fiction");
        CategoryDTO createdDto = CategoryDTO.builder().id(1).name("Fiction").build();

        when(categoryService.create(any(CategoryReq.class))).thenReturn(createdDto);

        mockMvc.perform(post("/rest/category/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rc").value(true));
    }

    @Test
    @DisplayName("Create Category: Service error, returns rc=false with message")
    void create_ServiceError_ReturnsRcFalse() throws Exception {
        // Arrange
        CategoryReq req = createCategoryReq(null, "Fiction");
        doThrow(new RuntimeException("Category already exists")).when(categoryService).create(any(CategoryReq.class));

        // Act & Assert
        mockMvc.perform(post("/rest/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("Category already exists"));
    }

    // --- UPDATE Endpoint Tests ---

    @Test
    @DisplayName("Update Category: Success, returns rc=true")
    void update_Success_ReturnsRcTrue() throws Exception {
        // Arrange
        CategoryReq req = createCategoryReq(1, "Sci-Fi");
        doNothing().when(categoryService).update(any(CategoryReq.class));

        // Act & Assert
        mockMvc.perform(put("/rest/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true));

        verify(categoryService, times(1)).update(any(CategoryReq.class));
    }
    
    @Test
    @DisplayName("Update Category: Service error, returns rc=false with message")
    void update_ServiceError_ReturnsRcFalse() throws Exception {
        // Arrange
        CategoryReq req = createCategoryReq(999, "Horror");
        doThrow(new RuntimeException("Category not found")).when(categoryService).update(any(CategoryReq.class));

        // Act & Assert
        mockMvc.perform(put("/rest/category/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("Category not found"));
    }

    // --- DELETE Endpoint Tests ---

    @Test
    @DisplayName("Delete Category: Success, returns rc=true")
    void delete_Success_ReturnsRcTrue() throws Exception {
        // Arrange
        CategoryReq req = createCategoryReq(1, null);
        doNothing().when(categoryService).delete(any(CategoryReq.class));

        // Act & Assert
        mockMvc.perform(post("/rest/category/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true));

        verify(categoryService, times(1)).delete(any(CategoryReq.class));
    }

    @Test
    @DisplayName("Delete Category: Service error, returns rc=false with message")
    void delete_ServiceError_ReturnsRcFalse() throws Exception {
        // Arrange
        CategoryReq req = createCategoryReq(999, null);
        doThrow(new RuntimeException("Category not found")).when(categoryService).delete(any(CategoryReq.class));

        // Act & Assert
        mockMvc.perform(post("/rest/category/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("Category not found"));
    }

    // --- GET_BY_ID Endpoint Tests ---

    @Test
    @DisplayName("Get Category By ID: Success, returns rc=true")
    void getById_Success_ReturnsRcTrue() throws Exception {
        // Arrange
        CategoryDTO mockCategory = createCategoryDTO(1, "Fiction");
        // Mock the getById method to return a DTO when called with ID 1
        when(categoryService.getById(1)).thenReturn(mockCategory);

        // Act & Assert
        mockMvc.perform(get("/rest/category/getById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true)); // The controller logic handles setting rc=true

        // Verify the service method was called
        verify(categoryService, times(1)).getById(1);
    }
    
    @Test
    @DisplayName("Get Category By ID: Service error, returns rc=false with message")
    void getById_ServiceError_ReturnsRcFalse() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Category not found")).when(categoryService).getById(999);

        // Act & Assert
        mockMvc.perform(get("/rest/category/getById")
                        .param("id", "999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("Category not found"));
    }

    // --- GET_ALL Endpoint Tests ---

    @Test
    @DisplayName("Get All Categories: Success, returns list of DTOs")
    void getAll_Success_ReturnsListOfCategoryDTOs() throws Exception {
        // Arrange
        List<CategoryDTO> mockList = List.of(
                createCategoryDTO(1, "Fiction"),
                createCategoryDTO(2, "Non-Fiction")
        );
        when(categoryService.getAll()).thenReturn(mockList);

        // Act & Assert
        mockMvc.perform(get("/rest/category/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.dati.length()").value(2))
                .andExpect(jsonPath("$.dati[0].name").value("Fiction"))
                .andExpect(jsonPath("$.dati[1].name").value("Non-Fiction"));

        verify(categoryService, times(1)).getAll();
    }
    
    @Test
    @DisplayName("Get All Categories: Service error, returns rc=false with message")
    void getAll_ServiceError_ReturnsRcFalse() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(categoryService).getAll();

        // Act & Assert
        mockMvc.perform(get("/rest/category/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("Database error"))
                .andExpect(jsonPath("$.dati").doesNotExist());
    }

    @Test
    @DisplayName("Get All Categories: No categories found, returns empty list")
    void getAll_NoCategoriesFound_ReturnsEmptyList() throws Exception {
        // Arrange
        when(categoryService.getAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/rest/category/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.dati.length()").value(0));

        verify(categoryService, times(1)).getAll();
    }
}
