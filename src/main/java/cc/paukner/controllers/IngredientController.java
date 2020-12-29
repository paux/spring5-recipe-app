package cc.paukner.controllers;

import cc.paukner.dtos.IngredientDto;
import cc.paukner.dtos.UnitOfMeasureDto;
import cc.paukner.services.IngredientService;
import cc.paukner.services.RecipeService;
import cc.paukner.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/recipes/{recipeId}/ingredients")
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping({"", "/"})
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug("Getting ingredients of recipe id " + recipeId);
        // using dto to avoid lazy load errors in Thymeleaf
        model.addAttribute("recipe", recipeService.findDtoById(Long.valueOf(recipeId)));
        return "recipes/ingredients/index";
    }

    @GetMapping("/{id}/details")
    public String showIngredient(@PathVariable String recipeId,
                                 @PathVariable String id,
                                 Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        return "recipes/ingredients/details";
    }

    @GetMapping("/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        // TODO: fail if recipe does not exist
        recipeService.findDtoById(Long.valueOf(recipeId));
        model.addAttribute("ingredient", IngredientDto.builder().recipeId(Long.valueOf(recipeId)).unitOfMeasure(new UnitOfMeasureDto()).build());
        model.addAttribute("allUnitsOfMeasure", unitOfMeasureService.listAll());
        return "recipes/ingredients/edit";
    }

    @GetMapping("/{id}/edit")
    public String editIngredient(@PathVariable String recipeId,
                                 @PathVariable String id,
                                 Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        model.addAttribute("allUnitsOfMeasure", unitOfMeasureService.listAll());
        return "recipes/ingredients/edit";
    }

    @PostMapping("/save")
    public String saveIngredient(@ModelAttribute IngredientDto ingredientDto) {
        IngredientDto savedIngredientDto = ingredientService.saveIngredientDto(ingredientDto);
        log.debug("Saved ingredient id " + savedIngredientDto.getId() + " to recipe id " + savedIngredientDto.getRecipeId());
        return "redirect:/recipes/" + savedIngredientDto.getRecipeId() + "/ingredients/" + savedIngredientDto.getId() + "/details";
    }

    @GetMapping("/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {
        log.debug("Deleting ingredient id " + id);
        ingredientService.deleteIngredientById(Long.valueOf(id));
        return "redirect:/recipes/" + recipeId + "/ingredients";
    }
}
