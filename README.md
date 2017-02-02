# KISS
Simple dependency injection for Android.


In your Application class init the modules:

```java
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        KISS.with(new APIModule());
    }

}
```


Your app module should look like this:
````java
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
```
To prevent add the inject command in all activities, create a GenericActivity and extends it:


```java
public class GenericActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KISS.inject(this);
    }

}
```
In your activities innject everything you need like this:

```java
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
```

Wait, and fragments? 
```java
public class ItemFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    public ItemFragment() {
    }

    public static ItemFragment newInstance(int columnCount) {
        return new ItemFragment();
    }
    
    @InjectView(R.id.list) private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        KISS.inject(this, view);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        return view;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DummyItem item);
    }
}
```
Wait, and viewholders?
```java
public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.id2) public View mView;
        @InjectView(R.id.content2) public TextView mIdView;
        @InjectView(R.id.content2) public TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            KISS.inject(this, view);
        }
        
    }

