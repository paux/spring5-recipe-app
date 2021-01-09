package cc.paukner.controllers;

import cc.paukner.dtos.RecipeDto;
import cc.paukner.exceptions.NotFoundException;
import cc.paukner.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    private static final String RECIPE_ATTRIBUTE = "recipe";
    private static final String RECIPE_EDIT_FORM = "recipes/edit";

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"", "/"})
    public String listRecipes(Model model) {
        log.debug("Listing all recipes");
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "recipes/index";
    }

    @GetMapping("/{id}/details")
    public String showById(@PathVariable String id, Model model) {
        model.addAttribute(RECIPE_ATTRIBUTE, recipeService.findById(Long.valueOf(id)));
        return "recipes/details";
    }

    @GetMapping("/new")
    public String newRecipe(Model model) {
        model.addAttribute(RECIPE_ATTRIBUTE, new RecipeDto());
        return RECIPE_EDIT_FORM;
    }

    @GetMapping("/{id}/edit")
    public String editRecipe(@PathVariable String id, Model model) {
        model.addAttribute(RECIPE_ATTRIBUTE, recipeService.findDtoById(Long.valueOf(id)));
        return RECIPE_EDIT_FORM;
    }

    @PostMapping("/save")
    public String saveRecipe(@Validated @ModelAttribute(RECIPE_ATTRIBUTE) RecipeDto recipeDto, BindingResult bindingResult) { // bind form POST parameters to dto
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
            return RECIPE_EDIT_FORM;
        }
        RecipeDto savedRecipeDto = recipeService.saveRecipeDto(recipeDto);
        return "redirect:/recipes/" + savedRecipeDto.getId() + "/details";
    }

    @GetMapping("/{id}/delete")
    public String deleteRecipeById(@PathVariable String id) {
        log.debug("Deleting id " + id);
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/recipes";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) // why is this needed? because this handler method is simply taking precedence and wouldn't throw
                                          // Note that this is already the _handler_
    @ExceptionHandler(NotFoundException.class) // the annotation on that class is ignored, so we have to repeat it
    public ModelAndView handleNotFound(Exception exception) {
        log.error("Handling not found exception. Message: " + exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }
}
