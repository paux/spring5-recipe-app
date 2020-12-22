package cc.paukner.converters;

import cc.paukner.dtos.RecipeDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RecipeDtoToRecipeTest {

    RecipeDtoToRecipe converter;

    @Before
    public void setUp() throws Exception {
        converter = new RecipeDtoToRecipe(new CategoryDtoToCategory(),
                new IngredientDtoToIngredient(new UnitOfMeasureDtoToUnitOfMeasure()),
                new NotesDtoToNotes());
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new RecipeDto()));
    }

    @Test
    public void convert() {
        // TODO: not shown in video
    }
}
