package com.msw.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent login = FXMLLoader.load(getClass().getResource("/login.fxml"));
		primaryStage.setTitle("工具类");
		primaryStage.setScene(new Scene(login));
		primaryStage.show();
		try {
			Thread.sleep(3000);
			Platform.runLater(() -> {
				try{
					new Main().start(new Stage());
				}catch (Exception e){
					e.printStackTrace();
				}
			});
			primaryStage.hide();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
