package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import java.lang.reflect.Method;

import co.appertise.kiss.annotation.event.ItemLongClick;

/**
 * Created by ronaldo on 26/11/15.
 */
public class ItemLongClickInjector extends AbstractInjector implements Injector {

    @Override
    public void inject(Class annotationClazz, final Method method, final Activity activity) {
        ItemLongClick click = (ItemLongClick) method.getAnnotation(annotationClazz);
        int viewId = click.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }

    private void injectInternal(final View view, final Method method, final Object activity) {
        if (view != null) {
            if (view instanceof AdapterView) {
                ((AdapterView) view).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        return invokeMethodForResponse(method, activity, adapterView, view, i, l);
                    }
                });
            }
        }
    }

    @Override
    public void inject(Class annotationClazz, Method method, Object instance, View activity) {
        ItemLongClick click = (ItemLongClick) method.getAnnotation(annotationClazz);
        int viewId = click.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }


}
