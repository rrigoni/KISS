package co.appertise.kiss;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by ronaldo on 22/11/15.
 */
public final class FieldInjector implements Injector {

    private static final FieldInjector INSTANCE = new FieldInjector();

    private FieldInjector() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static FieldInjector getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void set(Field field, Object value, Activity activity) {
        if (value != null) {
            if (field.getType().isAssignableFrom(value.getClass())) {
                field.setAccessible(true);
                try {
                    field.set(activity, value);
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    @Override
    public void set(Field field, Object value, Object instance, View activity) {
        if (value != null) {
            if (field.getType().isAssignableFrom(value.getClass())) {
                field.setAccessible(true);
                try {
                    field.set(instance, value);
                } catch (IllegalAccessException e) {
                }
            }
        }
    }
}
