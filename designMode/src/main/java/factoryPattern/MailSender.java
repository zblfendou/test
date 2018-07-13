package factoryPattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class MailSender implements Sender {
    @Override
    public void send() {
      log.debug("mail sender ...");
    }
}
