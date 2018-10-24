package com.msw.java;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController implements Initializable{

	@FXML
	private ImageView img;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Button reset;
	
	@FXML
	private Button login;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image image = new Image("/head_2.jpg");
		img.setImage(image);
		
	}
	
	public void reset(ActionEvent event){
		username.setText("");
		password.setText("");
	}
	
	public void login(ActionEvent event){
		if(!username.getText().toString().equals("root")
				||!password.getText().toString().equals("123456")){
			MainController.Alerts(false, "用户名或密码错误！");	
			return ;
		}
		MainController.Alerts(true, "登录成功！");	
		try {
			Main main = new Main();
			main.start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Stage stage = (Stage) login.getScene().getWindow();
		stage.close();
	}

}
