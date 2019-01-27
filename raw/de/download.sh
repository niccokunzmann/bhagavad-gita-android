#!/bin/bash

set -e

cd "`dirname \"$0\"`"

mkdir schriften.yoga-vidya.de
cd schriften.yoga-vidya.de

for page in `seq 1 18`; do
  wget -O "$page.html" "https://schriften.yoga-vidya.de/bhagavad-gita/tag/deutsche-ubersetzung/page/$page/"
done

