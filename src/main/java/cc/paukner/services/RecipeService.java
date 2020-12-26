package cc.paukner.services;

import cc.paukner.domain.Recipe;
import cc.paukner.dtos.RecipeDto;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getAllRecipes();

    Recipe findById(Long id);

    RecipeDto findDtoById(Long id);

    RecipeDto saveRecipeDto(RecipeDto recipeDto);

}
