package cc.paukner.services;

import cc.paukner.converters.IngredientToIngredientDto;
import cc.paukner.domain.Recipe;
import cc.paukner.dtos.IngredientDto;
import cc.paukner.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DefaultIngredientService implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientDto ingredientToIngredientDto;

    public DefaultIngredientService(RecipeRepository recipeRepository, IngredientToIngredientDto ingredientToIngredientDto) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientDto = ingredientToIngredientDto;
    }

    @Override
    public IngredientDto findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            // TODO: error handling
            log.error("Recipe id {} not found", recipeId);
            return null;
        }

        Optional<IngredientDto> ingredientDtoOptional = recipeOptional.get().getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientDto::convert)
                .findFirst();
        if (ingredientDtoOptional.isEmpty()) {
            // TODO: error handling
            log.error("Ingredient id {} not found", ingredientId);
            return null;
        }

        return ingredientDtoOptional.get();
    }
}
