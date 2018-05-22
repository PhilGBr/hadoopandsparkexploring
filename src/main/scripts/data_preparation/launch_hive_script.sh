#!/bin/bash

if [ -z "$1" ]
  then
    echo "No argument supplied to script ${0}. Exit"
    exit 0
fi

if [ ! -f "$1" ]; then
    echo "File ${1} does not exist: no Hive script executed by ${0}. Exit "
    exit 0
fi


#  Load environment variables
source 00_env.sh

beeline -u ${HIVE2_SRV_URL} -f ${1} --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT
