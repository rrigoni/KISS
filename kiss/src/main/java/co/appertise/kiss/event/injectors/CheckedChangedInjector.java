package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import java.lang.reflect.Method;

import co.appertise.kiss.annotation.event.CheckedChanged;

/**
 * Created by ronaldo on 26/11/15.
 */
public class CheckedChangedInjector extends AbstractInjector implements Injector {

    protected boolean isViewInvokable(View view, Class expectedType) {
        return view != null && expectedType.isInstance(view);
    }

    @Override
    public void inject(Class annotationClazz, final Method method, final Activity activity) {
        CheckedChanged checkedChanged = (CheckedChanged) method.getAnnotation(annotationClazz);
        int viewId = checkedChanged.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, activity);
    }


    private void injectInternal(final View view, final Method method,final Object activity){
        if (isViewInvokable(view, CompoundButton.class)) {
            ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    invokeMethod(method, activity, compoundButton, b);
                }
            });
        }
        if (isViewInvokable(view, RadioGroup.class)) {
            ((RadioGroup) view).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    invokeMethod(method, activity, radioGroup, i);
                }
            });
        }
    }

    @Override
    public void inject(final Class annotationClazz, final Method method, Object instance, final View activity) {
        CheckedChanged checkedChanged = (CheckedChanged) method.getAnnotation(annotationClazz);
        int viewId = checkedChanged.value();
        View view = getView(viewId, activity);
        injectInternal(view, method, instance);
    }


}
