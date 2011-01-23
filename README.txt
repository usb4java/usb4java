usb4java - USB library for Java based on libusb-0.1.x
Copyright (C) 2011 Klaus Reimer <k@ailis.de>
See LICENSE.txt for licensing information.
==============================================================================

The library is splitted into two modules. The first module is a shared library
which wraps libusb. The second module is the Java library which uses the shared
library to access USB.


Shared library
==============

The shared library is written in C and can (hopefully) be compiled on all
operating systems which are supported by autotools. If you have downloaded
usb4java via the Source Code Repository then the autotools files of the shared
library must first be generated:

  $ cd libusb4java
  $ make -f Makefile.scm
  
This step is not necessary if you downloaded the source archive of libusb4java
from the website.

To compile libusb4java you need to have a correctly set JAVA_HOME environment
variable and you must have the libusb development files installed.
On Debian systems this can be done with "apt-get install libusb-dev".

The compile the source you simply do this:

  $ ./configure
  $ make
  $ sudo make install
  

Java library
============

To compile the Java library you need Maven (http://maven.apache.org/). If you
are using Maven for your own project then you might just want to run 
"mvn install" to install the usb4java into your local Maven repository. If
you are not using Maven and you just want to package the usb4java.jar then
run "mvn package" instead. You'll find the generated JAR file in
usb4java/target. 


Binary distributions
====================

If you don't want to compile anything yourself then visit the usb4java
website (http://www.ailis.de/~k/projects/usb4java/). The JAR file can be
downloaded from there and maybe you even find a compiled shared library for
your operating system there.


Feedback, questions and bug reports
===================================

If you found a bug or if you have a question or you just want to give some
feedback then please contact me at k@ailis.de
