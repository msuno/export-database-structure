package com.msw.java;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;

/**
 * 把数据库中的表结构导出word中
 * @author MOSHUNWEI
 * @version 1.0
 */
public class App 
{
	private static Logger  log = Logger.getLogger(App.class);
    public static void main( String[] args ) throws IOException
    {
    	
    	if(args.length<4){
    		log.info("参数：");
    		log.info("-n=数据库名称");
    		log.info("-u=用户名");
    		log.info("-p=密码");
    		log.info("-d=文件输出路径");
    		System.exit(0);
    	}
    	
    	Map<String, String> map = Check(args);
    	
    	String outFile = map.get("-d")+"/数据库表结构.docx";
    	String sql1 = "SELECT table_name, table_type , ENGINE,table_collation,table_comment, create_options FROM information_schema.TABLES WHERE table_schema='"+map.get("-n")+"'";
    	
    	String sql2 = "SELECT ordinal_position,column_name,column_type, column_key, extra ,is_nullable, column_default, column_comment,data_type,character_maximum_length "
    			+ "FROM information_schema.columns WHERE table_schema='"+map.get("-n")+"' and table_name='";
		
		ResultSet rs = SqlUtils.getResultSet(SqlUtils.getConnnection(map.get("-u"), map.get("-p")),sql1);
		
		log.info("开始生成文件");
		List<Map<String, String>> list = getTableName(rs);
		RowRenderData header = getHeader();
		Map<String,Object> datas = new HashMap<String, Object>();
		datas.put("title", "数据结构表");
		List<Map<String,Object>> tableList = new ArrayList<Map<String,Object>>();
		int i = 0;
		for(Map<String, String> str : list){
			log.info(str);
			i++;
			String sql = sql2+str.get("table_name")+"'";
			ResultSet set = SqlUtils.getResultSet(SqlUtils.getConnnection(map.get("-u"), map.get("-p")),sql);
			List<RowRenderData> rowList = getRowRenderData(set);
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("no", ""+i);
			data.put("table_comment",str.get("table_comment")+"");
			data.put("engine",str.get("engine")+"");
			data.put("table_collation",str.get("table_collation")+"");
			data.put("table_type",str.get("table_type")+"");
			data.put("name", new TextRenderData(str.get("table_name"), POITLStyle.getHeaderStyle()));
			data.put("table", new MiniTableRenderData(header, rowList));
			tableList.add(data);
		}

		datas.put("tablelist", new DocxRenderData(FileUtils.Base64ToFile(outFile), tableList));
		XWPFTemplate template = XWPFTemplate.compile(FileUtils.Base64ToInputStream()).render(datas);
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outFile);
			log.info("生成文件结束");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.info("生成文件失败");
		}finally {
			try {
				template.write(out);
				out.flush();
				out.close();
				template.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
    }
    
    private static Map<String, String> Check(String[] args) {
    	Map<String, String> map = new HashMap<String, String>();
    	for(String str: args){
    		String[] split = str.split("=");
    		map.put(split[0], split[1]);
    	}
    	
    	if(!map.containsKey("-n")){
    		log.info("请输入数据库名称！");
    		System.exit(0);
    	}
    	if(!map.containsKey("-u")){
    		log.info("请输入数据库用户名！");
    		System.exit(0);
    	}
    	if(!map.containsKey("-p")){
    		log.info("请输入数据库密码！");
    		System.exit(0);
    	}
    	if(!map.containsKey("-d")){
    		log.info("请输入保存文件的目录！");
    		System.exit(0);
    	}
    	
    	return map;
	}

	/**
     * table的表头
     * @return RowRenderData
     */
    private static RowRenderData getHeader(){
    	RowRenderData header = RowRenderData.build(
				new TextRenderData("序号", POITLStyle.getHeaderStyle()),
				new TextRenderData("字段名称", POITLStyle.getHeaderStyle()),
				new TextRenderData("字段描述", POITLStyle.getHeaderStyle()),
				new TextRenderData("字段类型", POITLStyle.getHeaderStyle()),
				new TextRenderData("长度", POITLStyle.getHeaderStyle()),
				new TextRenderData("允许空", POITLStyle.getHeaderStyle()),
				new TextRenderData("缺省值", POITLStyle.getHeaderStyle()));
		header.setStyle(POITLStyle.getHeaderTableStyle());
		return header;
    }
    
    /**
     * 获取一张表的结构数据
     * @param set
     * @return List<RowRenderData>
     */
    private static List<RowRenderData> getRowRenderData(ResultSet set) {
    	List<RowRenderData> result = new ArrayList<RowRenderData>();
    	
    	try {
    		int i = 0;
			while(set.next()){
				i++;
				RowRenderData row = RowRenderData.build(
						new TextRenderData(set.getString("ordinal_position")+""),
						new TextRenderData(set.getString("column_name")+""),
						new TextRenderData(set.getString("column_comment")+""),
						new TextRenderData(set.getString("data_type")+""),
						new TextRenderData(set.getString("character_maximum_length")+""),
						new TextRenderData(set.getString("is_nullable")+""),
						new TextRenderData(set.getString("column_default")+"")
						);
				if(i%2==0){
					row.setStyle(POITLStyle.getBodyTableStyle());
					result.add(row);
				}else{
					result.add(row);
				}
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
		return result;
	}

    /**
     * 获取数据库的所有表名
     * @param rs
     * @return list
     */
    private static List<Map<String,String>> getTableName(ResultSet rs){
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    	
    	try {
			while(rs.next()){
				Map<String,String> result = new HashMap<String,String>();
				result.put("table_name", rs.getString("table_name")+"");
				result.put("table_type", rs.getString("table_type")+"");
				result.put("engine", rs.getString("engine")+"");
				result.put("table_collation", rs.getString("table_collation")+"");
				result.put("table_comment", rs.getString("table_comment")+"");
				result.put("create_options", rs.getString("create_options")+"");
				list.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return list;
    }

}
