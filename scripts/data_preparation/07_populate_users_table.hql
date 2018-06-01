WITH
seg_ratings AS
(
  select user_id,
         CASE
         WHEN nb_ratings >= 1000 THEN "1000+"
         WHEN nb_ratings >= 500 THEN "500-999"
         WHEN nb_ratings >= 100 THEN "100-499"
         WHEN nb_ratings >= 50 THEN "50-99"
         WHEN nb_ratings >= 10 THEN "10-49"
         ELSE "1-9"
         END as segment_rater
  from (
    select user_id, count(*) as nb_ratings
    from ratings
    group by user_id
  ) as user_nb_ratings
),
seg_tags AS
(
  select user_id,
         CASE
         WHEN nb_tags >= 1000 THEN "1000+"
         WHEN nb_tags >= 500 THEN "500-999"
         WHEN nb_tags >= 100 THEN "100-499"
         WHEN nb_tags >= 50 THEN "50-99"
         WHEN nb_tags >= 10 THEN "10-49"
         ELSE "1-9"
         END as segment_tagger
  from (
    select user_id, count(*) as nb_tags
    from tags
    group by user_id
  ) as user_nb_tags
)
--INSERT OVERWRITE TABLE users (user_id, segment_rater, segment_tagger) 
--   -> despite HIVE-9481, cannot figure out how to have this working 
--   => sqeeze column list specification from this INSERT statement
INSERT OVERWRITE TABLE users
SELECT seg_ratings.user_id, segment_rater, segment_tagger
FROM seg_ratings FULL OUTER JOIN seg_tags
  ON (seg_ratings.user_id = seg_tags.user_id);