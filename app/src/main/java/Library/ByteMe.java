package Library;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

/**
 * Created by Alexander on 10/17/2015.
 */
public class ByteMe {

    //================================================
    // <Global Variables>
    //================================================
    private static ByteMe instance = null;
    private int allocation_max;
    private int Kb = 1024;
    private int Mb = 1024 * 1024;
    private int Gb = 1024 * 1024 * 1024;

    /**
     * Empty Constructor
     */
    public ByteMe() {}

    /**
     * Context instance get method.
     */
    public static ByteMe getInstance(){
        if(instance == null)
        {
            instance = new ByteMe();
        }
        return instance;
    }

    //================================================
    // <Asynchronous Methods>
    //================================================

    public int run(int[] int_data,
                       String[] string_data,
                       short[] short_data,
                       long[] long_data,
                       byte[] byte_data,
                       float[] float_data,
                       double[] double_data,
                       char[] char_data,
                       boolean[] boolean_data,
                       Bitmap[] bitmap_data) {

        //TODO write this function to have all of the threads
        //TODO ask if the array is null first, then create the threads
        //TODO and start them. Make them join at the end with the combined
        //TODO bit value.


        // Initialize the multithreading class with the data
        // and settings
        if(int_data != null) {
            CalculateSize int_thread = new CalculateSize(0, int_data,
                    null, null, null, null, null, null, null, null, null);
            Thread thread1 = new Thread();
        }

        if(string_data != null) {
            CalculateSize string_thread = new CalculateSize(1, null,
                    string_data, null, null, null, null, null, null, null, null);
        }

        CalculateSize short_thread = new CalculateSize(2, null,
                null, short_data, null, null, null, null, null, null, null);

        CalculateSize long_thread = new CalculateSize(3, null,
                null, null, long_data, null, null, null, null, null, null);

        CalculateSize byte_thread = new CalculateSize(4, null,
                null, null, null, byte_data, null, null, null, null, null);

        CalculateSize float_thread = new CalculateSize(5, null,
                null, null, null, null, float_data, null, null, null, null);

        CalculateSize double_thread = new CalculateSize(6, null,
                null, null, null, null, null, double_data, null, null, null);

        CalculateSize char_thread = new CalculateSize(7, null,
                null, null, null, null, null, null, char_data, null, null);

        CalculateSize boolean_thread = new CalculateSize(8, null,
                null, null, null, null, null, null, null, boolean_data, null);

        CalculateSize bitmap_thread = new CalculateSize(9, null,
                null, null, null, null, null, null, null, null, bitmap_data);



        Thread thread1 = new Thread(foo);
        thread1.start();
        int value = 0;

        try
        {
            thread1.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }
        return value;
    }

    /**
     *  Run begins the multithreading process of calculating the bits in a given array of ints.
     *
     * @param int_data a static array of ints of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    public int run(int[] int_data)
    {
        // Initialize the multithreading class with the data
        // and settings
        CalculateSize foo = new CalculateSize(0, int_data,
                null, null, null, null, null, null, null, null, null);

        Thread thread = new Thread(foo);
        thread.start();
        int value = 0;

        try
        {
            thread.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }
        return value;
    }

    public int run(String[] string_data) {

        // Initialize the multithreading class with the data
        // and settings
        CalculateSize foo = new CalculateSize(1, null,
                string_data, null, null, null, null, null, null, null, null);

        Thread thread = new Thread(foo);
        thread.start();
        int value = 0;

        try
        {
            thread.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }

        return value;
    }

    public int run(short[] short_data) {
        // Initialize the multithreading class with the data
        // and settings
        CalculateSize foo = new CalculateSize(2, null,
                null, short_data, null, null, null, null, null, null, null);

        Thread thread = new Thread(foo);
        thread.start();
        int value = 0;

        try
        {
            thread.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }
        return value;
    }

    public int run(long[] long_data) {
        // Initialize the multithreading class with the data
        // and settings
        CalculateSize foo = new CalculateSize(3, null,
                null, null, long_data, null, null, null, null, null, null);

        Thread thread = new Thread(foo);
        thread.start();
        int value = 0;

        try
        {
            thread.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }
        return value;
    }

    public int run(byte[] byte_data) {
        // Initialize the multithreading class with the data
        // and settings
        CalculateSize foo = new CalculateSize(4, null,
                null, null, null, byte_data, null, null, null, null, null);

        Thread thread = new Thread(foo);
        thread.start();
        int value = 0;

        try
        {
            thread.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }
        return value;
    }

    public int run(float[] float_data) {
        // Initialize the multithreading class with the data
        // and settings
        CalculateSize foo = new CalculateSize(5, null,
                null, null, null, null, float_data, null, null, null, null);

        Thread thread = new Thread(foo);
        thread.start();
        int value = 0;

        try
        {
            thread.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }
        return value;
    }

    public int run(double[] double_data) {
        // Initialize the multithreading class with the data
        // and settings
        CalculateSize foo = new CalculateSize(6, null,
                null, null, null, null, null, double_data, null, null, null);

        Thread thread = new Thread(foo);
        thread.start();
        int value = 0;

        try
        {
            thread.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }
        return value;
    }

    public int run(char[] char_data) {
        // Initialize the multithreading class with the data
        // and settings
        CalculateSize foo = new CalculateSize(7, null,
                null, null, null, null, null, null, char_data, null, null);

        Thread thread = new Thread(foo);
        thread.start();
        int value = 0;

        try
        {
            thread.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }
        return value;
    }

    public int run(boolean[] boolean_data) {
        // Initialize the multithreading class with the data
        // and settings
        CalculateSize foo = new CalculateSize(8, null,
                null, null, null, null, null, null, null, boolean_data, null);

        Thread thread = new Thread(foo);
        thread.start();
        int value = 0;

        try
        {
            thread.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }
        return value;
    }

    public int run(Bitmap[] bitmap_data) {
        // Initialize the multithreading class with the data
        // and settings
        CalculateSize foo = new CalculateSize(9, null,
                null, null, null, null, null, null, null, null, bitmap_data);

        Thread thread = new Thread(foo);
        thread.start();
        int value = 0;

        try
        {
            thread.join();
            value = foo.getValue();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            value = -1;
            Log.d("" + this.getClass().getName(), "Error: ByteMe Asynchronous Constructor: " + e.getLocalizedMessage().toString());
        }
        return value;
    }
    //================================================
    // </Asynchronous Methods>
    //================================================


    //================================================
    // <Synchronous Methods>
    //================================================

    public void ByteMeSynchronous(int[] int_data) {
    }

    public void ByteMeSynchronous(String[] string_data) {
    }

    public void ByteMeSynchronous(Short[] short_data) {
    }

    public void ByteMeSynchronous(Long[] long_data) {
    }

    public void ByteMeSynchronous(Byte[] byte_data) {
    }

    public void ByteMeSynchronous(Float[] float_data) {
    }

    public void ByteMeSynchronous(Double[] double_data) {
    }

    public void ByteMeSynchronous(char[] char_data) {
    }

    public void ByteMeSynchronous(boolean[] boolean_data) {
    }

    public void ByteMeSynchronous(Bitmap[] bitmap_data) {
    }
    //================================================
    // </Synchronous Methods>
    //================================================


    //================================================
    // <Get/Set Methods>
    //================================================
    public void setAllocationMax(int allocation_max_m)
    {
        this.allocation_max = allocation_max_m;
    }

    public int getAllocationMax()
    {
        return this.allocation_max;
    }
    //================================================
    // </Get/Set Methods>
    //================================================


    //================================================
    // <Conversion Methods>
    //================================================
    public int convertBitToKb(int value)
    {
        return value/Kb;
    }

    public int convertBitToMb(int value)
    {
        return value/Mb;
    }

    public int convertBitToGb(int value)
    {
        return value/Gb;
    }
    //================================================
    // </Conversion Methods>
    //================================================


    //================================================
    // <Async Calculation Method>
    //================================================
    public class CalculateSize implements Runnable {

        private volatile int byteAmount;
        private volatile int algorithm_type;

        private volatile int[] int_data;
        private volatile String[] string_data;
        private volatile short[] short_data;
        private volatile long[] long_data;
        private volatile byte[] byte_data;
        private volatile float[] float_data;
        private volatile double[] double_data;
        private volatile char[] char_data;
        private volatile boolean[] boolean_data;
        private volatile Bitmap[] bitmap_data;

        public CalculateSize(int alogorithm,
                             int[] int_data,
                             String[] string_data,
                             short[] short_data,
                             long[] long_data,
                             byte[] byte_data,
                             float[] float_data,
                             double[] double_data,
                             char[] char_data,
                             boolean[] boolean_data,
                             Bitmap[] bitmap_data)
        {
            this.algorithm_type = alogorithm;
            this.int_data = int_data;
            this.string_data = string_data;
            this.short_data = short_data;
            this.long_data = long_data;
            this.byte_data = byte_data;
            this.float_data = float_data;
            this.double_data = double_data;
            this.char_data = char_data;
            this.boolean_data = boolean_data;
            this.bitmap_data = bitmap_data;
        }

        @Override
        public void run()
        {
            /**
             * The reason why there isn't an 'All'
             * option is due to the idea that for this
             * to be a true multithreading function,
             * the programmer should have all of the
             * data types running at the same time.
             *
             * algorithm_type:
             * 0 - int
             * 1 - string
             * 2 - short
             * 3 - long
             * 4 - byte
             * 5 - float
             * 6 - double
             * 7 - char
             * 8 - boolean
             * 9 - bitmap
             */
            switch (algorithm_type)
            {
                default:
                    break;
                case 0:
                    String binary_temp = "";

                    for(int i = 0; i < int_data.length; i++)
                    {
                        binary_temp += Integer.toString(int_data[i],2);
                    }

                    byteAmount = binary_temp.length();

                    break;
                case 1:
                    StringBuilder  binary = new StringBuilder();
                    for(int i = 0; i < string_data.length; i++)
                    {
                        byte[] bytes = string_data[i].getBytes();
                        binary = new StringBuilder();
                        for (byte b : bytes)
                        {
                            int val = b;
                            for (int m = 0; m < 8; m++)
                            {
                                binary.append((val & 128) == 0 ? 0 : 1);
                                val <<= 1;
                            }
                        }
                    }

                    byteAmount = binary.length();
                    break;
                case 2:

                    String binary_short = "";
                    for(int i = 0; i < short_data.length; i++)
                    {
                        binary_short += Integer.toString(0xFFFF & short_data[i], 2);
                    }

                    byteAmount = binary_short.length();
                    break;
                case 3:

                    String binary_long = "";
                    for(int i = 0; i < long_data.length; i++)
                    {
                        binary_long += Long.toString(long_data[i], 2);
                    }

                    byteAmount = binary_long.length();
                    break;
                case 4:

                    String binary_byte = "";
                    for(int i = 0; i < byte_data.length; i++)
                    {
                        binary_byte += String.format("%8s", Integer.toBinaryString(byte_data[i] & 0xFF)).replace(' ', '0');
                    }

                    byteAmount = binary_byte.length();
                    break;
                case 5:

                    String binary_float = "";
                    for(int i = 0; i < float_data.length; i++)
                    {
                        binary_float += Integer.toString(Float.floatToIntBits(float_data[i]), 2);
                    }

                    byteAmount = binary_float.length();
                    break;
                case 6:

                    String binary_double = "";
                    for(int i = 0; i < double_data.length; i++)
                    {
                        long bits = Double.doubleToLongBits(double_data[i]);
                        binary_double += Long.toString(bits, 2);
                    }

                    byteAmount = binary_double.length();
                    break;
                case 7:

                    String binary_char = "";
                    for(int i = 0; i < char_data.length; i++)
                    {
                        int value = (int)char_data[i];
                        binary_char += Integer.toString(value, 2);
                    }

                    byteAmount = binary_char.length();
                    break;
                case 8:

                    String binary_boolean = "";
                    for(int i = 0; i < boolean_data.length; i++)
                    {
                        int myInt = (boolean_data[i]) ? 1 : 0;
                        binary_boolean += Integer.toString(myInt, 2);
                    }

                    byteAmount = binary_boolean.length();
                    break;
                case 9:

                    int bitmap_temp = 0;
                    for(int i = 0; i < bitmap_data.length; i++)
                    {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
                            bitmap_temp += bitmap_data[i].getRowBytes() * bitmap_data[i].getHeight();
                        } else {
                            bitmap_temp += bitmap_data[i].getByteCount();
                        }
                    }

                    byteAmount = bitmap_temp;
                    break;
            }
        }

        public int getValue() {
            return byteAmount;
        }
    }
    //================================================
    // <Async Calculation Method>
    //================================================
}