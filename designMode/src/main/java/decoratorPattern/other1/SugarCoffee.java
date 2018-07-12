package decoratorPattern.other1;

public class SugarCoffee extends CoffeeDecorator {
    public SugarCoffee(Coffee mcoffee) {
        super(mcoffee);
    }

    @Override
    public int getPrice() {
        return mcoffee.getPrice() + 30;
    }

    @Override
    public StringBuffer getName() {
        return mcoffee.getName().append(" + ").append("糖");
    }
}
