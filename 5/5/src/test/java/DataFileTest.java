import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileDataTest {

    static Stream<Integer> provideNumbersFromFile() throws Exception {
        InputStream is = FileDataTest.class.getClassLoader()
                .getResourceAsStream("all_positive_numbers2.txt");
        Assumptions.assumeTrue(is != null, "couldn't find a file");
        List<Integer> numbers = new ArrayList<>();
        try (Scanner scanner = new Scanner(is)) {
            while (scanner.hasNextInt()) {
                numbers.add(scanner.nextInt());
            }
        }
        return numbers.stream();
    }

    @ParameterizedTest
    @MethodSource("provideNumbersFromFile")
    void testNumbersFromFileArePositive(int number) {
        assertTrue(number > 0, "number " + number + " isn't positive");
    }
}