package models;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.*;
public class DialogBox {
	static Stage messageStage = new Stage();
	static Button okButton;
	static Label messageLabel;
	static boolean btnYesClicked;
	public static void information1(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.getDialogPane().getStylesheets().add("/views/Olympic.css");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:resources/images/Ticon1.JPG"));
		
		alert.show();
	}
	public static void information(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.getDialogPane().getStylesheets().add("/views/Olympic.css");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:resources/images/Ticon1.JPG"));
		
		alert.showAndWait();
	}
	public static void error(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.getDialogPane().getStylesheets().add("/views/Olympic.css");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:resources/images/Ticon1.JPG"));
		
		alert.showAndWait();
	}
	public static boolean confirmation(String title, String headerText, String contentText, String yes_Btn, String no_Btn) {
		btnYesClicked = false;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.getDialogPane().getStylesheets().add("/views/Olympic.css");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:resources/images/Ticon1.JPG"));
		
		
		ButtonType yesBtn = new ButtonType(yes_Btn);
		ButtonType noBtn = new ButtonType(no_Btn, ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(yesBtn, noBtn);
		alert.showAndWait().ifPresent(reply -> {
			if(reply == yesBtn) {
				btnYesClicked = true;
			}
		});
		return btnYesClicked;
	}
	
	public static void messageBox(String title, String message) {
		messageStage.setTitle(title);
		messageStage.getIcons().add(new Image("file:resources/images/Ticon1.JPG"));
		messageStage.setMinWidth(600);
		messageStage.setMinHeight(500);
		
		messageLabel = new Label(message);
		okButton = new Button("OK");
		okButton.setOnAction(e -> {messageStage.close(); messageStage.hide();});
		Image keyboard = new Image("file:resources/images/keyb.png");
		ImageView viewer = new ImageView(keyboard);
		
		VBox box = new VBox(20, viewer, messageLabel,okButton);
		box.setAlignment(Pos.CENTER);
		Scene scene = new Scene(box);
		scene.getStylesheets().add("/views/Olympic.css");
		
		messageStage.setScene(scene);
		messageStage.showAndWait();
		
	}
}
