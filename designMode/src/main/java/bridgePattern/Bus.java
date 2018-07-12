package bridgePattern;

public class Bus extends AbstractCar{
    @Override
    void run() {
        super.run();
        System.out.print("公交车");
    }
}
