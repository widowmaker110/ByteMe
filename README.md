# ByteMe
Evolving library which calculates the bit allocation of custom objects and handles them based on a given cache algorithm (e.g. LRU, FIFO, etc.). This library is intended to help programmers have an all inclusive algorithm for all their memory handling needs within an application. While ByteMe may be more CPU intensive than other libraries, it offers a wider range of control when it comes to data being held in memory.

## Preface
- Coming Soon
- History
- How it works
- Installation
- Cache Distinctions
- Usage
- Contributing
- Credits
- License

## Coming Soon
The following is a list of objectives already set out by the developerin order to make the library more functional and stable. This list has no order or importance. If you have a suggestion, please see the Credits section of this readme to contact me.

- Addition of LFU cache algortihm.
- Fully functioning app to demonstrate how to use this library
- Create a runtime 2D array which records all of each classe's functions to reduce CPU load. Use a 2D array because each cell in the first dimension of the array will house the object's class name. Every row appending the first dimension will house the object's list of functions separately in each cell. 

## History (shows the most recent 5 alterations)
##### 1/24/2016

- Worked on `correctBitsToByteFormat`. Realized it was inaccurate and inefficient. Converted the method to a recursive form.
- Provided functionality for the programmer to define certain classes for the library not to compute for bit allication. In this library, if you want to set a list of your own methods to ignore during calculation, simply add either a `String` or `List<String` as the parameter to `setRestrictedMethodsByProgrammer`.

##### 1/23/2016

- Added some error handling to alert the programmer of specific issues. Most of the stacktraces are turned off but the `Log.e()` messages give the programmer a good idea of which function threw the error and why.
- Researched more about how to handle `null` values in calculating their respective bit allocation and found out that, at least according to my research, null values are actually pointers to a null instance which doesn't appear to take up memory.

##### 12/26/2015

- Added a more fine-tuned bit calculation algorithm. The problem with the version before this is that it would only count the bits without padding the left-hand side with zeros for an accurate byte length. For instance, if the integer 10 was given to be calculated as 1010 in binary. Unfortunately, this is not a byte (since a byte is 8 bits). So the function called `correctBitsToByteFormat` deals with this in a simplistic fashion.

## How it works

This library uses the [Relfect Java api](https://docs.oracle.com/javase/7/docs/api/java/lang/reflect/package-summary.html)  to get all of the methods within a given object. Once the methods such as `.equal()` and `.class()` are not in the list of methods of a given object, all of the methods are then called to return one of the following values:

* Int
* String
* Short
* Byte
* Long
* Float
* Double
* Char
* Boolean
* [Bitmap](http://developer.android.com/reference/android/graphics/Bitmap.html)

Then it figures out how many bits make up the object. For example, if given the integer value of 10, it would produce 1010. When the conversion is done, it would return a value of 4 bits since thats the byte representation of 10 in decimal. The function handling this calcuation would continously loop until all functions within a given object is called and returns the total value of bits. Then, based on which cache algorithm chosen, the object which might be exceeding a preset maxmimum bit allocation would be evicted. 

## Installation

I designed this whole project to be based around the [ByteMe.java](https://github.com/widowmaker110/ByteMe/blob/master/app/src/main/java/WidowMaker110Library/ByteMe.java) file. Instead of doing the pain-staking process of adding a module to your project, this whole API is within one java file for ease of installation and use. All I ask is when you use this code, please keep the MIT License with it and a reference to this work (URL to this repository is preferable).

An acceptable way of referencing would be
```
/**
 * Repository can be found at https://github.com/widowmaker110/ByteMe
 * 
 * The MIT License (MIT)
 *
 * Copyright © 2015-2016 Alexander Miller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
```

## Cache Distinctions

When developing this library, I wanted to give an in-depth guide into how these different caches operate. Some will work better for different loads and needs.

| Cache Name  | Advantage | Disadvantage |
| ------------- | ------------- | -------------|
| LRU (Least Recently Used)  | This algorithm tends to be a favorite among applications and is relatively fast. Majority of apps would use this algorithm for their needs. | Not always the somewhat most efficient since its likely to have objects in memory that you don't always need. Overhead is slightly higher than algorithms such as FIFO. |
| FIFO (First In First Out) | One of the simplest cache algorithms available. Cache overhead is arguably the lowest in this list. Very little CPU power is needed to operate this algorithm | No effort is made to keep items which are frequenly, or recently, used in memory. Not the most efficient at all.
| MRU (Most Recently Used) | See LRU | See LRU |

## Usage

In any class this library will be used in, you need to gather an instance of said library.
```
ByteMe b = new ByteMe(getApplicationContext()).getInstance();
```
However, these configurations only need to be set once unless you'll be changing them in a safe manner.
```
b.setAllocationMax(ByteMe.RAM_ONE_EIGHTH);
b.setAlgorithm(ByteMe.ALGORITHM_LRU);
```
setAllocationMax automatically gets the total amount of RAM in the device (tested and works will all SDK levels) and then divides it into the amount you choose. In this instance, I have set it to 1/8 of total RAM memory in the device to help ensure it doesn't cause the device to lag. The options available are 1/12, 1/10, 1/8, 1/7, 1/6, 1/5, 1/4. I would not recommend using more than 1/8 and even that is a lot on devices nowadays. For instance, something such as the [HTC One M8](https://www.google.com/shopping/product/3602112225780779300/specs?sourceid=chrome-psyapi2&ion=1&espv=2&ie=UTF-8&q=htc+one+m8+specs&oq=htc+one+m8+specs&aqs=chrome..69i57j0l5.4142j0j1&sa=X&ved=0ahUKEwjJqtyA7ojKAhXF7D4KHeeRDpYQuC8IvAI) has 2GB of RAM. 2GB dvided by 8 equals 262.144 MB. 

setAlgorithm must be set before placing objects into the cache. You are able to choose from any in the list:
* LRU - Least Recently Used
* FIFO - First In First Out
* MRU - Most Recently Used

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Credits

Alexander Miller

Bachelor of Arts in Computer Science, DePauw University (2016)

alexander.miller110@gmail.com
## License

The MIT License (MIT)

Copyright (c) 2015-2016 Alexander Miller

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
