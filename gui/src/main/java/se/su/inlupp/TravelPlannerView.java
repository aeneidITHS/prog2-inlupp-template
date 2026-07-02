package se.su.inlupp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TravelPlannerView extends BorderPane {
    private TravelPlannerModel model;
    private Pane mapPane;
    private Label statusLabel;
    private TextArea display = new TextArea();
    private FileChooser fileChooser = new FileChooser();
    private boolean changed = false;
    private Stage stage;
    private ImageView backgroundImage = new ImageView();



    public TravelPlannerView(TravelPlannerModel model, Stage stage) {
        this.model = model;
        this.stage = stage;


        VBox vbox = new VBox();
        MenuBar menuBar = new MenuBar();
        vbox.getChildren().add(menuBar);


        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);


        MenuItem newItem = new MenuItem("New");
        fileMenu.getItems().add(newItem);
        newItem.setOnAction(new NewHandler());


        MenuItem openItem = new MenuItem("Open");
        fileMenu.getItems().add(openItem);
        openItem.setOnAction(new OpenHandler());


        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);
        saveItem.setOnAction(new SaveHandler());


        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);
        exitItem.setOnAction(new ExitItemHandler());

        Menu algorithmMenu = new Menu("Algorithm");
        menuBar.getMenus().add(algorithmMenu);

        MenuItem bfsItem = new MenuItem("BFS");
        algorithmMenu.getItems().add(bfsItem);
        bfsItem.setOnAction(new BFSHandler());

        MenuItem dfsItem = new MenuItem("DFS");
        algorithmMenu.getItems().add(dfsItem);
        dfsItem.setOnAction(new DFSHandler());

        FlowPane controls = new FlowPane();
        controls.setAlignment(Pos.CENTER);
        controls.setHgap(5);

        Button addCityButton = new Button("Add City");
        Button findPathButton = new Button("Find Path");
        Button connectCitiesButton = new Button("Connect Cities");
        Button removeCityButton = new Button("Remove City");
        Button loadImageButton = new Button("Load Image");


        findPathButton.setOnAction(new FindPathHandler());
        connectCitiesButton.setOnAction(new ConnectCitiesHandler());
        addCityButton.setOnAction(new AddCityHandler());
        removeCityButton.setOnAction(new RemoveCityHandler());
        loadImageButton.setOnAction(new LoadImageHandler());



        controls.getChildren().addAll(addCityButton, findPathButton, connectCitiesButton, removeCityButton, loadImageButton);

        vbox.getChildren().add(controls);


        display.setWrapText(true);
        display.setEditable(false);

        mapPane = new Pane();
        statusLabel = new Label("Travel Planner");
        mapPane.getChildren().add(backgroundImage);


        setTop(vbox);
        setCenter(mapPane);
        setBottom(display);


        for (City city : model.getCities()) {
            addCityToMap(city);
        }


        stage.setOnCloseRequest(new ExitHandler());
    }


    public void addCityToMap(City city) {
        CityNodeView cityNode = new CityNodeView(city);
        mapPane.getChildren().add(cityNode);
    }

    private CityNodeView getCityNodeView(City city) {
        for (javafx.scene.Node node : mapPane.getChildren()) {
            if (node instanceof CityNodeView) {
                CityNodeView cityNode = (CityNodeView) node;
                if (cityNode.getCity().equals(city)) {
                    return cityNode;
                }
            }
        }
        return null;
    }


    class NewHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            if (changed) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Unsaved changes, continue anyway?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get().equals(ButtonType.OK)) {
                    mapPane.getChildren().clear();
                    changed = false;
                    statusLabel.setText("New map created");
                }
            } else {
                mapPane.getChildren().clear();
                statusLabel.setText("New map created");
            }
        }
    }

    class LoadImageHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            FileChooser imageChooser = new FileChooser();
            imageChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files",
                            "*.png", "*.jpg", "*.jpeg"));
            File file = imageChooser.showOpenDialog(stage);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                mapPane.getChildren().add(0, imageView);
                model.setImagePath(file.getAbsolutePath());
                changed = true;
                statusLabel.setText("Image loaded: " + file.getName());
            }
        }
    }


    class AddCityHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add City");
            dialog.setHeaderText("Enter city name:");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent() && !result.get().isEmpty()) {
                String name = result.get();
                mapPane.setOnMouseClicked(e -> {
                    City city = new City(name, 0, (int) e.getX(), (int) e.getY());
                    model.addCities(city);
                    addCityToMap(city);
                    changed = true;
                    statusLabel.setText("Added: " + name);
                    mapPane.setOnMouseClicked(null);
                });
            }
        }
    }


    class OpenHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                statusLabel.setText("Opened: " + file.getName());
                changed = false;
                try {
                    model.loadGraph(file);
                    System.out.println(model.getCities().toString());
                    for(City city : model.getCities()){
                        System.out.println(city.toString());
                        addCityToMap(city);
                    }
                    if (model.getImagePath() != null) {
                        Image image = new Image(
                                new File(model.getImagePath())
                                        .toURI().toString());
                        ImageView imageView = new ImageView(image);
                        mapPane.getChildren().add(0, imageView);
                    }
                    changed = false;
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }


    class SaveHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                statusLabel.setText("Saved: " + file.getName());
                changed = false;
                model.saveGraph(file);
            }
        }
    }


    class ExitItemHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    class RemoveCityHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            List<CityNodeView> selected = new ArrayList<>();
            for (javafx.scene.Node node : mapPane.getChildren()) {
                if (node instanceof CityNodeView) {
                    CityNodeView cityNode = (CityNodeView) node;
                    if (cityNode.isSelected()) {
                        selected.add(cityNode);
                    }
                }
            }

            if (selected.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Please select a city!");
                alert.showAndWait();
                return;
            }

            for (CityNodeView cityNode : selected) {
                model.removeCity(cityNode.getCity());
                mapPane.getChildren().remove(cityNode);
            }
            changed = true;
            statusLabel.setText("City removed");
        }
    }

    class BFSHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            model.useBFS();
            statusLabel.setText(" BFS ");
        }
    }

    class DFSHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            model.useDFS();
            statusLabel.setText(" DFS ");
        }
    }


    class ExitHandler implements EventHandler<WindowEvent> {
        public void handle(WindowEvent event) {
            if (changed) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Unsaved changes, exit anyway?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() &&
                        res.get().equals(ButtonType.CANCEL)) {
                    event.consume();
                }
            }
        }
    }


    class ConnectCitiesHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            List<CityNodeView> selected = new ArrayList<>();
            for (javafx.scene.Node node : mapPane.getChildren()) {
                if (node instanceof CityNodeView) {
                    CityNodeView cityNode = (CityNodeView) node;
                    if (cityNode.isSelected()) {
                        selected.add(cityNode);
                    }
                }
            }

            if (selected.size() != 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Please select two cities!");
                alert.showAndWait();
                return;
            }

            City from = selected.get(0).getCity();
            City to = selected.get(1).getCity();

            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Connect Cities");
            nameDialog.setHeaderText("Enter connection name:");
            Optional<String> name = nameDialog.showAndWait();

            if (!name.isPresent()) return;
            if (name.get().isEmpty()) return;

            TextInputDialog weightDialog = new TextInputDialog();
            weightDialog.setTitle("Connect Cities");
            weightDialog.setHeaderText("Enter weight:");
            Optional<String> weight = weightDialog.showAndWait();

            if (!weight.isPresent()) return;

            int weightValue = Integer.parseInt(weight.get());

            if (weightValue < 0) {
                new Alert(Alert.AlertType.ERROR, "Weight cannot be negative!").showAndWait();
                return;
            }

            model.connectCities(from, to, weightValue, name.get());
            changed = true;
            statusLabel.setText("Connected: " + from.name() + " - " + to.name());
        }
    }



    class FindPathHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            List<CityNodeView> selected = new ArrayList<>();
            for (javafx.scene.Node node : mapPane.getChildren()) {
                if (node instanceof CityNodeView) {
                    CityNodeView cityNode = (CityNodeView) node;
                    if (cityNode.isSelected()) {
                        selected.add(cityNode);
                    }
                }
            }

            if (selected.size() != 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Select two cities");
                alert.showAndWait();
                return;
            }

            City from = selected.get(0).getCity();
            City to = selected.get(1).getCity();

            Path<City> path = model.findPath(from, to);
            if (path == null) return; {
                display.setText("Algorithm: " + model.getCurrentAlgorithmName() + "\n" + path.toString());


                display.setVisible(true);

                ArrayList<double[]> coordinates = new ArrayList<>();
                List<City> nodes = path.getNodes();
                for (int i = 0; i < nodes.size() - 1; i++) {
                    CityNodeView fromNode = getCityNodeView(nodes.get(i));
                    CityNodeView toNode = getCityNodeView(nodes.get(i + 1));
                    if (fromNode != null && toNode != null) {
                        coordinates.add(new double[]{
                                fromNode.getLayoutX(), fromNode.getLayoutY(),
                                toNode.getLayoutX(), toNode.getLayoutY()
                        });
                    }
                    RouteEdgeView routeEdgeView = new RouteEdgeView(coordinates);
                    mapPane.getChildren().add(routeEdgeView);


                }
            }
        }
    }
}
