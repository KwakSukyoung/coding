package Help;

import java.util.Random;

/**
 * class for generating a random number
 */
public class randomNumber {
    /**
     * constructor
     */
    public randomNumber() {
    }

    /**
     * the main function
     */
    public String randomNumber() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
