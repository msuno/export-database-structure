package com.msw.java;

import javafx.application.Application;
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
	}

	public static void main(String[] args) {
		launch(args);
	}
}
