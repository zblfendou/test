package templatePattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class Test {
    public static void main(String[] args) {
        String exp = "8+8";
        AbstractCalculator plus = new Plus();
        int result = plus.calculate(exp, "\\+");
        log.debug(result);
    }
}
