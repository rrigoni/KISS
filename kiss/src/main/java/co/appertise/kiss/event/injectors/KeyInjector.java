package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;

import java.lang.reflect.Method;

import co.appertise.kiss.annotation.event.Key;

/**
 * Created by ronaldo on 26/11/15.
 */
public class KeyInjector extends AbstractInjector implements Injector {

    @Override
    public void inject(Class annotationClazz, final Method method, final Activity activity) {
        final Key key = (Key) method.getAnnotation(annotationClazz);
        int viewId = key.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }

    private void injectInternal(final View view, final Method method, final Object activity) {
        if (view != null) {
            view.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    return invokeMethodForResponse(method, activity, view, i, keyEvent);
                }
            });
        }
    }

    @Override
    public void inject(Class annotationClazz, Method method, Object instance, View activity) {
        final Key key = (Key) method.getAnnotation(annotationClazz);
        int viewId = key.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, instance);
    }


}
