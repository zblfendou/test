package strategy;

/**
 * 辅助类
 */
public abstract class AbstractCalculator {
    public int[] split(String exp, String opt) {
        String[] arrays = exp.split(opt);
        int[] arrayInts = new int[2];
        arrayInts[0] = Integer.parseInt(arrays[0]);
        arrayInts[1] = Integer.parseInt(arrays[1]);
        return arrayInts;
    }
}
