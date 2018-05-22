#!/bin/bash

./01_download_dataset.sh
./02_prepare_hdfs_directory.sh
./03_upload_in_hdfs.sh
./04_create_hive_tables.sh
./05_load_files_into_tables.sh
./06_count_all.sh
./07_populate_derived_data.sh

# En l'état les statistics ne sont pas calculées automatiquement par run_all.sh: l'idée est de pouvoir
# comparer le comportement des jobs Spark AVANT / APRES calcul des statistiques

#./08_compute_statistics.sh TABLES_ONLY
#./08_compute_statistics.sh TABLES_AND_COLUMNS





