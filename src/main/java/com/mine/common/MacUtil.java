package com.mine.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




/**
* @description：获取MAC地址
*/
public class MacUtil{
	
	protected static Log logger = LogFactory.getLog(MacUtil.class);
	
	private  static Set<String> MAC_FLAG_STR;
	static{
		MAC_FLAG_STR=new HashSet<String>();
		MAC_FLAG_STR.add("物理地址");
		MAC_FLAG_STR.add("硬件地址");
		MAC_FLAG_STR.add("hwaddr");
	}
	
	/**
	* @description: getMacAddress 
	*/
	public static Set<String> getMacAddress(){
		String os = getOSName();
		String execStr=null;
		if(os.startsWith("windows")){
			execStr = getSystemRoot()+"/system32/ipconfig /all";
		}else if (os.startsWith("linux")) {
			execStr="ifconfig ";
		}else{
			execStr="ifconfig ";
		}
		return exec(execStr);
	}
	
	
	/**
	 * @description : 获取widnowXp网卡的mac地址
	 */
	public static Set<String> exec(String execStr) {
		Set<String> macs=new HashSet<String>();
		
		Process process = null;
		BufferedReader bufferedReader =null;
		try {
			process = Runtime.getRuntime().exec(execStr);
			InputStream is=process.getInputStream();
			
			bufferedReader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			int index=-1;
			while ((line = bufferedReader.readLine()) != null) {
				line=line.toLowerCase();//小写
				for(String macFlag:MAC_FLAG_STR){
					macFlag=macFlag.toLowerCase();
					index = line.indexOf(macFlag);
					if (index != -1) {//含有标识
						if(line.indexOf("-")!=-1){//windows
							macFlag=":";
							index=line.indexOf(":");
						}
						//logger.info("line:"+line);
						String mac = line.substring(index + macFlag.length()).trim();
						//logger.info("sub:"+mac);
						mac=mac.toUpperCase();
						
						//mac=mac.replaceAll("-", "");
						
						//if(!mac.startsWith("00")){
							macs.add(mac);
						//}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null!=process){
				process.destroy();
			}
			
		}
		return macs;
	}
	
	/**
	* @description : 获取当前操作系统名称. return 操作系统名称 例如:windows,Linux,Unix等
	*/
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}
	
	/**
	* @description: getSystemRoot 
	*/
	public static String getSystemRoot(){
		String cmd = null;
		String os = null;
		String result = null;
		String envName = "windir";
		os = System.getProperty("os.name").toLowerCase();
		if(os.startsWith("windows")) {
			cmd = "cmd /c SET";
		}else {
			cmd = "env";
		}
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			InputStreamReader isr = new InputStreamReader(p.getInputStream());
			BufferedReader commandResult = new BufferedReader(isr);
			String line = null;
			while ((line = commandResult.readLine()) != null) {
				line=line.toLowerCase();//重要(同一操作系统不同电脑有些返回的是小写,有些是大写.因此需要统一转换成小写)
				if (line.indexOf(envName) > -1) {
					result =  line.substring(line.indexOf(envName)+ envName.length() + 1);
					return result;
				}
			}
		} catch (Exception e) {
			System.out.println("获取系统命令路径 error: " + cmd + ":" + e);
		}
		return null;
	}
	
	public static  boolean matchLocal(String mac){
		System.out.println(mac);
		mac=mac.toLowerCase();
		mac=mac.replaceAll("-", "");
		Set<String> macs=getMacAddress();
		System.out.println(macs);
		for(String m:macs){
			if(m.length()>12) continue;
			m=m.toLowerCase();
			m=m.replaceAll("-", "");
			if(mac.equals(m))return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}

