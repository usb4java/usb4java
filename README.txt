usb4java - USB library for Java based on libusb-0.1.x
Copyright (C) 2011 Klaus Reimer <k@ailis.de>
See LICENSE.txt for licensing information.
==============================================================================

This library can be used to access USB devices. It is based on the native
libusb-0.1.x shared library and reflects this API as complete as possible.

To use usb4java you have to compile the JAR file and the libusb JNI wrapper 
library. The following sections try to explain this in some short words.
For more detailed information please visit the usb4java website:

http://kayahr.github.com/usb4java


libusb JNI wrapper
==================

The wrapper library is written in C and can (hopefully) be compiled on all
operating systems which are supported by autotools. If you have downloaded
usb4java via the Source Code Repository then the source of the wrapper library
is located in the src/main/c directory. To auto-generate the necessary
autotools files run this command:

  $ make -f Makefile.scm
  
This step is not necessary if you downloaded the source archive of libusb4java
from the website because then the autotools files are already included.

To compile the wrapper library you need a correctly set JAVA_HOME environment
variable and you must have the libusb development files installed.
On Debian systems this can be done with "apt-get install libusb-dev".

To compile and install the wrapper library you simply do this:

  $ ./configure
  $ make
  $ sudo make install
  
After this the wrapper library is located in /usr/local/lib. 
  

Java library
============

To compile the Java library you need Maven (http://maven.apache.org/). If you
are using Maven for your own project then you might just want to run 
"mvn install" to install the usb4java into your local Maven repository. If
you are not using Maven and you just want to package the usb4java.jar then
run "mvn package" instead. After this you'll find the generated JAR file in
the target directory.


Binary distributions
====================

If you don't want to compile anything yourself then visit the usb4java
website (http://www.ailis.de/~k/projects/usb4java/). The JAR file can be
downloaded from there and maybe you even find a compiled wrapper library for
your operating system there.


Feedback, questions and bug reports
===================================

If you found a bug or if you have a question or you just want to give some
feedback then please contact me at k@ailis.de.
