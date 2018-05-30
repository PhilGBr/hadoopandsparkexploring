#!/bin/bash

## Mandatory parameter(s)
##  ->  $1 : schema name (AKA database name) to be created and populated; if not defined
##           the value 'movieslens' is used
##

if [ -n "$1" ]; then
    MY_SCHEMA=$1    
else
    MY_SCHEMA="movielens"
fi
    



./04a_create_hive_schema.sh $MY_SCHEMA
./04b_create_hive_tables.sh $MY_SCHEMA
./05_load_files_into_tables.sh $MY_SCHEMA
./06_count_all.sh $MY_SCHEMA 
./07_populate_derived_data.sh $MY_SCHEMA
#./08_compute_statistics.sh $MY_SCHEMA TABLES_ONLY
./08_compute_statistics.sh $MY_SCHEMA TABLES_AND_COLUMNS

