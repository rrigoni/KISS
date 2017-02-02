package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Method;

import co.appertise.kiss.annotation.event.LongClick;

/**
 * Created by ronaldo on 26/11/15.
 */
public class LongClickInjector extends AbstractInjector implements Injector {

    @Override
    public void inject(Class annotationClazz, final Method method, final Activity activity) {
        LongClick longClick = (LongClick) method.getAnnotation(annotationClazz);
        int viewId = longClick.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }

    private void injectInternal(final View view, final Method method, final Object activity) {
        if (view != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return invokeMethodForResponse(method, activity, view);
                }
            });
        }
    }

    @Override
    public void inject(Class annotationClazz, Method method, Object instance, View activity) {
        LongClick longClick = (LongClick) method.getAnnotation(annotationClazz);
        int viewId = longClick.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, instance);
    }


}
