#! /bin/bash

# Collect all of the required libraries.
# Assumes that Gervill4Beads (http://github.com/daveho/Gervill4Beads)
# has been checked out beside FunWithSound.

g4bDir=../../../Gervill4Beads
if [ ! -d "$g4bDir" ]; then
	echo "Gervill4Beads must be checked out beside FunWithSound"
	exit 1
fi

# Core FunWithSound project directory
fwsDir=../../FunWithSound

# Ensure that all of the libraries required for Gervill4Beads
# have been downloaded and extracted.
echo "Making sure Beads and Gervill libraries are available..."
(cd $g4bDir && ./fetchlibs.sh)

# Make sure Gervill4Beads jarfile is built
echo "Building Gervill4Beads library..."
(cd $g4bDir && ant)

# Make sure core FunWithSound library is built
echo "Building core FunWithSound library..."
(cd $fwsDir && ant)

# Copy all of the required libraries
echo "Copying libraries..."
cp $fwsDir/funwithsound-core.jar .
cp $g4bDir/gervill4beads.jar .
cp $g4bDir/lib/beads*.jar .
cp $g4bDir/lib/gervill.jar .
cp $g4bDir/lib/tritonus*.jar .

