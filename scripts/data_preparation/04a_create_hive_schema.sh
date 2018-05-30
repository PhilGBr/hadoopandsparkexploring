#!/bin/bash

## Mandatory parameter(s)
##	-> $1: the schema name (AKA database name) to be created
##

if [ -z "$1" ]; then
	echo "Arg \$1 (the schema name to be created) is missing. Exit script ${0}"
	exit 0
fi


#  Load environment variables
source 00_env.sh

TIMESTAMP=`date +"%Y%m%d_%H%M%S"`
TEMPLATE_HQL="04a_create_hive_schema.hql"
TRANSFORMED_HQL="${TEMPLATE_HQL}.${TIMESTAMP}"

sed "s/__REPL_SCHEMA_NAME__/$1/" < $TEMPLATE_HQL > $TRANSFORMED_HQL

# to create a new schema, we connect to the database with the "default" schema
./launch_hive_script.sh $TRANSFORMED_HQL "default"



