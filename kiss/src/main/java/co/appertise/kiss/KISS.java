package co.appertise.kiss;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import co.appertise.kiss.annotation.InjectDrawable;
import co.appertise.kiss.annotation.InjectManager;
import co.appertise.kiss.annotation.InjectService;
import co.appertise.kiss.annotation.InjectString;
import co.appertise.kiss.annotation.InjectView;
import co.appertise.kiss.annotation.Service;

/**
 * Created by ronaldo on 21/11/15.
 */
public final class KISS {

    public static final String TAG = "KISS-INJECTOR";

    private static final Map<Class, Injector> INJECTORS = new ConcurrentHashMap<Class, Injector>() {
        {
            put(ContentViewInjector.class, ContentViewInjector.getINSTANCE());
            put(FieldInjector.class, FieldInjector.getINSTANCE());
            put(EventInjector.class, EventInjector.getINSTANCE());
        }
    };

    private static final List SERVICES = new ArrayList();
    private static final Map<Class, Object> SERVICE_INSTANCES = new ConcurrentHashMap<Class, Object>();


    public static void with(Object... services) {
        for (Object service : services) {
            if (!SERVICES.contains(service)) {
                SERVICES.add(service);
                scanServiceMethods(service);
            }
        }
        for (Object service : services) {
            injectTransitiveMethodsDependencies(service);
        }
        for (Object service : SERVICE_INSTANCES.values()) {
            Field[] fields = service.getClass().getDeclaredFields();
            for(Field field : fields){
                if (field.isAnnotationPresent(InjectService.class)) {
                    injectService(field, service);
                }
            }
            Field[] parentFields = service.getClass().getSuperclass().getDeclaredFields();
            for(Field field : parentFields){
                if (field.isAnnotationPresent(InjectService.class)){
                    injectService(field, service);
                }
            }
        }
    }

    private static void injectTransitiveMethodsDependencies(Object service) {
        if (service != null) {
            Method[] methods = service.getClass().getDeclaredMethods();
            for (Method method : methods) {
                Class[] parameters = method.getParameterTypes();
                if (parameters.length > 0) {
                    List instanceParameters = new ArrayList();
                    for (Class paramClazz : parameters) {
                        if (SERVICE_INSTANCES.containsKey(paramClazz)) {
                            instanceParameters.add(SERVICE_INSTANCES.get(paramClazz));
                        }
                    }
                    try {
                        if(instanceParameters.size() == parameters.length){
                            Object serviceInstance = method.invoke(service, instanceParameters.toArray());
                            SERVICE_INSTANCES.put(serviceInstance.getClass(), serviceInstance);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    private static void scanServiceMethods(Object service) {
        if (service != null) {
            Method[] methods = service.getClass().getDeclaredMethods();
            for (Method method : methods) {
                try {
                    if (method.getParameterTypes().length == 0) {
                        if (method.isAnnotationPresent(Service.class)) {
                            Class type = method.getReturnType();
                            Object serviceInstance = method.invoke(service, null);
                            SERVICE_INSTANCES.put(type, serviceInstance);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static final void inject(Activity activity) {
        injectContentView(activity);
        injectFields(activity);
        injectEvents(activity);
    }


    public static final void inject(Object instance){
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(InjectService.class)) {
                injectService(field, instance);
            }
        }
    }

    public static void inject(Object instance, View parentView) {
        injectFields(instance, parentView);
        injectEvents(instance, parentView);
    }


    private static void injectEvents(Activity activity) {
        INJECTORS.get(EventInjector.class).set(null, null, activity);
    }


    private static void injectEvents(Object instance, View activity) {
        INJECTORS.get(EventInjector.class).set(null, null, instance, activity);
    }

    private static void injectContentView(Activity activity) {
        INJECTORS.get(ContentViewInjector.class).set(null, null, activity);
    }


    private static void injectFields(Activity activity) {
        Field[] declaredFields = activity.getClass().getDeclaredFields();
        Field[] superDeclaredFields = activity.getClass().getSuperclass().getDeclaredFields();
        injectActivityFields(activity, declaredFields);
        injectActivityFields(activity, superDeclaredFields);
    }

    private  static void injectActivityFields(Activity activity, Field[] fields){
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectView.class)) {
                injectView(field, activity);
            }
            if (field.isAnnotationPresent(InjectDrawable.class)) {
                injectDrawable(field, activity);
            }
            if (field.isAnnotationPresent(InjectManager.class)) {
                injectManager(field, activity);
            }
            if (field.isAnnotationPresent(InjectString.class)) {
                injectString(field, activity);
            }
            if (field.isAnnotationPresent(InjectService.class)) {
                injectService(field, activity);
            }
        }
    }

    private static void injectFields(Object instance, View activity) {
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(InjectView.class)) {
                injectView(field, instance, activity);
            }
            if (field.isAnnotationPresent(InjectDrawable.class)) {
                injectDrawable(field, instance, activity);
            }
            if (field.isAnnotationPresent(InjectManager.class)) {
                injectManager(field, instance, activity);
            }
            if (field.isAnnotationPresent(InjectString.class)) {
                injectString(field, instance, activity);
            }
            if (field.isAnnotationPresent(InjectService.class)) {
                injectService(field, instance, activity);
            }
        }
    }


    private static void injectService(Field field, Object instance) {
        Object serviceInstance = SERVICE_INSTANCES.get(field.getType());
        set(field, serviceInstance, instance);
    }

    private static void injectService(Field field, Object instance, View activity) {
        Object serviceInstance = SERVICE_INSTANCES.get(field.getType());
        set(field, serviceInstance, activity);
    }

    private static void injectString(Field field, Activity activity) {
        InjectString injectString = field.getAnnotation(InjectString.class);
        int stringId = injectString.value();
        Object injectingString = activity.getString(stringId);
        set(field, injectingString, activity);
    }

    private static void injectString(Field field, Object instance, View activity) {
        InjectString injectString = field.getAnnotation(InjectString.class);
        int stringId = injectString.value();
        Object injectingString = activity.getContext().getString(stringId);
        set(field, injectingString, activity);
    }

    private static void injectManager(Field field, Activity activity) {
        InjectManager injectDrawable = field.getAnnotation(InjectManager.class);
        String serviceId = injectDrawable.value();
        @SuppressWarnings("ResourceType")
        Object injectingService = activity.getSystemService(serviceId);
        set(field, injectingService, activity);
    }


    private static void injectManager(Field field, Object instance, View activity) {
        InjectManager injectDrawable = field.getAnnotation(InjectManager.class);
        String serviceId = injectDrawable.value();
        @SuppressWarnings("ResourceType")
        Object injectingService = activity.getContext().getSystemService(serviceId);
        set(field, injectingService, instance);
    }

    private static void injectDrawable(Field field, Activity activity) {
        InjectDrawable injectDrawable = field.getAnnotation(InjectDrawable.class);
        int drawableId = injectDrawable.value();
        field.setAccessible(true);
        Object injectingDrawable = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            injectingDrawable = activity.getResources().getDrawable(drawableId, activity.getTheme());
        } else {
            injectingDrawable = activity.getResources().getDrawable(drawableId);
        }
        set(field, injectingDrawable, activity);
    }

    private static void injectDrawable(Field field, Object instance, View activity) {
        InjectDrawable injectDrawable = field.getAnnotation(InjectDrawable.class);
        int drawableId = injectDrawable.value();
        field.setAccessible(true);
        Object injectingDrawable = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            injectingDrawable = activity.getResources().getDrawable(drawableId, activity.getContext().getTheme());
        } else {
            injectingDrawable = activity.getResources().getDrawable(drawableId);
        }
        set(field, injectingDrawable, activity);
    }


    private static void injectView(Field field, Activity activity) {
        InjectView view = field.getAnnotation(InjectView.class);
        int viewId = view.value();
        Object targetView = activity.findViewById(viewId);
        set(field, targetView, activity);
    }

    private static void injectView(Field field, Object instance, View activity) {
        InjectView view = field.getAnnotation(InjectView.class);
        int viewId = view.value();
        Object targetView = activity.findViewById(viewId);
        set(field, targetView, instance);
    }


    private static void set(Field field, Object value, Activity activity) {
        INJECTORS.get(FieldInjector.class).set(field, value, activity);
    }

    private static void set(Field field, Object value, Object instance) {
        INJECTORS.get(FieldInjector.class).set(field, value, instance, null);
    }

    public static void release() {
        INJECTORS.clear();
        SERVICES.clear();
        SERVICE_INSTANCES.clear();
    }


}
