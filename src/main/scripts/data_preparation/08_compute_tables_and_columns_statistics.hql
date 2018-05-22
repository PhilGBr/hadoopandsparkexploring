analyze table movielens.genome_scores compute statistics FOR COLUMNS movie_id, tag_id, relevance ;
analyze table movielens.genome_tags compute statistics  FOR COLUMNS tag_id, tag ;
analyze table movielens.links compute statistics FOR COLUMNS movie_id,imdb_id,tmdb_id;
analyze table movielens.movies compute statistics FOR COLUMNS movie_id,title,genres;
analyze table movielens.ratings compute statistics FOR COLUMNS user_id,movie_id,rating,time ;
analyze table movielens.tags compute statistics FOR COLUMNS user_id,movie_id,tag,time ;
analyze table movielens.users compute statistics FOR COLUMNS user_id, segment_rater, segment_tagger;