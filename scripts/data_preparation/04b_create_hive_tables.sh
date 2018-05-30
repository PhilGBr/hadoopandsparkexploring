#!/bin/bash

## Mandatory parameter(s)
##  ->  $1 : Hive script to be executed
##
## Optional parameter(s)
##	-> $1: schema name (AKA database name) to connect with to the Hive server 
##


if [ -z "$1" ]
  then
	echo "Arg \$1 (the schema name in which tables are to be create) is missing. Exit script ${0}"
    exit 0
fi

#  Load environment variables
source 00_env.sh


./launch_hive_script.sh ./04b_create_hive_tables.hql $1



