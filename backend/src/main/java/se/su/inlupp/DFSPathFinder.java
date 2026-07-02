package se.su.inlupp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DFSPathFinder<T> implements PathFinder<T> {

  @Override
  public Path<T> findPath(Graph<T> graph, T from, T to) {
    Set<T> visited = new HashSet<>();
    ArrayList<Edge<T>> edges = new ArrayList<>();
    return dfs(graph, from, from, to, visited, edges);
  }

  private Path<T> dfs(Graph<T> graph, T from, T current, T to, Set<T> visited, ArrayList<Edge<T>> edges) {
    visited.add(current);

    if(current.equals(to)) {
      return new GraphPath<>(from, edges);
    }

    for(Edge<T> edge : graph.getEdgesFrom(current)) {
      T neighbor = edge.getDestination();
      if(!visited.contains(neighbor)) {
        edges.add(edge);
        Path<T> path = dfs(graph, from, neighbor, to, visited, edges);
        if(path != null) {
          return path;
        }
        edges.remove(edges.size() - 1);
      }
    }

    return null;
  }
}

