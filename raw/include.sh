#!/bin/bash

set -e

cd "`dirname \"$0\"`"

strings_file="../app/src/main/res/values/strings.xml"
ids_folder="output"

if ! [ -f "$strings_file" ]; then
  1>&2 echo "ERROR: could not find file $strings_file."
  exit 1
fi

strings="`cat \"$strings_file\"`"

add=""
remove_lines="</resources>"

i=1
max_i="`ls \"$ids_folder\" | wc -l`"
for file in "$ids_folder/"*
do
  id="`basename \"$file\"`"
  if echo "$id" | grep -q "sanskrit"; then
    translatable=" translatable=\"false\""
  else
    translatable="" # true is default
  fi
  if echo "$id" | grep -qE "sanskrit|transliteration"; then
    typos=" tools:ignore=\"Typos\""
  else
    typos="" # normally do not ignore typos
  fi
  content_id="<string name=\"$id\""
  line="    $content_id$translatable$typos>`cat \"$file\"`</string>"
  remove_lines="$remove_lines
$content_id"
  add="$add
$line"
  echo "$i/$max_i"
  i="$(( i + 1 ))"
done

echo "$remove_lines"
echo -n "$strings" | grep -Fv "$remove_lines" | tee "$strings_file"
echo "$add" | grep -vE '^\s*$' | tee -a "$strings_file"
echo '</resources>' | tee -a "$strings_file"
               
