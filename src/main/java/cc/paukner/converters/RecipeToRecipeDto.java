package cc.paukner.converters;

import cc.paukner.domain.Recipe;
import cc.paukner.dtos.RecipeDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class RecipeToRecipeDto implements Converter<Recipe, RecipeDto> {

    private final CategoryToCategoryDto categoryConverter;
    private final IngredientToIngredientDto ingredientConverter;
    private final NotesToNotesDto notesConverter;

    public RecipeToRecipeDto(CategoryToCategoryDto categoryConverter, IngredientToIngredientDto ingredientConverter, NotesToNotesDto notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Override
    public RecipeDto convert(Recipe recipe) {
        if (null == recipe) {
            return null;
        }

        RecipeDto recipeDto = RecipeDto.builder()
                .id(recipe.getId())
                .cookTime(recipe.getCookTime())
                .prepTime(recipe.getPrepTime())
                .description(recipe.getDescription())
                .difficulty(recipe.getDifficulty())
                .directions(recipe.getDirections())
                .servings(recipe.getServings())
                .source(recipe.getSource())
                .url(recipe.getUrl())
                .image(recipe.getImage()) // not needed other way round
                .notes(notesConverter.convert(recipe.getNotes()))
                .build();

        if (recipe.getCategories() != null && !recipe.getCategories().isEmpty()) {
            recipeDto.setCategories(new HashSet<>());
            recipe.getCategories().forEach(category -> recipeDto.getCategories().add(categoryConverter.convert(category)));
        }
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            recipeDto.setIngredients(new HashSet<>());
            recipe.getIngredients().forEach(ingredient -> recipeDto.getIngredients().add(ingredientConverter.convert(ingredient)));
        }

        return recipeDto;
    }
}
