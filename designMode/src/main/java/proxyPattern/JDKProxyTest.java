package proxyPattern;

import java.lang.reflect.Proxy;

public class JDKProxyTest {
    public static void main(String[] args) {
        PersonDaoImpl personDao = new PersonDaoImpl();
        JDKHandlerForPerson handler = new JDKHandlerForPerson(personDao);
        PersonDao proxy = (PersonDao) Proxy.newProxyInstance(personDao.getClass().getClassLoader(),
                personDao.getClass().getInterfaces(), handler);
        proxy.say();
    }
}
