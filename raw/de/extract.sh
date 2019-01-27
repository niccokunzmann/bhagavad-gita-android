#!/bin/bash

set -e

cd "`dirname \"$0\"`"

output="output"

mkdir -p "$output"
rm -rf "$output/"*

HTML="s/<[^>]+> *//g"

function get_content() {
  local file="$1"
  local id="$2"
  local content="`cat "$file"`"
  local verse=`echo "$content" | grep -F '<h2 class="entry-title"><a href="https://schriften.yoga-vidya.de/bhagavad-gita/' | sed -r "$HTML"`
  if [ -z "$verse" ]; then
    1>&2 echo "ERROR: content of $file is empty."
    exit 2
  fi
  echo "$verse"
}

function verse_id() {
  echo -n $1 | sed -r s/^0?/chapter_/ | sed -r s/-0?/_verse_/ | sed -r 's#/[0-9]*##'
  echo -n _meaning
}

function verse_content() {
  shift
  echo -n $@
}

for page in schriften.yoga-vidya.de/*; do
  echo "$page"
  # iterate linewise https://superuser.com/a/284192
  get_content "$page" "entry-title" | while read verse ; do
    file="$output/`verse_id $verse`"
    if echo "$file" | grep -qF "Abschluss"; then
      continue
    fi
    verse_content $verse > "$file"
  done
done

