package productions.widowmaker110.byteme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

import Library.ByteMe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ByteMe b = new ByteMe();

        int[] int_array = {1, 100, 300, 1000};
        Log.d(""+this.getClass().getName(), "int[]: " + b.run(int_array));

        String[] string_array = {"Hello", "Github", "Computer Science"};
        Log.d(""+this.getClass().getName(),"String[]: " + b.run(string_array));

        short[] short_array = {10, 100, 55};
        Log.d(""+this.getClass().getName(),"short[]: " + b.run(short_array));

        long[] long_array = {12345678910L, 20002384093284L, 100000L};
        Log.d(""+this.getClass().getName(),"long[]: " + b.run(long_array));

        byte[] byte_array = {10, 11, 101};
        Log.d(""+this.getClass().getName(),"byte[]: " + b.run(byte_array));

        float[] float_array = {3.14159f, 1.9999f, 2.3f};
        Log.d(""+this.getClass().getName(),"float[]: " + b.run(float_array));

        double[] double_array = {3.14159, 1.9999, 2.3};
        Log.d(""+this.getClass().getName(),"double[]: " + b.run(double_array));

        char[] char_array = {'A', 'w', 'e', 's', 'o', 'm', 'e'};
        Log.d(""+this.getClass().getName(),"char[]: " + b.run(char_array));

        boolean[] boolean_array = {false, true, false};
        Log.d(""+this.getClass().getName(),"boolean[]: " + b.run(boolean_array));

        Bitmap[] bitmap_array = {BitmapFactory.decodeResource(getResources(), R.drawable.android_black),
                BitmapFactory.decodeResource(getResources(), R.drawable.android_white)};
        Log.d(""+this.getClass().getName(),"bitmap[]: " + b.run(bitmap_array));

        // generic Object type
        Object[] objects = {1, "Hello", 12345678910L, (short) 10, (byte) 10, 3.14f, 3.14, 'A', false,
                BitmapFactory.decodeResource(getResources(), R.drawable.android_black), int_array};
        Log.d(""+this.getClass().getName(),"object[]: " + b.run(objects));

        // All of the objects combined.
        Log.d(""+this.getClass().getName(),"all: " + b.run(int_array, string_array, short_array,
                long_array, byte_array, float_array, double_array, char_array, boolean_array,
                bitmap_array));
    }
}