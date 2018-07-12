package decoratorPattern;

public class Client {
    public static void main(String[] args) {
        Person employee = new Employee();
        employee = new ManagerA(employee);
        employee = new ManagerB(employee);
        employee.doCoding();
    }
}
