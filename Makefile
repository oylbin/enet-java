clean:
	rm -rf java/org/ src/jni.c build libs obj out/

swig:
	sh gen-swig.sh
	cmake -S . -B build -DCMAKE_BUILD_TYPE=Debug -DJDK_HOME=${JDK_HOME} && cmake --build build
	cp ./build/libenet.so libenet.so

javac:
	javac -d out --source-path java/ java/com/example/*.java java/org/bespin/enet/*.java

server:
	java -Dfile.encoding=UTF-8 -classpath out/ com.example.EnetTestServer

client:
	java -Dfile.encoding=UTF-8 -classpath out/ com.example.EnetTestClient
