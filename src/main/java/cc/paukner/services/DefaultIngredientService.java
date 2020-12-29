package cc.paukner.services;

import cc.paukner.converters.IngredientDtoToIngredient;
import cc.paukner.converters.IngredientToIngredientDto;
import cc.paukner.domain.Ingredient;
import cc.paukner.domain.Recipe;
import cc.paukner.dtos.IngredientDto;
import cc.paukner.repositories.RecipeRepository;
import cc.paukner.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class DefaultIngredientService implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientToIngredientDto ingredientToIngredientDto;
    private final IngredientDtoToIngredient ingredientDtoToIngredient;

    public DefaultIngredientService(RecipeRepository recipeRepository,
                                    UnitOfMeasureRepository unitOfMeasureRepository,
                                    IngredientToIngredientDto ingredientToIngredientDto, IngredientDtoToIngredient ingredientDtoToIngredient) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientToIngredientDto = ingredientToIngredientDto;
        this.ingredientDtoToIngredient = ingredientDtoToIngredient;
    }

    @Override
    public IngredientDto findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            // TODO: error handling
            log.error("Recipe id {} not found", recipeId);
            return new IngredientDto();
        }

        Optional<IngredientDto> ingredientDtoOptional = recipeOptional.get().getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientDto::convert)
                .findFirst();
        if (ingredientDtoOptional.isEmpty()) {
            // TODO: error handling
            log.error("Ingredient id {} not found", ingredientId);
            return new IngredientDto();
        }

        return ingredientDtoOptional.get();
    }

    @Override
    @Transactional
    public IngredientDto saveIngredientDto(IngredientDto ingredientDto) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientDto.getRecipeId());
        if (recipeOptional.isEmpty()) {
            log.error("Recipe id {} not found", ingredientDto.getRecipeId());
            // TODO: throw error if not found
            return new IngredientDto();
        }

        Recipe recipe = recipeOptional.get();
        // If already exists, update, otherwise create
        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientDto.getId()))
                .findFirst();
        if (ingredientOptional.isPresent()) {
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(ingredientDto.getDescription());
            ingredientFound.setAmount(ingredientDto.getAmount());
            ingredientFound.setUnitOfMeasure(unitOfMeasureRepository.findById(ingredientDto.getUnitOfMeasure().getId())
                    // TODO: make more elegant
                    .orElseThrow(() -> new RuntimeException("Unit of measure not found"))
            );
        } else {
            // new ingredient
            recipe.setIngredients(Set.of(Objects.requireNonNull(ingredientDtoToIngredient.convert(ingredientDto))));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        // TODO: check for fail
        return ingredientToIngredientDto.convert(savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientDto.getId()))
                .findFirst()
                .get());
    }
}
