package chainOfResponsibilityPattern;

public class Test {
    public static void main(String[] args) {
        ProjectHandler projectHandler = new ProjectHandler();
        DeptHandler deptHandler = new DeptHandler();
        GeneralHandler generalHandler = new GeneralHandler();
        projectHandler.setNextHandler(deptHandler);
        deptHandler.setNextHandler(generalHandler);

        projectHandler.doHandler("lwx", 450);

        projectHandler.doHandler("lwx", 600);

        projectHandler.doHandler("zy", 600);

        projectHandler.doHandler("zy", 1500);

        projectHandler.doHandler("lwxzy", 1500);
    }
}
