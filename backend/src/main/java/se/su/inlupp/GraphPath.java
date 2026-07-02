package se.su.inlupp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GraphPath<T> implements Path<T>{
    private final T start;
    private final List<Edge<T>> edges;

    public GraphPath(T start, List<Edge<T>> edges){
        this.start = start;
        this.edges = new ArrayList<>(edges);

    }

    @Override
    public Iterator<Edge<T>> iterator() {
        return edges.iterator();
    }

    @Override
    public T getStart() {
        return start;
    }

    @Override
    public T getEnd() {
        if (edges.isEmpty()) {
            return start;
        }
        return edges.get(edges.size() - 1).getDestination();
    }

    @Override
    public int getTotalWeight() {
        int total = 0;
        for (Edge<T> edge : edges) {
            total += edge.getWeight();
        }
        return total;
    }

    @Override
    public List<Edge<T>> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    @Override
    public List<T> getNodes() {
        List<T> nodes = new ArrayList<>();
        nodes.add(start);
        for (Edge<T> edge : edges) {
            nodes.add(edge.getDestination());
        }
        return nodes;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Start: ").append(start).append("\n");
        for(Edge<T> edge: edges)  {
            sb.append(edge).append("\n");
        }
        sb.append("End: ").append(getEnd()).append("\n");
        sb.append("Totalweight: ").append(getTotalWeight());
        return sb.toString();

    }


}
