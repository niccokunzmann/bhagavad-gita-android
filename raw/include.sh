#!/bin/bash

set -e

cd "`dirname \"$0\"`"

for language in en de; do
  1>&2 echo " ---------- start of $language ---------- "
  if [ "$language" == "en" ]; then
    suffix=""
  else
    suffix="-$language"
  fi

  strings_file="../app/src/main/res/values${suffix}/strings.xml"
  ids_folder="$language/output"

  if ! [ -f "$strings_file" ]; then
    1>&2 echo "ERROR: could not find file $strings_file."
    continue
  fi
  
  if ! [ -d "$ids_folder" ]; then
    1>&2 echo "ERROR: could not find folder $ids_folder."
    continue
  fi

  strings="`cat \"$strings_file\"`"

  add=""
  remove_lines="</resources>"

  i=1
  max_i="`ls \"$ids_folder\" | wc -l`"
  for file in "$ids_folder/"*
  do
    id="`basename \"$file\"`"
    if echo "$id" | grep -q "sanskrit|transliteration"; then
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
    content="`cat \"$file\"`"
    # Transform the content from html to XML
    # see https://stackoverflow.com/a/5966570
    content="`echo \"$content\" | sed \"s/&#39;/\\\\\'/g\"`"
    line="    $content_id$translatable$typos>$content</string>"
    remove_lines="$remove_lines
  $content_id"
    add="$add
  $line"
    1>&2 echo "$i/$max_i"
    i="$(( i + 1 ))"
  done

  echo "$remove_lines"
  echo -n "$strings" | grep -Fv "$remove_lines" | tee "$strings_file"
  echo "$add" | grep -vE '^\s*$' | tee -a "$strings_file"
  echo '</resources>' | tee -a "$strings_file"
  1>&2 echo " ---------- end of $language ---------- "
done
