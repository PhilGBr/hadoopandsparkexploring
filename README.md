# hadoop-and-spark-exploration

 
<h1>Objectifs</h1>
<p>Le code et les exemples contenus dans ce repository ont eu pour objectifs de se familiariser avec l'environnement Hadoop/Spark, ainsi que le développement d'une application Spark en language Java.</p>


<h1>Priorités</h1>
Spark étant un sujet "relativement" vaste et complexe (tant en terme d'APIs que de configurations de déploiement - et même que de configuration tout court !), j'ai du établir un choix drastique quant aux APIs que j'allais aborder ici.</p>
<p>Le thème initialement retenu était l'étude des différentes possibilités et techniques de JOINTURE de datasets avec Spark, les jointures sur de gros volumes étant toujours une source de lenteurs et d'erreurs d'exécution (OOM, No space left on disk, ...).</p>
<p>A date, le code ne reflète pas ce choix initial</p>

<h1>Choix de la version de Spark</h1>
J'ai choisi de travailler avec une version Spark 2.x récente. Je me suis aligné sur la version de la distribution que j'allais employer pour les tests, à savoir [Hortonworks Data Platform 2.6.4](https://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.6.4/bk_release-notes/content/comp_versions.html) qui supporte la version 2.2.
</p>
<h1>Datasets</h1>
[The movielens data (ml-latest.zip)](https://grouplens.org/datasets/movielens/latest/)

<h1>Réalisation</h1>


<p>En terme d'APIs, j'ai choisi d'utiliser et de comparer les 3 APIs disponibles, à savoir Dataset, Dataframe et "RDD classic".</p>

<p>La phase de réalisation m'a confronté à des obstacles plus prosaïques que le sujet initial. A titre d'exemple:<ul>
<li>la lenteur d'exécution des tests unitaires nécessitant une SparkSession m'a conduit à rechercher une solution et retenir au final l'annotation <code>SparkTest</code></li>
</ul>

<h1>Résultats</h1>
A date, le repository contient:<ul>
<li> des scripts de chargements d'un dataset et leur chargement dans HIVE</li>
<li> deux taches <code>FindMoviesWithLowestAvgRating</code> et <code>GroupMoviesByRatings</code> qui contiennnent différentes implémentations (DF, DS, RDD) de la même fonctionnalité, en ajoutant parfois une "erreur de programmation" (ex.: oublier une projection) en vue d'en mesurer l'impact sur les performances</li>
<li> les classes de tests unitaires correspondantes</li>
<li> un <code>Aspect</code> permettant d'adjoindre aux taches un timer, et de réaliser par la suite un benchmark comparatif des implémentations et/ou des configurations de déploiement
<li> un programme <code>Main</code> principal qui exécute les taches
<li> un script de lancement du programme via <code>spark-submit</code>
</ul>
</p>


<h1>Exploitation</h1>
<h2>Pré-requis</h2>

<h3>Pré-requis: environnement au build</h1>
<ul>
<li>Git</li>
<li>Java 8+</li>
<li>Maven 3.3+ doit être installé afin de builder le programme</li>
<li>Optionnellement (pour un build au sein d'un IDE (Eclipse, Intellij, ...)):
<ul><li>l'installation des plugins AspectJ dédiés à cet IDE est nécessaire</li>
![Les Plugins AspectJ pour Eclipse](README_images/aspectj-plugins-eclipse.PNG)
    <li>à défaut, le code sera fonctionnel, mais les temps d'exécution ne sont ni calculés, ni consignés par le logger.</li>
</ul></ul>


<h3>Pré-requis: environnement d'exécution</h3>
<p><b>Un environnement Hadoop (HDFS, HIVE) + Spark 2.x est nécessaire</b></p>
<p>Un compte utilisateur <b>accessible via ssh, et pour lequel l'environnement Hadoop/Spark est accessible et configuré</b></p>
<p>Pour la création de la base HIVE et le chargement des données, les scripts de chargement nécessitent l'outil en ligne de commande beeline
<p>Les scripts de chargement nécessitent que <code>bash</code> soit configuré comme shell par défaut via un lien sur <code>/bin/sh</code> (au besoin, le #shebang des scripts .sh pourra être modifié).</p>


<h2>Installation</h2>

<h3>Etape 1: clone du repository</h3>

<p><code>git clone https://github.com/PhilGBr/hadoopandsparkexploring.git</code></p>

<h3>Etape 2: paramétrage des variables d'environnement pour le chargement des données</h3>
<p><code>cd hadoopandsparkexploring</code></p>
<p><code>vi scripts/data_preparation/00_env.sh</code></p>
<p>Vérifier et modifier si besoin les variables suivantes:</p>
<p><code>export LOCAL_MOVIELENS_DATADIR=~/tmp/movielens   -- (stockage du dataset <b>sur le fs local du noeud driver</b>)</code></p>  
<p><code>export HDFS_MOVIELENS_DATADIR=/tmp/data/movielens-data-200  -- (stockage du dataset <b>sous HDFS</b>)</code></p> 
<p><code>export HDFS_MOVIELENS_DATADIR_FOR_HIVE=/tmp/data/movielens-data-200-copy-for-hive  -- (copie du dataset <b>sous HDFS</b>)</code></p>
<p><code>export <b>HIVE2_SRV_URL</b>=jdbc:hive2://sandbox.hortonworks.com:10000/default</code></p>

<h3>Etape 3: copie des scripts sur le noeud driver</h3>
<h3>Etape 3.1: preparation des répertoires sur le fs local du driver</h3>

<p><code>ssh -p &lt;port&gt; -i &lt;/some/patch/to/&lt;rsa_private_key&gt; &lt;user&gt;@&lt;ip&gt;</code></p>
<p><code>mkdir spark-test</code></p>
<p><code>cd spark-test</code></p>
<p><code>mkdir scripts</code></p>
<p>Après exécution, quitter pour revenir à la session sur le poste (de dev)</p>
<p><code>exit</code></p>
<h3>Etape 3.2: copie depuis le poste (de dev) sur le fs local du driver</h3>
<p><code>scp -P &lt;port&gt; -i &lt;/some/patch/to/&lt;rsa_private_key&gt; scripts/data_preparation/* &lt;user&gt;@&lt;ip&gt;:~/spark-test/scripts</code></p>
<p><code>scp -P &lt;port&gt; -i &lt;/some/patch/to/&lt;rsa_private_key&gt; scripts/launching/run_with_spark-submit.sh &lt;user&gt;@&lt;ip&gt;:~/spark-test</code></p>
<h3>Etape 3.3: Execution du script de chargement depuis le driver</h3>
<p><code>ssh -p &lt;port&gt; -i &lt;/some/patch/to/&lt;rsa_private_key&gt; &lt;user&gt;@&lt;ip&gt;</code></p>
<p><code>cd spark-test</code></p>
<p><code>./scripts/run_all.sh</code></p>
<p>Après le chargement des données, quitter pour revenir à la session sur le poste (de dev)</p>
<p><code>exit</code></p>

<h2>Etape 4: Build du progamme</h2>
<p><code>maven package</code></p>

<h2>Etape 5: Déploiement sur le driver</h2>
<code>scp -P &lt;port&gt; -i &lt;/some/patch/to/&lt;rsa_private_key&gt; target/*.jar &lt;user&gt;@&lt;ip&gt;:~/spark-test</code>

<h2>Etape 6: Configuration log4j</h2>
<p>Notes:<ul>
<li>Il existe différentes façons de (re-)configurer log4j pour une application Spark, qui varient selon le mode de soumission du job à exécuter, et la partie de code ciblée par la modification de configuration</li>
<li>En l'occurrence, la modification de configuration log4j impacte uniquement du code exécuté par le driver</li>
<li>Par ailleurs, la soumission de job spark se fait en mode <code>'client'</code></li>
<li>Il n'est donc pas nécessaire ici de propager la configuration aux executors</li></ul>
</p>
<h3>Etape 6.1: localisation du fichier à modifier</h3>
<p>Localiser le fichier log4j.properties de la distribution Spark installée sur le driver.</p>
<p>Exemple: pour la distribution HDP 2.6.4 d'Hortonworks, en étant loggué en tant que <code>maria_dev</code>, le fichier se trouve sous <code>/etc/spark2/2.6.4.0-91/0/log4j.properties</code></p>
<h3>Etape 6.2: édition du fichier</h3>
<p>Editer le fichier, et ajouter la configuration suivante <span style="color:red">(en prenant soin de modifier le chemin vers le fichier de logs)</span>:</p>
<p><code>log4j.logger.philgbr.exploration.spark.utils.LogExecutionTime=INFO, console, exectimeFileAppender</code></p>
<p><code>log4.additivity.philgbr.exploration.spark.utils.LogExecutionTime=true</code></p>

<p><code>log4j.appender.exectimeFileAppender=org.apache.log4j.FileAppender</code></p>
<p><code>log4j.appender.exectimeFileAppender.File=<span style="color:red">/tmp/spark-tasks-execution-time.log</span></code></p>
<p><code>log4j.appender.exectimeFileAppender.layout=org.apache.log4j.PatternLayout</code></p>
<p><code>log4j.appender.exectimeFileAppender.layout.ConversionPattern=%d{yy/MM/dd HH:mm:ss} %p %c{1}: %m%n</code></p>


<h2>Etape 7: Lancement du programme depuis le noeud driver</h2>

<p><code>ssh -p &lt;port&gt; -i &lt;/some/patch/to/&lt;rsa_private_key&gt; &lt;user&gt;@&lt;ip&gt;</code></p>
<p><code>cd spark-test</code></p>
<p>Editer le fichier <code>run_with_spark-submit.sh</code> et <span style="color:red"><b>modifier si besoin la valeur de la variable <code>MASTER_URL</code></span></b>
<p><code>vi run_with_spark-submit.sh</code></p>
<p><code>./run_with_spark-submit.sh</code></p>

<h2>Etape 8: Consulter les résultats</h2>
<p> Ils sont consultables dans le fichier dont le chemin complet a été configuré à l'étape 6 (dans notre example: <code>/tmp/spark-tasks-execution-time.log</code></p> 

<p> Par ailleurs, la sortie erreur et la sortie standard sont redirigées vers les fichiers horodatés <code>output-$TIMESTAMP.log</code> et <code>error-$TIMESTAMP.log</code> dans le répertoire courant<p>

<h1>Reste à faire</h1>
<h2>Corrections</h2>
<p>En l'état, 2 erreurs d'exécution sur le cluster HDP n'ont pas encore été corrigées. Elles concernent toutes les deux les implémentations RDD. Ces erreurs ne se produisent pas dans les tests unitaires.<p>
<p><ul>
<li> Faire varier les clauses de stockages des tables HIVE pour observer les effets sur les temps de traitements</li>
<li> "Scaler les données", ajouter et faire varier les clauses de partionnements, et constater les effets en terme de temps de traitement</li>
<li> Déployer sur un cluster EMR, faire varier les configs de cluster, et constater les performances des différentes configurations de cluster</li>
</ul>


