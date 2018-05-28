#!/bin/bash

#  Load environment variables
source 00_env.sh


beeline -u ${HIVE2_SRV_URL} -f ./04_create_hive_tables.hql --verbose=$HIVE_VERBOSE --silent=$BEELINE_SILENT



