package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Method;

import co.appertise.kiss.annotation.event.Click;

/**
 * Created by ronaldo on 26/11/15.
 */
public class ClickInjector extends AbstractInjector implements Injector {


    @Override
    public void inject(Class annotationClazz, final Method method, final Activity activity) {
        Click click = (Click) method.getAnnotation(annotationClazz);
        int viewId = click.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }


    private void injectInternal(final View view, final Method method, final Object activity) {
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    invokeMethod(method, activity, view);
                }
            });
        }
    }

    @Override
    public void inject(Class annotationClazz, Method method, Object instance, View activity) {
        Click click = (Click) method.getAnnotation(annotationClazz);
        int viewId = click.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, instance);
    }


}
