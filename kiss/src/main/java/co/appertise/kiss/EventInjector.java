package co.appertise.kiss;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import co.appertise.kiss.annotation.event.CheckedChanged;
import co.appertise.kiss.annotation.event.Click;
import co.appertise.kiss.annotation.event.EditorAction;
import co.appertise.kiss.annotation.event.FocusChanged;
import co.appertise.kiss.annotation.event.ItemClick;
import co.appertise.kiss.annotation.event.ItemLongClick;
import co.appertise.kiss.annotation.event.ItemSelected;
import co.appertise.kiss.annotation.event.Key;
import co.appertise.kiss.annotation.event.LongClick;
import co.appertise.kiss.annotation.event.Touch;
import co.appertise.kiss.event.injectors.CheckedChangedInjector;
import co.appertise.kiss.event.injectors.ClickInjector;
import co.appertise.kiss.event.injectors.EditorActionInjector;
import co.appertise.kiss.event.injectors.FocusChangedInjector;
import co.appertise.kiss.event.injectors.ItemClickInjector;
import co.appertise.kiss.event.injectors.ItemLongClickInjector;
import co.appertise.kiss.event.injectors.ItemSelectedInjector;
import co.appertise.kiss.event.injectors.KeyInjector;
import co.appertise.kiss.event.injectors.LongClickInjector;
import co.appertise.kiss.event.injectors.TouchInjector;

/**
 * Created by ronaldo on 22/11/15.
 */
public final class EventInjector implements Injector {

    private static final EventInjector INSTANCE = new EventInjector();

    private static final Map<Class, Class> LISTENERS = new HashMap<Class, Class>() {
        {
            put(Click.class, View.OnClickListener.class);
            put(CheckedChanged.class, CompoundButton.OnCheckedChangeListener.class);
            put(EditorAction.class, TextView.OnEditorActionListener.class);
            put(FocusChanged.class, View.OnFocusChangeListener.class);
            put(ItemClick.class, AdapterView.OnItemClickListener.class);
            put(ItemLongClick.class, AdapterView.OnItemLongClickListener.class);
            put(ItemSelected.class, AdapterView.OnItemSelectedListener.class);
            put(LongClick.class, View.OnLongClickListener.class);
            put(Touch.class, View.OnTouchListener.class);
            put(Key.class, View.OnKeyListener.class);
        }
    };


    private static final Map<Class, co.appertise.kiss.event.injectors.Injector> INJECTORS = new HashMap<Class, co.appertise.kiss.event.injectors.Injector>() {
        {
            put(Click.class, new ClickInjector());
            put(CheckedChanged.class, new CheckedChangedInjector());
            put(EditorAction.class, new EditorActionInjector());
            put(FocusChanged.class, new FocusChangedInjector());
            put(ItemClick.class, new ItemClickInjector());
            put(ItemLongClick.class, new ItemLongClickInjector());
            put(ItemSelected.class, new ItemSelectedInjector());
            put(Key.class, new KeyInjector());
            put(LongClick.class, new LongClickInjector());
            put(Touch.class, new TouchInjector());
        }
    };


    private EventInjector() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static EventInjector getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void set(Field field, Object value, Activity activity) {
        Method[] methods = activity.getClass().getDeclaredMethods();
        for (Method method : methods) {
            scanMethod(method, activity);
        }
    }

    @Override
    public void set(Field field, Object value, Object instance, View activity) {
        Method[] methods = activity.getClass().getDeclaredMethods();
        for (Method method : methods) {
            scanMethod(method, instance, activity);
        }
    }

    private void scanMethod(Method method, Activity activity) {
        for (Class annotationClass : LISTENERS.keySet()) {
            Annotation annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                bindAnnotation(annotationClass, method, activity);
            }
        }
    }

    private void scanMethod(Method method, Object instance, View activity) {
        for (Class annotationClass : LISTENERS.keySet()) {
            Annotation annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                bindAnnotation(annotationClass, method, instance, activity);
            }
        }
    }

    private void bindAnnotation(Class annotation, Method method, Activity activity) {
        INJECTORS.get(annotation).inject(annotation, method, activity);
    }

    private void bindAnnotation(Class annotation, Method method, Object instance, View activity) {
        INJECTORS.get(annotation).inject(annotation, method, instance, activity);
    }
}
