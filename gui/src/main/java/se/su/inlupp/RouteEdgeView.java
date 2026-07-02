package se.su.inlupp;

import javafx.scene.Group;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class RouteEdgeView extends Group {


    public RouteEdgeView(ArrayList<double[]> coordinates) {

        for (int i = 0; i < coordinates.size(); i++) {
            Line line = new Line(coordinates.get(i)[0], coordinates.get(i)[1], coordinates.get(i)[2], coordinates.get(i)[3]);
            getChildren().add(line);
        }
    }

    public void RemoveLine(int removeLine) {
        if (removeLine >= 0 && removeLine < getChildren().size()) {
            getChildren().remove(removeLine);
        }
    }
}
