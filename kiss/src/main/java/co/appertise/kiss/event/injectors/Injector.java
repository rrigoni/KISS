package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by ronaldo on 26/11/15.
 */
public interface Injector {

    public void inject(Class annotationClazz, Method method, Activity activity);
    public void inject(Class annotationClazz, Method method, Object instance, View activity);

}
