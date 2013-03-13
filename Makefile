VERSION=0.4.5

.PHONY: all test #prof install clean uninstall tarball win-dist dists deb-dist

all:
	mkdir -p bin/
	rm -rf bin/*
	javac -O -d bin/ -sourcepath src/ -classpath "lib/*" src/gui/Main.java
# 	mkdir -p zipped
# 	unzip -o lib/dom4j-*.jar -d zipped
# 	unzip -o lib/xstream*.jar -d zipped
# 	unzip -o lib/poi-3*.jar -d zipped
# 	unzip -o lib/poi-ooxml-3*.jar -d zipped
# 	unzip -o lib/poi-ooxml-schemas*.jar -d zipped
# 	unzip -o lib/xerces*.jar -d zipped
# 	unzip -o lib/xml-apis.jar -d zipped
# 	unzip -o lib/xmlbeans-*.jar -d zipped
# 	unzip -o lib/odfdom*.jar -d zipped
# 	rm -rf zipped/META-INF/
# 	rm -rf zipped/font_metrics.properties
# 	mv zipped/* bin/.
# 	rm -rf zipped
	
test: all
	java -cp bin:lib/* gui.Main

# prof: all
# 	java -cp bin -prof gui.Main
# 
# install:
# 	mkdir -p ${DESTDIR}/usr/share/OpenTTT/
# 	cp -r bin/* ${DESTDIR}/usr/share/OpenTTT/
# 	cp -r icons/ ${DESTDIR}/usr/share/OpenTTT/
# 	cp -r templates/ ${DESTDIR}/usr/share/OpenTTT/
# 	cp -r lib/ ${DESTDIR}/usr/share/OpenTTT/
# 	cp language.dat ${DESTDIR}/usr/share/OpenTTT/
# 	mkdir -p ${DESTDIR}/usr/bin/
# 	cp OpenTTT.sh ${DESTDIR}/usr/bin/openttt
# 	mkdir -p ${DESTDIR}/usr/share/pixmaps/
# 	cp icons/main.png ${DESTDIR}/usr/share/pixmaps/OpenTTT.png
# 	mkdir -p ${DESTDIR}/usr/share/applications/
# 	cp OpenTTT.desktop ${DESTDIR}/usr/share/applications/
# 	
# uninstall:
# 	rm -rf ${DESTDIR}/usr/share/{OpenTTT,applications/OpenTTT.desktop,pixmaps/OpenTTT.png}
# 	rm -rf ${DESTDIR}/usr/bin/openttt
# 
# clean:
# 	rm -rf OpenTTT-${VERSION}/
# 	rm -rf openttt-${VERSION}/
# 
# tarball: clean Makefile src/ OpenTTT.desktop OpenTTT.sh language.dat openttt.exe
# 	mkdir -p openttt-${VERSION}/
# 	cp -r -t openttt-${VERSION}/ Makefile lib/ src/ OpenTTT.desktop OpenTTT.sh language.dat  icons/ templates/ openttt.exe
# 	tar -czf dists/openttt-${VERSION}.tar.gz openttt-${VERSION}  --exclude=".svn"
# 	rm -rf openttt-${VERSION}/
# 
# win-dist: clean all
# 	mkdir -p OpenTTT-${VERSION}/
# 	cp -r -t OpenTTT-${VERSION}/ openttt.exe bin/ icons/ templates/
# 	iconv --from-code=UTF-8 --to-code=ISO-8859-1 language.dat  > OpenTTT-${VERSION}/language.dat
# 	mkdir -p OpenTTT-${VERSION}/icons/
# 	rm -f dists/OpenTTT-${VERSION}.zip
# 	zip -r dists/OpenTTT-${VERSION}.zip OpenTTT-${VERSION}/ -x \*.svn\*
# 
# deb-dist: tarball
# 	tar xfv openttt-${VERSION}.tar.gz
# 	cd openttt-${VERSION}/
# 	DEBFULLNAME="Tobias Denkinger" dh_make -i -c gpl -e flub123@gmx.de --createorig
# 	mv debian/control .
# 	cat control | sed "s/Section: unknown/Section: office/g" | sed "s/<insert up to 60 chars description>/Software for managing table tennis tournaments. Written in java./g" | sed "s/<insert long description, indented with spaces>/Software for managing table tennis tournaments. Written in java./g" | sed "s/<insert the upstream URL, if relevant>/http:\/\/www1.inf.tu-dresden.de\/~s7480817\/OpenTTT\//g" | sed "s/3.9.1/${VERSION}/g"> debian/control
# 	dpkg-buildpackage
# 
# dists: win-dist tarball
