package factoryPattern;

public class SmsSendFacotry implements Provider {
    @Override
    public Sender produce() {
        return new SmsSender();
    }
}
