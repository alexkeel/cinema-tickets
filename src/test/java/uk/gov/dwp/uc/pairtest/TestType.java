package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.Type;

import static org.junit.Assert.assertEquals;

public class TestType
{
    @Test
    public void testTypeValues() {
        assertEquals(Type.ADULT.getPrice(), 20);
        assertEquals(Type.CHILD.getPrice(), 10);
        assertEquals(Type.INFANT.getPrice(), 0);
    }
}
