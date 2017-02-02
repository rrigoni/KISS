package co.appertise.kiss;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by ronaldo on 22/11/15.
 */
public interface Injector {


    /**
     * Inject the value into the activity's field if the type matches.
     * @param field
     * @param value
     * @param activity
     */
    public void set(Field field, Object value, Activity activity);

    /**
     * Inject the value into the activity's field if the type matches.
     * @param field
     * @param value
     * @param activity
     */
    public void set(Field field, Object value, Object instance, View activity);

}
