package com.betacom.books.be.categoryTest;

import com.betacom.books.be.dto.CategoryDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.Category;
import com.betacom.books.be.repositories.ICategoryRepository;
import com.betacom.books.be.requests.CategoryReq;
import com.betacom.books.be.services.implementations.CategoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private CategoryImpl categoryService;

    private CategoryReq validCategoryReq;
    private Category validCategory;
    private CategoryDTO validCategoryDTO;

    @BeforeEach
    public void setUp() {
        validCategoryReq = new CategoryReq(1, "FICTION");
        validCategory = new Category();
        validCategory.setId(1);
        validCategory.setName("FICTION");
        validCategory.setBooks(new ArrayList<>());
        validCategoryDTO = new CategoryDTO(1, "FICTION");
    }

    // --- CREATE Method Tests ---

    @Test
    @DisplayName("Create Category: Valid data should save successfully")
    void create_ValidData_SavesSuccessfully() throws BooksException {
        when(categoryRepository.findByName(validCategoryReq.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(validCategory);

        CategoryDTO result = categoryService.create(validCategoryReq);

        assertNotNull(result);
        assertEquals(validCategoryDTO.getName(), result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Create Category: Null or empty name should throw exception")
    void create_NullName_ThrowsException() {
        CategoryReq req = new CategoryReq(null, null);

        BooksException thrown = assertThrows(BooksException.class, () -> categoryService.create(req));
        assertEquals("Category name cannot be null or empty.", thrown.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Create Category: Existing name should throw exception")
    void create_ExistingName_ThrowsException() {
        when(categoryRepository.findByName(validCategoryReq.getName())).thenReturn(Optional.of(validCategory));

        BooksException thrown = assertThrows(BooksException.class, () -> categoryService.create(validCategoryReq));
        assertEquals("Category with name: 'FICTION' already exists.", thrown.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    // --- UPDATE Method Tests ---

    @Test
    @DisplayName("Update Category: Valid data should save changes")
    void update_ValidData_SavesSuccessfully() throws BooksException {
        validCategoryReq.setName("SCI-FI");
        when(categoryRepository.findById(validCategoryReq.getId())).thenReturn(Optional.of(validCategory));
        when(categoryRepository.findByName(validCategoryReq.getName())).thenReturn(Optional.empty());

        categoryService.update(validCategoryReq);

        verify(categoryRepository, times(1)).findById(validCategoryReq.getId());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Update Category: Null ID should throw exception")
    void update_NullId_ThrowsException() {
        CategoryReq req = new CategoryReq(null, "Test");

        BooksException thrown = assertThrows(BooksException.class, () -> categoryService.update(req));
        assertEquals("Category ID cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("Update Category: Not found ID should throw exception")
    void update_NotFound_ThrowsException() {
        when(categoryRepository.findById(validCategoryReq.getId())).thenReturn(Optional.empty());

        BooksException thrown = assertThrows(BooksException.class, () -> categoryService.update(validCategoryReq));
        assertEquals("Category with ID 1 not found.", thrown.getMessage());
    }

    @Test
    @DisplayName("Update Category: New name already exists on different category should throw exception")
    void update_NewNameAlreadyExists_ThrowsException() {
        Category existingCategory = new Category();
        existingCategory.setId(2);
        existingCategory.setName("SCI-FI");
        validCategoryReq.setName("SCI-FI");

        when(categoryRepository.findById(validCategoryReq.getId())).thenReturn(Optional.of(validCategory));
        when(categoryRepository.findByName(validCategoryReq.getName())).thenReturn(Optional.of(existingCategory));

        BooksException thrown = assertThrows(BooksException.class, () -> categoryService.update(validCategoryReq));
        assertEquals("Category with name 'SCI-FI' already exists.", thrown.getMessage());
    }

    // --- DELETE Method Tests ---

    @Test
    @DisplayName("Delete Category: Valid ID should delete successfully")
    void delete_ValidId_DeletesSuccessfully() throws BooksException {
        when(categoryRepository.findById(validCategoryReq.getId())).thenReturn(Optional.of(validCategory));

        categoryService.delete(validCategoryReq);

        verify(categoryRepository, times(1)).delete(validCategory);
    }
    
    @Test
    @DisplayName("Delete Category: Null ID should throw exception")
    void delete_NullId_ThrowsException() {
        CategoryReq req = new CategoryReq(null, null);

        BooksException thrown = assertThrows(BooksException.class, () -> categoryService.delete(req));
        assertEquals("Category ID cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("Delete Category: Not found ID should throw exception")
    void delete_NotFound_ThrowsException() {
        when(categoryRepository.findById(validCategoryReq.getId())).thenReturn(Optional.empty());

        BooksException thrown = assertThrows(BooksException.class, () -> categoryService.delete(validCategoryReq));
        assertEquals("Category with ID 1 not found.", thrown.getMessage());
    }

    // --- GET BY ID Method Tests ---

    @Test
    @DisplayName("Get By Id: Valid ID should return correct DTO")
    void getById_ValidId_ReturnsDTO() throws BooksException {
        when(categoryRepository.findById(validCategoryReq.getId())).thenReturn(Optional.of(validCategory));

        CategoryDTO result = categoryService.getById(validCategoryReq.getId());

        assertNotNull(result);
        assertEquals(validCategoryDTO.getName(), result.getName());
    }

    @Test
    @DisplayName("Get By Id: Not found ID should throw exception")
    void getById_NotFound_ThrowsException() {
        when(categoryRepository.findById(validCategoryReq.getId())).thenReturn(Optional.empty());

        BooksException thrown = assertThrows(BooksException.class, () -> categoryService.getById(validCategoryReq.getId()));
        assertEquals("Category with ID 1 not found.", thrown.getMessage());
    }
    
    @Test
    @DisplayName("Get By Id: Associated with books should throw exception")
    void getById_AssociatedWithBooks_ThrowsException() {
        validCategory.setBooks(List.of(new Book())); // Simulate association with a book
        when(categoryRepository.findById(validCategoryReq.getId())).thenReturn(Optional.of(validCategory));

        BooksException thrown = assertThrows(BooksException.class, () -> categoryService.getById(validCategoryReq.getId()));
        assertEquals("Cannot delete category with ID 1 as it is associated with books.", thrown.getMessage());
    }

    // --- GET ALL Method Tests ---

    @Test
    @DisplayName("Get All Categories: Should return a list of DTOs")
    void getAll_ReturnsListOfDTOs() {
        List<Category> categoryList = List.of(validCategory, new Category());
        when(categoryRepository.findAll()).thenReturn(categoryList);

        List<CategoryDTO> result = categoryService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Get All Categories: No categories should return empty list")
    void getAll_ReturnsEmptyList() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<CategoryDTO> result = categoryService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}