#!/bin/bash


if [ -z "$1" ]
  then
    echo "No argument supplied to script :${0}. Exit"
    exit 1
fi


./launch_hive_script.sh  ./07_populate_users_table.hql

