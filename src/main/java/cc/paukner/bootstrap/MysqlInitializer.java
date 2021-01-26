package cc.paukner.bootstrap;

import cc.paukner.domain.Category;
import cc.paukner.domain.UnitOfMeasure;
import cc.paukner.repositories.CategoryRepository;
import cc.paukner.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class MysqlInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public MysqlInitializer(CategoryRepository categoryRepository,
                            UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Context refreshed event, bootstrapping data");
        if (categoryRepository.count() == 0) {
            log.debug("Creating categories");
            createCategories();
        }
        if (unitOfMeasureRepository.count() == 0) {
            log.debug("Creating units of measure");
            createUnitsOfMeasure();
        }
    }

    private void createCategories() {
        categoryRepository.saveAll(Set.of(
                Category.builder().description("American").build(),
                Category.builder().description("Italian").build(),
                Category.builder().description("Mexican").build(),
                Category.builder().description("Fast Food").build()
        ));
    }

    private void createUnitsOfMeasure() {
        unitOfMeasureRepository.saveAll(Set.of(
                UnitOfMeasure.builder().description("Teaspoon").build(),
                UnitOfMeasure.builder().description("Tablespoon").build(),
                UnitOfMeasure.builder().description("Cup").build(),
                UnitOfMeasure.builder().description("Pinch").build(),
                UnitOfMeasure.builder().description("Ounce").build()
        ));
    }
}
