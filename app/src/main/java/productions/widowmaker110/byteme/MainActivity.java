package productions.widowmaker110.byteme;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import WidowMaker110Library.ByteMe;

public class MainActivity extends AppCompatActivity {

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random rnd = new Random();

    ArrayList<ExampleObject> ExampleArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of the ByteMe Class
        ByteMe b = new ByteMe(getApplicationContext()).getInstance();
        b.setAllocationMaxManually(200000);
        b.setAlgorithm(ByteMe.ALGORITHM_MRU);

        fillArray();

        new AsyncAddToCache(ExampleArray, b).execute("");
    }

    class AsyncAddToCache extends AsyncTask<String,Void,String>{

        private ArrayList<ExampleObject> array;
        private ByteMe byteMe;

        public AsyncAddToCache(ArrayList<ExampleObject> arr, ByteMe by) {
            this.array = arr;
            this.byteMe = by;
        }

        @Override
        protected String doInBackground(String... params) {

            for(int i = 0; i < array.size(); i++)
            {
                byteMe.addToCache(array.get(i));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(""+this.getClass().getName(), "AsyncAddToCache: finished");
            Log.d("" + this.getClass().getName(), "Max allocation in bits: " + byteMe.getAllocationMax());
            Log.d("" + this.getClass().getName(), "Current allocation in bits: " + byteMe.getAllocation_current());
        }
    }

    private void fillArray(){
        for(int i = 0; i < 10; i++)
        {
            ExampleArray.add(new ExampleObject(randomString(rnd.nextInt(100) + 1),
                    rnd.nextInt(100) + 1,
                    randomString(rnd.nextInt(200) + 1),
                   randomString(rnd.nextInt(50) + 1),
                    randomString(rnd.nextInt(200) + 1)));
        }
    }

    private String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}