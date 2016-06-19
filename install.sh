#!/bin/bash

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

for TYPE in tv film; do
  for ACTION in ripper concatenator; do
    sed "s!RIPPER!$DIR!g" desktop/$TYPE-$ACTION.desktop > $HOME/.local/share/applications/$TYPE-$ACTION.desktop
   done
done