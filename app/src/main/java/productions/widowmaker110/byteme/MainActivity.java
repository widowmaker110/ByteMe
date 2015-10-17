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

        ByteMe b = new ByteMe();

        int[] int_array = {1, 100, 300, 1000};
        Log.d(""+this.getClass().getName(), "int[]: " + b.run(int_array));
    }
}