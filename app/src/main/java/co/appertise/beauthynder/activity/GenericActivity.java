package co.appertise.beauthynder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.appertise.kiss.KISS;

public class GenericActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KISS.inject(this);
    }

}
