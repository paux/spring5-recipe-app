package cc.paukner.converters;

import cc.paukner.domain.Category;
import cc.paukner.dtos.CategoryDto;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryDto implements Converter<Category, CategoryDto> {

    @Synchronized
    @Nullable
    @Override
    public CategoryDto convert(Category category) {
        if (null == category) {
            return null;
        }

        return CategoryDto.builder()
                .id(category.getId())
                .description(category.getDescription())
                .build();
    }
}
