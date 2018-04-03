package com.study.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zbl on 2017/6/23.
 *
 */
public class CommonUtil {
    /**
     * 从request中获得参数Map，并返回可读的Map.
     *
     * @param request the request
     * @return the parameter map
     */
    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map<String, String[]> properties = request.getParameterMap();
        //返回值Map
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Set<String> keySet = properties.keySet();
        for (String key : keySet) {
            String[] values = properties.get(key);
            StringBuilder value = new StringBuilder();
            if (values != null && (values.length == 1 && StringUtils.isNotBlank(values[0]))) {
                for (String value1 : values) {
                    if (value1 != null && !"".equals(value1)) {
                        value.append(value1).append(",");
                    }
                }
                if (!"".equals(value.toString())) {
                    value = new StringBuilder(value.substring(0, value.length() - 1));
                }
                if (key.equals("keywords")) {//关键字特殊查询字符转义
                    value = new StringBuilder(value.toString().replace("_", "\\_").replace("%", "\\%"));
                }
                returnMap.put(key, value.toString());
            }
        }
        return returnMap;
    }
}
