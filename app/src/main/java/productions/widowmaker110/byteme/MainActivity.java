package productions.widowmaker110.byteme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import Library.ByteMe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of the ByteMe Class
        ByteMe b = new ByteMe(MainActivity.this).getInstance();
        b.setAllocationMax(ByteMe.RAM_ONE_EIGHT);
        b.setAlgorithm(ByteMe.LRU_ALGORITHM);

        // Create a custom object with a given set of variables
        ExampleObject object = new ExampleObject("Bob", 20, "indy", "male", "its bobby");
        b.addObjectToCache(object);
        ExampleObject object2 = (ExampleObject) b.getObjectFromCache(object.hashCode());
    }
}