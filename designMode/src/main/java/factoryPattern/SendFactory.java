package factoryPattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class SendFactory {

    public static Sender produceMail() {
        return new MailSender();
    }

    public static Sender produceSms() {
        return new SmsSender();
    }
}
