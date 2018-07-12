package chainOfResponsibilityPattern;

public abstract class ConsumerHandler {
    private ConsumerHandler nextHandler;

    public ConsumerHandler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(ConsumerHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void doHandler(String userName, double free);
}
