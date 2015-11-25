package Library;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * ByteMe.java
 *
 * This class calculates the bits within any given object by instanitating the run() function
 * with an array of a given primitive type or bitmap.
 *
 * Created by Alexander on 10/17/2015.
 *
 * Cache algorithms available:
 * -Least Recently Used (LinkedList)
 * -Least Frequently Used (LinkedHashMap)
 * -First In First Out (Queue)
 *
 * http://javalandscape.blogspot.com/2009/01/cachingcaching-algorithms-and-caching.html
 * http://www.coderanch.com/how-to/java/CachingStrategies
 *
 * Least Recently Used 2: objects must be used twice to be cached.
 * Adaptive Replacement Cache: Combination of LRU and LFU; two LRU lists, one of objects seen once "recently", and one contains entries seen twice or more "recently"
 * Most Recently Used: Remove the most recently used object
 */
public class ByteMe {

    /**
     * developerMode is a boolean variable used to turn on or off the
     * Log.d messages of every action within the Library. Mostly used for
     * debugging.
     *
     * true = messages on.
     * false = messages off.
     */
    private boolean developerMode = false;

    //================================================
    // <Cache Algorithms>
    //================================================

    /**
     * LRU Cache code.
     *
     * http://www.programcreek.com/2013/03/leetcode-lru-cache-java/
     */
    public class LRU_Cache
    {
        // Node which will hold each object
        private class Node{
            int key;
            Object value;
            Node pre;
            Node next;

            public Node(int key, Object value){
                this.key = key;
                this.value = value;
            }
        }

        // Actual cache
        HashMap<Integer, Node> map = new HashMap<Integer, Node>();
        Node head=null;
        Node end=null;

        /**
         * Empty Constructor
         */
        public LRU_Cache() {}

        public Object get(int key) {
            if(map.containsKey(key)){
                Node n = map.get(key);
                remove(n);
                setHead(n);

                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"LRU, get(), removed from position and set as head. Value: " + n.value);
                }

                return n.value;
            }

            return -1;
        }

        public void remove(Node n){
            if(n.pre!=null){
                n.pre.next = n.next;
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"LRU, remove(), set previous node to next node.");
                }
            }else{
                head = n.next;
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"LRU, remove(), set head node to next node.");
                }
            }

            if(n.next!=null){
                n.next.pre = n.pre;
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"LRU, remove(), set next node to previous node.");
                }
            }else{
                end = n.pre;
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"LRU, remove(), set end node to previous node.");
                }
            }

        }

        public void setHead(Node n){
            n.next = head;
            n.pre = null;

            if(head!=null)
                head.pre = n;

            head = n;

            if(end ==null)
                end = head;

            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"LRU, setHead(), set node as head of list.");
            }
        }

        /**
         * Add
         *
         * Add takes the object and adds it to the head of the linked list.
         * If the Linked List has reached the max allocation, it will remove the last node in the list
         * and make the new value the head.
         * @param key hashcode value of the value being added
         * @param value custom object given by the programmer
         */
        public void add(int key, Object value) {

            // Replace the node if its already in the linked list.
            if(map.containsKey(key)){

                Node old = map.get(key);

                int oldBitValue = getTotalBitOfSingleObject(value);
                setAllocation_current(getAllocation_current() - oldBitValue);

                old.value = value;

                int newBitValue = getTotalBitOfSingleObject(value);
                setAllocation_current(getAllocation_current() + newBitValue);

                remove(old);
                setHead(old);

                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"LRU, add(), found node with the same key, replaced the value. Made it the head of the list");
                }
            }
            // create a new one if it doesn't exist
            else{
                Node created = new Node(key, value);

                if(getAllocation_current() >= getAllocationMax()){

                    int oldBitValue = getTotalBitOfSingleObject(end.value);
                    setAllocation_current(getAllocation_current() - oldBitValue);

                    map.remove(end.key);
                    remove(end);
                    setHead(created);

                    int newBitValue = getTotalBitOfSingleObject(value);
                    setAllocation_current(getAllocation_current() + newBitValue);

                    if(developerMode)
                    {
                        Log.d(""+this.getClass().getName(),"LRU, add(), created a new node and made it the head of the list. Had to remove the tail due to going over max allocation.");
                    }
                }else{
                    setHead(created);
                    int newBitValue = getTotalBitOfSingleObject(value);
                    setAllocation_current(getAllocation_current() + newBitValue);

                    if(developerMode)
                    {
                        Log.d(""+this.getClass().getName(),"LRU, add(), created a new node and made it the head of the list.");
                    }
                }

                map.put(key, created);
            }
        }
    }

    /**
     * FIFO Cache code.
     */
    public class FIFO_Cache
    {
        Queue fifo_cache = new LinkedList();

        /**
         * empty constructor
         */
        public FIFO_Cache(){}

        /**
         * add
         *
         * Add takes the object and adds it to the head of the queue.
         * If the queue has reached the max allocation, it will remove the first node in the queue
         * and make the new value the head.
         * @param obj custom object.
         */
        public void add(Object obj)
        {
            int cacheOfObject = getTotalBitOfSingleObject(obj);
            if(getAllocation_current() >= getAllocationMax())
            {
                // remove the head of the queue.
                fifo_cache.remove();
                boolean flag = fifo_cache.offer(obj);
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"FIFO, add(), cache too big, removed head, added object if true: " + flag);
                }
                setAllocation_current(getAllocation_current() + cacheOfObject);
            }
            else
            {
                boolean flag = fifo_cache.offer(obj);
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"FIFO, add(), added object if true: " + flag);
                }
                setAllocation_current(getAllocation_current() + cacheOfObject);
            }
        }

        /**
         * getHead
         * @return Object of the head of the queue.
         */
        public Object getHead()
        {
            return fifo_cache.peek();
        }

        /**
         * removeHead
         *
         * Removes the head node of the queue.
         */
        public void removeHead()
        {
            fifo_cache.poll();
        }
    }

    /**
     * LFU Class
     *
     * Cache design inspired by http://stackoverflow.com/questions/21117636/how-to-implement-a-least-frequently-used-lfu-cache
     */
    public class LFU_Cache
    {
        private class CacheEntry
        {
            private Object data;
            private int frequency;

            // default constructor
            private CacheEntry()
            {}

            public Object getData() {
                return data;
            }
            public void setData(Object data) {
                this.data = data;
            }

            public int getFrequency() {
                return frequency;
            }
            public void setFrequency(int frequency) {
                this.frequency = frequency;
            }

        }

        /* LinkedHashMap is used because it has features of both HashMap and LinkedList.
         * Thus, we can get an entry in O(1) and also, we can iterate over it easily.
         */
        private LinkedHashMap<Integer, CacheEntry> cacheMap = new LinkedHashMap<Integer, CacheEntry>();

        public LFU_Cache(){}

        public void addCacheEntry(int key, Object data)
        {
            if(getAllocation_current() >= getAllocationMax())
            {
                CacheEntry temp = new CacheEntry();
                temp.setData(data);
                temp.setFrequency(0);

                cacheMap.put(key, temp);
            }
            else
            {
                int entryKeyToBeRemoved = getLFUKey();
                cacheMap.remove(entryKeyToBeRemoved);

                CacheEntry temp = new CacheEntry();
                temp.setData(data);
                temp.setFrequency(0);

                cacheMap.put(key, temp);
            }
        }

        public int getLFUKey()
        {
            int key = 0;
            int minFreq = Integer.MAX_VALUE;

            for(Map.Entry<Integer, CacheEntry> entry : cacheMap.entrySet())
            {
                if(minFreq > entry.getValue().frequency)
                {
                    key = entry.getKey();
                    minFreq = entry.getValue().frequency;
                }
            }

            return key;
        }

        public Object getCacheEntry(int key)
        {
            if(cacheMap.containsKey(key))  // cache hit
            {
                CacheEntry temp = cacheMap.get(key);
                temp.frequency++;
                cacheMap.put(key, temp);
                return temp.data;
            }
            return null; // cache miss
        }
    }

    public MRU_Cache
    {
        // Node which will hold each object
        private class Node{
            int key;
            Object value;
            Node pre;
            Node next;

            public Node(int key, Object value){
                this.key = key;
                this.value = value;
            }
        }

        // Actual cache
        HashMap<Integer, Node> map = new HashMap<Integer, Node>();
        Node head=null;
        Node end=null;

        /**
         * Empty Constructor
         */
        public MRU_Cache() {}

        public Object get(int key) {
            if(map.containsKey(key)){
                Node n = map.get(key);
                remove(n);
                setHead(n);

                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"MRU, get(), removed from position and set as head. Value: " + n.value);
                }

                return n.value;
            }

            return -1;
        }

        public void remove(Node n){
            if(n.pre!=null){
                n.pre.next = n.next;
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"MRU, remove(), set previous node to next node.");
                }
            }else{
                head = n.next;
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"MRU, remove(), set head node to next node.");
                }
            }

            if(n.next!=null){
                n.next.pre = n.pre;
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"MRU, remove(), set next node to previous node.");
                }
            }else{
                end = n.pre;
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"MRU, remove(), set end node to previous node.");
                }
            }

        }

        public void setHead(Node n){
            n.next = head;
            n.pre = null;

            if(head!=null)
                head.pre = n;

            head = n;

            if(end ==null)
                end = head;

            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"MRU, setHead(), set node as head of list.");
            }
        }

        /**
         * Add
         *
         * Add takes the object and adds it to the head of the linked list.
         * If the Linked List has reached the max allocation, it will remove the last node in the list
         * and make the new value the head.
         * @param key hashcode value of the value being added
         * @param value custom object given by the programmer
         */
        public void add(int key, Object value) {

            // Replace the node if its already in the linked list.
            if(map.containsKey(key)){

                Node old = map.get(key);

                int oldBitValue = getTotalBitOfSingleObject(value);
                setAllocation_current(getAllocation_current() - oldBitValue);

                old.value = value;

                int newBitValue = getTotalBitOfSingleObject(value);
                setAllocation_current(getAllocation_current() + newBitValue);

                remove(old);
                setHead(old);

                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"MRU, add(), found node with the same key, replaced the value. Made it the head of the list");
                }
            }
            // create a new one if it doesn't exist
            else{
                Node created = new Node(key, value);

                if(getAllocation_current() >= getAllocationMax()){

                    int oldBitValue = getTotalBitOfSingleObject(head.value);
                    setAllocation_current(getAllocation_current() - oldBitValue);

                    map.remove(head.key);
                    remove(head);
                    setHead(created);

                    int newBitValue = getTotalBitOfSingleObject(value);
                    setAllocation_current(getAllocation_current() + newBitValue);

                    if(developerMode)
                    {
                        Log.d(""+this.getClass().getName(),"MRU, add(), created a new node and made it the head of the list. Had to remove the head due to going over max allocation.");
                    }
                }else{
                    setHead(created);
                    int newBitValue = getTotalBitOfSingleObject(value);
                    setAllocation_current(getAllocation_current() + newBitValue);

                    if(developerMode)
                    {
                        Log.d(""+this.getClass().getName(),"MRU, add(), created a new node and made it the head of the list.");
                    }
                }

                map.put(key, created);
            }
        }
    }

    // LRU2 Class

    // ARC Class

    //================================================
    // </Cache Algorithms>
    //================================================


    //================================================
    // <Global Variables>
    //================================================
    private static ByteMe instance = null;
    private static Context mContext;

    private LRU_Cache lru_cache;
    private FIFO_Cache fifo_cache;
    private LFU_Cache lfu_cache;
    private MRU_Cache mru_cache;

    private int allocation_max;
    private int allocation_current;

    // used to say how much of the device memory the cache so take max
    public static int RAM_ONE_FOURTH = 4;
    public static int RAM_ONE_FIFTH = 5;
    public static int RAM_ONE_SIXTH = 6;
    public static int RAM_ONE_SEVENTH = 7;
    public static int RAM_ONE_EIGHTH = 8;
    public static int RAM_ONE_TENTH = 10;
    public static int RAM_ONE_TWELFTH = 12;

    private static long android_total_ram;

    private int Kb = 1024;
    private int Mb = 1024 * 1024;
    private int Gb = 1024 * 1024 * 1024;

    // static variable to say which algorithm the objects will be held in.
    public int CHOSEN_ALGORITHM = 0;
    public static int ALGORITHM_LRU = 1;
    public static int ALGORITHM_FIFO = 2;
    public static int ALGORITHM_LFU = 3;
    public static int ALGORITHM_LRU2 = 4;
    public static int ALGORITHM_ARC = 5;
    public static int ALGORITHM_MRU = 6;

    // list of method names which are java API based.
    private List<String> restrictedMethods = Arrays.asList("equals","getClass",
            "hashCode", "notify", "notifyAll", "toString", "wait");

    /**
     * Empty Constructor
     */
    public ByteMe() {}

    /**
     * Constructor with Context in order to perform some needed operations.
     */
    public ByteMe(Context context) {
        mContext = context;
    }

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

    /**
     * examine
     *
     * examine figures out what type the custom object component is in a loop.
     * After figuring out which type it is, it figures out the byte allocation of the object.
     *
     * @param obj Any object within the Java API. The supported types are
     *            Int
     *            String
     *            Short
     *            Byte
     *            Long
     *            Float
     *            Double
     *            Char
     *            Boolean
     *            Bitmap
     */
    public int getTotalBitOfSingleObject(Object obj) {

        //Get the list of possible methods held in this object.
        Method[] methods = obj.getClass().getMethods();

        // Keep track of how many bytes is being used
        int bytesUsed = 0;

        // iterate through them
        for (Method method : methods) {

            // see if the method name is restricted
            if (!isRestrictedMethod(method.getName())) {

                // switch through the possible primitive types and bitmap methods.
                // get the amount of bytes of each type within the custom object.
                switch (method.getReturnType().toString()) {

                    case "class java.lang.String":

                        try {
                            String temp = (String) method.invoke(obj);
                            int tempInt = run(new String[]{temp});
                            bytesUsed += tempInt;

                            if(developerMode)
                            {
                                Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), class java.lang.String. Value: " + temp+ "  byte amount: " + tempInt);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "int":

                        try {
                            int temp = (int) method.invoke(obj);
                            int tempInt = run(new int[]{temp});
                            bytesUsed += tempInt;
                            if(developerMode)
                            {
                                Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), int. Value: " + temp+ "  byte amount: " + tempInt);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "class java.lang.Short":

                        try {
                            short temp = (short) method.invoke(obj);
                            int tempInt = run(new short[]{temp});
                            bytesUsed += tempInt;
                            if(developerMode)
                            {
                                Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), class java.lang.Short. Value: " + temp+ "  byte amount: " + tempInt);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "class java.lang.Long":

                        try {
                            long temp = (long) method.invoke(obj);
                            int tempInt = run(new long[]{temp});
                            bytesUsed += tempInt;
                            if(developerMode)
                            {
                                Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), class java.lang.Long. Value: " + temp+ "  byte amount: " + tempInt);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "class java.lang.Byte":

                        try {
                            byte temp = (byte) method.invoke(obj);
                            int tempInt = run(new byte[]{temp});
                            bytesUsed += tempInt;
                            if(developerMode)
                            {
                                Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), class java.lang.Byte. Value: " + temp+ "  byte amount: " + tempInt);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "class java.lang.Float":

                        try {
                            float temp = (float) method.invoke(obj);
                            int tempInt = run(new float[]{temp});
                            bytesUsed += tempInt;
                            if(developerMode)
                            {
                                Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), class java.lang.Float. Value: " + temp+ "  byte amount: " + tempInt);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "class java.lang.Double":

                        try {
                            double temp = (double) method.invoke(obj);
                            int tempInt = run(new double[]{temp});
                            bytesUsed += tempInt;
                            if(developerMode)
                            {
                                Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), class java.lang.Double. Value: " + temp+ "  byte amount: " + tempInt);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "class java.lang.Char":

                        try {
                            char temp = (char) method.invoke(obj);
                            int tempInt = run(new char[]{temp});
                            bytesUsed += tempInt;
                            if(developerMode)
                            {
                                Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), class java.lang.Char. Value: " + temp+ "  byte amount: " + tempInt);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "class java.lang.Boolean":

                        try {
                            boolean temp = (boolean) method.invoke(obj);
                            int tempInt = run(new boolean[]{temp});
                            bytesUsed += tempInt;
                            if(developerMode)
                            {
                                Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), class java.lang.Boolean. Value: " + temp+ "  byte amount: " + tempInt);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "class android.graphics.Bitmap":

                        try {
                            Bitmap temp = (Bitmap) method.invoke(obj);
                            int tempInt = run(new Bitmap[]{temp});
                            bytesUsed += tempInt;
                            if(developerMode)
                            {
                                Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), class android.graphics.Bitmap. Value: " + temp+ "  byte amount: " + tempInt);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        break;
                }
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"ByteMe, getTotalBitOfSingleObject(), bytesUsed Value: " + bytesUsed);
                }
            }
        }
        return bytesUsed;
    }

    /**
     * isRestrictedMethod
     *
     * isRestrictedMethod returns boolean value saying whether or not the name of the method
     * is made by the programmer or if it came from a java API.
     *
     * @param method This is the name of the method.
     * @return True if the method name is a non-java base API (toString, hashCode, etc).
     */
    private boolean isRestrictedMethod(String method) {
        if(restrictedMethods.contains(method))
        {
            return true;
        }
        return false;
    }

    /**
     * clearCache
     *
     * clearCache sets the cache being used to null, then sets the current allocation
     * amount to zero, and finally remakes an instance of the same cache.
     */
    public void clearCache() {

        if (getAlgorithm() == ALGORITHM_LRU)
        {
            lru_cache = null;
            setAllocation_current(0);
            lru_cache = new LRU_Cache();
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, clearCache(), LRU cache cleared, allocation_current set to 0, and an instance of LRU made again.");
            }
        }
        else if(getAlgorithm() == ALGORITHM_FIFO)
        {
            fifo_cache = null;
            setAllocation_current(0);
            fifo_cache = new FIFO_Cache();
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, clearCache(), FIFO cache cleared, allocation_current set to 0, and an instance of FIFO made again.");
            }
        }
        else if(getAlgorithm() == ALGORITHM_MRU)
        {
            mru_cache = null;
            setAllocation_current(0);
            mru_cache = new MRU_Cache();
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, clearCache(), MRU cache cleared, allocation_current set to 0, and an instance of MRU made again.");
            }
        }
        else if(getAlgorithm() == ALGORITHM_LFU)
        {
            lfu_cache = null;
            setAllocation_current(0);
            lfu_cache = new LFU_Cache();
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, clearCache(), LFU cache cleared, allocation_current set to 0, and an instance of LFU made again.");
            }
        }
    }

    /**
     * addToCache
     *
     * addToCache sends a single object into the cache which was already chosen.
     *
     * @param obj Custom object being added to the cache.
     */
    public void addToCache(Object obj) {
        addObjectToCache(obj);
    }

    /**
     * addToCache
     *
     * addToCache sends an array of objects into the cache which was already chosen.
     *
     * @param obj Custom object array being added to the cache.
     */
    public void addToCache(Object[] obj) {
        for(int i = 0; i < obj.length; i++)
        {
            addObjectToCache(obj[i]);
        }
    }

    /**
     * addToCache
     *
     * addToCache sends an arraylist of objects into the cache which was already chosen.
     *
     * @param obj Custom object arraylist being added to the cache.
     */
    public void addToCache(ArrayList<Object> obj) {
        for(int i = 0; i < obj.size(); i++)
        {
            addObjectToCache(obj.get(i));
        }
    }

    /**
     * addToCache
     *
     * addToCache sends a list of object into the cache which was already chosen.
     *
     * @param obj Custom list of objects being added to the cache.
     */
    public void addToCache(List<Object> obj) {
        for(int i = 0; i < obj.size(); i++)
        {
            addObjectToCache(obj.get(i));
        }
    }

    //================================================
    // <Asynchronous Methods>
    //================================================
    /**
     *  Run begins the multithreading process of calculating the bits in a given array of ints.
     *
     * @param int_data a static array of ints of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    public int run(int[] int_data) {
        if(int_data == null)
        {
            return -2;
        }

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
    // <Get/Set Methods>
    //================================================
    public Object[] getAllFromCache() {
        //TODO work on this
        Object[] arrayOfObjects = {};
        return null;
    }

    public void setAllocationMax(int allocation_max_m) {
        int tempMaxMemory = (int) getTotal_ram();
        this.allocation_max = tempMaxMemory / allocation_max_m;
        if(developerMode)
        {
            Log.d(""+this.getClass().getName(),"ByteMe, setAllocationMax(), Allocation Max: " + this.allocation_max);
        }
    }

    public int getAllocationMax()
    {
        return this.allocation_max;
    }

    public int getAllocation_current() {
        return allocation_current;
    }

    private void setAllocation_current(int allocation_current) {
        this.allocation_current = allocation_current;
        if(developerMode)
        {
            Log.d(""+this.getClass().getName(),"ByteMe, setAllocationMax(), Allocation Max: " + this.allocation_current);
        }
    }

    public long getTotal_ram() {
        // get the current SDK level of the device
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        // if API 15 or lower
        if (currentapiVersion <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
            android_total_ram = getTotalMemoryOld();
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, getTotal_ram(), SDK level 15 or lower.");
            }
        }
        // API is 16 or higher
        else{
            android_total_ram = getTotalMemoryNew();
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, getTotal_ram(), SDK level 16 or higher.");
            }
        }
        return android_total_ram;
    }

    /**
     * getTotalMemoryOld
     *
     * getTotalMemoryOld is a method which will return the long byte value of TOTAL ram.
     * This style is meant for pre-16 API.
     *
     * http://stackoverflow.com/questions/12551547/is-there-a-way-to-get-total-device-rami-need-it-for-an-optimization
     * @return Byte value of total ram.
     */
    public static long getTotalMemoryOld() {

        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(    localFileReader, 8192);
            str2 = localBufferedReader.readLine();//meminfo
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            //total Memory
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();
            return initial_memory;
        }
        catch (IOException e)
        {
            return -1;
        }
    }

    /**
     * getTotalMemoryOld
     *
     * getTotalMemoryOld is a method which will return the long byte value of TOTAL ram.
     * This style is meant for 16 API and above.
     *
     * http://stackoverflow.com/questions/12551547/is-there-a-way-to-get-total-device-rami-need-it-for-an-optimization
     * @return Byte value of total ram.
     */
    public static long getTotalMemoryNew() {

        ActivityManager actManager = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        return memInfo.totalMem;
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        ByteMe.mContext = mContext;
    }

    public void setAlgorithm(int algorithm){
        if(algorithm == ALGORITHM_LRU)
        {
            lru_cache = new LRU_Cache();
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, setAlgorithm(), LRU cache chosen. Instance of cache created.");
            }
        }
        else if(algorithm == ALGORITHM_FIFO)
        {
            fifo_cache = new FIFO_Cache();
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, setAlgorithm(), FIFO cache chosen. Instance of cache created.");
            }
        }
        else if(algorithm == ALGORITHM_LFU)
        {
            lfu_cache = new LFU_Cache();
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, setAlgorithm(), LFU cache chosen. Instance of cache created.");
            }
        }
        else if(algorithm == ALGORITHM_MRU)
        {
            mru_cache = MRU_Cache();
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, setAlgorithm(), MRU cache chosen. Instance of cache created.");
            }
        }
        this.CHOSEN_ALGORITHM = algorithm;
    }

    public int getAlgorithm()
    {
        return CHOSEN_ALGORITHM;
    }
    //================================================
    // </Get/Set Methods>
    //================================================

    //================================================
    // <General Cache Functions>
    //================================================

    private void addObjectToCache(Object obj) {
        if(getAlgorithm() == ALGORITHM_LRU)
        {
            lru_cache.add(obj.hashCode(), obj);
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, addObjectToCache(), Object added to LRU cache.");
            }
        }
        else if(getAlgorithm() == ALGORITHM_FIFO)
        {
            fifo_cache.add(obj);
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, addObjectToCache(), Object added to FIFO cache.");
            }
        }
        else if(getAlgorithm() == ALGORITHM_LFU)
        {
            lfu_cache.addCacheEntry(obj.hashCode(), obj);
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, addObjectToCache(), Object added to LFU cache.");
            }
        }
        else if(getAlgorithm() == ALGORITHM_MRU)
        {
            mru_cache.addCacheEntry(obj.hashCode(), obj);
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, addObjectToCache(), Object added to MRU cache.");
            }
        }
    }

    public Object getObjectFromCache(int hashcode) {
        Object obj = null;

        if(getAlgorithm() == ALGORITHM_LRU)
        {
            obj = lru_cache.get(hashcode);
        }
        else if(getAlgorithm() == ALGORITHM_FIFO)
        {
            obj = fifo_cache.getHead();
        }
        //TODO write get method for LFU and MRU?

        return obj;
    }
    //================================================
    // </General Cache Functions>
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
