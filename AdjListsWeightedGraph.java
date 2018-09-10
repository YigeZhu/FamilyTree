import java.util.*;
import java.io.*;
import javafoundations.ArrayIterator;
import javafoundations.LinkedStack;
import javafoundations.LinkedQueue;

/**
 * implementation of the Graph ADT
 * an adaptation of the AdjListsGraph<T> Class that creates a graph with weights and 
 * include method that could allow us to find a path from the start vertex to a destination
 * 
 * @Destiny Zhu, Melory So, Coco Kneer
 * @05/14/2018
 */
public class AdjListsWeightedGraph<T> implements Graph<T>
{ 
    private Vector<T> vertices;
    private Vector<LinkedList<T>> arcs;
    private Vector<LinkedList<Integer>> weights;
    /**
     * Constructor for objects of class AdjListsGraph
     */
    public AdjListsWeightedGraph()
    {
        vertices = new Vector<T>();
        arcs = new Vector<LinkedList<T>>();
        weights = new Vector<LinkedList<Integer>>();

    }
    
    public T getVertex(int index){
        return vertices.get(index);
    }
    
    public Vector<T> getVertices(){
        return this.vertices;
    }
    
    /**
     * Helper method.Inserts an arc with its weight between two vertices of the graph.
     * If arc already exists, ignores the addition.
     *
     * @param  int n1, int n2, int weight
     * @return no return
     */
    private void addArc(int n1, int n2, int weight){
        if(n1 >= vertices.size() || n1 < 0 || n2 >= vertices.size() || n2 < 0)
            return;
        if(arcs.get(n1).contains(vertices.get(n2)))
            return;
        LinkedList<T> l = arcs.get(n1);
        LinkedList<Integer> w = weights.get(n1);

        l.add(vertices.get(n2));
        w.add(weight);
    }

    /**
     * Inserts an arc with its weight between two vertices of the graph.
     *
     * @param  T v1, T v2, int weight
     * @return no return
     */
    public void addArc (T v1, T v2, int weight){
        int n1 = vertices.indexOf(v1);
        int n2 = vertices.indexOf(v2);
        addArc(n1, n2, weight);
    }

    /**
     * Inserts an edge between two vertices of the graph.
     * If one or both vertices do not exist, ignores the addition.
     *
     * @param  T vertex1, T vertex2, int weight
     * @return no
     */
    public void addEdge (T vertex1, T vertex2, int weight) {
        // getIndex will return NOT_FOUND if a vertex does not exist,
        // and the addArc() will not insert it
        this.addArc (vertex1, vertex2, weight); //set 0 as default weight
        addArc (vertex2, vertex1, weight);
    }

    /**
     * Remove a vertex from the graph. Ignores the removal if the vertex does not exist.
     *
     * @param  T v -- the vertex to be removed
     * @return no return
     */
    public void removeVertex(T v){
        int index = vertices.indexOf(v);
        //if v does not exist
        if(index==-1) return;
        //remove v from vertices
        vertices.remove(v);
        //remove the corresponding LL from arcs
        arcs.remove(index);
        //remove the corresponding LL from weights
        weights.remove(index);

        //remove v from all linked lists
        LinkedList<T> ll = new LinkedList<T>();
        LinkedList<Integer> w = new LinkedList<Integer>();
        for (int i=0; i < arcs.size(); i++){
            ll = arcs.get(i);
            int j = ll.indexOf(v);
            ll.remove(v);

            w = weights.get(i);
            if(j>=0) {
                w.remove(j);
            }
        }
    }

    /**
     * Remove an arc and its weight from the graph. Ignores the removal if the index is not valid.
     *
     * @param  int index1, int index2
     * @return no return
     */
    private void removeArc (int index1, int index2) {
        if (index1 >= 0 && index1 < this.vertices.size() && index2 >= 0 && index2 < this.vertices.size()) {
            T to = vertices.get(index2);
            LinkedList<T> connections = arcs.get(index1);
            LinkedList<Integer> w = weights.get(index1);

            int i = connections.indexOf(to);
            connections.remove(to);
            w.remove(i);
        }
    }

    /**
     * Get the weight of an arc. Return a invalid maximum integer value if there is no arc exist
     * between two vertices
     *
     * @param  T start, T destination
     * @return int weight
     */
    public int getWeight(T start, T destination) {
        int i = vertices.indexOf(start);

        LinkedList<T> ll = arcs.get(i);
        int j = ll.indexOf(destination);
        if(j < 0)
            return Integer.MAX_VALUE;

        return weights.get(i).get(j);
    }

    /**
     * Returns a string representation of the graph.
     *
     * @param  no
     * @return String -- the string representation of the graph
     **/
    public String toString() {
        if (vertices.size() == 0) return "Graph is empty";

        String result = "Vertices: \n";
        result = result + vertices;

        result = result + "\n\nEdges: \n";
        for (int i=0; i< vertices.size(); i++)
            result = result + "from " + vertices.get(i) + ": "  + arcs.get(i) + "\n";

        result = result + "\n\nCorresponding Weights: \n";
        for(int i=0; i< vertices.size(); i++)
            result = result + "from " + vertices.get(i) + ": " + weights.get(i) + "\n";
        return result;
    }

    /**
     * Get a path between two vertices
     *
     * @param  T vertex, T target
     * @return LinkedList<T> -- the path
     **/
    public LinkedList<T> getPath(T vertex, T target) {
        LinkedStack<T> ai = this.getPathStack(vertex, target);
        LinkedStack<T> aiReversed = new LinkedStack<T>();
        LinkedList<T> ll = new LinkedList();
        while (!ai.isEmpty()){
            aiReversed.push(ai.pop());
        }
        while(!aiReversed.isEmpty()){
            ll.add(aiReversed.pop());
        }
        return ll;
    }

    /**
     * Helper. Returns a stack representation of the path
     *
     * @param  T vertex, T target
     * @return LinkedStack<T> -- a stack representation of the path
     **/
    public LinkedStack<T> getPathStack(T vertex, T target) {
        T currentVertex;//keeps track of which vertex the traversal is currently at
        LinkedStack<T> traversalStack = new LinkedStack<T>();
        ArrayIterator<T> iter = new ArrayIterator<T>();
        boolean[] visited = new boolean[vertices.size()];//marks if each vertex is visited
        boolean found;//represents if an unvisited neighbour can be found

        int startIndex = vertices.indexOf(vertex);
        int targetIndex = vertices.indexOf(target);

        if(startIndex < 0)
            return traversalStack;

        //initially all vertices are unvisited    
        for(int vertexIdx = 0; vertexIdx < vertices.size(); vertexIdx++)
            visited[vertexIdx] = false;

        currentVertex = vertex;
        traversalStack.push(currentVertex);
        iter.add(vertices.get(startIndex));
        visited[startIndex] = true;//marks the vertex as visited

        while(!traversalStack.isEmpty()){
            currentVertex = traversalStack.peek();
            int currentIndex = vertices.indexOf(currentVertex);
            found = false;
            for (int vertexIdx = 0; vertexIdx < vertices.size()&& !found; vertexIdx++)

                if (arcs.get(currentIndex).contains(vertices.get(vertexIdx))
                && !visited[vertexIdx]){
                    traversalStack.push(vertices.get(vertexIdx));
                    iter.add(vertices.get(vertexIdx));
                    visited[vertexIdx] = true;
                    found = true;
                    if((vertexIdx == targetIndex))
                        return traversalStack;
                }
            //starts backtracking
            if (!found && !traversalStack.isEmpty()){
                traversalStack.pop();
            }
        }
        return traversalStack;
    }

    /************************************************
     * Below are the methods UNEDITED
     ************************************************/

    /******************************************************************
     * Inserts an edge between two vertices of the graph.
     * If one or both vertices do not exist, ignores the addition.
     ******************************************************************/
    public void addEdge (T vertex1, T vertex2) {
        // getIndex will return NOT_FOUND if a vertex does not exist,
        // and the addArc() will not insert it
        this.addArc (vertex1, vertex2);
        addArc (vertex2, vertex1);
    }

    /******************************************************************
     * Inserts an arc from v1 to v2.
     * If the vertices exist, else does not change the graph. 
     ******************************************************************/
    public void addArc (T source, T destination){
        int sourceIndex = vertices.indexOf(source);
        int destinationIndex = vertices.indexOf(destination);

        //if source and destination exist, add the arc. do nothing otherwise
        if ((sourceIndex != -1) && (destinationIndex != -1)){
            LinkedList<T> l = arcs.get(sourceIndex);
            l.add(destination);
        }
    }

    //  /******************************************************************
    //    Helper. Inserts an edge between two vertices of the graph.
    //    ******************************************************************/
    protected void addArc (int index1, int index2) {
        //if (indexIsValid(index1) && indexIsValid(index2))
        //vertices.get(index1).add(v2);
        LinkedList<T> l = arcs.get(index1-1);
        T v = vertices.elementAt(index2-1);
        l.add(v);
    }

    /**
     * Return true if the graph has no vertex (is empty)
     *
     * @param  none
     * @return true if the graph is empty
     */
    public boolean isEmpty(){
        return vertices.isEmpty();
    }

    /******************************************************************
     * Returns the number of vertices in the graph.
     ******************************************************************/
    public int getNumVertices(){
        return vertices.size();
    }

    /******************************************************************
     * Returns the number of arcs in the graph by counting them.
     ******************************************************************/
    public int getNumArcs(){
        int n = 0;
        for (int i=0 ; i < arcs.size(); i++){
            n = n+ arcs.get(i).size();
        }
        return n;
    }

    /** Returns true if this graph contains the vertex, false otherwise. */
    public boolean containsVertex(T vertex){
        return vertices.contains(vertex);
    }

    /******************************************************************
     * Returns true iff a directed edge exists from v1 to v2.
     ******************************************************************/
    public boolean isArc (T vertex1, T vertex2){ 
        try {
            int index = vertices.indexOf(vertex1);
            LinkedList<T> l = arcs.get(index);
            return (l.indexOf(vertex2) != -1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(vertex1 + " vertex does not belong in the graph");
            return false;
        }
    }

    // /******************************************************************
    // Helper. Returns true iff an arc exists between two given indices 
    // ******************************************************************/
    // private boolean isArc (int index1, int index2) {
    // if (indexIsValid(index1) && indexIsValid(index2))
    // return arcs[index1][index2];
    // else 
    // return false;
    // }

    /******************************************************************
    Returns true iff an edge exists between two given vertices
    which means that two corresponding arcs exist in the graph
     ******************************************************************/
    public boolean isEdge (T vertex1, T vertex2) {
        return (isArc(vertex1, vertex2) && isArc(vertex2, vertex1)); 
    }

    /******************************************************************
     * Returns true IFF the graph is undirected, that is, for every 
     *    pair of nodes i,j for which there is an arc, the opposite arc
     *    is also present in the graph.  
     * An empty graph is undirected ####is that all right?????
     ******************************************************************/
    public boolean isUndirected() {
        for (int i=0; i<arcs.size(); i++) {
            for (T vertex : arcs.get(i)) {
                int index = vertices.indexOf(vertex);
                LinkedList<T> l = arcs.get(index);
                if (l.indexOf(vertices.get(i)) == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    /******************************************************************
     * Saves the current graph into a .tgf file.
     * If it cannot save the file, a message is printed. 
     *****************************************************************/
    public void saveToTGF(String file){
        try{
            PrintWriter printer = new PrintWriter(new File(file));
            for(int i=0; i<vertices.size(); i++)
                printer.println((i+1) + " " + vertices.get(i));
            printer.println("#");

            for(int i=0; i<arcs.size(); i++){
                for(int j=0; j<arcs.get(i).size(); j++){
                    int to = vertices.indexOf(arcs.get(i).get(j)) + 1;
                    int weight = vertices.indexOf(weights.get(i).get(j));
                    printer.println(i+1 + " " + to + " " + weights);
                }
            }
            printer.close();
        }catch (IOException e){
            System.out.println("Cannot write in the file " + file);
        }
    }

    public void addVertex (T vertex){
        //if vertex already in the graph, do not add it again
        if(vertices.contains(vertex)) return;

        //add vertex
        vertices.add(vertex);
        //add its corresponding empty list of connection
        arcs.add(new LinkedList<T>());
        //add its corresponding empty list of weights
        weights.add(new LinkedList<Integer>());

    }

    public static AdjListsWeightedGraph<String> AdjListsGraphFromFile(String f){
        AdjListsWeightedGraph<String> g = new AdjListsWeightedGraph<String>();
        try{
            Scanner scan = new Scanner(new File(f));

            while(!scan.next().equals("#")){//read and discard the token
                String s = scan.next();
                g.addVertex(s);
            }
            int n1, n2, n3;
            while(scan.hasNext()){
                //read in arcs
                n1 = scan.nextInt();
                n2 = scan.nextInt();
                n3 = scan.nextInt();
                g.addArc(n1-1,n2-1, n3);
            }
            scan.close();
        }catch(IOException e){
            System.out.println("Cannot read from " + f);
        }
        return g;
    }

    /******************************************************************
     * Removes an edge between two vertices of the graph.
     * If one or both vertices do not exist, ignores the removal.
     ******************************************************************/
    public void removeEdge (T vertex1, T vertex2) {
        removeArc (vertex1, vertex2);
        removeArc (vertex2, vertex1);
    }

    /******************************************************************
     * Removes an arc from vertex v1 to vertex v2,
     * if the vertices exist, else does not change the graph. 
     ******************************************************************/
    public void removeArc (T vertex1, T vertex2) {
        int index1 = vertices.indexOf(vertex1);
        int index2 = vertices.indexOf(vertex2);
        removeArc (index1, index2);
    }

    public LinkedList<T> getPredecessors(T v){
        int index = vertices.indexOf(v);
        LinkedList<T> ll = new LinkedList<T>();
        for (int i=0; i<arcs.size(); i++){
            if(arcs.get(i).contains(v))
                ll.add(vertices.get(i));
        }
        return ll;
    }

    public LinkedList<T> getSuccessors(T v){
        int index = vertices.indexOf(v);
        //if vertex does not exist, return null
        if(index<0 || index>=vertices.size())
            return null;

        return arcs.get(index);
    }

    public static void main(String[] args){
        AdjListsWeightedGraph<String> g0 = new AdjListsWeightedGraph<String>();
        g0.addVertex("T");g0.addVertex("Q"); g0.addVertex("S"); g0.addVertex("W"); g0.addVertex("P");
        g0.addVertex("Y");g0.addVertex("Z"); g0.addVertex("R"); g0.addVertex("X");

        g0.addArc("P", "R" ,1); g0.addArc("P", "W" ,2);
        g0.addArc("R", "X" ,3);
        g0.addArc("Q", "X" ,4);
        g0.addArc("W", "Y" ,5); g0.addArc("W", "S" ,5);
        g0.addArc("Y", "R" ,6); g0.addArc("Y", "Z" ,7);
        g0.addArc("S", "T" ,8);
        g0.addArc("T", "W" ,1);

        System.out.println("\nTesting for the constructor and addArc:");
        System.out.println(g0);

        System.out.println("\nTesting for getWeight: ");
        System.out.println("from P to R. Expected: 1. Actual: " + g0.getWeight("P", "R"));
        System.out.println("from Q to X. Expected: 4. Actual: " + g0.getWeight("Q", "X"));

        System.out.println("\nTesting for getPath: ");
        System.out.println("from P to Z. Expected: [P, W, Y, Z]. Actual " + g0.getPath("P","Z"));
        System.out.println("from P to P. Expected: []. Actual " + g0.getPath("P","P"));
        System.out.println("from R to X. Expected: [R, X]. Actual " + g0.getPath("R","X"));
        System.out.println("from T to R. Expected: [T, W, Y, R]. Actual " + g0.getPath("T","R"));
        System.out.println("from Y to Q. Expected: []. Actual " + g0.getPath("Y","Q"));        

        System.out.println("\nTesting for removeVertex: ");
        g0.removeVertex("Y");
        System.out.println(g0);
        g0.removeVertex("Q");
        System.out.println(g0);

        System.out.println("\nTesting for addEdge: ");
        g0.addEdge("P", "X", 0);
        System.out.println(g0);

        System.out.println("\nTesting for removeArc: ");
        g0.removeArc("X","P");
        System.out.println(g0);
        g0.removeArc("P","R");
        System.out.println(g0);
        
        AdjListsWeightedGraph<String> g1 = new AdjListsWeightedGraph<String>();
        g1.addVertex("A");
        System.out.println(g0.getPath("A","A"));
    }
}

