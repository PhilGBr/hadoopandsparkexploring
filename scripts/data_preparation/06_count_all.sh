#!/bin/bash

## Mandatory parameter(s)
##	-> $1: the schema name (AKA database name) in which tables are located
##

if [ -z "$1" ]; then
	echo "Arg \$1 (the schema name) is missing. Exit script ${0}"
	exit 0
fi

#  Load environment variables
source 00_env.sh


# Counts number of lines of .csv source files as they were downloaded (note: they have 1 header line)
wc -l ${LOCAL_MOVIELENS_DATADIR}/ml-latest/*.csv

HIVE2_SRV_URL=$(get_hive_cnx_url $1)

# Counts number of rows contained in HIVE tables
beeline  -u ${HIVE2_SRV_URL} -f ./06_count_all.hql --outputformat=csv  --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT;