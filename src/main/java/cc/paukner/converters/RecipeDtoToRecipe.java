package cc.paukner.converters;

import cc.paukner.domain.Recipe;
import cc.paukner.dtos.RecipeDto;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class RecipeDtoToRecipe implements Converter<RecipeDto, Recipe> {

    private final CategoryDtoToCategory categoryConverter;
    private final IngredientDtoToIngredient ingredientConverter;
    private final NotesDtoToNotes notesConverter;

    public RecipeDtoToRecipe(CategoryDtoToCategory categoryConverter, IngredientDtoToIngredient ingredientConverter, NotesDtoToNotes notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeDto recipeDto) {
        if (null == recipeDto) {
            return null;
        }

        Recipe recipe = Recipe.builder()
                .id(recipeDto.getId())
                .cookTime(recipeDto.getCookTime())
                .prepTime(recipeDto.getPrepTime())
                .description(recipeDto.getDescription())
                .difficulty(recipeDto.getDifficulty())
                .directions(recipeDto.getDirections())
                .servings(recipeDto.getServings())
                .source(recipeDto.getSource())
                .url(recipeDto.getUrl())
                .notes(notesConverter.convert(recipeDto.getNotes()))
                .build();

        if (recipeDto.getCategories() != null && !recipeDto.getCategories().isEmpty()) {
            recipe.setCategories(new HashSet<>());
            recipeDto.getCategories().forEach(categoryDto -> recipe.getCategories().add(categoryConverter.convert(categoryDto)));
        }
        if (recipeDto.getIngredients() != null && !recipeDto.getIngredients().isEmpty()) {
            recipe.setIngredients(new HashSet<>());
            recipeDto.getIngredients().forEach(ingredientDto -> recipe.getIngredients().add(ingredientConverter.convert(ingredientDto)));
        }

        return recipe;
    }
}
