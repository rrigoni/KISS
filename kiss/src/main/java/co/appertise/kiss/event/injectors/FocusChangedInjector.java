package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Method;

import co.appertise.kiss.annotation.event.FocusChanged;

/**
 * Created by ronaldo on 26/11/15.
 */
public class FocusChangedInjector extends AbstractInjector implements Injector {

    @Override
    public void inject(Class annotationClazz, final Method method, final Activity activity) {
        FocusChanged click = (FocusChanged) method.getAnnotation(annotationClazz);
        int viewId = click.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }

    private void injectInternal(final View view, final Method method, final Object activity) {
        if (view != null) {
            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    invokeMethod(method, activity, view, b);
                }
            });
        }
    }

    @Override
    public void inject(Class annotationClazz, Method method, Object instance, View activity) {
        FocusChanged click = (FocusChanged) method.getAnnotation(annotationClazz);
        int viewId = click.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, instance);
    }


}
