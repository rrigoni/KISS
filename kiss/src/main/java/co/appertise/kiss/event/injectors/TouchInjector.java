package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Method;

import co.appertise.kiss.annotation.event.Touch;

/**
 * Created by ronaldo on 26/11/15.
 */
public class TouchInjector extends AbstractInjector implements Injector {

    @Override
    public void inject(Class annotationClazz, final Method method, final Activity activity) {
        Touch touch = (Touch) method.getAnnotation(annotationClazz);
        int viewId = touch.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }


    private void injectInternal(final View view, final Method method, final Object activity) {
        if (view != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return invokeMethodForResponse(method, activity, view, motionEvent);
                }
            });
        }
    }

    @Override
    public void inject(Class annotationClazz, Method method, Object instance, View activity) {
        Touch touch = (Touch) method.getAnnotation(annotationClazz);
        int viewId = touch.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, instance);
    }


}
