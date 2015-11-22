package productions.widowmaker110.byteme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;

import Library.ByteMe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ByteMe b = new ByteMe();

        ExampleObject object = new ExampleObject("Bob", 20, "indy", "male", "its bobby");
        try {
            b.examine(object);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}