package se.su.inlupp;

import java.util.*;

public class BFSPathFinder<T> implements PathFinder<T> {

  @Override
  public Path<T> findPath(Graph<T> graph, T from, T to) {
    Set<T> visited = new HashSet<>();
    LinkedList<T> queue = new LinkedList<>();
    Map<T, Edge<T>> cameFrom = new HashMap<>();
    Map<T, T> previousNode = new HashMap<>();

    queue.add(from);
    visited.add(from);

    while(!queue.isEmpty()) {
      T current = queue.poll();

      if(current.equals(to)) {
        LinkedList<Edge<T>> edges = new LinkedList<>();
        T node = to;
        while(!node.equals(from)) {
          Edge<T> edge = cameFrom.get(node);
          edges.addFirst(edge);
          node = previousNode.get(node);
        }
        return new GraphPath<>(from, edges);
      }

      for(Edge<T> edge : graph.getEdgesFrom(current)) {
        T neighbor = edge.getDestination();
        if(!visited.contains(neighbor)) {
          visited.add(neighbor);
          queue.add(neighbor);
          cameFrom.put(neighbor, edge);
          previousNode.put(neighbor, current);
        }
      }
    }

    return null;

  }
}

