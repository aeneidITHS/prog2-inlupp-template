package se.su.inlupp;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CityNodeView extends Group {
    private City city;
    private Circle circle;
    private double startX, startY;
    private boolean selected = false;



    public CityNodeView(City city) {
        this.city = city;


        relocate(city.x(), city.y());



        circle = new Circle(0, 0, 12);
        circle.setFill(Color.ALICEBLUE);
        circle.setStroke(Color.BLACK);


        Label label = new Label(city.name());
        label.setLayoutX(-10);
        label.setLayoutY(15);


        getChildren().addAll(circle, label);


        setOnMousePressed(new StartDragHandler());
        setOnMouseDragged(new DragHandler());
        setOnMouseClicked(new ClickHandler());
    }

    public boolean isSelected() {
        return selected;
    }


    class StartDragHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            startX = event.getX();
            startY = event.getY();
        }
    }


    class DragHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            double newX = getLayoutX() + event.getX() - startX;
            double newY = getLayoutY() + event.getY() - startY;
            relocate(newX, newY);
        }
    }

    class ClickHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            CityNodeView c = (CityNodeView) event.getSource();
            if (!selected) {
                selected = true;
                c.circle.setFill(Color.ORANGE);
            } else {
                selected = false;
                c.circle.setFill(Color.ALICEBLUE);
            }
        }
    }


    public Circle getCircle() { return circle; }
    public City getCity() { return city; }

}
