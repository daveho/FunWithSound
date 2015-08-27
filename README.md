# FunWithSound

FunWithSound is a library intended to make it easy to create music using [Processing](http://processessing.org) and [Beads](http://www.beadsproject.net).  It can also be used from plain Java code.

## Requirements

For Java, you need Java 1.7 or higher.

For Processing, you need Processing 3.0 or higher.

## Downloads

See the [Releases](https://github.com/daveho/FunWithSound/releases) page for downloadable releases, including a Processing library.

## Documentation

There is documentation on the [wiki](https://github.com/daveho/FunWithSound/wiki).

Also, you may find some of the demo programs useful for reference.

Java demo programs:

* [Demo]((https://github.com/daveho/FunWithSound/blob/master/FunWithSound/demo/io/github/daveho/funwithsound/demo/Demo.java)
* [Demo2]((https://github.com/daveho/FunWithSound/blob/master/FunWithSound/demo/io/github/daveho/funwithsound/demo/Demo2.java)
* [Demo3]((https://github.com/daveho/FunWithSound/blob/master/FunWithSound/demo/io/github/daveho/funwithsound/demo/Demo3.java)
* [Demo4]((https://github.com/daveho/FunWithSound/blob/master/FunWithSound/demo/io/github/daveho/funwithsound/demo/Demo4.java)

Processing demo programs:

* [FWSDemo](https://github.com/daveho/FunWithSound/blob/master/FunWithSoundProcessingLib/examples/FWSDemo/FWSDemo.pde)
* [FWSDemo2](https://github.com/daveho/FunWithSound/blob/master/FunWithSoundProcessingLib/examples/FWSDemo2/FWSDemo2.pde)
* [WachetAuf](https://github.com/daveho/FunWithSound/blob/master/FunWithSoundProcessingLib/examples/WachetAuf/WachetAuf.pde)

## Technical stuff, compiling from source

The project depends on [Gervill4Beads](https://github.com/daveho/Gervill4Beads).  To run the Java demos, just have both projects checked out in the same Eclipse workspace.  You'll need to run the `fetchlibs.sh` script in the Gervill4Beads directory to download all of the required libraries.

You can build the Java and Processing libraries from the command line by running the `build.sh` script.  This script assumes that you have Gervill4Beads checked out next to FunWithSound.

The code is distributed under the [Apache License 2.0](https://github.com/daveho/FunWithSound/blob/master/LICENSE.md).

## Contact

If you are using FunWithSound, or have thoughts about it, send email to [David Hovemeyer](mailto:david.hovemeyer@gmail.com).
