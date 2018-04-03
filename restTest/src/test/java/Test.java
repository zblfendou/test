import vo.SiteOrderReq;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws IllegalAccessException {
        SiteOrderReq siteOrderReq = new SiteOrderReq();
        siteOrderReq.setAgentCode("test agentCode");
        siteOrderReq.setFreeTimeAmount(1);
        siteOrderReq.setProductCode("test productCode");
        siteOrderReq.setServiceCode("test serviceCode");
        siteOrderReq.setTimeAmount(3);
        Map<String, Object> stringObjectMap = objectToMap(siteOrderReq);
        Object[] keys = stringObjectMap.keySet().toArray();
        Arrays.sort(keys);
        for (Object key : keys) {
            System.out.println(key+" -> "+stringObjectMap.get(key));
        }
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isFinal(field.getModifiers())) {//过滤掉序列化属性serialVersionUID
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                map.put(fieldName, value);
            }
        }
        return map;
    }

}
