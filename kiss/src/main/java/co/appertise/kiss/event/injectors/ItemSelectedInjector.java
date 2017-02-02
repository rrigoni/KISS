package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import java.lang.reflect.Method;

import co.appertise.kiss.annotation.event.ItemSelected;

/**
 * Created by ronaldo on 26/11/15.
 */
public class ItemSelectedInjector extends AbstractInjector implements Injector {

    @Override
    public void inject(Class annotationClazz, final Method method, final Activity activity) {
        ItemSelected itemSelected = (ItemSelected) method.getAnnotation(annotationClazz);
        int viewId = itemSelected.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }


    private void injectInternal(final View view, final Method method, final Object activity) {
        if (view != null) {
            if (view instanceof AdapterView) {
                ((AdapterView) view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        invokeMethod(method, activity, adapterView, view, i, l);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        invokeMethod(method, adapterView, adapterView);
                    }
                });
            }
        }
    }

    @Override
    public void inject(Class annotationClazz, Method method, Object instance, View activity) {
        ItemSelected itemSelected = (ItemSelected) method.getAnnotation(annotationClazz);
        int viewId = itemSelected.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, instance);
    }


}
