package cc.paukner.services;

import cc.paukner.converters.IngredientToIngredientDto;
import cc.paukner.converters.UnitOfMeasureToUnitOfMeasureDto;
import cc.paukner.domain.Ingredient;
import cc.paukner.domain.Recipe;
import cc.paukner.dtos.IngredientDto;
import cc.paukner.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultIngredientServiceTest {

    IngredientService ingredientService;

    @Mock
    RecipeRepository recipeRepository;

    private final IngredientToIngredientDto ingredientToIngredientDto;

    public DefaultIngredientServiceTest() {
        this.ingredientToIngredientDto = new IngredientToIngredientDto(new UnitOfMeasureToUnitOfMeasureDto());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientService = new DefaultIngredientService(recipeRepository, ingredientToIngredientDto);
    }

    @Test
    public void findByRecipeIdAndIngredientId() {
    }

    @Test
    public void findByRecipeIdAndIngredientIdHappyCase() {
        // Beware when going past setIngredients when using the builder: back ref of recipe!
        Recipe recipe = Recipe.builder().id(1L).build();
        recipe.setIngredients(Set.of(
                Ingredient.builder().id(1L).build(),
                Ingredient.builder().id(2L).build(),
                Ingredient.builder().id(3L).build()
        ));
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        IngredientDto ingredientDto = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);

        assertEquals(Long.valueOf(3L), ingredientDto.getId());
        assertEquals(Long.valueOf(1L), ingredientDto.getRecipeId());
        verify(recipeRepository).findById(anyLong());
    }
}
