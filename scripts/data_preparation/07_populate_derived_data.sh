#!/bin/bash

## Mandatory parameter(s)
##	-> $1: the schema name (AKA database name) in which tables are stored
##

if [ -z "$1" ]; then
	echo "Arg \$1 (the schema name) is missing. Exit script ${0}"
	exit 0
fi

./launch_hive_script.sh  ./07_populate_users_table.hql $1

