package cc.paukner.services;

import cc.paukner.converters.UnitOfMeasureToUnitOfMeasureDto;
import cc.paukner.domain.UnitOfMeasure;
import cc.paukner.dtos.UnitOfMeasureDto;
import cc.paukner.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultUnitOfMeasureServiceTest {

    UnitOfMeasureService unitOfMeasureService;
    UnitOfMeasureToUnitOfMeasureDto unitOfMeasureToUnitOfMeasureDto = new UnitOfMeasureToUnitOfMeasureDto();

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        unitOfMeasureService = new DefaultUnitOfMeasureService(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureDto);
    }

    @Test
    public void listAll() {
        when(unitOfMeasureRepository.findAll()).thenReturn(Set.of(
                UnitOfMeasure.builder().id(1L).build(),
                UnitOfMeasure.builder().id(2L).build()
        ));

        Set<UnitOfMeasureDto> allDtos = unitOfMeasureService.listAll();

        assertEquals(2, allDtos.size());
        verify(unitOfMeasureRepository).findAll();
    }
}
