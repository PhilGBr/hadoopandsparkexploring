#!/bin/bash

#  Load environment variables
source 00_env.sh

cd ${LOCAL_MOVIELENS_DATADIR}

if [ ! -f ml-latest.zip ]; then
    echo "downloading the  movielens dataset (ml-latest.zip) ...."
    wget http://files.grouplens.org/datasets/movielens/ml-latest.zip
    echo "download's done! the  movielens dataset (ml-latest.zip) is now available "
    else
        echo "The movielens dataset (ml-latest.zip) is already available !! "
fi

