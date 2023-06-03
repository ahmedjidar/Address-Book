package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.sql.*;

// this packs all accessed methods within multiple classes at a time
public class DataUtils {
    public static void displayContent(Connection connection, TableView<Person> tableView, ObservableList<Person> data) {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM adressbook");
    
                // this is the observable list that will hold the objects of Person Data
                data = FXCollections.observableArrayList();
    
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String email = resultSet.getString("email");
                    int age = resultSet.getInt("age");
                    String apogee = resultSet.getString("apogee");
                    // add the current fetched row to Data List to make an underlying source for the tableView UI
                    data.add(new Person(id, firstName, lastName, email, age, apogee));
                }
                tableView.setItems(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
