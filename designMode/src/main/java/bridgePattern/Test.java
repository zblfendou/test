package bridgePattern;

public class Test {
    public static void main(String[] args) {
        SpeedWay speedWay = new SpeedWay();
        speedWay.aCar = new Car();
        speedWay.run();

        Street street = new Street();
        street.aCar = new Bus();
        street.run();
    }
}
