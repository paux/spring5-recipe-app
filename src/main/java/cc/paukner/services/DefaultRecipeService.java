package cc.paukner.services;

import cc.paukner.converters.RecipeDtoToRecipe;
import cc.paukner.converters.RecipeToRecipeDto;
import cc.paukner.domain.Recipe;
import cc.paukner.dtos.RecipeDto;
import cc.paukner.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class DefaultRecipeService implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeDtoToRecipe recipeDtoToRecipe;
    private final RecipeToRecipeDto recipeToRecipeDto;

    public DefaultRecipeService(RecipeRepository recipeRepository, RecipeDtoToRecipe recipeDtoToRecipe, RecipeToRecipeDto recipeToRecipeDto) {
        this.recipeRepository = recipeRepository;
        this.recipeDtoToRecipe = recipeDtoToRecipe;
        this.recipeToRecipeDto = recipeToRecipeDto;
    }

    @Override
    public Set<Recipe> getAllRecipes() {
        log.debug("I'm in the service, getting all recipes");
        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (!recipeOptional.isPresent()) {
            throw new RuntimeException("Recipe not found");
        }
        return recipeOptional.get();
    }


    @Override
    @Transactional // to avoid bumping against lazily loaded properties
    public RecipeDto findDtoById(Long id) {
        return recipeToRecipeDto.convert(findById(id));
    }

    @Override
    @Transactional
    public RecipeDto saveRecipeDto(RecipeDto recipeDto) {
        if (null == recipeDto) {
            throw new RuntimeException("Cannot persist null object");
        }
        Recipe savedRecipe = recipeRepository.save(Objects.requireNonNull(recipeDtoToRecipe.convert(recipeDto)));
        log.debug("Saved recipe id: " + savedRecipe.getId());
        return recipeToRecipeDto.convert(savedRecipe);
    }
}
