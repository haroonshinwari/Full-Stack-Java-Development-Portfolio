package guessthenumber.service;

import guessthenumber.TestApplicationConfiguration;
import guessthenumber.data.GameDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class ServiceLayerImplTest {

    @Autowired
    ServiceLayer service;

    @Test
    public void checkGuess() {

        //ARRANGE
        String randomNumber = "1234";

        String guess1 = "4587";
        String guess2 = "4581";
        String guess3 = "1289";
        String guess4 = "6512";
        String guess5 = "5873";
        String guess6 = "1324";
        String guess7 = "1234";

        //ACT
        String result1 = service.checkGuess(randomNumber, guess1);
        String result2 = service.checkGuess(randomNumber, guess2);
        String result3 = service.checkGuess(randomNumber, guess3);
        String result4 = service.checkGuess(randomNumber, guess4);
        String result5 = service.checkGuess(randomNumber, guess5);
        String result6 = service.checkGuess(randomNumber, guess6);
        String result7 = service.checkGuess(randomNumber, guess7);

        //ASSERT
        assertEquals("e:0:p:1",result1);
        assertEquals("e:0:p:2",result2);
        assertEquals("e:2:p:0",result3);
        assertEquals("e:0:p:2",result4);
        assertEquals("e:0:p:1",result5);
        assertEquals("e:2:p:2",result6);
        assertEquals("e:4:p:0",result7);

    }

    @Test
    public void getRandomDigit() {

        //ARRANGE
        //ACT
        List<Integer> random4DigitNumber = service.getRandomDigit();
        Set<Integer> setOfRandomNumber = new HashSet<>(random4DigitNumber);
        Set<Integer> allowedDigits = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

        //ASSERT
        assertEquals(4, random4DigitNumber.size()); //ensures we return a list with only 4 elements
        assertEquals(4, setOfRandomNumber.size()); //a set cannot include duplicates so this ensures each digit is unique

        // now we check to ensure each digit is a value between 0-9
        assertTrue(allowedDigits.contains(random4DigitNumber.get(0)));
        assertTrue(allowedDigits.contains(random4DigitNumber.get(1)));
        assertTrue(allowedDigits.contains(random4DigitNumber.get(2)));
        assertTrue(allowedDigits.contains(random4DigitNumber.get(3)));
    }
}