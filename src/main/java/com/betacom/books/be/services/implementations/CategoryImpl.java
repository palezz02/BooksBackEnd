package com.betacom.books.be.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.CategoryDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Category;
import com.betacom.books.be.repositories.ICategoryRepository;
import com.betacom.books.be.requests.CategoryReq;
import com.betacom.books.be.services.interfaces.ICategoryService;
import com.betacom.books.be.utils.CategoryUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CategoryImpl implements ICategoryService{
	private ICategoryRepository categoryRepository;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public CategoryDTO create(CategoryReq req) throws BooksException {
		log.debug("create: ", req);

		if (req.getName() == null || req.getName().isBlank()) {
			throw new BooksException("Category name cannot be null or empty.");
		}

		Optional<Category> existingCategory = categoryRepository.findByName(req.getName().trim().toUpperCase());
		if (existingCategory.isPresent()) {
			throw new BooksException("Category with name: '" + req.getName() + "' already exists.");
		}

		Category category = new Category();
		category.setName(req.getName().trim().toUpperCase());

		categoryRepository.save(category);
		return CategoryUtils.toDTO(category);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(CategoryReq req) throws BooksException {
		log.debug("update: ", req);

		if (req.getId() == null) {
			throw new BooksException("Category ID cannot be null");
		}

		Category category = categoryRepository.findById(req.getId())
				.orElseThrow(() -> new BooksException("Category with ID " + req.getId() + " not found."));

		if (req.getName() != null) {
			Optional<Category> existingCategory = categoryRepository.findByName(req.getName().trim().toUpperCase());
			if (existingCategory.isPresent() && !existingCategory.get().getId().equals(req.getId())) {
				throw new BooksException("Category with name '" + req.getName() + "' already exists.");
			}
			category.setName(req.getName().trim().toUpperCase());
		}

		categoryRepository.save(category);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(CategoryReq req) throws BooksException {
		log.debug("delete: ", req);

		if (req.getId() == null) {
			throw new BooksException("Category ID cannot be null");
		}

		Category category = categoryRepository.findById(req.getId())
				.orElseThrow(() -> new BooksException("Category with ID " + req.getId() + " not found."));

		categoryRepository.delete(category);
	}

	@Override
	public CategoryDTO getById(Integer id) throws BooksException {
		log.debug("getById: ", id);

		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new BooksException("Category with ID " + id + " not found."));
		
	    if (!category.getBooks().isEmpty()) {
	        throw new BooksException("Cannot delete category with ID " + id + " as it is associated with books.");
	    }

		return CategoryUtils.toDTO(category);
	}

	@Override
	public List<CategoryDTO> getAll() {
		log.debug("getAll");
		List<Category> categories = categoryRepository.findAll();
		return CategoryUtils.toDTOList(categories);
	}

}
