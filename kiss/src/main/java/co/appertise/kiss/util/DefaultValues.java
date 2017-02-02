package co.appertise.kiss.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public final class DefaultValues {
    /**
     * @param clazz the class for which a default value is needed
     * @return A reasonable default value for the given class (the boxed default value for primitives, <code>null</code>
     * otherwise).
     */
    public static Object getDefaultValueForClass(Class clazz) {
        return DEFAULT_VALUES.get(clazz);
    }

    private static final Map<Class, Object> DEFAULT_VALUES = unmodifiableMap(new HashMap<Class, Object>() {
        // Default primitive values
        private boolean b;
        private byte by;
        private char c;
        private double d;
        private float f;
        private int i;
        private long l;
        private short s;

        {
            for (final Field field : getClass().getDeclaredFields()) {
                try {
                    put(field.getType(), field.get(this));
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
    });

    private DefaultValues() {
    }
}