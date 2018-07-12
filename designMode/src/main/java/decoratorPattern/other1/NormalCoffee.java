package decoratorPattern.other1;

public class NormalCoffee extends Coffee {
    @Override
    public int getPrice() {
        return 10;
    }

    @Override
    public StringBuffer getName() {
        return new StringBuffer("原味咖啡");
    }
}
