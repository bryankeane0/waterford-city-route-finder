package main;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML private ImageView iv;
    @FXML private Label contextLabel, lengthLabel;
    @FXML private TextField addPointX, addPointY;
    private Image rawImage, blackWhiteImage;
    private ArrayList<Integer> pointCoordinates = new ArrayList<>(4);
    public static ArrayList<Node<?>> agendaList = new ArrayList<>();
    int limit = 10;

    public void initialize() {
        initializeMap();
        try {
            Node<?>[] nodes = Main.loadLandmarks();
            addCulturalNodesOnMap(nodes);
        } catch (Exception e) {
            System.out.println(e);
        }
        rawImage = iv.getImage();
    }

    public void initializeMap() {
        Image startImage=new Image("MapOfWaterford.png", (int)iv.getFitWidth(), (int)iv.getFitHeight(), false, true);
        iv.setImage(startImage);
        blackWhiteImage=new Image("BlackWhiteMap.png", (int)iv.getFitWidth(), (int)iv.getFitHeight(), false, true);
    }

    public void resetMap() {
        pointCoordinates = new ArrayList<>(8);
        iv.setImage(rawImage);
        contextLabel.setText("");
        lengthLabel.setText("");
    }

    public void addCulturalNodesOnMap(Node<?>[] nodes) {
        int buttonSize=25;
        int labelOffsetX=30;
        int labelOffsetY=5;

        Button[] landmarkButtons = new Button[nodes.length];
        Label[] landmarkLabels = new Label[nodes.length];
        for (int i=0; i<nodes.length; i++) {
            landmarkButtons[i] = new Button();
            landmarkButtons[i].getStyleClass().clear();
            landmarkButtons[i].setStyle("-fx-background-radius: 50%; -fx-background-image: url('geopoint.png');");
            landmarkButtons[i].setMinSize(buttonSize, buttonSize);
            landmarkButtons[i].setMaxSize(buttonSize, buttonSize);
            landmarkButtons[i].setPrefSize(buttonSize, buttonSize);
            landmarkButtons[i].setTranslateX(nodes[i].getXCoord());
            landmarkButtons[i].setTranslateY(nodes[i].getYCoord());

            landmarkLabels[i]=new Label(nodes[i].getNodeName());
            landmarkLabels[i].setTranslateX(nodes[i].getXCoord() + labelOffsetX);
            landmarkLabels[i].setTranslateY(nodes[i].getYCoord() + labelOffsetY);

            ((Pane)iv.getParent()).getChildren().add(landmarkButtons[i]);
            ((Pane)iv.getParent()).getChildren().add(landmarkLabels[i]);
        }
    }

    public void addMarkerOnMap(int x, int y) {
        Node<?> temp = new Node<>(limit == 0 ? "Start" : "End", x, y);
        agendaList.add(temp);
        Button markerBtn = new Button();
        markerBtn.getStyleClass().add(limit == 0 ? "mapStartBtn" : "mapEndBtn");
        double radius = 5;
        markerBtn.setShape(new Circle(radius));
        markerBtn.setMinSize(2 * radius, 2 * radius);
        markerBtn.setMaxSize(2 * radius, 2 * radius);
        markerBtn.setTranslateX(x - radius);
        markerBtn.setTranslateY(y - radius);
        ((Pane)iv.getParent()).getChildren().add(markerBtn);
    }

    public void mapClicked(MouseEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        addPointX.setText(String.valueOf((int) e.getX()));
        addPointY.setText(String.valueOf((int) e.getY()));
        if (pointCoordinates.size() / 2 < limit) {
            addMarkerOnMap(x, y);
            pointCoordinates.add(x);
            pointCoordinates.add(y);
        }
    }

}
