VERSION=0.4.6

.PHONY: all test #prof install clean uninstall tarball win-dist dists deb-dist

all:
	mkdir -p bin/
	rm -rf bin/*
	javac -O -d bin/ -sourcepath "src:resources:language:icons" -classpath "lib/*" src/gui/Main.java

jar: all
	ant jar

test: all
	java -cp "bin:lib/*:language:icons:resources" gui.Main

prof: all
	java -cp "bin:lib/*:language:icons:resources" -prof gui.Main

install: jar
	mkdir -p ${DESTDIR}/usr/share/openttt
	cp openttt.jar ${DESTDIR}/usr/share/openttt/.
	mkdir -p ${DESTDIR}/usr/bin
	cp openttt.sh ${DESTDIR}/usr/bin/openttt
	mkdir -p ${DESTDIR}/usr/share/applications
	cp OpenTTT.desktop ${DESTDIR}/usr/share/applications/.
 	
uninstall:
	rm -rf ${DESTDIR}/usr/share/{openttt,applications/OpenTTT.desktop}
	rm -rf ${DESTDIR}/usr/bin/openttt

clean:
	rm -rf bin
