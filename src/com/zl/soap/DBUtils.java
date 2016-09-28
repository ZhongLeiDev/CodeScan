package com.zl.soap;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class DBUtils {
	private ArrayList<String> arrayList = new ArrayList<String>();
	private ArrayList<String> brrayList = new ArrayList<String>();
	private ArrayList<String> crrayList = new ArrayList<String>();
	private HttpConnSoap Soap = new HttpConnSoap();
	private Map<String,String> rebuild = new HashMap<String,String>();

	public static Connection getConnection() {
		Connection con = null;
		try {
			//Class.forName("org.gjt.mm.mysql.Driver");
			//con=DriverManager.getConnection("jdbc:mysql://192.168.0.106:3306/test?useUnicode=true&characterEncoding=UTF-8","root","initial");  		    
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return con;
	}
	
	/**
	 * 根据SN码查询微鲸数据
	 * @return SN码对应的一行数据
	 */
	public Map<String,String> getMessageWithSN(String SN){
		arrayList.clear();
		brrayList.clear();
		rebuild.clear();
		
		arrayList.add("SN");
		brrayList.add(SN);
		
		rebuild = Soap.GetWebServre("QureryWhaley", arrayList, brrayList);
		
		return rebuild;
	}
	
	/**
	 * 根据出库状态查询当前批次已扫描的数量
	 * @param state 出库状态
	 * @return 当前批次已扫描的数量
	 */
	public int getSumWithOutbondState(String state){
		int result = -1;
		
		arrayList.clear();
		brrayList.clear();
		rebuild.clear();
		
		arrayList.add("state");
		brrayList.add(state);
		
		rebuild = Soap.GetWebServre("getSumWithOutbondState ", arrayList, brrayList);
		
		if(rebuild.get("KEY")!=null){
			result = Integer.parseInt(rebuild.get("KEY"));
		}
		
		return result;
	}
	
	/**
	 * 根据SN码将数据库中对应记录的Outbound值置为state
	 * @param SN 扫描到的SN码
	 * @param state 需要设置的状态
	 * @return 操作是否成功，成功返回true，失败选择false
	 */
	public boolean updateOutbondState(String SN,String state){
		boolean result = false;
		
		arrayList.clear();
		brrayList.clear();
		rebuild.clear();
		
		arrayList.add("SN");
		arrayList.add("state");
		brrayList.add(SN);
		brrayList.add(state);
		
		rebuild = Soap.GetWebServre("updateOutbondState ", arrayList, brrayList);
		
		if((rebuild.get("KEY")).equals("OK")){
			result = true;
		}else if((rebuild.get("KEY")).equals("NG")){
			Log.i("updateOutbondState", "update failed!");
		}
		
		return result;
	}

	/**
	 * 获取所有货物的信息
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getAllInfo() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		arrayList.clear();
		brrayList.clear();
		crrayList.clear();

//		crrayList = Soap.GetWebServre("selectAllCargoInfor", arrayList, brrayList);

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("Cno", "Cno");
		tempHash.put("Cname", "Cname");
		tempHash.put("Cnum", "Cnum");
		list.add(tempHash);
		
		for (int j = 0; j < crrayList.size(); j += 3) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("Cno", crrayList.get(j));
			hashMap.put("Cname", crrayList.get(j + 1));
			hashMap.put("Cnum", crrayList.get(j + 2));
			list.add(hashMap);
		}

		return list;
	}

	/**
	 * 增加一条货物信息
	 * 
	 * @return
	 */
	public void insertCargoInfo(String Cname, String Cnum) {

		arrayList.clear();
		brrayList.clear();
		
		arrayList.add("Cname");
		arrayList.add("Cnum");
		brrayList.add(Cname);
		brrayList.add(Cnum);
		
		Soap.GetWebServre("insertCargoInfo", arrayList, brrayList);
	}
	
	/**
	 * 删除一条货物信息
	 * 
	 * @return
	 */
	public void deleteCargoInfo(String Cno) {

		arrayList.clear();
		brrayList.clear();
		
		arrayList.add("Cno");
		brrayList.add(Cno);
		
		Soap.GetWebServre("deleteCargoInfo", arrayList, brrayList);
	}
}
