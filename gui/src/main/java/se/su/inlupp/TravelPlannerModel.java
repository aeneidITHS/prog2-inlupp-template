package se.su.inlupp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TravelPlannerModel {
    private final ListGraph<City> cities;
    private final TravelFileManager travelFileManager;
    private PathFinder<City> pathFinder = new BFSPathFinder<>();
    private String algorithmName = "BFS";
    private String imagePath;
    public TravelPlannerModel(){
        cities = new ListGraph<>();
        createBasicCityList();
        this.travelFileManager = new TravelFileManager(this);

        pathFinder = new BFSPathFinder<>();
        algorithmName = "BFS";

    }


    private void createBasicCityList(){
        cities.add(new City("Stockholm",0,250,150));
        cities.add(new City("London",1,200,100));
        cities.add(new City("Berlin",2,350,140));
        cities.add(new City("Helsinki",3,400,120));
        cities.add(new City("Rome",4,130,170));
        cities.add(new City("Paris",5,180,80));
    }

    public Set<City> getCities() {
        return cities.getNodes();
    }

    public City getCity(City city){
        for (City city1 : cities){
            if(city1.equals(city)){
                return city1;
            }
        }
        return null;
    }

    public City getCityByName(String name){
        for(City city: cities){
            if(city.name().equalsIgnoreCase(name)){
                return city;
            }
        }
        return null;
    }

    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public void useBFS() {
        pathFinder = new BFSPathFinder<>();
        algorithmName = "BFS";
    }

    public void useDFS() {
        pathFinder = new DFSPathFinder<>();
        algorithmName = "DFS";
    }

    public String getCurrentAlgorithmName() {
        return algorithmName;
    }

    public boolean removeCity(City city){
        if(cities.hasNode(city)){
            cities.remove(city);
            return true;
        }
        else {
            System.out.println("That city does not exist!");
            return false;
        }

    }
    public void removeAllCities(){
        for (City city: new ArrayList<>(cities.getNodes())){
            cities.remove(city);
        }
    }

    public boolean addCities(City city){
        if(cities.hasNode(city)){
            System.out.println("The city already exists!");
            return  false;
        }
        else {
            cities.add(city);
            return true;
        }
    }

    public void connectCities(City from, City to, int weight, String connectionName){
        if(graphContainsNodes(from,to)){
            cities.connect(from,to,connectionName,weight);
        }
    }

    public Set<String> getConnections(){
        Set<String> connectionLines = new HashSet<>();
        Set<String> addedConnections = new HashSet<>();

        for(City from : cities){
            for(Edge<City> edge: cities.getEdgesFrom(from)){
                City to = edge.getDestination();

                String key1 =  from.name() + ";" + to.name();
                String key2 =  to.name() + ";" + from.name();

                if (!addedConnections.contains(key1) && !addedConnections.contains(key2)) {
                    connectionLines.add("EDGE;" +
                                    from.name() + ";" +
                                    to.name() + ";" +
                                    edge.getName() + ";" +
                                    edge.getWeight() + "\n");

                    addedConnections.add(key1);
                }
            }
        }
        return connectionLines;
    }


    public Path<City> findPath(City from, City to) {
        Path<City> path = pathFinder.findPath(cities, from, to);
        return path;
    }

    public void saveGraph(File fileName){
        travelFileManager.saveGraph(fileName);
    }

    public void loadGraph(File fileName) throws FileNotFoundException {
        setImagePath(null);
        travelFileManager.loadGraph(fileName);
    }

    private List<String> convertPathToCityNames(Path<City> path){
        List<String> cityNames = new ArrayList<>();

        for(City city : path.getNodes()){
            cityNames.add(city.name());
        }
        return cityNames;
    }

    private boolean graphContainsNodes(City node1, City node2){
        return cities.hasNode(node1) && cities.hasNode(node2);
    }
}
