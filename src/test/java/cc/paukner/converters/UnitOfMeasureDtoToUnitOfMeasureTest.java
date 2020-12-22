package cc.paukner.converters;

import cc.paukner.domain.UnitOfMeasure;
import cc.paukner.dtos.UnitOfMeasureDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UnitOfMeasureDtoToUnitOfMeasureTest {

    static final String DESCRIPTION = "description";
    static final Long ID = 1L;

    UnitOfMeasureDtoToUnitOfMeasure converter;

    @Before
    public void setUp() throws Exception {
        converter = new UnitOfMeasureDtoToUnitOfMeasure();
    }

    @Test
    public void testNullParameter() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new UnitOfMeasureDto()));
    }

    @Test
    public void convert() throws Exception {
        UnitOfMeasure unitOfMeasure = converter.convert(UnitOfMeasureDto.builder().id(ID).description(DESCRIPTION).build());

        assertNotNull(unitOfMeasure);
        assertEquals(ID, unitOfMeasure.getId());
        assertEquals(DESCRIPTION, unitOfMeasure.getDescription());
    }
}
