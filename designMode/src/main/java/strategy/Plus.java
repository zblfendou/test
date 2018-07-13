package strategy;

public class Plus extends AbstractCalculator implements ICalculator {
    @Override
    public int calculate(String exp) {
        int[] arrayInts = split(exp, "\\+");
        return arrayInts[0] + arrayInts[1];
    }
}
