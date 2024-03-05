all:
	sh gen-swig.sh
	cmake -S . -B build -D CMAKE_BUILD_TYPE=Debug && cmake --build build

install:
	cp ./build/libenet.so ../enet-jni/
	rsync -av --delete ./java/org/ ../enet-jni/src/java/org/

clean:
	rm -rf java src/jni.c build libs obj
