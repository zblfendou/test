package strategy;

public class Minus extends AbstractCalculator implements ICalculator {
    @Override
    public int calculate(String exp) {
        int[] arrayInts = split(exp, "-");
        return arrayInts[0]-arrayInts[1];
    }
}
