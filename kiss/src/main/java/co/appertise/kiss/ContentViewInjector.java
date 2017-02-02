package co.appertise.kiss;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

import co.appertise.kiss.annotation.InjectContentView;

/**
 * Created by ronaldo on 22/11/15.
 */
public final class ContentViewInjector implements Injector {

    private static final ContentViewInjector INSTANCE = new ContentViewInjector();

    private ContentViewInjector() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }


    public static ContentViewInjector getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void set(Field field, Object value, Activity activity) {
        Class clazz = activity.getClass();
        if (clazz.isAnnotationPresent(InjectContentView.class)) {
            int contentView = ((InjectContentView) clazz.getAnnotation(InjectContentView.class)).value();
            activity.setContentView(contentView);
        }
    }

    @Override
    public void set(Field field, Object value, Object instance, View activity) {
        // none
    }


}
