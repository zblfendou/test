package decoratorPattern.other1;

public class Test {
    public static void main(String[] args) {
        Coffee coffee = new NormalCoffee();
        coffee = new MilkCoffee(coffee);
        coffee = new SugarCoffee(coffee);
        System.out.println(coffee.getName() + " 售价:" + coffee.getPrice());
    }
}
