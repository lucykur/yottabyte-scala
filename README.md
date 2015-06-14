Spark shell on the cluster can be started by,
 spark-shell --master yarn
 
To run your code on cluster, you need to first create a fat jar by doing,
sbt assembly

Running job from your jar : 
spark-submit --class classToRun --master masterCanBeLocalOrYarn applicationJar appArguments
 
