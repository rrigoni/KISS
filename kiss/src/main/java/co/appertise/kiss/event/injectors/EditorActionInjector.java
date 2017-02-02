package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;

import co.appertise.kiss.annotation.event.EditorAction;

/**
 * Created by ronaldo on 26/11/15.
 */
public class EditorActionInjector extends AbstractInjector implements Injector {



    @Override
    public void inject(Class annotationClazz, final Method method, final Activity activity) {
        EditorAction editorAction = (EditorAction) method.getAnnotation(annotationClazz);
        int viewId = editorAction.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }

    private void injectInternal(final View view, final Method method, final Object activity) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        return invokeMethodForResponse(method, activity, textView, i, keyEvent);
                    }
                });
            }
        }
    }

    @Override
    public void inject(Class annotationClazz, Method method, Object instance, View activity) {
        EditorAction editorAction = (EditorAction) method.getAnnotation(annotationClazz);
        int viewId = editorAction.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, instance);
    }


}
