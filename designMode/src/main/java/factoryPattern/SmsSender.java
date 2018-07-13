package factoryPattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class SmsSender implements Sender {
    @Override
    public void send() {
      log.debug("sms sender ...");
    }
}
