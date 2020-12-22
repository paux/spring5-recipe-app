package cc.paukner.converters;

import cc.paukner.domain.Ingredient;
import cc.paukner.dtos.IngredientDto;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientDtoToIngredient implements Converter<IngredientDto, Ingredient> {

    private final UnitOfMeasureDtoToUnitOfMeasure unitOfMeasureDtoToUnitOfMeasure;

    public IngredientDtoToIngredient(UnitOfMeasureDtoToUnitOfMeasure unitOfMeasureDtoToUnitOfMeasure) {
        this.unitOfMeasureDtoToUnitOfMeasure = unitOfMeasureDtoToUnitOfMeasure;
    }

    @Synchronized // thread-safe
    @Nullable
    @Override
    public Ingredient convert(IngredientDto ingredientDto) {
        if (null == ingredientDto) {
            return null;
        }

        return Ingredient.builder()
                .id(ingredientDto.getId())
                .description(ingredientDto.getDescription())
                .amount(ingredientDto.getAmount())
                .unitOfMeasure(unitOfMeasureDtoToUnitOfMeasure.convert(ingredientDto.getUnitOfMeasure()))
                .build();
    }
}
