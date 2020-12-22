package cc.paukner.converters;

import cc.paukner.domain.Category;
import cc.paukner.domain.Difficulty;
import cc.paukner.domain.Ingredient;
import cc.paukner.domain.Notes;
import cc.paukner.domain.Recipe;
import cc.paukner.dtos.CategoryDto;
import cc.paukner.dtos.IngredientDto;
import cc.paukner.dtos.RecipeDto;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RecipeToRecipeDtoTest {

    public static final Long RECIPE_ID = 1L;
    public static final Integer COOK_TIME = 5;
    public static final Integer PREP_TIME = 7;
    public static final String DESCRIPTION = "Description";
    public static final String DIRECTIONS = "Directions";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;
    public static final Integer SERVINGS = 3;
    public static final String SOURCE = "Source";
    public static final String URL = "http://host";
    public static final Long CAT_ID_1 = 1L;
    public static final Long CAT_ID_2 = 2L;
    public static final Long INGRED_ID_1 = 3L;
    public static final Long INGRED_ID_2 = 4L;
    public static final Long NOTES_ID = 9L;

    RecipeToRecipeDto converter;

    @Before
    public void setUp() throws Exception {
        converter = new RecipeToRecipeDto(new CategoryToCategoryDto(),
                new IngredientToIngredientDto(new UnitOfMeasureToUnitOfMeasureDto()),
                new NotesToNotesDto());
    }

    @Test
    public void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new Recipe()));
    }

    @Test
    public void convert() {
        Recipe recipe = Recipe.builder()
                .id(RECIPE_ID)
                .cookTime(COOK_TIME)
                .prepTime(PREP_TIME)
                .description(DESCRIPTION)
                .difficulty(DIFFICULTY)
                .directions(DIRECTIONS)
                .servings(SERVINGS)
                .source(SOURCE)
                .url(URL)
                .notes(Notes.builder().id(NOTES_ID).build())
                .categories(Set.of(
                        Category.builder().id(CAT_ID_1).build(),
                        Category.builder().id(CAT_ID_2).build()
                ))
                .ingredients(Set.of(
                        Ingredient.builder().id(INGRED_ID_1).build(),
                        Ingredient.builder().id(INGRED_ID_2).build()
                ))
                .build();

        RecipeDto recipeDto = converter.convert(recipe);

        assertNotNull(recipeDto);
        assertEquals(RECIPE_ID, recipeDto.getId());
        assertEquals(COOK_TIME, recipeDto.getCookTime());
        assertEquals(PREP_TIME, recipeDto.getPrepTime());
        assertEquals(DESCRIPTION, recipeDto.getDescription());
        assertEquals(DIFFICULTY, recipeDto.getDifficulty());
        assertEquals(DIRECTIONS, recipeDto.getDirections());
        assertEquals(SERVINGS, recipeDto.getServings());
        assertEquals(SOURCE, recipeDto.getSource());
        assertEquals(URL, recipeDto.getUrl());
        assertEquals(2, recipeDto.getCategories().size());
        // Not shown in video, but I did it anyway :)
        assertThat(recipeDto.getCategories()).extracting(CategoryDto::getId).containsExactly(CAT_ID_1, CAT_ID_2);
        assertEquals(2, recipeDto.getIngredients().size());
        assertThat(recipeDto.getIngredients()).extracting(IngredientDto::getId).containsExactlyInAnyOrder(INGRED_ID_1, INGRED_ID_2);
    }
}
