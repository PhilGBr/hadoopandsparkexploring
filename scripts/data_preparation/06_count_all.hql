    select 'genomes_scores' as name, count(*) as cpt from movielens.genome_scores
union all
	select 'genome_tags' as name, count(*) as cpt from movielens.genome_tags
union all
	select 'links' as name, count(*) as cpt from movielens.links
union all
	select 'movies' as name, count(*) as cpt from movielens.movies
union all
	select 'ratings' as name, count(*) as cpt from movielens.ratings
union all
	select 'tags' as name, count(*) as cpt from movielens.tags
;