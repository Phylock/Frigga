#!/bin/bash
BND_FILE=$1
INPUT_FILE=$2
INPUT_DIRECTORY="originals"
BND_TOOL="../tools/bnd.jar"
#TODO: file verification, right filetype etc

BASE_NAME=$(basename $BND_FILE .bnd)

eval `head -n 1 $BND_FILE | sed -e 's/[[:space:]]*\=[[:space:]]*/=/g' \
    -e 's/;.*$//' \
    -e 's/[[:space:]]*$//' \
    -e 's/^[[:space:]]*//' \
    -e "s/^\(.*\)=\([^\"']*\)$/\1=\"\2\"/"`
if [ -z $INPUT_FILE ]; then
    INPUT_FILE="$INPUT_DIRECTORY/$BASE_NAME-$version.jar"
fi
    
OUTPUT_FILE="$BASE_NAME.jar"

echo "=== Collected Information ==="
echo "BND          : $BND_FILE"
echo "Package      : $BASE_NAME"
echo "Found version: $version"
echo "Input        : $INPUT_FILE"
echo "Output       : $OUTPUT_FILE"
echo "==="

if [ ! -f "$INPUT_FILE" ];
then
    echo "Inputfile not found!"
    exit
fi

$BND_TOOL wrap -properties $BND_FILE -output $OUTPUT_FILE $INPUT_FILE
$BND_TOOL print $OUTPUT_FILE
