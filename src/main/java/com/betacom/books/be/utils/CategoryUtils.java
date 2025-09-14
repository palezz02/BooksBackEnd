package com.betacom.books.be.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.CategoryDTO;
import com.betacom.books.be.models.Category;

public final class CategoryUtils {

	private CategoryUtils() {	}

	public static CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }


    public static List<CategoryDTO> toDTOList(List<Category> categories) {
        if (categories == null) {
            return Collections.emptyList();
        }
        return categories.stream()
                          .map(CategoryUtils::toDTO)
                          .collect(Collectors.toList());
    }
}
