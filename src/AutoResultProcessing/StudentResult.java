/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoResultProcessing;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
/**
 *
 * @author PETER
 */
public class StudentResult extends Pane{
    final TableView<SearchStudentsController.Results> table = new TableView<>();
    final ObservableList<SearchStudentsController.Results> data;
    Label level, semester;
    Text gpa, cgpa,classposition;
    StudentResult(ObservableList<SearchStudentsController.Results> data, String level, String semester, String gpa, String cgpa,String classposition){
        this.data = data;
        this.semester = new Label(semester);
        table.placeholderProperty().set(new Label("Examinations Yet To Be Undertaken"));
        
        TableColumn courseCode = new TableColumn("Course Code");
        courseCode.setPrefWidth(95);
        courseCode.setCellValueFactory(new PropertyValueFactory<>("resultCourseCode"));
        TableColumn courseTitle = new TableColumn("Course Title");
        courseTitle.setPrefWidth(275);
        courseTitle.setCellValueFactory(new PropertyValueFactory<>("resultCourseTitle"));
        TableColumn units = new TableColumn("Units");
        units.setPrefWidth(60);
        units.setCellValueFactory(new PropertyValueFactory<>("resultUnits"));
        TableColumn grade = new TableColumn("Grade");
        grade.setPrefWidth(60);
        grade.setCellValueFactory(new PropertyValueFactory<>("resultGrade"));
        table.setItems(data);
        table.getColumns().addAll(courseCode, courseTitle, units, grade);
        
        String ladderColor = "";
        Rectangle screenRect = new Rectangle(411, 245, 5, 5);
        try{
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            ImageIO.write(capture, "png", new File("snapshot.png"));
            WritableImage snapshot = SwingFXUtils.toFXImage(capture, null);
            ladderColor = "#"+snapshot.getPixelReader().getColor(1, 1).toString().substring(2, 8);//color after blend mode applied to background
        }catch(IOException | AWTException ex){}
        
        this.level = new Label(level+": ");
        this.level.setLayoutX(5);
        this.level.setLayoutY(3);
        this.level.setStyle("-fx-font: lighter 36 Signika; -fx-fill: ladder("+ladderColor+", white 49%,rgba(0,0,0,0.8) 50%); -fx-effect: dropshadow( three-pass-box , ladder("+ladderColor+", black 49%,white 50%), 5, 0, 0, 1);");
        this.semester.setLayoutX(75);
        this.semester.setLayoutY(14);
        this.semester.setStyle("-fx-font: 24 Lato; -fx-fill: ladder("+ladderColor+", white 49%,rgba(0,0,0,0.8) 50%); -fx-effect: dropshadow( three-pass-box , ladder("+ladderColor+", black 49%,white 50%), 5, 0, 0, 1);");
        this.gpa = new Text("GPA: "+gpa);
        this.gpa.setLayoutX(28);
        this.gpa.setLayoutY(287);
        this.gpa.setStyle("-fx-font: 18 Signika; -fx-fill: ladder("+ladderColor+", white 49%,rgba(0,0,0,0.8) 50%); -fx-effect: dropshadow( three-pass-box , ladder("+ladderColor+", black 49%,white 50%), 5, 0, 0, 1);");
        
        
        
        this.cgpa = new Text("CGPA: "+cgpa);
        this.cgpa.setLayoutX(416);
        this.cgpa.setLayoutY(287);
        this.cgpa.setStyle("-fx-font: 18 Signika; -fx-fill: ladder("+ladderColor+", white 49%,rgba(0,0,0,0.8) 50%); -fx-effect: dropshadow( three-pass-box , ladder("+ladderColor+", black 49%,white 50%), 5, 0, 0, 1);");
        
        this.classposition = new Text("Position: "+classposition);
        this.classposition.setLayoutX(222);
        this.classposition.setLayoutY(287);
        this.classposition.setStyle("-fx-font: 18 Signika; -fx-fill: ladder("+ladderColor+", white 49%,rgba(0,0,0,0.8) 50%); -fx-effect: dropshadow( three-pass-box , ladder("+ladderColor+", black 49%,white 50%), 5, 0, 0, 1);");
        
        
        table.setLayoutX(25);
        table.setLayoutY(50);
        table.setPrefHeight(200);
        DropShadow dShadow = new DropShadow(8.5, Color.BLACK);
        dShadow.setHeight(18);
        dShadow.setWidth(18);
        table.setEffect(dShadow);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.15)");
        setMinHeight(300);
        
        getChildren().addAll(this.level, this.semester, table, this.gpa, this.cgpa, this.classposition);
    }

}