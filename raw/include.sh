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

for file in "$ids_folder/"*
do
  id="`basename \"$file\"`"
  if echo "$id" | grep -q "sanskrit"; then
    translatable="translatable=\"false\""
  else
    translatable="" # true is default
  fi
  content_id="<string name=\"$id\""
  line="    $content_id$translatable>`cat \"$file\"`</string>"
  if echo "$strings" | grep -qF "$content_id"; then
    # found in strings
    strings="`echo \"$strings\" | grep -vF \"$content_id\"`"
  fi
  add="$add
$line"
done

echo "$strings" | grep -Fv '</resources>' | grep -vE '^\s*$' | tee "$strings_file"
echo "$add" | tee -a "$strings_file"
echo '</resources>' | tee -a "$strings_file"
               
