package bridgePattern;

public class SpeedWay extends AbstractRoad {
    @Override
    void run() {
        super.run();
        aCar.run();
        System.out.println("在高速公路行驶");
    }
}
