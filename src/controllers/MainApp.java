package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.DialogBox;

public class MainApp extends Application {
	Stage stage;
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/appView.fxml"));
			Scene scene = new Scene(root, 900,700);
			scene.getStylesheets().add("/views/Olympic.css");
			
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("file:resources/images/Ticon1.JPG"));
			primaryStage.setMaximized(true);
			primaryStage.setTitle("Touch Typist 1.0");
			primaryStage.show();
			primaryStage.setOnCloseRequest(e -> {e.consume(); close();});
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void close() {
		 DialogBox.information("Exit", null, "Due to some unresolved errors, you can not close the application from this command.\n"
		 		+ "Go to File->Exit, to exit.  ");
					
	}


	public static void main(String[] args) {
		launch(args);
	}
}
