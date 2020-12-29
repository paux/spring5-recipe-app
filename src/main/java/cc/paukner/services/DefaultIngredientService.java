package cc.paukner.services;

import cc.paukner.converters.IngredientDtoToIngredient;
import cc.paukner.converters.IngredientToIngredientDto;
import cc.paukner.domain.Ingredient;
import cc.paukner.domain.Recipe;
import cc.paukner.dtos.IngredientDto;
import cc.paukner.repositories.IngredientRepository;
import cc.paukner.repositories.RecipeRepository;
import cc.paukner.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class DefaultIngredientService implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientToIngredientDto ingredientToIngredientDto;
    private final IngredientDtoToIngredient ingredientDtoToIngredient;

    public DefaultIngredientService(RecipeRepository recipeRepository,
                                    UnitOfMeasureRepository unitOfMeasureRepository,
                                    IngredientRepository ingredientRepository,
                                    IngredientToIngredientDto ingredientToIngredientDto,
                                    IngredientDtoToIngredient ingredientDtoToIngredient) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientRepository = ingredientRepository;
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
            Ingredient newIngredient = ingredientDtoToIngredient.convert(ingredientDto);
            assert newIngredient != null;
            newIngredient.setRecipe(recipe);
            recipe.getIngredients().add(newIngredient);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientDto.getId()))
                .findFirst();
        // check by description
        if (savedIngredientOptional.isEmpty()) {
            // not totally safe, but best guess
            savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getDescription().equals(ingredientDto.getDescription()))
                    .filter(ingredient -> ingredient.getAmount().equals(ingredientDto.getAmount()))
                    .filter(ingredient -> ingredient.getUnitOfMeasure().getId().equals(ingredientDto.getUnitOfMeasure().getId()))
                    .findFirst();
        }
        // TODO: check for fail
        return ingredientToIngredientDto.convert(savedIngredientOptional.get());
    }

    @Override
    public void deleteIngredientById(Long id) {
        // We don't care about what recipe it belongs to
        ingredientRepository.deleteById(id);
    }
}
