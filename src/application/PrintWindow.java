package application;
import application.utils.*;

import java.sql.Connection;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class PrintWindow extends Application {

    private TableView<Person> tableView;
    private ObservableList<Person> data;
    private Person selectedPerson;
    private Button updateButton;
    private Connection connection;

    // constructor function for an abstract class
    public void openPrintWindow(Person person, Connection connection, TableView<Person> tableView, ObservableList<Person> data) {
        this.selectedPerson = person;
        this.connection = connection;
        this.tableView = tableView;
        this.data = data;
    }

    @Override
    public void start(Stage stage){

        BorderPane root = new BorderPane();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        // header
        // Create the top section for the pictures
        HBox topSection = new HBox();
        topSection.setId("topSection"); // Apply an ID to the top section
        topSection.setAlignment(Pos.CENTER);
       
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
        HBox.setMargin(imageView1, new Insets(0, 20, 0, 0));
        HBox.setMargin(imageView3, new Insets(0, 20, 0, 0));

        grid.addRow(0, topSection);

        Text headTitle = new Text("About");
        grid.setHalignment(headTitle, HPos.CENTER);
        grid.setValignment(headTitle, VPos.CENTER);
        grid.addRow(1, headTitle);

        Label fnLabel = new Label("First Name: ");
        Text firstname = new Text();
        firstname.setText(selectedPerson.getFirstName());
        grid.addRow(2, fnLabel, firstname);

        Label lnLabel = new Label("Last Name: ");
        Text lastname = new Text();
        lastname.setText(selectedPerson.getLastName());
        grid.addRow(3, lnLabel, lastname);

        Label emLabel = new Label("Email: ");
        Text email = new Text();
        email.setText(selectedPerson.getEmail());
        grid.addRow(4, emLabel, email);

        Label agLabel = new Label("Age: ");
        Text age = new Text();
        age.setText(String.valueOf(selectedPerson.getAge()));
        grid.addRow(5, agLabel, age);

        Label apLabel = new Label("Apogee: ");
        Text apogee = new Text();
        apogee.setText(selectedPerson.getApogee());
        grid.addRow(6, apLabel, apogee);

        Label schoolLabel = new Label("ENSA: ");
        Text school = new Text("d'Oujda");
        grid.addRow(7, schoolLabel, school);

        Label collegeLabel = new Label("University: ");
        Text college = new Text("Mohammed 1st");
        grid.addRow(8, collegeLabel, college);

        Label seasonLabel = new Label("Season: ");
        Text season = new Text("2022/2023");
        grid.addRow(9, seasonLabel, season);

        Label industryLabel = new Label("Industry: ");
        Text industry = new Text("Engineering");
        grid.addRow(10, industryLabel, industry);

        Label signatureLabel = new Label("Signature Here: ");
        grid.setConstraints(signatureLabel, 5, 12);
        grid.getChildren().add(signatureLabel);

        Label boxLabel = new Label("");
        grid.setConstraints(boxLabel, 6, 12);
        grid.getChildren().add(boxLabel);

        Button print = new Button("Print");
        grid.setValignment(print, VPos.CENTER);
        grid.setConstraints(print, 0, 12);
        grid.getChildren().add(print);

        root.setCenter(grid);
        root.setTop(topSection);

        firstname.setId("fn");
        lastname.setId("ln");
        email.setId("em");
        age.setId("ag");
        apogee.setId("apg");
        root.setId("root");

        school.setId("schl");
        college.setId("clg");
        season.setId("ssn");
        industry.setId("ind");
        signatureLabel.setId("sig");
        boxLabel.setId("box");
        headTitle.setId("headTitle");

        //print action
        print.setOnAction(event -> {
            // Get the printer job
            PrinterJob printerJob = PrinterJob.createPrinterJob();
           
            if (printerJob != null) {
                // Show the print dialog
                boolean showDialog = printerJob.showPrintDialog(stage);
               
                if (showDialog) {
                    // Exclude the button from printing
                    print.setVisible(false);
                   
                    // Get the root node of the scene
                    Node node = stage.getScene().getRoot();
                    
                    // Set print page layout matching scene dimensions
                    PageLayout pageLayout = printerJob.getPrinter().getDefaultPageLayout();
                    double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
                    double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
                    Scale scale = new Scale(scaleX, scaleY);
                    node.getTransforms().add(scale);
                    
                    // Print to PDF
                    printerJob.printPage(node);
                    printerJob.endJob();
                   
                    // Remove the scaling transformation
                    node.getTransforms().remove(scale);
                   
                    // Restore the visibility of the button
                    print.setVisible(true);
                }
            }
        });
    
    
        Scene scene = new Scene(root, 600, 700);
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Details");
        stage.show();
        
    }
}
