/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package theshedcrud;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/**
 *
 * @author Brett
 */
public class FXMLDocumentController implements Initializable {
    
    
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtBrand;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtDesc;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnExit;
    @FXML
    private TableView<Bikes> tvBikes;
    @FXML
    private TableColumn<Bikes, Integer> colId;
    @FXML
    private TableColumn<Bikes, String> colName;
    @FXML
    private TableColumn<Bikes, String> colBrand;
    @FXML
    private TableColumn<Bikes, String> colType;
    @FXML
    private TableColumn<Bikes, Double> colPrice;
    @FXML
    private TableColumn<Bikes, String> colDesc;
    @FXML
    private Label label;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnImage;
    @FXML
    private TableColumn<?, ?> colImage;
    
    @FXML
    public void handleButtonAction(ActionEvent event) {
    
        if(event.getSource()==btnAdd){
            insertRecord();
            clearTextFields();
        }else if(event.getSource()==btnUpdate){
            updateRecord();
            clearTextFields();
        }else if(event.getSource()==btnDelete){
            deleteRecord();
            clearTextFields();
        }else if (event.getSource() == btnImage) {
            chooseImage();
        }
        else if(event.getSource()==btnClear){
            clearTextFields();
        }
        else if(event.getSource()==btnExit){
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showBikes();
    }    
    
    public Connection getConnection() {
    Connection conn = null;
    try {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/theshed", "root", "");
        System.out.println("Connected to the database");
    } catch (SQLException ex) {
        System.out.println("Error connecting to the database: " + ex.getMessage());
    }
    return conn;
}

    
public ObservableList<Bikes> getBikesList() {
    ObservableList<Bikes> bikeList = FXCollections.observableArrayList();
    Connection conn = getConnection();
    String query = "select * from bikes";
    Statement st;
    ResultSet rs;

    try {
        st = conn.createStatement();
        rs = st.executeQuery(query);
        Bikes bikes;
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String brand = rs.getString("brand");
            String type = rs.getString("type");
            String description = rs.getString("description");
            double price = rs.getDouble("price");

            // Check if the 'image' column is not null before accessing its data
            Blob imageBlob = rs.getBlob("image");
            byte[] imageBytes = (imageBlob != null) ? imageBlob.getBytes(1, (int) imageBlob.length()) : null;

            bikes = new Bikes(id, name, brand, type, description, price, imageBytes);
            bikeList.add(bikes);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return bikeList;
}


    
    public void showBikes(){
        ObservableList<Bikes> list = getBikesList();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        tvBikes.setItems(list);
    }
    
private void insertRecord() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose an Image File");

    // Set the file extension filters if needed
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
    fileChooser.getExtensionFilters().add(extFilter);

    // Show the dialog
    File selectedFile = fileChooser.showOpenDialog(null);

    if (selectedFile != null) {
        try {
            FileInputStream fis = new FileInputStream(selectedFile);

            // Read the image into a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }

            byte[] imageBytes = bos.toByteArray();

            // Prepare the SQL query with image
            String query = "INSERT INTO bikes (name, brand, type, price, description, image) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, txtName.getText());
                preparedStatement.setString(2, txtBrand.getText());
                preparedStatement.setString(3, txtType.getText());
                preparedStatement.setDouble(4, Double.parseDouble(txtPrice.getText()));
                preparedStatement.setString(5, txtDesc.getText());
                preparedStatement.setBytes(6, imageBytes);

                preparedStatement.executeUpdate();
            }

            fis.close();
            showBikes(); // Refresh the table view
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}


private void executeQuery(String query, byte[] imageBytes) {
    try (Connection conn = getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(query)) {

        preparedStatement.setString(1, txtName.getText());
        preparedStatement.setString(2, txtBrand.getText());
        preparedStatement.setString(3, txtType.getText());
        preparedStatement.setDouble(4, Double.parseDouble(txtPrice.getText()));
        preparedStatement.setString(5, txtDesc.getText());
        preparedStatement.setBytes(6, imageBytes);

        preparedStatement.executeUpdate();
        showBikes();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    private void clearTextFields() {
    // Assuming you have references to your text fields
    txtId.clear();
    txtName.clear();
    txtBrand.clear();
    txtType.clear();
    txtPrice.clear();
    txtDesc.clear();
}
private void updateRecord() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose an Image File");

    // Set the file extension filters if needed
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
    fileChooser.getExtensionFilters().add(extFilter);

    // Show the dialog
    File selectedFile = fileChooser.showOpenDialog(null);

    if (selectedFile != null) {
        try {
            FileInputStream fis = new FileInputStream(selectedFile);

            // Read the image into a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }

            byte[] imageBytes = bos.toByteArray();

            // Prepare the SQL query with image for update
            String query = "UPDATE bikes SET name = ?, brand = ?, type = ?, price = ?, description = ?, image = ? WHERE id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, txtName.getText());
                preparedStatement.setString(2, txtBrand.getText());
                preparedStatement.setString(3, txtType.getText());
                preparedStatement.setDouble(4, Double.parseDouble(txtPrice.getText()));
                preparedStatement.setString(5, txtDesc.getText());
                preparedStatement.setBytes(6, imageBytes);
                preparedStatement.setInt(7, Integer.parseInt(txtId.getText()));

                preparedStatement.executeUpdate();
            }

            fis.close();
            clearTextFields();
            showBikes(); // Refresh the table view
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}



public void deleteRecord() {
    try {
        int id = Integer.parseInt(txtId.getText());
        String query = "DELETE FROM bikes WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Record deleted successfully");
                showBikes();  // Refresh the table after deletion
            } else {
                System.out.println("No rows affected. Record may not exist.");
            }
        }
    } catch (NumberFormatException e) {
        System.out.println("Invalid ID. Please enter a numeric ID for deletion.");
    } catch (SQLException e) {
        System.out.println("Error executing delete query: " + e.getMessage());
    }
}


    @FXML
    private void handleMouseAction(MouseEvent event) {
        Bikes bikes = tvBikes.getSelectionModel().getSelectedItem();
        txtId.setText(""+bikes.getId());
        txtName.setText(bikes.getName());
        txtBrand.setText(bikes.getBrand());
        txtType.setText(bikes.getType());
        txtPrice.setText(""+bikes.getPrice());
        txtDesc.setText(bikes.getDesc());
    }
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Display the selected file path or handle the file as needed
            System.out.println("Selected File: " + selectedFile.getAbsolutePath());
            // You can set the file path to a TextField or other GUI element if needed
        }
    }
    
}