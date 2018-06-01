set hive.variable.substitute=true;

-- filename: genome-scores.csv
-- movieId,tagId,relevance
-- 1,1,0.024749999999999994
CREATE TABLE IF NOT EXISTS genome_scores (
    movie_id INT,
    tag_id INT,
    relevance FLOAT)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' ESCAPED BY '"'
STORED AS TEXTFILE;

-- filename: genome-tags.csv
-- header: tagId,tag
-- example: 3,18th century
CREATE TABLE IF NOT EXISTS genome_tags (
    tag_id INT,
    tag STRING)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' ESCAPED BY '"'
STORED AS TEXTFILE;


-- filename: links.csv
-- header: movieId,imdbId,tmdbId
-- example: 1,0114709,862
CREATE TABLE IF NOT EXISTS links (
    movie_id INT,
    imdb_id INT,
    tmdb_id INT)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' ESCAPED BY '"'
STORED AS TEXTFILE;


-- filename: movies.csv
-- header: movieId,title,genres
-- example: 1,Toy Story (1995),Adventure|Animation|Children|Comedy|Fantasy
CREATE TABLE IF NOT EXISTS movies (
    movie_id INT,
    title STRING,
    genres STRING)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' ESCAPED BY '"'
STORED AS TEXTFILE;


-- filename: ratings.csv
-- header: userId,movieId,rating,timestamp
-- example: 1,110,1.0,1425941529

CREATE TABLE IF NOT EXISTS ratings (
    user_id INT,
    movie_id INT,
    rating FLOAT,
    time INT)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' ESCAPED BY '"'
STORED AS TEXTFILE;



-- filename: tags.csv
-- header: userId,movieId,tag,timestamp
-- example: 20,4306,Dreamworks,1459855607

CREATE TABLE IF NOT EXISTS tags (
    user_id INT,
    movie_id INT,
    tag STRING,
    time INT)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' ESCAPED BY '"'
STORED AS TEXTFILE;


-- no filename: the users table will contain user statistics derived from ratings and tags tables

CREATE TABLE IF NOT EXISTS users (
    user_id INT,
    segment_rater STRING,
    segment_tagger STRING)
STORED AS ORC;
