package cc.paukner.controllers;

import cc.paukner.services.IngredientService;
import cc.paukner.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    @RequestMapping("/recipes/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug("Getting ingredients of recipe id " + recipeId);
        // using dto to avoid lazy load errors in Thymeleaf
        model.addAttribute("recipe", recipeService.findDtoById(Long.valueOf(recipeId)));
        return "recipes/ingredients/index";
    }

    @GetMapping
    @RequestMapping("/recipes/{recipeId}/ingredients/{id}/details")
    public String showIngredient(@PathVariable String recipeId,
                                       @PathVariable String id,
                                       Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        return "recipes/ingredients/details";
    }
}
