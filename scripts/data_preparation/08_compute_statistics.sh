#!/bin/bash


if [ -z "$1" ]
  then
    echo "No argument supplied to script :${0}. Exit"
    exit 1
fi


if [ "$1" =  "TABLES_ONLY" ]
  then
    ./launch_hive_script.sh  ./08_compute_tables_statistics_only.hql
elif [ "$1" = "TABLES_AND_COLUMNS" ]
  then
    ./launch_hive_script.sh  ./08_compute_tables_and_columns_statistics.hql
else
    echo "Invalid argument '${1}' passed to ${0}. It should be 'TABLES_ONLY' or 'TABLES_AND_COLUMNS'"
    exit 1
fi
