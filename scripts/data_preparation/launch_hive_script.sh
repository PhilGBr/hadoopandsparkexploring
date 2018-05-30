#!/bin/bash

## Mandatory parameter(s)
##  ->  $1 : Hive script to be executed
##
## Optional parameter(s)
##	-> $2: schema name (AKA database name) to connect with to the Hive server 
##
## See also:
##    https://cwiki.apache.org/confluence/display/Hive/HiveServer2+Clients#HiveServer2Clients-ConnectionURLs


if [ -z "$1" ]; then
  echo "Arg \$1 (the hive script  to be executed) is missing. Exit script ${0}"
    exit 0
fi

if [ ! -f "$1" ]; then
    echo "File ${1} does not exist. Exit script ${0}"
    exit 0
fi

if [ -z "$2" ]; then
  echo "Arg \$2 (schema name) is not specified. Using 'default' schema to connect to Hive server"
fi

#  Load environment variables
source 00_env.sh

if [ -z "$2" ]; then
	HIVE2_SRV_URL=$(get_hive_cnx_url)
else
	HIVE2_SRV_URL=$(get_hive_cnx_url $2)	
fi

beeline -u ${HIVE2_SRV_URL} -f ${1} --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT
