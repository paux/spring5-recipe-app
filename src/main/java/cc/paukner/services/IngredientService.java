package cc.paukner.services;

import cc.paukner.dtos.IngredientDto;

public interface IngredientService {

    IngredientDto findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientDto saveIngredientDto(IngredientDto ingredientDto);

    void deleteIngredientById(Long id);
}
