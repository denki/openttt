#!/bin/sh

CONVERT="convert -background none -scale $1"

mkdir -p $1
$CONVERT svg/print_preview.svg $1/print_preview.png
$CONVERT svg/back.svg $1/back.png
$CONVERT svg/next.svg $1/next.png
$CONVERT svg/save.svg $1/save.png
$CONVERT svg/clone.svg $1/clone.png
$CONVERT svg/down.svg $1/down.png
$CONVERT svg/clear.svg $1/clear.png
$CONVERT svg/remove.svg $1/remove.png
$CONVERT svg/up.svg $1/up.png
$CONVERT svg/saveas.svg $1/saveas.png
$CONVERT svg/add.svg $1/add.png
$CONVERT svg/new.svg $1/new.png
$CONVERT svg/properties.svg $1/properties.png
$CONVERT svg/print.svg $1/print.png
$CONVERT svg/open.svg $1/open.png
$CONVERT svg/quit.svg $1/quit.png
