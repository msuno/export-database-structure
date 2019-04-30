package com.msw.java;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController implements Initializable{

	@FXML
	private ImageView img;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image image = new Image("/head_2.jpg");
		img.setImage(image);
		
	}
}
