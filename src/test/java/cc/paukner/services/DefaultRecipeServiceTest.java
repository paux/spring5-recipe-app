package cc.paukner.services;

import cc.paukner.converters.RecipeDtoToRecipe;
import cc.paukner.converters.RecipeToRecipeDto;
import cc.paukner.domain.Recipe;
import cc.paukner.exceptions.NotFoundException;
import cc.paukner.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultRecipeServiceTest {

    DefaultRecipeService recipeService;

    @Mock // creates a minimal hull _pretending_ to be a full repo
            // in reality, you have to tell it what to do or return on certain requests
    RecipeRepository recipeRepository;
    @Mock
    RecipeDtoToRecipe recipeDtoToRecipe;
    @Mock
    RecipeToRecipeDto recipeToRecipeDto;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeService = new DefaultRecipeService(recipeRepository, recipeDtoToRecipe, recipeToRecipeDto); // dep injection
    }

    @Test
    public void getAllRecipes() {
        // tell the mock what to do
        when(recipeRepository.findAll()).thenReturn(Set.of(new Recipe()));

        Set<Recipe> recipes = recipeService.getAllRecipes();
        assertEquals(1, recipes.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test(expected = NotFoundException.class)
    public void getRecipeByIdNotFound() throws Exception {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        recipeService.findById(1L);
    }

    @Test
    public void getRecipeById() throws Exception {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(Recipe.builder().id(1L).build()));

        Recipe recipeReturned = recipeService.findById(1L);

        assertNotNull("Null recipe returned", recipeReturned);
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void deleteById() throws Exception {
        // nothing to mock, as return type is void:
        recipeService.deleteById(1L);

        verify(recipeRepository).deleteById(anyLong());
    }
}
