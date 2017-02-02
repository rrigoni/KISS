package co.appertise.beauthynder;

import android.app.Application;

import co.appertise.beauthynder.di.service.APIModule;
import co.appertise.kiss.KISS;

/**
 * Created by ronaldo on 21/11/15.
 */
public class Beauthinder extends Application{


    @Override
    public void onCreate() {
        super.onCreate();
        KISS.with(new APIModule());
    }

}
