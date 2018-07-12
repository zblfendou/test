package decoratorPattern.other1;

public class MilkCoffee extends CoffeeDecorator {
    public MilkCoffee(Coffee mcoffee) {
        super(mcoffee);
    }

    @Override
    public int getPrice() {
        return mcoffee.getPrice() + 12;
    }

    @Override
    public StringBuffer getName() {
        return mcoffee.getName().append(" + ").append("牛奶");
    }
}
