package se.su.inlupp;

import java.util.*;

public class ListGraph<T> implements Graph<T> {

  private final Map<T, List<Edge<T>>> adjacencyList = new HashMap<>();


  public ListGraph(){
  }


  @Override
  public void add(T node) {
    adjacencyList.putIfAbsent(node, new ArrayList<>());
  }

  @Override
  public void remove(T node) {
    if(!hasNode(node)){
      throw new NoSuchElementException("The node doesn't exist");    }
    for(T otherNode : adjacencyList.keySet()){
      if(!otherNode.equals(node)){
        adjacencyList.get(otherNode).removeIf(edge -> edge.getDestination().equals(node));
      }
    }
    adjacencyList.remove(node);
  }

  @Override
  public boolean hasNode(T node) {
    return adjacencyList.containsKey(node);
  }

  @Override
  public void connect(T node1, T node2, String name, int weight) {
    if (!hasNode(node1) || !hasNode(node2)) {
      throw new NoSuchElementException("Both nodes must exist");
    }

    if (weight < 0) {
      throw new IllegalArgumentException("Weight cannot be negative");
    }

    if (node1.equals(node2)) {
      throw new IllegalArgumentException("A node cannot connect to itself");
    }

    if (getEdgeBetween(node1, node2) != null) {
      throw new IllegalStateException("Connection already exists");
    }

    adjacencyList.get(node1).add(new GraphEdge<>(node2, name, weight));
    adjacencyList.get(node2).add(new GraphEdge<>(node1, name, weight));
  }

  @Override
  public void disconnect(T node1, T node2) {
    if (!hasNode(node1) || !hasNode(node2)) {
      throw new NoSuchElementException("Both nodes must exist");
    }

    if (getEdgeBetween(node1, node2) == null) {
      throw new IllegalStateException("Connection does not exist");
    }

    adjacencyList.get(node1).removeIf(edge ->
            edge.getDestination().equals(node2));

    adjacencyList.get(node2).removeIf(edge ->
            edge.getDestination().equals(node1));
  }

  @Override
  public void setConnectionWeight(T node1, T node2, int weight) {
    if(!hasNode(node1) || !hasNode(node2)){
      throw new NoSuchElementException("Both nodes must exist");    }
    if(weight < 0 ){
      System.out.println("Weight can not be negative");
    }

    Edge<T> edge1 = getEdgeBetween(node1,node2);
    Edge<T> edge2 = getEdgeBetween(node2,node1);

    if (edge1 == null || edge2 == null){
      throw new NoSuchElementException("Edge doesn't exist");
    }

    edge1.setWeight(weight);
    edge2.setWeight(weight);
  }

  @Override
  public Set<T> getNodes() {
    return new HashSet<>(adjacencyList.keySet());
  }

  @Override
  public Collection<Edge<T>> getEdgesFrom(T node) {
    if(!hasNode(node)){
      throw new NoSuchElementException("Doesn't have node!");
    }

    return new ArrayList<>(adjacencyList.get(node));
  }

  @Override
  public Edge<T> getEdgeBetween(T node1, T node2) {
    if(!hasNode(node1) || !hasNode(node2)){
      throw new NoSuchElementException("Both nodes must exist");
    }
    for(Edge<T> edge : adjacencyList.get(node1)){
      if(edge.getDestination().equals((node2))){
        return edge;
      }
    }
    return null;
  }

  @Override
  public Iterator<T> iterator() {
    return  adjacencyList.keySet().iterator();
  }
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    for (T node : adjacencyList.keySet()) {
      builder.append(node).append(" -> ");
      builder.append(adjacencyList.get(node));
      builder.append("\n");
    }

    return builder.toString();
  }
}

