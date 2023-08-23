package com.msw.java;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import com.msw.java.DBTypeEnum;

public class MainController implements Initializable{

	/**
	 * 主机
	 */
	@FXML TextField host;
	/**
	 * 端口
	 */
	@FXML
	private TextField port;
	/**
	 * 类型
	 */
	@FXML
	private ChoiceBox<String> dbType;
	/**
	 * 用户名
	 */
	@FXML
	private TextField username;
	/**
	 * 密码
	 */
	@FXML
	private PasswordField password;
	/**
	 * 数据库 Select
	 */
	@FXML
	private ChoiceBox<String> dbName;
	/**
	 * 数据库Label
	 */
	@FXML
	private Label dbLabel;
	/**
	 * 目录
	 */
	@FXML
	private Label dirPath;


	/**
	 * 测试连接按钮
	 */
	@FXML
	private Button testCon;
	/**
	 * 导出文档按钮
	 */
	@FXML
	private Button exportWord;
	/**
	 * 选择目录按钮
	 */
	@FXML
	private Button choisePath;


	/**
	 * 左侧图片
	 */
	@FXML
	private ImageView img;

	/**
	 * 初始化方法
	 * @param location
	 * The location used to resolve relative paths for the root object, or
	 * <tt>null</tt> if the location is not known.
	 *
	 * @param resources
	 * The resources used to localize the root object, or <tt>null</tt> if
	 * the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbType.setItems(FXCollections.observableArrayList(DBTypeEnum.getNameList()));
		dbType.getSelectionModel().select(0);
		port.setText(DBTypeEnum.getEnumByCode(0).getPort());
		Image image = new Image("/head_2.jpg");
		img.setImage(image);
		// 类型Select OnChange事件
		dbType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// 根据 newValue 取得当前 DBTypeEnum
				DBTypeEnum currentDBType = DBTypeEnum.getEnumByName(newValue);
				dbName.setVisible(currentDBType.getDbNameVisible());
				dbLabel.setVisible(currentDBType.getDbNameVisible());
				port.setText(currentDBType.getPort());
			}
		});
	}
	/**
	 * 数据库 Select OnClick事件
	 * @param event
	 */
	public void dbTouch(MouseEvent event){
		String type = dbType.getValue();
		String user = username.getText();
		String pwd = password.getText();
		String value = dbName.getValue();
		String p = port.getText();
		String h = host.getText();
		if (value != null || "".equals(value)) {
			return ;
		}
		// 只有mysql数据库可以选择库
		if (DBTypeEnum.MYSQL.getName().equals(type)) {
			Connection con = SqlUtils.getConnnection(String.format("jdbc:mysql://%s:%s",h,p),user, pwd);
			if (con==null) {
				Alerts(false,"connecting to database failed");
				return ;
			}
			ResultSet set = SqlUtils.getResultSet(con, "show databases");
			try {
				List<String> list = new ArrayList<String>();
				while (set.next()) {
					list.add(set.getString(1));
				}
				System.out.println(list.toString());
				dbName.setItems(FXCollections.observableArrayList(list));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 选择文件保存路径
	 * @param event
	 */
	public void selectDirPath(ActionEvent event){
		Stage mainStage = null;
		DirectoryChooser directory = new DirectoryChooser();
		directory.setTitle("选择路径");
		File file = directory.showDialog(mainStage);
		if (file != null) {
			String path = file.getPath();
			dirPath.setText(path);			
		}
	}
	
	public void typeTouch(MouseEvent event){
		String name = dbType.getValue();
		System.out.println(name);
		dbName.setVisible(DBTypeEnum.getEnumByName(name).getDbNameVisible());
	}

	/**
	 * 测试数据库连接
	 * @param event
	 */
	public void testCon(ActionEvent event){
		isCon();
	}

	/**
	 * 校验是否数据库连接成功
	 * @return boolean
	 */
	public boolean isCon(){
		String type = dbType.getValue();
		String user = username.getText();
		String pwd = password.getText();
		String p = port.getText();
		String h = host.getText();
		if(DBTypeEnum.MYSQL.getName().equals(type)){
			Connection con = SqlUtils.getConnnection(String.format("jdbc:mysql://%s:%s",h,p),user, pwd);
			if(con!=null){
				Alerts(true,"connected to database success");
				return true;
			}else{
				Alerts(false,"connecting to database failed");
				return false;
			}
		}
		if(DBTypeEnum.ORACLE.getName().equals(type)){
			Connection con = OracleUtils.getConnnection(String.format("jdbc:oracle:thin:@%s:%s:ORCL",h,p),user, pwd);
			if(con!=null){
				Alerts(true,"connected to database success");
				return true;
			}else{
				Alerts(false,"connecting to database failed");
				return false;
			}
		}
		return false;
	}

	/**
	 * 导出文档
	 * @param event
	 */
	public void exportDoc(ActionEvent event){
		String type = dbType.getValue();
		String user = username.getText();
		String pwd = password.getText();
		String dir = dirPath.getText();
		String value = dbName.getValue();
		String p = port.getText();
		String h = host.getText();
		if(dir.equals("未选择")){
			Alerts(false, "请选择文件路径");
			return;
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("-t", type);
		map.put("-u", user);
		map.put("-p", pwd);
		map.put("-d", dir);
		map.put("-n", value);
		map.put("p",p);
		map.put("h",h);
		if(DBTypeEnum.MYSQL.getName().equals(type)){
			// Validation Check
			boolean b = check(map);
			if(!b){
				return ;
			}
			try {
				App.MySQL(map);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(DBTypeEnum.ORACLE.getName().equals(type)){
			try {
				App.Oracle(map);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Validation Check
	 * @param map Map<String,String>
	 * @return boolean
	 */
	private static boolean check(Map<String,String> map){
		if(!map.containsKey("-n")||map.get("-n")==null||map.get("-n").equals("")){
    		Alerts(false,"请输入数据库名称！");
    		return false;
    	}
    	if(!map.containsKey("-u")||map.get("-u")==null||map.get("-u").equals("")){
    		Alerts(false,"请输入数据库用户名！");
    		return false;
    	}
    	if(!map.containsKey("-p")||map.get("-p")==null||map.get("-p").equals("")){
    		Alerts(false,"请输入数据库密码！");
    		return false;
    	}
    	if(!map.containsKey("-d")||map.get("-d")==null||map.get("-d").equals("")){
    		Alerts(false,"请输入保存文件的目录！");
    		return false;
    	}
    	return true;
	}

	/**
	 * 弹窗Alert
	 * @param is boolean
	 * @param content String
	 */
	public static void Alerts(boolean is,String content){
		Alert alert = new Alert(AlertType.INFORMATION);
		if(is){
			alert.setTitle("Dialog");	
			alert.setHeaderText(null);
			alert.setContentText(content);
		}else{
			alert.setTitle("Dialog");	
			alert.setHeaderText(null);
			alert.setContentText(content);
		}
		alert.showAndWait();
	}

}
