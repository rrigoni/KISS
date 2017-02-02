package co.appertise.kiss.event.injectors;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import co.appertise.kiss.KISS;
import co.appertise.kiss.util.DefaultValues;

/**
 * Created by Ronaldo on 10/01/2016.
 */
class AbstractInjector {


    /**
     * Get the view from an activity
     *
     * @param viewId   the view id
     * @param activity the activity
     * @return the view, or null if not found
     */
    protected View getView(int viewId, Activity activity) {
        View view = activity.findViewById(viewId);
        if (view == null) {
            Log.w(KISS.TAG, String.format("Unable to find view %d on activity %s", viewId, activity.getClass().getSimpleName()));
        }
        return view;
    }

    /**
     * Get the view from another view
     *
     * @param viewId the viewid
     * @param view   the parent view
     * @return
     */
    protected View getView(int viewId, View view) {
        if (view == null) {
            Log.e(KISS.TAG, String.format("Unable to find view %d on a null parent view", viewId));
        }
        View view1 = view.findViewById(viewId);
        if (view1 == null) {
            Log.e(KISS.TAG, String.format("View not found %d", viewId));
        }
        return view1;
    }

    /**
     * Invoke a method without return
     *
     * @param method   the method
     * @param instance the object instance
     * @param params   the method's parameters
     */
    protected void invokeMethod(Method method, Object instance, Object... params) {
        try {
            method.setAccessible(true);
            Object[] paramsToInvoke = extractMethodParams(method, params);
            method.invoke(instance, paramsToInvoke);
            method.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * Extract validate and generate a array parameters based on required types.
     * @param method the method
     * @param params the param values.
     * @return
     */
    private Object[] extractMethodParams(Method method, Object... params) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] paramsToInvoke = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class methodParamType = parameterTypes[i];
            if (params != null && i < params.length) {
                Class paramClass = params[i].getClass();
                if (methodParamType.isAssignableFrom(paramClass)) {
                    paramsToInvoke[i] = params[i];
                } else {
                    paramsToInvoke[i] = methodParamType.isPrimitive() ? DefaultValues.getDefaultValueForClass(methodParamType) : null;
                }
            } else {
                paramsToInvoke[i] = methodParamType.isPrimitive() ? DefaultValues.getDefaultValueForClass(methodParamType) : null;
            }
        }
        return paramsToInvoke.length > 0 ? paramsToInvoke : null;
    }


    /**
     * Invoke a method for response
     *
     * @param method   the method to be invoked
     * @param instance the method object instnace
     * @param params   the method params
     * @return true if the method's response is boolean indicating if the event was consumed or not, false if the method return is not boolean
     */
    protected boolean invokeMethodForResponse(Method method, Object instance, Object... params) {
        Object value = false;
        try {
            method.setAccessible(true);
            Object[] paramsToInvoke = extractMethodParams(method, params);
            method.invoke(instance, paramsToInvoke);
            method.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
            value = false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }
}
