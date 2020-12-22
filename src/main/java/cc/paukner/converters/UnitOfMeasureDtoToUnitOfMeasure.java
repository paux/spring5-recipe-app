package cc.paukner.converters;

import cc.paukner.domain.UnitOfMeasure;
import cc.paukner.dtos.UnitOfMeasureDto;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureDtoToUnitOfMeasure implements Converter<UnitOfMeasureDto, UnitOfMeasure> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert(UnitOfMeasureDto unitOfMeasureDto) {
        if (null == unitOfMeasureDto) {
            return null;
        }

        return UnitOfMeasure.builder()
                .id(unitOfMeasureDto.getId())
                .description(unitOfMeasureDto.getDescription())
                .build();
    }
}
