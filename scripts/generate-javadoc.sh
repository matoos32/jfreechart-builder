#!/bin/sh

if [ "$1" = "" ]; then
  echo "You must specify the version tag as an argument"
  exit 1
fi

TAG=$1

mvn javadoc:javadoc \
-Dsource=8 \
-DdestDir=javadoc \
-Dnotimestamp \
-Dshow=public \
-Dheader="Javadoc for <b><a href="https://github.com/matoos32/jfreechart-builder/tree/$TAG">jfreechart-builder $TAG</a></b>" \
-Dfooter="Javadoc for <b><a href="https://github.com/matoos32/jfreechart-builder/tree/$TAG">jfreechart-builder $TAG</a></b>" \
-Dbottom=""
