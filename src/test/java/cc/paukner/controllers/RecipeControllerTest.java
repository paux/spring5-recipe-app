package cc.paukner.controllers;

import cc.paukner.domain.Recipe;
import cc.paukner.dtos.RecipeDto;
import cc.paukner.exceptions.NotFoundException;
import cc.paukner.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class RecipeControllerTest {

    RecipeController recipeController;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeController = new RecipeController(recipeService);
        // good for Spring MVC controllers
        // With webAppContextSetup that'd no longer be a unit test
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void testMockMvc() throws Exception {
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/index"));
    }

    @Test
    public void listRecipes() {
        when(recipeService.getAllRecipes()).thenReturn(Set.of(Recipe.builder().description("one").build(),
                Recipe.builder().description("two").build()));
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        String view = recipeController.listRecipes(model);

        assertEquals("recipes/index", view);
        verify(recipeService, times(1)).getAllRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        Set<Recipe> returnedRecipes = argumentCaptor.getValue();
        assertEquals(2, returnedRecipes.size());
    }

    @Test
    public void getRecipe() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
        when(recipeService.findById(anyLong())).thenReturn(Recipe.builder().id(1L).build());

        mockMvc.perform(get("/recipes/1/details"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/details"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void getRecipeNotFound() throws Exception {
        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipes/1/details"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404"));
    }

    @Test
    public void getRecipe_IdInvalidNumber() throws Exception {
        mockMvc.perform(get("/recipes/invalid/details"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400"));
    }

    @Test
    public void getNewRecipe() throws Exception {
        mockMvc.perform(get("/recipes/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/edit"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void postNewRecipeForm() throws Exception {
        when(recipeService.saveRecipeDto(any())).thenReturn(RecipeDto.builder().id(2L).build());

        mockMvc.perform(post("/recipes/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some values")
                .param("directions", "some values")
        ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipes/2/details"));
    }

    @Test
    public void postNewRecipeForm_validationFail() throws Exception {
        when(recipeService.saveRecipeDto(any())).thenReturn(RecipeDto.builder().id(2L).build());

        mockMvc.perform(post("/recipes/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("cookTime", "3000")
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipes/edit"));
    }

    @Test
    public void getUpdateView() throws Exception {
        when(recipeService.findDtoById(anyLong())).thenReturn(RecipeDto.builder().id(2L).build());

        mockMvc.perform(get("/recipes/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/edit"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void deleteAction() throws Exception {
        mockMvc.perform(get("/recipes/1/delete")) // REST has got DELETE, but HTML forms cannot do that
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipes"));

        verify(recipeService).deleteById(anyLong());
    }
}
