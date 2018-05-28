set hive.variable.substitute=true;

--
--       Failed attempts to use variable substitution within .hql scripts
--

LOAD DATA INPATH '${HDFS_MOVIELENS_DATADIR_FOR_HIVE}/tags.csv' OVERWRITE INTO TABLE movielens.tags;
LOAD DATA INPATH '${env:/HDFS_MOVIELENS_DATADIR_FOR_HIVE}/tags.csv' OVERWRITE INTO TABLE movielens.tags;
LOAD DATA INPATH '${hiveconf:/tmp/data/movielens-data-200-copy-for-hive}/tags.csv' OVERWRITE INTO TABLE movielens.tags;
