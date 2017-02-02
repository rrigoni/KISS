package co.appertise.beauthynder.activity;

import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.appertise.beauthynder.R;
import co.appertise.beauthynder.di.service.APIService;
import co.appertise.beauthynder.di.service.APIServiceImpl;
import co.appertise.beauthynder.fragment.ItemFragment;
import co.appertise.beauthynder.fragment.MyItemRecyclerViewAdapter;
import co.appertise.beauthynder.fragment.dummy.DummyContent;
import co.appertise.kiss.annotation.InjectContentView;
import co.appertise.kiss.annotation.InjectDrawable;
import co.appertise.kiss.annotation.InjectManager;
import co.appertise.kiss.annotation.InjectService;
import co.appertise.kiss.annotation.InjectString;
import co.appertise.kiss.annotation.InjectView;
import co.appertise.kiss.annotation.event.Click;
import co.appertise.kiss.annotation.event.Key;


@InjectContentView(R.layout.activity_main)
public class MainActivity extends GenericActivity implements ItemFragment.OnListFragmentInteractionListener{

    @InjectView(R.id.textview) TextView textView;
    @InjectView(R.id.edittext) EditText editText;
    @InjectView(R.id.buttom) Button button;
    @InjectDrawable(R.drawable.twitter) Drawable twitterLogo;
    @InjectManager(LOCATION_SERVICE) LocationManager locationManager;
    @InjectString(R.string.testString) String testString;

    @InjectService APIService apiModule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, ItemFragment.newInstance(1))
                .commit();
    }

    @Click(R.id.buttom)
    private void onButtonClick(){
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
    }

    @Key(R.id.edittext)
    private void keyEventListener(){
        Toast.makeText(this, "Key event", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
