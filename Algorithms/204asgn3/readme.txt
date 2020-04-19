Program reads word_pairs.txt and builds a complete graph from all those words listed in that text file.
To calculate the cosine similarity for determining the weights between edges, program uses wordVec.txt.
For each word in word_pairs.txt cosine similarity is calculated and a complete graph is built according to those where
each and every vertex is a neighbour.
If cosine similarity is evaluated as a negative number following formula is applied:
new_cosine_similarity = (1-old_cosine_similarity) / 2
After building the graph minimum spanning tree is formed using Prim's algorithm.
Lastly using this minimum spanning tree graph clustered into given amount of cluster number.
For clustering everytime element in the middle is deleted from MST. 

Compiles with
-javac Assignment3.java
Runs with
-java Assignment3 <word Vec file>.txt <word pairs file>.txt <output File Name>.txt <numberOfClusters>
Example run command
-java Assignment3 wordVec.txt word_pairs.txt cluster.txt 4