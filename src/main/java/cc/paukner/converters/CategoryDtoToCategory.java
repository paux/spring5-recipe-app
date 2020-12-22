package cc.paukner.converters;

import cc.paukner.domain.Category;
import cc.paukner.dtos.CategoryDto;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoToCategory implements Converter<CategoryDto, Category> {

    @Synchronized
    @Nullable
    @Override
    public Category convert(CategoryDto categoryDto) {
        if (null == categoryDto) {
            return null;
        }

        return Category.builder()
                .id(categoryDto.getId())
                .description(categoryDto.getDescription())
                .build();
    }
}
