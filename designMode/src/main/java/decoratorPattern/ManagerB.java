package decoratorPattern;

public class ManagerB extends Manager{
    private Person person;

    public ManagerB(Person person) {
        this.person = person;
    }

    @Override
    public void doCoding() {
        person.doCoding();
        doEndWork();
    }

    private void doEndWork() {
        System.out.println("项目经理B 在做收尾工作");
    }
}
