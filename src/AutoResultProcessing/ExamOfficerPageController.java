 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoResultProcessing;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author PETER-PC
 */
public class ExamOfficerPageController implements Initializable, ControlledScreen {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeDB();
        try {
            if (level.getItems().isEmpty()) 
                getLevel();
            if (department.getItems().isEmpty()) 
                getDepartment();
            if (institution.getItems().isEmpty()) 
                getInstitution();
            if (session.getItems().isEmpty()) 
                getSession();
            
            getActivityType();
            toDate.setOnAction(e -> {
                    try{getActivityLogs();}
                catch(SQLException ex){}
            });
            fromDate.setOnAction(e -> {
                    try{getActivityLogs();}
                catch(SQLException ex){}
            });
            ActivityTypeCbox.setOnAction(e -> {
                    try{getActivityLogs();}
                catch(SQLException ex){}
            });
        } catch (SQLException ex) {}
        
        Callback<TableColumn, TableCell> cellFactory
                = (TableColumn p) -> new EditingCell();

        CRSemesterComp.setCellFactory(cellFactory);
        CRSemesterElect.setCellFactory(cellFactory);
        CRSessionComp.setCellFactory(cellFactory);
        CRSemesterElect.setCellFactory(cellFactory);
        
        CRSemesterComp.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CourseRequirement, Integer>>() {

                @Override
                public void handle(TableColumn.CellEditEvent<CourseRequirement, Integer> t) {
                    rowIndex = t.getTablePosition().getRow();
                    ((CourseRequirement) t.getTableView()
                    .getItems()
                    .get(rowIndex)).setSemesterComp(t.getNewValue());
                }
            });
        CRSemesterElect.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<CourseRequirement, Integer>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<CourseRequirement, Integer> t) {
                rowIndex = t.getTablePosition().getRow();
                ((CourseRequirement) t.getTableView()
                        .getItems()
                        .get(rowIndex)).setSemesterComp(t.getNewValue());
            }
        });
        CRSessionComp.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<CourseRequirement, Integer>>() {

                @Override
                public void handle(TableColumn.CellEditEvent<CourseRequirement, Integer> t) {
                    rowIndex = t.getTablePosition().getRow();
                    ((CourseRequirement) t.getTableView()
                    .getItems()
                    .get(rowIndex)).setSemesterComp(t.getNewValue());
                }
            });
        CRSessionElect.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<CourseRequirement, Integer>>() {

                @Override
                public void handle(TableColumn.CellEditEvent<CourseRequirement, Integer> t) {
                    rowIndex = t.getTablePosition().getRow();
                    ((CourseRequirement) t.getTableView()
                    .getItems()
                    .get(rowIndex)).setSemesterComp(t.getNewValue());
                }
            });

        Timeline commitFadeInOut = new Timeline(new KeyFrame(Duration.millis(400), new KeyValue(commitImage.opacityProperty(), 1, Interpolator.EASE_OUT)));
        commitFadeInOut.setDelay(Duration.millis(500));
        commitFadeInOut.setOnFinished((ActionEvent t) -> {
            new Timeline(new KeyFrame(Duration.millis(800), new KeyValue(commitImage.opacityProperty(), 0, Interpolator.EASE_IN))).play();
        });

        commitButton.setOnMouseClicked(e -> {
            try {
                connection.commit();
                commitFadeInOut.play();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        table.setRowFactory(new Callback<TableView<CourseRequirement>, TableRow<CourseRequirement>>() {

            @Override
            public TableRow<CourseRequirement> call(TableView<CourseRequirement> tableView) {
                final TableRow<CourseRequirement> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem rmMenuItem = new MenuItem("Delete Course Requirement");
                rmMenuItem.setOnAction(e -> {
                    String queryString = "delete from CourseRequirements where institution = ? and department = ? and level = ? and semester = ?";
                    try {
                        PreparedStatement statement = connection.prepareStatement(queryString);
                        TablePosition pos = table.getSelectionModel().getSelectedCells().get(0);
                        int row1 = pos.getRow();
                        CourseRequirement item = table.getItems().get(row1);
                        statement.setString(1, "");
                        statement.setString(2, "");
                        statement.setString(3, "");
                        statement.setString(4, "");
                        statement.execute();
                    } catch (SQLException ex) {
                    }
                    table.getItems().remove(row.getItem());
                });
                contextMenu.getItems().add(rmMenuItem);

                row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
                return row;
            }
        });
    }

    int rowIndex;
    
    private void getLevel() throws SQLException{
        String queryString = "SELECT DISTINCT StudentLevel FROM Student, Result WHERE ResultMatric = matric";
        statement = connection.prepareStatement(queryString);
        ResultSet rSet = statement.executeQuery();
        while(rSet.next()){
            level.getItems().add(rSet.getString("StudentLevel"));
            level1.getItems().add(rSet.getString("StudentLevel"));
        }
    }
    
    private void getDepartment() throws SQLException{
        String queryString = "SELECT DISTINCT Department FROM Student, Result WHERE ResultMatric = matric";
        statement = connection.prepareStatement(queryString);
        ResultSet rSet = statement.executeQuery();
        while(rSet.next()){
            department.getItems().add(rSet.getString("Department"));
            department1.getItems().add(rSet.getString("Department"));
        }
    }
    
    private void getInstitution() throws SQLException{
        String queryString = "SELECT DISTINCT ShortName FROM Institution, Student, Result "
                + "WHERE ResultMatric = matric AND Student.institution = Institution.institution";
        statement = connection.prepareStatement(queryString);
        ResultSet rSet = statement.executeQuery();
        while(rSet.next()){
            institution.getItems().add(rSet.getString("ShortName"));
            institution1.getItems().add(rSet.getString("ShortName"));
        }
    }
    
    private void getSession() throws SQLException{
        String queryString = "SELECT DISTINCT ResultSession FROM Student, Result WHERE ResultMatric = matric";
        statement = connection.prepareStatement(queryString);
        ResultSet rSet = statement.executeQuery();
        while(rSet.next())
            session.getItems().add(rSet.getString("ResultSession"));
    }
    
    private void getActivityType() throws SQLException{
        String queryString = "SELECT DISTINCT ActivityType FROM ActivityLog";
        statement = connection.prepareStatement(queryString);
        ResultSet rSet = statement.executeQuery();
        if(rSet.next()){
            ActivityTypeCbox.getItems().add("");
            rSet.beforeFirst();
            while(rSet.next())
                ActivityTypeCbox.getItems().add(rSet.getString("ActivityType"));
        }
    }
    
    PreparedStatement statement;
    ScreenController myController;

    @FXML
    private Pane container;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnSearchPortal;
    @FXML
    private ComboBox level;
    @FXML
    private ComboBox department;
    @FXML
    private ComboBox institution;
    @FXML
    private ComboBox session;
    @FXML
    private TableView<Activities> ActivityTable;
    @FXML
    private TableColumn ModifierIdCol;
    @FXML
    private TableColumn ModificationTimeCol;
    @FXML
    private TableColumn ActivityTypeCol;
    @FXML
    private ComboBox<String> ActivityTypeCbox;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private VBox studentResultContent;
    @FXML
    private AnchorPane resultContent;
    
    private void getActivityLogs() throws SQLException{
        ModifierIdCol.setCellValueFactory(new PropertyValueFactory<>("modifierId"));
        ModificationTimeCol.setCellValueFactory(new PropertyValueFactory<>("modificationTime"));
        ActivityTypeCol.setCellValueFactory(new PropertyValueFactory<>("activityType"));

        String modTime;
        if(fromDate.getValue() != null && toDate.getValue() != null)
            modTime = "ModificationTime between ? and ?";
        else if(fromDate.getValue() != null && toDate.getValue() == null)
            modTime = "ModificationTime >= ?";
        else if(fromDate.getValue() == null && toDate.getValue() != null)
            modTime = "ModificationTime =< ?";
        else
            modTime = "ModificationTime LIKE '%'";
        
        String getStudents = "SELECT ModifierId, ModificationTime, ActivityType, ModificationDetails "
                + "FROM ActivityLog WHERE ActivityType LIKE ? AND "+modTime+" ORDER BY ModificationTime ASC";

        statement = connection.prepareStatement(getStudents);
        statement.setString(1, ActivityTypeCbox.getValue()+"%");
        if(fromDate.getValue() != null && toDate.getValue() != null){
            statement.setString(2, fromDate.getValue().toString());
            statement.setString(3, toDate.getValue().toString());
        }
        else if(fromDate.getValue() != null && toDate.getValue() == null)
            statement.setString(2, fromDate.getValue().toString());
        else if(fromDate.getValue() == null && toDate.getValue() != null)
            statement.setString(2, toDate.getValue().toString());  

        ResultSet rSet = statement.executeQuery();
        ObservableList<Activities> data = FXCollections.observableArrayList();
        ArrayList<String> modDetails = new ArrayList<>();
        while (rSet.next()) {
            data.add(new Activities(rSet.getString("ModifierId"),
                    rSet.getString("ModificationTime"),
                    rSet.getString("ActivityType")));
            modDetails.add(rSet.getString("ModificationDetails"));
        }
        ActivityTable.setItems(data);
//        ActivityTypeCol.setCellFactory(new Callback<TableColumn, TableCell>(){
//            @Override
//            public TableCell call(TableColumn param) {
//                return new TableCell<Activities, String>(){
//                    @Override
//                    protected void updateItem(String item, boolean empty){
//                        super.updateItem(item, empty);
//                        if(item.isEmpty() || empty == true){
//                            setText(null);
//                            setGraphic(null);
//                        }else{
//                            setText(item);
//                            setTooltip(new Tooltip(modDetails.get(index)));
//                            this.setCursor(Cursor.HAND);
//                        }
//                            //stackoverflow why does it return negative values
////                        }
//                    }
//                };
//            }
//        });
            
    }
    int index = 0;
    
    public static class Activities {
        private final SimpleStringProperty modifierId;
        private final SimpleStringProperty modificationTime;
        private final SimpleStringProperty activityType;

        private Activities(String modifierId, String modificationTime, String activityType) {
            this.modifierId = new SimpleStringProperty(modifierId);
            this.modificationTime = new SimpleStringProperty(modificationTime);
            this.activityType = new SimpleStringProperty(activityType);
        }

        public String getModifierId() {return modifierId.get();}

        public String getModificationTime() {return modificationTime.get();}

        public String getActivityType() {return activityType.get();}

        public void setModifiedId(String modifierId) {this.modifierId.set(modifierId);}

        public void setModificationTime(String modificationTime) {this.modificationTime.set(modificationTime);}

        public void setActivityType(String activityType) {this.activityType.set(activityType);}
    }
        
    @Override
    public void setScreenParent(ScreenController screenParent) {myController = screenParent;}
    
    @FXML
    private void goToViewStudentByCourse(ActionEvent event) {
        myController.setScreen(AutoResultProcessing.viewStudentByCourse);
//        if (!content.getChildren().isEmpty()) {
//            content.getChildren().clear();
//        }
    }
    
    @FXML 
    private void goToSearchResult(ActionEvent event){
        myController.setScreen(AutoResultProcessing.viewResults);
        ActivityTable.getItems().clear();
        if(!studentResultContent.getChildren().isEmpty())studentResultContent.getChildren().clear();
        if(!resultContent.getChildren().isEmpty())resultContent.getChildren().clear();
    }

    @FXML
    private void goToMainScreen(ActionEvent event) {
        myController.setScreen(AutoResultProcessing.viewResults);
        ActivityTable.getItems().clear();
        if(!studentResultContent.getChildren().isEmpty())studentResultContent.getChildren().clear();
        if(!resultContent.getChildren().isEmpty())resultContent.getChildren().clear();
//        if (!table.getItems().isEmpty()) {
//            table.getItems().clear();
//        }
//        if (!menuSelection.getItems().isEmpty()) {
//            menuSelection.getItems().clear();
//        }
    }
    
    @FXML
    private void logout(ActionEvent event) {
        myController.setScreen(AutoResultProcessing.login);
        Pane loginPageRoot = (Pane) ScreenController.screens.get(AutoResultProcessing.login);
        ((Label) loginPageRoot.lookup("#label2")).setText("");
        ActivityTable.getItems().clear();
        if(!studentResultContent.getChildren().isEmpty())studentResultContent.getChildren().clear();
        if(!resultContent.getChildren().isEmpty())resultContent.getChildren().clear();
    }
    
        @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void hover_in(MouseEvent event) {
        Button actionBtn = ((Button) event.getSource());
        ImageView closeImage = (ImageView) actionBtn.getGraphic();
        actionBtn.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK, 10, 0, 0, 0));
        closeImage.setEffect(new ColorAdjust(0, 0, 0.70, 0));
    }

    @FXML
    private void hover_out(MouseEvent event) {
        Button actionBtn = ((Button) event.getSource());
        ImageView closeImage = (ImageView) actionBtn.getGraphic();
        actionBtn.setEffect(null);
        closeImage.setEffect(new ColorAdjust(0, 0, 0, 0));
    }

    @FXML
    private void requestfocus(MouseEvent event) {
        container.requestFocus();
    }

    double yOffset = 0;
    double xOffset = 0;

    @FXML
    private void determine(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    @FXML
    private void pick(MouseEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        ((Node) event.getSource()).setCursor(Cursor.CLOSED_HAND);
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    private void drop(MouseEvent event) {
        ((Node) event.getSource()).setCursor(Cursor.OPEN_HAND);
    }
    
    Connection connection;

    public void initializeDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");

            connection = DriverManager.getConnection("jdbc:mysql://localhost/studentrecords", "root", "");
            System.out.println("Database Connection");
        } catch (ClassNotFoundException | SQLException ex) {
        }
    }
    
    @FXML
    private TableColumn CRLevel;
    @FXML
    private TableColumn CRSemester;
    @FXML
    private TableColumn CRSemesterComp;
    @FXML
    private TableColumn CRSemesterElect;
    @FXML
    private TableColumn CRSessionComp;
    @FXML
    private TableColumn CRSessionElect;
    @FXML
    private ComboBox<String> department1;
    @FXML
    private ComboBox<String> institution1;
    @FXML
    private ComboBox<String> level1;
    @FXML
    private ComboBox<String> semester1;
    @FXML
    private TextField tfSemesterComp;
    @FXML
    private TextField tfSemesterElect;
    @FXML
    private TextField tfSessionComp;
    @FXML
    private TextField tfSessionElect;
    @FXML
    private TableView<CourseRequirement> table;
    
    private void viewCourseRequirements() throws SQLException {
        CRLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        CRSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        CRSemesterComp.setCellValueFactory(new PropertyValueFactory<>("semesterComp"));
        CRSemesterElect.setCellValueFactory(new PropertyValueFactory<>("semesterElect"));
        CRSessionComp.setCellValueFactory(new PropertyValueFactory<>("sessionComp"));
        CRSessionElect.setCellValueFactory(new PropertyValueFactory<>("sessionElect"));
        //add semester and session parameter
        String queryString = "select Level, Semester, SemesterCompulsoryUnits, SemesterElectiveUnits, "
                + "SessionCompulsoryUnits, SessionElectiveUnits from CourseRequirement "
                + "where institution like ? and department like ? ORDER BY department ASC, level ASC";
        statement = connection.prepareStatement(queryString);
        statement.setString(1, institution1.getValue()+"%");
        statement.setString(2, department1.getValue()+"%");
        ResultSet rSet = statement.executeQuery();
        ObservableList<CourseRequirement> data = FXCollections.observableArrayList();
        while (rSet.next()) 
            data.add(new CourseRequirement(rSet.getString("Level"), rSet.getString("Semester"), 
                    rSet.getInt("SemesterCompulsoryUnits"), rSet.getInt("SemesterElectiveUnits"), 
                    rSet.getInt("SessionCompulsoryUnits"), rSet.getInt("SessionElectiveUnits")));
        
        table.setItems(data);
    }
    
    @FXML
    private ImageView addImage;
    @FXML
    private ImageView commitImage;
    @FXML
    private void addCourseRequirements(ActionEvent event){
        try{
            connection.setAutoCommit(false);
            String queryString = "Insert into CourseRequirements set institution = ?, level = ?, semester = ?, SemesterCompulsoryUnits = ?, "
                    + "SemesterElectiveUnits = ?, SessionCompuloryUnits = ?, SessionElectiveUnits = ?";

            //Course - Level, semester, institution
            statement = connection.prepareStatement(queryString);
            statement.setString(1, institution1.getValue());
            statement.setString(2, level1.getValue());
            statement.setString(3, semester1.getValue());
            statement.setString(4, tfSemesterComp.getText());
            statement.setString(5, tfSemesterElect.getText());
            statement.setString(6, tfSessionComp.getText());
            statement.setString(7, tfSessionElect.getText());
            
            if(warning_addCourseRequirement()){
                statement.executeUpdate();
                
                Timeline addFadeInOut = new Timeline(new KeyFrame(Duration.millis(400), new KeyValue(addImage.opacityProperty(), 1, Interpolator.EASE_OUT)),
                        new KeyFrame(Duration.millis(800), new KeyValue(addImage.opacityProperty(), 0, Interpolator.EASE_IN)));
                addFadeInOut.setDelay(Duration.millis(500));
                addFadeInOut.play();

                viewCourseRequirements();
            }
        }catch(SQLException ex){}
    }
    
    class EditingCell extends TableCell<ViewStudentByCoursesController.Result, Double> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }

            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
//                    try {
//                        updateRecord(getInt());
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                    }
                }
            }
        }

        private void updateRecord(int semesterCompulsoryUnits, int semesterElectiveUnits, int sessionCompulsoryUnits, int sessionElectiveUnits) throws SQLException {
            String queryString = "update CourseRequirements set SemesterCompulsoryUnits = ?, SemesterElectiveUnits = ?, "
                    + "SessionCompulsoryUnits = ?, SessionElectiveUnits = ? where institution = ? and "
                    + "department = ? and level = ?";

            statement = connection.prepareStatement(queryString);

            statement.setInt(1, semesterCompulsoryUnits);
            statement.setInt(2, semesterElectiveUnits);
            statement.setInt(3, sessionCompulsoryUnits);
            statement.setInt(4, sessionElectiveUnits);
            statement.executeUpdate();
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnKeyPressed((KeyEvent t) -> {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(Double.parseDouble(textField.getText()));
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
        
        private int getInt(){
            return getItem() == null ? null : getItem().intValue();
        }
    }
 
    @FXML
    private Button addButton;
    @FXML
    private Button commitButton;
    
    private boolean warning_addCourseRequirement(){
        if(institution1.getValue().equals("") && level1.getValue().equals("") && semester1.getValue().equals("") && 
                tfSemesterComp.getText().equals("") && tfSemesterElect.getText().equals("") && 
                tfSessionComp.getText().equals("") && tfSessionElect.getText().equals(""))
            return true;
        else{
            Scene scene = new Scene(new Pane(new Label("can't insert record. require all data inputted!!!")), 200, 100);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            return false;
        }
    }
    
    public static class CourseRequirement {
        private final SimpleStringProperty level;
        private final SimpleStringProperty semester;
        private final SimpleIntegerProperty semesterComp;
        private final SimpleIntegerProperty semesterElect;
        private final SimpleIntegerProperty sessionComp;
        private final SimpleIntegerProperty sessionElect;

        private CourseRequirement(String level, String semester, int semesterComp, int semesterElect, int sessionComp, int sessionElect) {
            this.level = new SimpleStringProperty(level);
            this.semester = new SimpleStringProperty(semester);
            this.semesterComp = new SimpleIntegerProperty(semesterComp);
            this.semesterElect = new SimpleIntegerProperty(semesterElect);
            this.sessionComp = new SimpleIntegerProperty(sessionComp);
            this.sessionElect = new SimpleIntegerProperty(sessionElect);
        }

        public String getLevel() {   return level.get();    }

        public String getSemester() {    return semester.get();    }

        public int getSemesterComp() {    return semesterComp.get();    }

        public int getSemesterElect() {    return semesterElect.get();     }

        public int getSessionComp() {    return sessionComp.get();     }

        public int getSessionElect() {     return sessionElect.get();    }

        public void setLevel(String level) {    this.level.set(level);     }

        public void setSemester(String semester) {    this.semester.set(semester);     }

        public void setSemesterComp(int semesterComp) {     this.semesterComp.set(semesterComp);     }

        public void setSemesterElect(int semesterElect) {     this.semesterElect.set(semesterElect);     }

        public void setSessionComp(int sessionComp) {     this.sessionComp.set(sessionComp);      }

        public void setSessionElect(int sessionElect) {     this.sessionElect.set(sessionElect);      }
    }
}
