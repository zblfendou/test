package bridgePattern;

public class Street extends AbstractRoad {
    @Override
    void run() {
        super.run();
        aCar.run();
        System.out.println("在市区街道行驶");
    }
}
