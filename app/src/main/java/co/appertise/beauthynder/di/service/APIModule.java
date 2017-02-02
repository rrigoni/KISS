package co.appertise.beauthynder.di.service;

import co.appertise.kiss.annotation.InjectService;
import co.appertise.kiss.annotation.Service;

/**
 * Created by ronaldo on 21/11/15.
 */
public class APIModule {


    @Service
    public APIService apiService(){
        return new APIServiceImpl();
    }


    @Service
    public ExampleService exampleService(@InjectService APIService apiService){
        return  new ExampleService() {};
    }


}
