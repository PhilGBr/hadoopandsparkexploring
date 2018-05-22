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
   echo "  copying: ${file}"
   hdfs dfs -copyFromLocal $file $HDFS_MOVIELENS_DATADIR
   hdfs dfs -cp $HDFS_MOVIELENS_DATADIR/$file $HDFS_MOVIELENS_DATADIR_FOR_HIVE
done





