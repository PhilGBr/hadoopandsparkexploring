analyze table genome_scores compute statistics FOR COLUMNS movie_id, tag_id, relevance ;
analyze table genome_tags compute statistics  FOR COLUMNS tag_id, tag ;
analyze table links compute statistics FOR COLUMNS movie_id,imdb_id,tmdb_id;
analyze table movies compute statistics FOR COLUMNS movie_id,title,genres;
analyze table ratings compute statistics FOR COLUMNS user_id,movie_id,rating,time ;
analyze table tags compute statistics FOR COLUMNS user_id,movie_id,tag,time ;
analyze table users compute statistics FOR COLUMNS user_id, segment_rater, segment_tagger;