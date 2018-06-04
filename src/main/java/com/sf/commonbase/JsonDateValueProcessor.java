package com.sf.commonbase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * 对象转json时，对特殊对象的拦截处理类
 * @author tuzhaoliang
 * @date 2018年5月3日
 */
public class JsonDateValueProcessor implements JsonValueProcessor {   
    
    private String format ="yyyy-MM-dd HH:mm:ss";   
        
    public Object processArrayValue(Object value, JsonConfig config) {   
        return process(value);   
    }   
   
    public Object processObjectValue(String key, Object value, JsonConfig config) {   
        return process(value);   
    }   
        
    private Object process(Object value){   
            
        if(value instanceof Date){   
            SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.UK);   
            return sdf.format(value);   
        }   
        return value == null ? "" : value.toString();   
    }   
}
