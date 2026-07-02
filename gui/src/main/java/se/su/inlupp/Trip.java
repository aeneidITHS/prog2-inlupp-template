package se.su.inlupp;

import java.time.LocalDateTime;
import java.util.List;

public record Trip(String from, String to, String algorithm, List<String> path, int totalWeight,
                   LocalDateTime createdAt) {

    public Trip(String from, String to, String algorithm, List<String> path, int totalWeight) {
        this(from, to, algorithm, path, totalWeight, LocalDateTime.now());
    }


    public String toFileString() {
        return algorithm + ";" +
                from + ";" +
                to + ";" +
                path + ";" +
                totalWeight + ";" +
                createdAt;
    }

    @Override
    public String toString() {
        return "<Trip/>"
                + "\t<algorithm/>" + algorithm + "</algorithm>"
                + "\t<from/>" + from + "</from>"
                + "\t<to/>" + to + "</from>"
                + "\t<Path/>" + path.toString() + "</Path>"
                + "\t<created at/>" + createdAt + "</created at>" + ";";
    }
}
