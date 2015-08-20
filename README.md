FunWithSound
============

Library intended to make it easy to create music using [Processing](http://processessing.org) and [Beads](http://www.beadsproject.net).  It can also be used from plain Java code.

This is a work in progress.  However, it actually works: see the [Demo](https://github.com/daveho/FunWithSound/blob/master/FunWithSound/demo/io/github/daveho/funwithsound/demo/Demo.java) and [Demo2](https://github.com/daveho/FunWithSound/blob/master/FunWithSound/demo/io/github/daveho/funwithsound/demo/Demo2.java) programs for a couple quick examples.  Also, see the sketches [FWSDemo](https://github.com/daveho/FunWithSound/blob/master/FunWithSoundProcessingLib/examples/FWSDemo/FWSDemo.pde) and [FWSDemo2](https://github.com/daveho/FunWithSound/blob/master/FunWithSoundProcessingLib/examples/FWSDemo2/FWSDemo2.pde) for examples of using the library with Processing.  (Processing library download coming soon...)

Note that you need Processing 3.0 or later to use FunWithSound.

There is some pretty decent documentation on the [wiki](https://github.com/daveho/FunWithSound/wiki).

The project depends on [Gervill4Beads](https://github.com/daveho/Gervill4Beads).  To run the Java demos, just have both projects checked out in the same Eclipse workspace.  You'll need to run the `fetchlibs.sh` script in the Gervill4Beads directory to download all of the required libraries.

You can build the Java and Processing libraries from the command line by running the `build.sh` script.  This script assumes that you have Gervill4Beads checked out next to FunWithSound.

The code is distributed under the [MIT license](https://github.com/daveho/FunWithSound/blob/master/LICENSE.txt).

Contact: [David Hovemeyer](mailto:david.hovemeyer@gmail.com)
