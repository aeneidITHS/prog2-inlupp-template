package se.su.inlupp;

public record City(String name, int id, int x, int y) {


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof City city) {
            return name.equals(city.name);
        }
        return false;
    }


    @Override
    public int hashCode() {
        return name.hashCode();


    }


    @Override
    public String toString() {
        return name;
    }
}

