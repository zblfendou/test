package bridgePattern;

public class Car extends AbstractCar {
    @Override
    void run() {
        super.run();
        System.out.print("小汽车");
    }
}
