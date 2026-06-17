import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    private final Service service = new Service();

    @Test
    void testSimpleDiscount() {
        assertEquals(90.0, service.applyDiscount(100.0, 10.0));
    }

    @ParameterizedTest
    @ValueSource(ints = {17, 18, 31, 121})
    void IsItCorrectAge(int age) {
        assertTrue(service.verifyAge(age), "age must be >= 18 and <= 120");
    }

    @ParameterizedTest
    @CsvSource({
            "100, 10, 90",
            "200, 25, 150",
            "50, 50, 25",
            "100, 0, 100"
    })
    void applyDiscountReturnsCorrectPrice(double price, double discount, double expectedPrice) {
        assertEquals(expectedPrice, service.applyDiscount(price, discount));
    }

    @TestFactory
    Collection<DynamicTest> verifyAgeDynamic() {
        Service service = new Service();

        return Stream.of(17, 18, 25, 120, 121)
                .map(age ->
                        DynamicTest.dynamicTest("age = " + age, () -> {
                                    boolean expected = age >= 18 && age <= 120;
                                    assertEquals(expected, service.verifyAge(age));
                                })).toList();
    }


    @Test
    @Tag("validation")
    void validAge() {
        assertTrue(service.verifyAge(25));
    }

    @Test
    @Tag("validation")
    void invalidAge() {
        assertFalse(service.verifyAge(150));
    }

    @Test
    @Tag("discount")
    void discount10Percent() {
        assertEquals(90,
                service.applyDiscount(100, 10));
    }

    @Test
    @Tag("discount")
    void discount50Percent() {
        assertEquals(50,
                service.applyDiscount(100, 50));
    }

}