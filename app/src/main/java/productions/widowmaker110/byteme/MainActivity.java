package productions.widowmaker110.byteme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Library.ByteMe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of the ByteMe Class
        ByteMe b = new ByteMe();

        // Create a custom object with a given set of variables
        ExampleObject object = new ExampleObject("Bob", 20, "indy", "male", "its bobby");
        b.examine(object);
    }
}