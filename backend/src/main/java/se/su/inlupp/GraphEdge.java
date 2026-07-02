package se.su.inlupp;

public class GraphEdge<T> implements Edge<T> {

    private final T destination;
    private final String name;
    private int weight;


    public GraphEdge(T destination, String name, int weight){
        if(weight < 0 ){
            throw new IllegalArgumentException("weight can not be negative");
        }
        this.destination = destination;
        this.name = name;
        this.weight = weight;
    }
    @Override
    public T getDestination() {
        return destination;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setWeight(int weight) {
        if(weight < 0){
            throw new IllegalArgumentException("weight can not be negative");
        }
        this.weight = weight;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "till " + destination + " med " + name + " tar " + weight;
    }
}
