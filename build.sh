#! /bin/bash

# Build (or rebuild) everything from scratch, including
# funwithsound-core.jar (the main Java library), and
# the Processing library zipfile.

g4bDir=../Gervill4Beads
fwsDir=FunWithSound
fwsProcLibDir=FunWithSoundProcessingLib

# Do an ant clean in the Gervill4Beads and (core) FunWithSound directories,
# to ensure that funwithsound-core.jar and gervill4beads.jar are rebuilt
# when the libraries required by the Processing library are built/collected.
(cd $g4bDir && ant clean)
(cd $fwsDir && ant clean)

# Clean out Processing library lib dir
rm -f $fwsProcLibDir/lib/*.jar

# Collect libraries needed by Processing library: this will
# build funwithsound-core.jar and gervill4beads.jar as a side
# effect
(cd $fwsProcLibDir/lib && ./collectLibs.sh)

# Now we can build the Processing library
(cd $fwsProcLibDir/resources && ant)
