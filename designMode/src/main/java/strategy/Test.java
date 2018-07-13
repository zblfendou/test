package strategy;

import lombok.extern.log4j.Log4j;

@Log4j
public class Test {
    public static void main(String[] args) {
        String exp = "2+8";
        Plus plus = new Plus();
        print(exp, plus.calculate(exp));
    }

    private static void print(String exp, int result) {
        log.debug(exp + "=" + result);
    }
}

