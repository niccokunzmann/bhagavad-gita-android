#!/bin/bash

set -e

cd "`dirname \"$0\"`"

if [ -z "$@" ]; then
  echo "$0 <language>" 
fi

verse_count=(0 47 72 43 42 29 47 30 28 34 42 55 20 35 27 20 24 28 78)

for language in "$@"; do
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
  
  # make sure we have all ids required.
  for chapter in `seq 1 18`; do
    for verse in `seq 1 ${verse_count[chapter]}`; do
      touch "$ids_folder/chapter_${chapter}_verse_${verse}_meaning"
    done
  done

  strings="`cat \"$strings_file\"`"

  add=""
  remove_lines="</resources>"

  i=1
  max_i="`ls \"$ids_folder\" | wc -l`"
  for file in "$ids_folder/"*
  do
    id="`basename \"$file\"`"
    if echo "$id" | grep -qE "sanskrit|transliteration"; then
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
  echo -n "verses: "
  ls "$ids_folder/"*_meaning | wc -l 
  1>&2 echo " ---------- end of $language ---------- "
done
