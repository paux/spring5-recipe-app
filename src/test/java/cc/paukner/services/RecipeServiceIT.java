package cc.paukner.services;

import cc.paukner.converters.RecipeDtoToRecipe;
import cc.paukner.converters.RecipeToRecipeDto;
import cc.paukner.domain.Recipe;
import cc.paukner.dtos.RecipeDto;
import cc.paukner.repositories.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
//won't work, too lightweight//@DataJpaTest
@SpringBootTest // whole Spring Boot context
public class RecipeServiceIT {

    public static final String NEW_DESCRIPTION = "New description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeDtoToRecipe recipeDtoToRecipe;

    @Autowired
    RecipeToRecipeDto recipeToRecipeDto;

    @Transactional
    @Test
    public void testSaveDescription() throws Exception {
        // Behavior Driven Development
        // given
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe testRecipe = recipes.iterator().next();
        RecipeDto testRecipeDto = recipeToRecipeDto.convert(testRecipe);

        assertNotNull(testRecipeDto);

        // when
        testRecipeDto.setDescription(NEW_DESCRIPTION);
        RecipeDto savedRecipeDto = recipeService.saveRecipeDto(testRecipeDto);

        // then
        assertEquals(NEW_DESCRIPTION, savedRecipeDto.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeDto.getId());
        assertEquals(testRecipe.getCategories().size(), savedRecipeDto.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedRecipeDto.getIngredients().size());
    }
}
