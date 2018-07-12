package decoratorPattern.other1;

public abstract class CoffeeDecorator extends Coffee {
    protected Coffee mcoffee;

    public CoffeeDecorator(Coffee mcoffee) {
        this.mcoffee = mcoffee;
    }
}
