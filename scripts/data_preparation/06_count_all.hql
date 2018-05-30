    select 'genomes_scores' as name, count(*) as cpt from genome_scores
union all
	select 'genome_tags' as name, count(*) as cpt from genome_tags
union all
	select 'links' as name, count(*) as cpt from links
union all
	select 'movies' as name, count(*) as cpt from movies
union all
	select 'ratings' as name, count(*) as cpt from ratings
union all
	select 'tags' as name, count(*) as cpt from tags
;