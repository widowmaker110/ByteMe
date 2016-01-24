/*
* The MIT License (MIT)
*
* Copyright (c) 2015 Alexander Miller
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

package WidowMaker110Library;

import android.annotation.TargetApi;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * ByteMe.java
 *
 * This class calculates the bits within any given object by instantiating the run() function
 * with an array of a given primitive type or bitmap.
 *
 * Created by Alexander on 10/17/2015.
 *
 * Cache algorithms available:
 * - First In First Out (Queue)
 * - Least Recently Used (LinkedList)
 * - Most Recently Used (LinkedList)
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

        public Object[] getAllObjects()
        {
            List<Object> array = new ArrayList<>();

            for ( Integer key : map.keySet() ) {
                array.add(get(key));
            }

            Object[] objArray = new Object[array.size()];

            return array.toArray(objArray);
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

                int cacheOfObject = getTotalBitOfSingleObject(value);

                while(getAllocation_current() + cacheOfObject >= getAllocationMax())
                {
                    removeLestRecentlyUsed();
                }

                setHead(created);
                setAllocation_current(getAllocation_current() + cacheOfObject);
                map.put(key, created);
            }
        }

        public void removeLestRecentlyUsed()
        {
            setAllocation_current(getAllocation_current() - getTotalBitOfSingleObject(end.value));
            map.remove(end.key);
            remove(end);
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

            while(getAllocation_current() + cacheOfObject >= getAllocationMax())
            {
                removeHead();
            }

            if(getAllocation_current() + cacheOfObject >= getAllocationMax())
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
            else {
                boolean flag = fifo_cache.offer(obj);
                if(developerMode)
                {
                    Log.d(""+this.getClass().getName(),"FIFO, add(), added object if true: " + flag);
                }
                setAllocation_current(getAllocation_current() + cacheOfObject);
            }
        }

        public Object[] getAllObjects() {

            List<Object> array = new ArrayList<>();

            for(Object s : fifo_cache) {
                array.add(s);
            }

            Object[] objArray = new Object[array.size()];

            return array.toArray(objArray);
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
            int cacheAmountOfObject = getTotalBitOfSingleObject(getHead());
            fifo_cache.poll();
            setAllocation_current(getAllocation_current() - cacheAmountOfObject);

            Log.d("" + this.getClass().getName(), "FIFO, removeHead(), Removing head, bit value of head: " + cacheAmountOfObject);
        }
    }

    /**
     * MRU Cache code.
     */
    public class MRU_Cache
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

        public Object[] getAllObjects()
        {
            List<Object> array = new ArrayList<>();

            for ( Integer key : map.keySet() ) {
                array.add(get(key));
            }

            Object[] objArray = new Object[array.size()];

            return array.toArray(objArray);
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

                int cacheOfObject = getTotalBitOfSingleObject(value);

                while(getAllocation_current() + cacheOfObject >= getAllocationMax())
                {
                    removeMostRecentlyUsed();
                }

                setHead(created);
                setAllocation_current(getAllocation_current() + getTotalBitOfSingleObject(value));
                map.put(key, created);
            }
        }

        public void removeMostRecentlyUsed()
        {
            setAllocation_current(getAllocation_current() - getTotalBitOfSingleObject(head.value));
            map.remove(head.key);
            Node newHead = head.next;
            remove(head);
            setHead(newHead);
        }
    }

    //================================================
    // <Global Variables>
    //================================================
    private static ByteMe instance = null;
    private static Context mContext;

    private LRU_Cache lru_cache;
    private FIFO_Cache fifo_cache;
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
    public static int ALGORITHM_MRU = 3;

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
     * Constructor with Context in order to perform some needed operations. Also
     * set the Algorithm instantly.
     */
    public ByteMe(Context context, int algorithm) {
        mContext = context;
        setAlgorithm(algorithm);
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
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            if(developerMode) {
                                String error_message = "";
                                error_message += "Error: NullPointer Exception raised. ";
                                error_message += "On Method " + method.getName() + " which appears to contain a null value.";
                                Log.e("ByteMe", error_message);
                                //e.printStackTrace();
                            }
                        }

                        break;
                    case "int":

                        try {
                            int temp = (int) method.invoke(obj);
                            int tempInt = run(new int[]{temp});
                            bytesUsed += tempInt;
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            if(developerMode) {
                                String error_message = "";
                                error_message += "Error: NullPointer Exception raised. ";
                                error_message += "On Method " + method.getName() + " which appears to contain a null value.";
                                Log.e("ByteMe", error_message);
                                //e.printStackTrace();
                            }
                        }

                        break;
                    case "class java.lang.Short":

                        try {
                            short temp = (short) method.invoke(obj);
                            int tempInt = run(new short[]{temp});
                            bytesUsed += tempInt;
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            if(developerMode) {
                                String error_message = "";
                                error_message += "Error: NullPointer Exception raised. ";
                                error_message += "On Method " + method.getName() + " which appears to contain a null value.";
                                Log.e("ByteMe", error_message);
                                //e.printStackTrace();
                            }
                        }

                        break;
                    case "class java.lang.Long":

                        try {
                            long temp = (long) method.invoke(obj);
                            int tempInt = run(new long[]{temp});
                            bytesUsed += tempInt;
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            if(developerMode) {
                                String error_message = "";
                                error_message += "Error: NullPointer Exception raised. ";
                                error_message += "On Method " + method.getName() + " which appears to contain a null value.";
                                Log.e("ByteMe", error_message);
                                //e.printStackTrace();
                            }
                        }

                        break;
                    case "class java.lang.Byte":

                        try {
                            byte temp = (byte) method.invoke(obj);
                            int tempInt = run(new byte[]{temp});
                            bytesUsed += tempInt;
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            if(developerMode) {
                                String error_message = "";
                                error_message += "Error: NullPointer Exception raised. ";
                                error_message += "On Method " + method.getName() + " which appears to contain a null value.";
                                Log.e("ByteMe", error_message);
                                //e.printStackTrace();
                            }
                        }

                        break;
                    case "class java.lang.Float":

                        try {
                            float temp = (float) method.invoke(obj);
                            int tempInt = run(new float[]{temp});
                            bytesUsed += tempInt;
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            if(developerMode) {
                                String error_message = "";
                                error_message += "Error: NullPointer Exception raised. ";
                                error_message += "On Method " + method.getName() + " which appears to contain a null value.";
                                Log.e("ByteMe", error_message);
                                //e.printStackTrace();
                            }
                        }

                        break;
                    case "class java.lang.Double":

                        try {
                            double temp = (double) method.invoke(obj);
                            int tempInt = run(new double[]{temp});
                            bytesUsed += tempInt;
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            if(developerMode) {
                                String error_message = "";
                                error_message += "Error: NullPointer Exception raised. ";
                                error_message += "On Method " + method.getName() + " which appears to contain a null value.";
                                Log.e("ByteMe", error_message);
                                //e.printStackTrace();
                            }
                        }

                        break;
                    case "class java.lang.Char":

                        try {
                            char temp = (char) method.invoke(obj);
                            int tempInt = run(new char[]{temp});
                            bytesUsed += tempInt;
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            if(developerMode) {
                                String error_message = "";
                                error_message += "Error: NullPointer Exception raised. ";
                                error_message += "On Method " + method.getName() + " which appears to contain a null value.";
                                Log.e("ByteMe", error_message);
                                //e.printStackTrace();
                            }
                        }

                        break;
                    case "class java.lang.Boolean":

                        try {
                            boolean temp = (boolean) method.invoke(obj);
                            int tempInt = run(new boolean[]{temp});
                            bytesUsed += tempInt;
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            if(developerMode) {
                                String error_message = "";
                                error_message += "Error: NullPointer Exception raised. ";
                                error_message += "On Method " + method.getName() + " which appears to contain a null value.";
                                Log.e("ByteMe", error_message);
                                //e.printStackTrace();
                            }
                        }

                        break;
                    case "class android.graphics.Bitmap":

                        try {
                            Bitmap temp = (Bitmap) method.invoke(obj);
                            int tempInt = run(new Bitmap[]{temp});
                            bytesUsed += tempInt;
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            if(developerMode) {
                                String error_message = "";
                                error_message += "Error: NullPointer Exception raised. ";
                                error_message += "On Method " + method.getName() + " which appears to contain a null value.";
                                Log.e("ByteMe", error_message);
                                //e.printStackTrace();
                            }
                        }

                        break;
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

    /**
     *  Run calculates the bits in a given array of ints.
     *
     * @param int_data a static array of ints of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    private int run(int[] int_data) {
        if(int_data == null)
        {
            if(developerMode)
                Log.e("ByteMe","Error: int array was null");
            return -2;
        }
        else {

            // Initialize the multithreading class with the data
            // and settings
            String binary_temp = "";

            for (int i = 0; i < int_data.length; i++) {
                binary_temp += Integer.toString(int_data[i], 2);
            }

            return correctBitsToByteFormat(binary_temp.length());
        }
    }

    /**
     *  Run calculates the bits in a given array of strings.
     *
     * @param string_data a static array of strings of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    private int run(String[] string_data) {

        if(string_data == null)
        {
            if(developerMode)
                Log.e("ByteMe","Error: run(String[]): string array was null");
            return -2;
        }
        else {

            StringBuilder binary = new StringBuilder();
            for (int i = 0; i < string_data.length; i++) {
                byte[] bytes = string_data[i].getBytes();
                binary = new StringBuilder();
                for (byte b : bytes) {
                    int val = b;
                    for (int m = 0; m < 8; m++) {
                        binary.append((val & 128) == 0 ? 0 : 1);
                        val <<= 1;
                    }
                }
            }

            return correctBitsToByteFormat(binary.length());
        }
    }

    /**
     *  Run calculates the bits in a given array of shorts.
     *
     * @param short_data a static array of shorts of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    private int run(short[] short_data) {
        if(short_data == null)
        {
            if(developerMode)
                Log.e("ByteMe","Error: run(short[]): short array was null");
            return -2;
        }
        else {
            String binary_short = "";
            for (int i = 0; i < short_data.length; i++) {
                binary_short += Integer.toString(0xFFFF & short_data[i], 2);
            }

            return binary_short.length();
        }
    }

    /**
     *  Run calculates the bits in a given array of longs.
     *
     * @param long_data a static array of longs of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    private int run(long[] long_data) {
        if(long_data == null)
        {
            if(developerMode)
                Log.e("ByteMe","Error: run(long[]): long array was null");
            return -2;
        }
        else {
            String binary_long = "";
            for (int i = 0; i < long_data.length; i++) {
                binary_long += Long.toString(long_data[i], 2);
            }

            return correctBitsToByteFormat(binary_long.length());
        }
    }

    /**
     *  Run calculates the bits in a given array of bytes.
     *
     * @param byte_data a static array of bytes of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    private int run(byte[] byte_data) {
        if(byte_data == null)
        {
            if(developerMode)
                Log.e("ByteMe","Error: run(byte[]): byte array was null");
            return -2;
        }
        else {
            String binary_byte = "";
            for (int i = 0; i < byte_data.length; i++) {
                binary_byte += String.format("%8s", Integer.toBinaryString(byte_data[i] & 0xFF)).replace(' ', '0');
            }

            return correctBitsToByteFormat(binary_byte.length());
        }
    }

    /**
     *  Run calculates the bits in a given array of floats.
     *
     * @param float_data a static array of floats of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    private int run(float[] float_data) {
        if(float_data == null)
        {
            if(developerMode)
                Log.e("ByteMe","Error: run(float[]): float array was null");
            return -2;
        }
        else {
            String binary_float = "";
            for (int i = 0; i < float_data.length; i++) {
                binary_float += Integer.toString(Float.floatToIntBits(float_data[i]), 2);
            }

            return correctBitsToByteFormat(binary_float.length());
        }
    }

    /**
     *  Run calculates the bits in a given array of doubles.
     *
     * @param double_data a static array of doubles of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    private int run(double[] double_data) {
        if(double_data == null)
        {
            if(developerMode)
                Log.e("ByteMe","Error: run(double[]): double array was null");
            return -2;
        }
        else {
            String binary_double = "";
            for (int i = 0; i < double_data.length; i++) {
                long bits = Double.doubleToLongBits(double_data[i]);
                binary_double += Long.toString(bits, 2);
            }

            return correctBitsToByteFormat(binary_double.length());
        }
    }

    /**
     *  Run calculates the bits in a given array of chars.
     *
     * @param char_data a static array of chars of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    private int run(char[] char_data) {
        if(char_data == null)
        {
            if(developerMode)
                Log.e("ByteMe","Error: run(char[]): char array was null");
            return -2;
        }
        else {
            String binary_char = "";
            for (int i = 0; i < char_data.length; i++) {
                int value = (int) char_data[i];
                binary_char += Integer.toString(value, 2);
            }

            return correctBitsToByteFormat(binary_char.length());
        }
    }

    /**
     *  Run calculates the bits in a given array of booleans.
     *
     * @param boolean_data a static array of booleans of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    private int run(boolean[] boolean_data) {
        if(boolean_data == null)
        {
            if(developerMode)
                Log.e("ByteMe","Error: run(boolean[]): boolean array was null");
            return -2;
        }
        else {
            String binary_boolean = "";
            for (int i = 0; i < boolean_data.length; i++) {
                int myInt = (boolean_data[i]) ? 1 : 0;
                binary_boolean += Integer.toString(myInt, 2);
            }

            return correctBitsToByteFormat(binary_boolean.length());
        }
    }

    /**
     *  Run calculates the bits in a given array of bitmaps.
     *
     * @param bitmap_data a static array of bitmaps of any value within the scope of the Java API.
     * @return numbers of bits if calculation completes, -1 if the thread was interrupted. -2
     * would be returned if the array given was null.
     */
    private int run(Bitmap[] bitmap_data) {
        if(bitmap_data == null)
        {
            if(developerMode) {
                Log.e("ByteMe", "Error: run(Bitmap[]): bitmap array was null");
            }
            return -2;
        }
        else {
            int bitmap_temp = 0;
            for (int i = 0; i < bitmap_data.length; i++) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
                    bitmap_temp += bitmap_data[i].getRowBytes() * bitmap_data[i].getHeight();
                } else {
                    bitmap_temp += bitmap_data[i].getByteCount();
                }
            }

            return correctBitsToByteFormat(bitmap_temp);
        }
    }

    /**
     * correctBitsToByteFormat
     *
     * if the length is not in an 8-bit format,
     * e.g. only 5 bits such as 10000, then 3
     * more bits needed to be added to make it accurate.
     *
     * @param value the length of the bits to be corrected
     * @return corrected bit length
     */
    public int correctBitsToByteFormat(int value)
    {
        // "The logarithm is your friend"
        int length = (int)(Math.log10(value)+1);


        if(!(length % 8 == 0))
        {
            int newLength = length;
            while(!(newLength % 8 == 0 ))
            {
                newLength += 1;
            }
            return newLength;
        }
        return length;
    }

    //================================================
    // <Get/Set Methods>
    //================================================
    public void setAllocationMax(int allocation_max_m) {
        int tempMaxMemory = (int) getTotal_ram();
        this.allocation_max = tempMaxMemory / allocation_max_m;
        if(developerMode)
        {
            Log.d(""+this.getClass().getName(),"ByteMe, setAllocationMax(), Allocation Max: " + this.allocation_max);
            Log.d(""+this.getClass().getName(),"ByteMe, setAllocationMax(), Ram Max memory: " + tempMaxMemory);
        }
    }

    public void setAllocationMaxManually(int allocation_max_m) {
        this.allocation_max = allocation_max_m;
        if(developerMode)
        {
            Log.d(""+this.getClass().getName(),"ByteMe, setAllocationMaxManually(), Allocation Max: " + this.allocation_max);
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
            Log.d(""+this.getClass().getName(),"ByteMe, setAllocation_current(), Allocation Current: " + this.allocation_current);
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
            Log.e("ByteMe","Error: getTotalMemoryOld(): unknown IOException");
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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
        else if(algorithm == ALGORITHM_MRU)
        {
            mru_cache = new MRU_Cache();
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
    // <General Cache Functions>
    //================================================
    /**
     * addObjectToCache
     *
     * addObjectToCache adds an object to a chosen cache which has already been set by
     * setAlgorithm(). If no algorithm was selected, message
     * "ERROR: addObjectToCache(), no algorithm selected. Object not saved in cache."
     * @param obj Custom object of choosing
     */
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
        else if(getAlgorithm() == ALGORITHM_MRU) {
            mru_cache.add(obj.hashCode(), obj);
            if(developerMode)
            {
                Log.d(""+this.getClass().getName(),"ByteMe, addObjectToCache(), Object added to MRU cache.");
            }
        }
        else
        {
            //ERROR
            Log.d(""+this.getClass().getName(),"ERROR: addObjectToCache(), no algorithm selected. Object not saved in cache.");
        }
    }

    /**
     * getObjectFromCache
     *
     * getObjectFromCache using a hashcode (only works with LRU, LFU, and MRU) or some other int key set
     * retrieve  a given object based on hashcode. If retrieving from FIFO, will only return head;
     * make sure to set hashcode to null or zero.
     *
     * @param hashcode java built-in hashcode of a given object.
     * @return Object matched to hashcode. Returns null if object not found in cache.
     */
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
        else if(getAlgorithm() == ALGORITHM_MRU)
        {
            obj = mru_cache.get(hashcode);
        }

        if(obj.equals(-1)) {
            Log.e("ByteMe","Error: getObjectFromCache(): could not get object from cache");
            return null;
        }
        else
            return obj;
    }

    /**
     * getAllObjectsFromCache
     *
     * getAllObjectsFromCache based on chosen algorithm, returns all objects
     * found in said cache.
     *
     * @return All objects found in chosen cache. Null if no cache is found or cache is empty.
     */
    public Object[] getAllObjectsFromCache() {

        if(getAlgorithm() == ALGORITHM_LRU)
        {
            return lru_cache.getAllObjects();
        }
        else if(getAlgorithm() == ALGORITHM_MRU)
        {
            return mru_cache.getAllObjects();
        }
        else if(getAlgorithm() == ALGORITHM_FIFO)
        {
            return fifo_cache.getAllObjects();
        }
        else
        {
            Log.e("ByteMe","Error: getAllObjectsFromCache(): could not get all objects from cache. Algorithm couldn't be determined.");
            return null;
        }
    }

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
}