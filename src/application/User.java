package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;

public class User extends Application {

    private TableView<Person> tableView;
    private ObservableList<Person> data;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Address Book");

        TableColumn<Person, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Person, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

        TableColumn<Person, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        TableColumn<Person, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

		TableColumn<Person, Integer> ageColumn = new TableColumn<>("Age");
		ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());

		TableColumn<Person, String> apogeeColumn = new TableColumn<>("Apogee");
		apogeeColumn.setCellValueFactory(cellData -> cellData.getValue().apogeeProperty());

        tableView = new TableView<>();
        tableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, ageColumn, apogeeColumn);

		// applying styles
		tableView.getStyleClass().add("table-view");
		Font.loadFont(getClass().getResourceAsStream("fonts/poppins/Poppins-Regular.ttf"), 14);

		// procedural styling for tableView
		tableView.setStyle("-fx-background-color: #ffffff;");

		idColumn.setStyle("-fx-alignment: CENTER;");
		firstNameColumn.setStyle("-fx-alignment: CENTER;");
		lastNameColumn.setStyle("-fx-alignment: CENTER;");
		emailColumn.setStyle("-fx-alignment: CENTER;");
		ageColumn.setStyle("-fx-alignment: CENTER;");
		apogeeColumn.setStyle("-fx-alignment: CENTER;");
		
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		// startMenu usefull for hiding and showing specified/checked columns
		// tableView.setTableMenuButtonVisible(true);
		tableView.setPrefHeight(350);

        // Create the main layout
        VBox mainLayout = new VBox();
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(10));

        // Create the form layout
        GridPane formLayout = new GridPane();
        formLayout.setVgap(10);
        formLayout.setHgap(10);
		formLayout.setPadding(new Insets(20, 0, 20, 0));
        formLayout.setAlignment(Pos.CENTER);

        // Create form labels
        Label firstNameLabel = new Label("First Name:");
        Label lastNameLabel = new Label("Last Name:");
        Label emailLabel = new Label("Email:");
        Label apogeeLabel = new Label("Apogee:");
        Label ageLabel = new Label("Age:");
		Label message = new Label();
		message.setId("message");

        // Create form fields
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField emailField = new TextField();
        TextField apogeeField = new TextField();
        TextField ageField = new TextField();

        // Create buttons
		Button updateButton = new Button("Modify Coordinates");
        Button registerButton = new Button("Register");
        Button displayButton = new Button("Display All");
        Button deleteButton = new Button("Delete");
		Button viewButton = new Button("View Details");
		updateButton.setId("update-button");

        // Add form components to the grid pane
        formLayout.addRow(0, firstNameLabel, firstNameField, lastNameLabel, lastNameField, emailLabel, emailField);
        formLayout.addRow(1, apogeeLabel, apogeeField, ageLabel, ageField);
		formLayout.add(message, 5, 1);

        // Create a horizontal layout for the buttons
        HBox buttonLayout = new HBox(10);
		buttonLayout.setPadding(new Insets(30, 0, 20, 0));
        buttonLayout.setAlignment(Pos.CENTER);
		buttonLayout.setSpacing(30);
        buttonLayout.getChildren().addAll(updateButton, registerButton, displayButton, deleteButton, viewButton);

        // Add the form layout and button layout to the main layout
        mainLayout.getChildren().addAll(tableView, formLayout, buttonLayout);

        Scene scene = new Scene(mainLayout);

		// linking our stylesheet
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

		// actions
		registerButton.setOnAction(e -> registerUser(firstNameField, lastNameField, emailField, ageField, apogeeField, message));

        displayButton.setOnAction(e -> {
			DataUtils.displayContent(connection, tableView, data);
		});

		deleteButton.setOnAction(e -> deleteObject(message));

		updateButton.setOnAction(e -> navigateUpdate());

		viewButton.setOnAction(e -> navigatePrint());

        // DB connection using JDBC API
        String url = "jdbc:mysql://localhost:3306/adressbook";
        String username = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

	// Styling Classes
    private TableColumn<Person, ?> createColumn(String title, String propertyName) {
        TableColumn<Person, ?> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.getStyleClass().add("table-column");
        column.getStyleClass().add("column-header");
        return column;
    }
	

	// Handlers
	private void registerUser(TextField firstName, TextField lastName, TextField email, TextField age, TextField apogee, Label message){
		
		String firstname = firstName.getText();
		String lastname = lastName.getText();
		String mail = email.getText();
		String apg = apogee.getText();
		int ag = Integer.parseInt(age.getText());

			try {
				// Create a prepared statement with placeholders for the values
				String query = "INSERT INTO adressbook (firstname, lastname, email, age, apogee) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement statement = connection.prepareStatement(query);
		
				// Set the values for the placeholders
				statement.setString(1, firstname);
				statement.setString(2, lastname);
				statement.setString(3, mail);
				statement.setInt(4, ag);
				statement.setString(5, apg);
				
				// Execute the statement to insert the data
				int rowsInserted = statement.executeUpdate();
		
				if (rowsInserted > 0) {
					System.out.println("Data inserted successfully.");
					message.setText("Successfuly Registred!");
					DataUtils.displayContent(connection, tableView, data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		firstName.clear();
		lastName.clear();
		email.clear();
		age.clear();
		apogee.clear();
	}

	private void deleteObject(Label message){
			// to select a row in the UI
			// i store the returned tableView object in a Person instance
			Person selectedPerson = tableView.getSelectionModel().getSelectedItem();
		
			if (selectedPerson != null) {
				try {
					String query = "DELETE FROM adressbook WHERE id = ?";
					PreparedStatement statement = connection.prepareStatement(query);
					// initialize the data list first
					data = FXCollections.observableArrayList();
					// here i used the Person Bean getId()
					// it can easily be selected because of the property interface feature
					statement.setInt(1, selectedPerson.getId());

					// here i remove the selected row from the DB
					int rowsDeleted = statement.executeUpdate();
		
					if (rowsDeleted > 0) {
						System.out.println("Data deleted successfully.");
						// here we remove the selected row from the observableList
						data.remove(selectedPerson);
						// DataUtils dataUtils1 = new DataUtils();
						message.setText("Successfuly Deleted!");
						DataUtils.displayContent(connection, tableView, data);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	// this navigateUpdate function that will link the selected Person Object (row) from the tableView with the Update B
	// and then trigger an openUpdateWindow function for that selected Object
	private void navigateUpdate() {
		Person selectedPerson = tableView.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
            Stage updateStage = new Stage();
			UpdateWindow updateWindow = new UpdateWindow();
			updateWindow.openUpdateWindow(selectedPerson, connection, tableView, data);
            updateWindow.start(updateStage);
		}
	}

	private void navigatePrint(){
		Person selectedPerson = tableView.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			Stage printStage = new Stage();
			PrintWindow printWindow = new PrintWindow();
			printWindow.openPrintWindow(selectedPerson, connection, tableView, data);
			printWindow.start(printStage);
		}
	}
}


