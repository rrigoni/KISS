package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import java.lang.reflect.Method;

import co.appertise.kiss.annotation.event.ItemClick;

/**
 * Created by ronaldo on 26/11/15.
 */
public class ItemClickInjector extends AbstractInjector implements Injector {

    @Override
    public void inject(Class annotationClazz, final Method method, final Activity activity) {
        ItemClick itemClick = (ItemClick) method.getAnnotation(annotationClazz);
        int viewId = itemClick.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }

    private void injectInternal(final View view, final Method method, final Object activity) {
        if (view != null) {
            if (view instanceof AdapterView) {
                ((AdapterView) view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        invokeMethod(method, activity, view, i, l);
                    }
                });
            }
        }
    }

    @Override
    public void inject(Class annotationClazz, Method method, Object instance, View activity) {
        ItemClick itemClick = (ItemClick) method.getAnnotation(annotationClazz);
        int viewId = itemClick.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }


}
