# ByteMe
Library to calculate runtime allocation of objects

## Installation

I designed this whole project to be based around the [Library.ByteMe.java]  (https://github.com/widowmaker110/ByteMe/tree/master/app/src/main/java/Library/ByteMe.java) file. Instead of doing the pain-staking process of adding a module to your project, this whole API is within one java file for ease of installation and use. All I ask is when you use this code, please keep the MIT License with it and a reference to this work (URL to this repository is preferable).

## Cache Distinctions

When developing this API, I wanted to give an in-depth guide into how these different caches operate. Some will work better for different loads and needs.

| Cache Name  | Advantage | Disadvantage |
| ------------- | ------------- | -------------
| LRU (Least Recently Used)  | Overhead is constant, increases logarithmically with cache size. Simple algorithm overall | Not the most efficient since its likely to have objects in memory that you don't always need. |

source(s): http://www.coderanch.com/how-to/java/CachingStrategies

## Usage

Runtime allocation measurements of:
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

In any class this library will be used in, you need to gather an instance of said library.
```
ByteMe b = new ByteMe(MainActivity.this).getInstance();
```
However, these configurations only need to be set once unless you'll be changing them in a safe manner.
```
b.setAllocationMax(ByteMe.RAM_ONE_EIGHTH);
b.setAlgorithm(ByteMe.ALGORITHM_LRU);
```
setAllocationMax automatically gets the total amount of RAM in the device (tested and works will all SDK levels) and then divides it into the amount you choose. In this instance, I have set it to 1/8 of total RAM memory in the device to help ensure it doesn't cause the device to lag. The options available are 1/12, 1/10, 1/8, 1/7, 1/6, 1/5, 1/4. I would not recommend using more than 1/5 and even that is a lot on devices nowadays.

setAlgorithm must be set before placing objects into the cache. You are able to choose from any in the list:
* [LRU] - Least Recently Used (https://www.youtube.com/watch?v=I9_BpSXBodU)
* LFU - Least Frequently Used
* FIFO - First In First Out

The library automatically calculates the runtime bit usage of your custom objects and makes sure that the caches don't go over the maximum set. 

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Credits

Alexander Miller, DePauw University (2016)

## License

The MIT License (MIT)

Copyright (c) 2015 Alexander Miller

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
