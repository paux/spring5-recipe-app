package cc.paukner.converters;

import cc.paukner.domain.Recipe;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class IngredientDtoToIngredientTest {

    public static final Recipe RECIPE = new Recipe();
    public static final BigDecimal AMOUNT = BigDecimal.valueOf(1);
    public static final String DESCRIPTION = "Cheeseburger";
    public static final Long ID = 1L;
    public static final Long UNIT_OF_MEASURE_ID = 2L;

    IngredientDtoToIngredient converter;

    @Before
    public void setUp() throws Exception {
        converter = new IngredientDtoToIngredient(new UnitOfMeasureDtoToUnitOfMeasure());
    }

    @Test
    public void convert() {
        // TODO
    }
}
