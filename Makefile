all:
	sh gen-swig.sh
	cmake -S . -B build -D CMAKE_BUILD_TYPE=Debug && cmake --build build
	cp ./build/libenet.so libenet.so

clean:
	rm -rf java/org/ src/jni.c build libs obj out/

javac:
	javac -d out --source-path java/ java/com/example/EnetTest.java java/org/bespin/enet/*.java

run:
	java -Dfile.encoding=UTF-8 -classpath out/ com.example.EnetTest
