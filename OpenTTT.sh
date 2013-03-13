#!/bin/sh

OLDPWD=$PWD
cd /usr/share/OpenTTT/
java -cp /usr/share/OpenTTT:/usr/share/pixmaps gui.Main $OLDPWD/$1
