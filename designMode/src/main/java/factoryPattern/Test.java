package factoryPattern;

public class Test {
    public static void main(String[] args) {
//        SendFactory.produceMail().send();
//        SendFactory.produceSms().send();
        Provider provider = (Provider) new MailSendFactory();
        provider.produce().send();
        provider = new SmsSendFacotry();
        provider.produce().send();
    }
}
