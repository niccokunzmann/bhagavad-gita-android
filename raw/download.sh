#!/bin/bash

set -e

cd "`dirname \"$0\"`"

wget --mirror --convert-links --adjust-extension --page-requisites https://bhagavadgita.io

