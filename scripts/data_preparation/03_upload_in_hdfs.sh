#!/bin/bash

#  Load environment variables
source 00_env.sh

# Unzip then copy to HDFS
#
#   Note: I guess this method is suboptimal ... but it turns out that file handling (unzipping/copying) as well as
#   directory handling (duplicating) over HDFS is not trivial
#
#   Mounting HDFS on the NFS client could be an option (but out of our scope)
#

cd $LOCAL_MOVIELENS_DATADIR
unzip -o ml-latest.zip

echo "Copying files to HDFS directories"
cd ml-latest
files=$(ls *.csv)
for file in $files; do
	
	# Hive support for 'skip.header.line.count' does not span outside its own context 
	# (even for MANAGED TABLES) and thus does not span to Spark Sessions
	#  
	# => it's cleaner to trim data at the source, rather that spilling out implementation
	#  details (like spark.read(...).option("header" -> "true") in client application
	# source code
	
   echo " skipping 1st line then copying: ${file}"
   tail -n +2 ${file} > "${file}.trimmed"
   hdfs dfs -copyFromLocal "${file}.trimmed" $HDFS_MOVIELENS_DATADIR/${file}
   hdfs dfs -cp $HDFS_MOVIELENS_DATADIR/${file} $HDFS_MOVIELENS_DATADIR_FOR_HIVE
   rm "${file}.trimmed"
done





