package Library;

import android.graphics.Bitmap;
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
                       Short[] short_data,
                       Long[] long_data,
                       Byte[] byte_data,
                       Float[] float_data,
                       Double[] double_data,
                       char[] char_data,
                       boolean[] boolean_data,
                       Bitmap[] bitmap_data) {
        return 0;
    }

    /**
     *  Run begins the multithreading process of calculating the bits in a given array of ints.
     *
     * @param int_data a static array of ints of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted.
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
        return 0;
    }

    public int run(Short[] short_data) {
        return 0;
    }

    public int run(Long[] long_data) {
        return 0;
    }

    public int run(Byte[] byte_data) {
        return 0;
    }

    public int run(Float[] float_data) {
        return 0;
    }

    public int run(Double[] double_data) {
        return 0;
    }

    public int run(char[] char_data) {
        return 0;
    }

    public int run(boolean[] boolean_data) {
        return 0;
    }

    public int run(Bitmap[] bitmap_data) {
        return 0;
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
        private volatile Short[] short_data;
        private volatile Long[] long_data;
        private volatile Byte[] byte_data;
        private volatile Float[] float_data;
        private volatile Double[] double_data;
        private volatile char[] char_data;
        private volatile boolean[] boolean_data;
        private volatile Bitmap[] bitmap_data;

        public CalculateSize(int alogorithm,
                             int[] int_data,
                             String[] string_data,
                             Short[] short_data,
                             Long[] long_data,
                             Byte[] byte_data,
                             Float[] float_data,
                             Double[] double_data,
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
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
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