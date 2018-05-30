#!/bin/bash

## Mandatory parameter(s)
##	-> $1: the schema name (AKA database name) in which tables are located
##	-> $2: method of statistics calculation: TABLES_ONLY or TABLES_AND_COLUMNS

if [ -z "$1" ]; then
	echo "Arg \$1 (the schema name) is missing. Exit script ${0}"
	exit 0
fi

#  Load environment variables
source 00_env.sh


if [ "$2" =  "TABLES_ONLY" ]
  then
    ./launch_hive_script.sh  ./08_compute_tables_statistics_only.hql $1
elif [ "$2" = "TABLES_AND_COLUMNS" ]
  then
    ./launch_hive_script.sh  ./08_compute_tables_and_columns_statistics.hql $1
else
    echo "Invalid value for Arg \$2. It should be 'TABLES_ONLY' or 'TABLES_AND_COLUMNS. Exit script ${0}'"
    exit 1
fi
