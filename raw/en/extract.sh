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
  local verse="`echo $content | grep -oE \"<[^>]*class=\\\"$id\\\"([^<]|<[^/]|</[^p])*</p>\" | sed -r \"$HTML\"`"
  if [ -z "$verse" ]; then
    1>&2 echo "ERROR: content of $file is empty."
    exit 2
  fi
  echo "$verse"
}

for chapter in bhagavadgita.io/chapter/*; do
  if ! [ -d "$chapter" ]; then
    echo "Skip $chapter because it is not a folder."
    continue
  fi
  chapter_number="`basename \"$chapter\"`"
  chapter_file="$chapter/index.html"
  if ! [ -f "$chapter_file" ]; then
    1>&2 echo "ERROR: Expected chapter summary $chapter_file."
    exit 3
  fi
  cat "$chapter_file" | grep "<h2>" | sed -r "$HTML" > "$output/chapter_${chapter_number}_name"
  cat "$chapter_file" | grep "<h3>" | sed -r "$HTML" > "$output/chapter_${chapter_number}_title"
  get_content "$chapter_file" "sharebr" > "$output/chapter_${chapter_number}_summary"
  for verse in "$chapter/verse/"*; do
    if ! [ -d "$verse" ]; then
      echo "Skip $verse because it is not a folder."
      continue
    fi
    content_file="$verse/index.html"
    if ! [ -f "$content_file" ]; then
      1>&2 echo "ERROR: Excpected file $content_file"
      exit 1
    fi
    verse_number="`basename \"$verse\" | sed 's/-/_/g'`"
    verse_sanskrit="`get_content \"$content_file\" \"verse-sanskrit\"`"
    verse_transliteration="`get_content \"$content_file\" \"verse-transliteration\"`"
    verse_meaning="`get_content \"$content_file\" \"verse-meaning\"`"
    echo "$verse_sanskrit" > "$output/chapter_${chapter_number}_verse_${verse_number}_sanskrit"
    echo "$verse_transliteration" > "$output/chapter_${chapter_number}_verse_${verse_number}_transliteration"
    echo "$verse_meaning" > "$output/chapter_${chapter_number}_verse_${verse_number}_meaning"
  done
done

