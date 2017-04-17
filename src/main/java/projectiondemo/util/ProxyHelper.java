package projectiondemo.util;

import java.lang.reflect.Method;

/**
 * @author Cepro, 2017-04-17
 */
public class ProxyHelper {
    
    public static Object getTargetObject(final Object proxied) {
    
        try {
            Method[] methods = proxied.getClass().getDeclaredMethods();
            Method targetMethod;
            for (Method method : methods) {
                if (method.getName().endsWith("getTarget")) {
                    targetMethod = method;
                    targetMethod.setAccessible(true);
                    return targetMethod.invoke(proxied);
                }
            }
            throw new IllegalStateException(
                    "Could not find target source method on proxied object ["
                            + proxied.getClass() + "]");
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}