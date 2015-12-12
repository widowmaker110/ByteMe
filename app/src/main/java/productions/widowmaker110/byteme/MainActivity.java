package productions.widowmaker110.byteme;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import Library.ByteMe;

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
        b.setAllocationMaxManually(1000);
        b.setAlgorithm(ByteMe.ALGORITHM_LRU);
        Log.d("" + this.getClass().getName(), "" + b.convertBitToMb(b.getAllocationMax()));

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
        }
    }

    private void fillArray(){
        for(int i = 0; i < 100; i++)
        {
            ExampleArray.add(new ExampleObject(randomString(10), rnd.nextInt(), randomString(20), randomString(4), randomString(20)));
        }
    }

    private String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
