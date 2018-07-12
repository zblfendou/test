package chainOfResponsibilityPattern;

public class DeptHandler extends ConsumerHandler {
    @Override
    public void doHandler(String userName, double free) {
        if (free < 1000) {
            if (userName.equals("zy")) {
                System.out.println("给予报销:" + free);
            } else {
                System.out.println("报销不通过");
            }
        } else {
            if (getNextHandler() != null) {
                getNextHandler().doHandler(userName, free);
            }

        }
    }

}