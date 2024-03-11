# ENet for Java

A JNI wrapper for ENet.

## Info

Just execute gen-swig followed by *-build ("android" only for now).
The java source files will be on the "java" directory while the binaries will be on the "libs" directory.

## Test

The following commands are tested on Ubuntu 20.04.

```
sudo apt-get install swig
sudo apt-get install openjdk-11-jdk-headless
make swig JDK_HOME=/usr/lib/jvm/java-11-openjdk-amd64

# start server
make javac server

# start client
make javac client 
```
