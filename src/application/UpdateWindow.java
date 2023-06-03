package application;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateWindow extends Application {
    private TableView<Person> tableView;
    private ObservableList<Person> data;
    private Person selectedPerson;
    private Button updateButton;
    private Connection connection;

    // constructor function for an abstract class
    public void openUpdateWindow(Person person, Connection connection, TableView<Person> tableView, ObservableList<Person> data) {
        this.selectedPerson = person;
        this.connection = connection;
        this.tableView = tableView;
        this.data = data;
    }

    @Override
    public void start(Stage primaryStage) {

        Label fnLabel = new Label("First Name ");
        Label lnLabel = new Label("Last Name ");
        Label emLabel = new Label("Email ");
        Label agLabel = new Label("Age ");
        Label apLabel = new Label("Apogee ");

        TextField firstNameField = new TextField(selectedPerson.getFirstName());
        TextField lastNameField = new TextField(selectedPerson.getLastName());
        TextField emailField = new TextField(selectedPerson.getEmail());
        TextField ageField = new TextField(String.valueOf(selectedPerson.getAge()));
        TextField apogeeField = new TextField(selectedPerson.getApogee());

        updateButton = new Button("Update");
        updateButton.setId("update-button");
        // here we already made changes to the fields
        updateButton.setOnAction(e -> updatePersonDetails(firstNameField.getText(), lastNameField.getText(),
        emailField.getText(), Integer.parseInt(ageField.getText()), apogeeField.getText()));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(0,fnLabel, firstNameField);
        gridPane.addRow(1, lnLabel, lastNameField);
        gridPane.addRow(2, emLabel, emailField);
        gridPane.addRow(3, agLabel, ageField);
        gridPane.addRow(4, apLabel, apogeeField);
        gridPane.add(updateButton, 1,5);

        Scene scene = new Scene(gridPane, 500, 600);
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Update Person");
        primaryStage.show();
    }

    private void updatePersonDetails(String firstName, String lastName, String email, int age, String apogee) {
        // Create an updated Person object with the modified values
        Person updatedPerson = new Person(selectedPerson.getId(), firstName, lastName, email, age, apogee);
        // we pass the client side changes to updatePerson to perform them on server side
        updatePerson(updatedPerson);
        // closing the window
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
    }

	private void updatePerson(Person updatedPerson) {
		try {
			String query = "UPDATE adressbook SET firstName = ?, lastName = ?, email = ?, age = ?, apogee = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
	
			statement.setString(1, updatedPerson.getFirstName());
			statement.setString(2, updatedPerson.getLastName());
			statement.setString(3, updatedPerson.getEmail());
			statement.setInt(4, updatedPerson.getAge());
			statement.setString(5, updatedPerson.getApogee());
			statement.setInt(6, updatedPerson.getId());
	
			int rowsUpdated = statement.executeUpdate();
	
			if (rowsUpdated > 0) {
				System.out.println("Data updated successfully.");
				DataUtils.displayContent(connection, tableView, data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
