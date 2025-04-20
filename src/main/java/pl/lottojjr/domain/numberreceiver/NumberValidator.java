package pl.lottojjr.domain.numberreceiver;

import java.util.Set;

public class NumberValidator {
    private static final Integer MIN_NUMBER = 1 ;
    private static final Integer MAX_NUMBER = 99;
    private static final Integer AMOUNT_OF_NUMBERS = 6;

    public void validateNumbers(Set<Integer> numberToValidate) {
        if (numberToValidate.stream()
                .filter(number -> number >= MIN_NUMBER)
                .filter(number -> number <= MAX_NUMBER)
                .count() != AMOUNT_OF_NUMBERS) {
            throw new IllegalArgumentException("Incorrect number or range of numbers.");
        }
    }
}
