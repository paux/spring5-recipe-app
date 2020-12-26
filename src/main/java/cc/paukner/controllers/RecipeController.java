package cc.paukner.controllers;

import cc.paukner.dtos.RecipeDto;
import cc.paukner.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/"})
    public String listRecipes(Model model) {
        log.debug("Listing all recipes");
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "recipes/index";
    }

    @RequestMapping("/{id}/details")
    public String showById(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
        return "recipes/details";
    }

    @RequestMapping("/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeDto());
        return "recipes/edit";
    }

    @RequestMapping("/{id}/edit")
    public String editRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findDtoById(Long.valueOf(id)));
        return "recipes/edit";
    }

    @PostMapping
    @RequestMapping("/save")
    public String saveRecipe(@ModelAttribute RecipeDto recipeDto) { // bind form POST parameters to dto
        RecipeDto savedRecipeDto = recipeService.saveRecipeDto(recipeDto);
        return "redirect:/recipes/" + savedRecipeDto.getId() + "/details";
    }
}
