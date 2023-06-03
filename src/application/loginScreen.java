package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginScreen extends Application {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/adressbook";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Screen");

        // Create the main layout using BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setId("background");// Apply CSS class to the container
        borderPane.setPadding(new Insets(10));

        // Create the top section for the pictures
        HBox topSection = new HBox();
        topSection.setId("topSection"); // Apply an ID to the top section
        topSection.setAlignment(Pos.TOP_LEFT);
        topSection.setPadding(new Insets(20, 5, 3, 5));
       
        Image image1 = new Image(getClass().getResourceAsStream("/application/utils/logoump.jpg"));

        Image image2 = new Image(getClass().getResourceAsStream("/application/utils/logoensa.png"));

        Image image3 = new Image(getClass().getResourceAsStream("/application/utils/nm.png"));

        // Load the first image
        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitWidth(120);
        imageView1.setPreserveRatio(true);

        // Load the second image
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitWidth(100);
        imageView2.setPreserveRatio(true);

        //load the third image
        ImageView imageView3 = new ImageView(image3);
        imageView3.setFitWidth(280);
        imageView3.setPreserveRatio(true);


        topSection.getChildren().addAll(imageView1,imageView3, imageView2);
        borderPane.setTop(topSection);
        topSection.setMargin(imageView1, new Insets(0, 20, 0, 0));
        topSection.setMargin(imageView3, new Insets(0, 20, 0, 0));

        // Create the center section for login fields
        GridPane centerSection = new GridPane();
        
        centerSection.setAlignment(Pos.CENTER);
        centerSection.setHgap(10);
        centerSection.setVgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setId("loginButton"); // Apply an ID to the login button
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            navigateUser(username, password);
            usernameField.clear();
            passwordField.clear();
        });

        centerSection.add(usernameLabel, 0, 0);
        centerSection.add(usernameField, 1, 0);
        centerSection.add(passwordLabel, 0, 1);
        centerSection.add(passwordField, 1, 1);
        centerSection.add(loginButton, 1, 2);

        borderPane.setCenter(centerSection);

        usernameLabel.setId("username");
        usernameField.setId("userField");
        passwordLabel.setId("password");
        passwordField.setId("userField");


        Scene scene = new Scene(borderPane, 600, 500);
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticateUser(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // User exists if the result set has at least one row
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateUser(String username, String password){
        if(authenticateUser(username, password)){
            Stage userStage = new Stage();
            User userWindow = new User();
            userWindow.start(userStage);
        }
        else {showAlert("Invalid Credentials: ","The Username or Password is Incorrect!");}
	}
    public static void main(String[] args) {
        launch(args);
    }
}
