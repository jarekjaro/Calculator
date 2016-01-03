import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CalculatorTest {

    @Test
    public void bracesSuccessTest(){
        assertEquals(Calculator.parenthesisTest("((2+2)*2)/(-4)"),true);
    }

    @Test
    public void bracesFailTest(){
        assertEquals(Calculator.parenthesisTest("((2+4)/3"),false);
    }

    @Test
    public void calculationPriorityTest(){
        assertEquals(Calculator.calculate("2+2*2"), 6);
    }

}