package cc.paukner.controllers;

import cc.paukner.dtos.IngredientDto;
import cc.paukner.dtos.RecipeDto;
import cc.paukner.services.IngredientService;
import cc.paukner.services.RecipeService;
import cc.paukner.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IngredientControllerTest {

    IngredientController ingredientController;

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientController = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    public void listIngredients() throws Exception {
        when(recipeService.findDtoById(anyLong())).thenReturn(new RecipeDto());

        mockMvc.perform(get("/recipes/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredients/index"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findDtoById(anyLong());
    }

    @Test
    public void showIngredient() throws Exception {
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(new IngredientDto());

        mockMvc.perform(get("/recipes/1/ingredients/2/details"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredients/details"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    public void editIngredient() throws Exception {
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(new IngredientDto());
        when(unitOfMeasureService.listAll()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipes/1/ingredients/2/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes/ingredients/edit"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("allUnitOfMeasures"));
    }

    @Test
    public void saveIngredient() throws Exception {
        when(ingredientService.saveIngredientDto(any())).thenReturn(IngredientDto.builder().id(3L).recipeId(2L).build());

        mockMvc.perform(post("/recipes/2/ingredients/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "dummy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipes/2/ingredients/3/details"));
    }
}
