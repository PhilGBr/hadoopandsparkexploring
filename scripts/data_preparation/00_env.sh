#!/bin/bash

# This script:
#   - defines and exports the environment variables used by the different scripts
#	- defines the get_hive_cnx_url() utility function
#   - creates the ${LOCAL_MOVIELENS_DATADIR} directory hierarchy under the user home directoty executing this script
#
#
# PLEASE, adapt the value of ${HDFS_MOVIELENS_DATADIR_FOR_HIVE} and ${HDFS_MOVIELENS_DATADIR_FOR_HIVE} path variables
# to any HDFS directory that ccould be host the dataset
#
# Local directory containing data to be moved to HDFS
export LOCAL_MOVIELENS_DATADIR=~/tmp/movielens
if [ ! -d ${LOCAL_MOVIELENS_DATADIR} ]; then
  mkdir -p ${LOCAL_MOVIELENS_DATADIR}
fi


#
# HDFS_MOVIELENS_DATADIR: HDFS directory containing the movielens dataset
#
export HDFS_MOVIELENS_DATADIR=/tmp/data/movielens-data-200

#
# HDFS_MOVIELENS_DATADIR_FOR_HIVE: contains a copy of ${HDFS_MOVIELENS_DATADIR} content, intended to be loaded into HIVE as MANAGED TABLEs
#
# Doing that allows us to play with the dataset with other tools (Pig, MR, or even HIVE / EXTERNAL TABLE) independently of HIVE testings
# and options (partitioning). # We'll also able to safely DROP tables into HIVE without removing the original files
#
export HDFS_MOVIELENS_DATADIR_FOR_HIVE=/tmp/data/movielens-data-200-copy-for-hive


# JDBC URL Cnx to HIVE: component are defined separately  
#
# 	See  https://cwiki.apache.org/confluence/display/Hive/HiveServer2+Clients#HiveServer2Clients-ConnectionURLs


export HIVE2_PROTOCOL="jdbc:hive2"
export HIVE2_HOST="sandbox.hortonworks.com"
export HIVE2_IP="10000"
export HIVE2_DEFAULT_DB="default"

# If no extra connection string parameters are required, leave it blank 
export HIVE2_EXTRA_CNX_STRING_PARAM=""

# Optionally, you can pass one argument that override the ${HIVE2_DEFAULT_DB} variable
get_hive_cnx_url() {

	URL="${HIVE2_PROTOCOL}://${HIVE2_HOST}:${HIVE2_IP}"	
	
	if [ -n "$1" ]; then
		URL="${URL}/$1"	
	elif [ -n "$HIVE2_DEFAULT_DB" ]; then
		URL="${URL}/${HIVE2_DEFAULT_DB}"	
	else
		URL="${URL}/default"
	fi
	
	if [ -n "$HIVE2_EXTRA_CNX_STRING_PARAM" ]; then
		URL="${URL};${HIVE2_EXTRA_CNX_STRING_PARAM}" 
	fi
	echo $URL
}


# Make sure UTF-8 is the default encoding (as CSV files to be loaded by HIVE are UTF-8 encoded)
export LANG=en_US.UTF-8

#HIVE_VERBOSE=false
export HIVE_VERBOSE=true

#BEELINE_SILENT=true
export BEELINE_SILENT=false