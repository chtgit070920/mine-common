package com.mine.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;


public class FileUtil {

	private static final String FOLDER_SEPARATOR = "/";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	private static final char EXTENSION_SEPARATOR = '.';

	//write string  to file.
	public static void  writStringToFile(String str,String path) throws IOException{
		Assert.notNull(str);
		Assert.isTrue(!StringUtil.isBlank(path));

		File file=new File(path);
		writStringToFile(str,file);
	}

	//write string  to file.
	public static void  writStringToFile(String str,File file) throws IOException{
		Assert.notNull(str);
		Assert.notNull(file);

		File parent=file.getParentFile();
		if(!file.exists()) parent.mkdirs();

		FileOutputStream fos=new FileOutputStream(file);
		StreamUtil.writeStringToOutputStream(str, Charset.forName("UTF-8"),fos);
	}

	//extra string from file
	public static String getStringFromFile(String path) throws IOException{
		Assert.isTrue(!StringUtil.isBlank(path));
		return getStringFromFile(new File(path));
	}
	
	//extra string from file
	public static String getStringFromFile(File file) throws IOException{

		Assert.notNull(file);
		Assert.isFile(file);
		return StreamUtil.getStringFromInputStream(new FileInputStream(file), Charset.forName("UTF-8"));
	}

	//extra bytes from file
	public static byte[] getBytesFromFile(String path) throws IOException{
		Assert.isTrue(!StringUtil.isBlank(path));
		return getBytesFromFile(new File(path));
	}

	//extra bytes from file
	public static byte[] getBytesFromFile(File file) throws IOException{
		Assert.notNull(file);
		Assert.isFile(file);
		return StreamUtil.getBytesFromInputStream(new FileInputStream(file));
	}



	/**
	 * 文件前缀
	 * e.g. "mypath/myfile.txt" -> "mypath/myfile".
	 */
	public static String getFilePrefix(String path) {
		if (path == null) {
			return null;
		}
		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return path;
		}
		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (folderIndex > extIndex) {
			return path;
		}
		return path.substring(0, extIndex);
	}


	/**
	 * 文件扩展名
	 * e.g. "mypath/myfile.txt" -> "txt".
	 */
	public static String getFileSuffix(String path) {
		if (path == null) {
			return null;
		}
		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return null;
		}
		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (folderIndex > extIndex) {
			return null;
		}
		return path.substring(extIndex + 1);
	}


	/**
	 * 文件名简写
	 * e.g. "mypath/myfile.txt" -> "myfile.txt".
	 */
	public static String getSmpleFileName(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}



	/**
	 * 相对地址
	 */
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		}
		else {
			return relativePath;
		}
	}

	/**
	 * 文件路径 清理
	 * Normalize the path by suppressing sequences like "path/.." and inner simple dots.
	 */
	public static String cleanPath(String path) {
		if (path == null) {
			return null;
		}
		String pathToUse =StringUtil.replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			if (prefix.contains("/")) {
				prefix = "";
			}
			else {
				pathToUse = pathToUse.substring(prefixIndex + 1);
			}
		}
		if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
			prefix = prefix + FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = StringUtil.splitStringToArray(pathToUse, FOLDER_SEPARATOR);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
				// Points to current directory - drop it.
			}
			else if (TOP_PATH.equals(element)) {
				// Registering top path found.
				tops++;
			}
			else {
				if (tops > 0) {
					// Merging path element with element corresponding to top path.
					tops--;
				}
				else {
					// Normal path element found.
					pathElements.add(0, element);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + StringUtil.collectionToString(pathElements, FOLDER_SEPARATOR);
	}

	/**
	 * 创建目录：
	 */
	public static void mkDirs(String dir)throws IOException{
		Assert.isTrue(!StringUtil.isBlank(dir));
		File file=new File(dir);
		mkDirs(file);
	}
	public static void mkDirs(File dir)throws IOException{
		Assert.notNull(dir);
		if(!dir.exists()){
			dir.mkdirs();
		}
	}

	/**
	 * 创建文件：
	 */
	public static void ceateFile(String fileName)throws IOException{
		Assert.isTrue(!StringUtil.isBlank(fileName));
		File file=new File(fileName);
		ceateFile(file);
	}
	public static void ceateFile(File file)throws IOException{
		Assert.notNull(file);
		if(!file.exists()){
			file.createNewFile();
		}
	}




	/**
	 * 复制：文件->文件
	 */
	public static void copyFileToFile(String source,String desc)throws IOException{
		Assert.isTrue(!StringUtil.isBlank(source));
		Assert.isTrue(!StringUtil.isBlank(desc));

		File sourceFile = new File(source);
		File descFile = new File(desc);

		copyFileToFile(sourceFile,descFile);
	}
	public static void copyFileToFile(File sourceFile,File descFile)throws IOException{
		Assert.notNull(sourceFile);
		Assert.notNull(descFile);

		Assert.isFile(sourceFile);

		FileInputStream sourceInputStream=new FileInputStream(sourceFile);

		// 创建目录：new File("/tmp/one/two/three").mkdirs();执行后，会建立tmp/one/two/three四级目录
		// 创建目录：new File("/tmp/one/two/three").mkdir();执行后则不会建立任何目录，因为找不到/tmp/one/two目录
		// 创建文件：newFile("/tmp/one/two/three/aa").createNewFile;执行后，若/tmp/one/two/three/目录不存在，则文件会创建失败，不过我们一般不会显式的创建文件。
		// 判定文件或目录是否存在：exists;它可以判定文件，也可以判定目录。
		descFile.getParentFile().mkdirs();
		descFile.createNewFile();

		FileOutputStream descOutputStream=new FileOutputStream(descFile);
		StreamUtil.inToOut(sourceInputStream,descOutputStream);
	}

	/**
	 * 复制：文件->目录
	 */
	public static void copyFileToDir(String source,String desc) throws IOException{
		Assert.isTrue(!StringUtil.isBlank(source));
		Assert.isTrue(!StringUtil.isBlank(desc));
		File sourceFile = new File(source);
		Assert.isFile(sourceFile);

		String simplename = source.substring(source.lastIndexOf(FOLDER_SEPARATOR) + 1);
		File descFile = new File(desc+FOLDER_SEPARATOR+simplename);
		copyFileToDir(source,desc);
	}
	public static void copyFileToDir(File sourceFile,File descFile) throws IOException{
		Assert.notNull(sourceFile);
		Assert.notNull(descFile);

		Assert.isFile(sourceFile);
		Assert.isDir(descFile);

		FileInputStream sourceInputStream=new FileInputStream(sourceFile);

		descFile.getParentFile().mkdirs();
		descFile.createNewFile();

		FileOutputStream descOutputStream=new FileOutputStream(descFile);
		StreamUtil.inToOut(sourceInputStream,descOutputStream);
	}

	/**
	 * 复制：文件或目录->目录
	 */
	public static void copyRecursively(File src, File dest) throws IOException {
		Assert.isTrue(src != null && (src.isDirectory() || src.isFile()), "Source File must denote a directory or file");
		Assert.notNull(dest, "Destination File must not be null");
		Assert.isDir(dest);
		doCopyRecursively(src, dest);
	}

	private static void doCopyRecursively(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			dest.mkdir();
			File[] entries = src.listFiles();
			if (entries == null) {
				throw new IOException("Could not list files in directory: " + src);
			}
			for (File entry : entries) {
				doCopyRecursively(entry, new File(dest, entry.getName()));
			}
		}
		else if (src.isFile()) {
			try {
				dest.createNewFile();
			}
			catch (IOException ex) {
				IOException ioex = new IOException("Failed to create file: " + dest);
				ioex.initCause(ex);
				throw ioex;
			}
			StreamUtil.inToOut(new FileInputStream(src),new FileOutputStream(dest));
		}
		else {
			// Special File handle: neither a file not a directory.
			// Simply skip it when contained in nested directory...
		}
	}
	

	/**
	 * 删除：
	 *
	 * 删除单个文件时，还请注意，在执行file.delete()时,
	 * 如果该文件被其他代码或者其他应用使用，则file.delete()返回false,
	 * 返回false,意味着该文件并没有被删除，所以，你务必注意
	 * 在执行该方法使，要先执行关闭流等。。操作
	 */
	public static boolean deleteFile(String file){

		if(null==file) return false;
		return deleteFile(new File(file));
	}
	public static boolean deleteFiles(String[] files){
		if(null==files) return false;
		
		List l=new ArrayList();
		for(String file:files){
			l.add(new File(file));
		}
		return deleteFiles((File[])l.toArray(new File [l.size()]));
	}
	public static boolean  deleteFiles(File[] files) {
		if(files==null) return false;
		boolean success=false;
		for (int i = 0; i < files.length; i++) {
			success=deleteFile(files[i]);
		}
		return success;
	}
	public static boolean deleteFile(File file){
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				if (children != null) {
					for (File child : children) {
						deleteFile(child);
					}
				}
			}
			return file.delete();
		}
		return false;
	}

	
	/**
	 *  为了解决sun jar包解压缩时的中文乱码问题（这里的中文乱码是指被压缩的文件名出现中文乱码），
	 *  我们使用apache公司的ant.jar包来解压缩！
	 *  
	 *  1. 说明假设a.zip下有两个文件夹：aaa和bbb,而且aaa文件下有
	 * 	   shang.txt和xue.txt,而bbb文件夹下有tang.txt，那么zip条目有
	 *     多少个？
	 *     回答： 5个！文件夹两个，文件三个！
	 *  2. 假设文件夹a下有b.zip,那么b.zip解压后所得文件位置在哪里？在a文件夹下！
	 *     而不是在a文件夹下的b文件下！（这个尤为重要）
	 *     
	 *  3. 解决了中文乱码问题
	 *  
	 *  4. 只能解压zip，而rar  java无法解压！
	 */ 
	//zipToFile
//	public static void  zipToFile(String zipName, String toPath) {
//		try {
//				// 创建zip文件
//				org.apache.tools.zip.ZipFile zf= new org.apache.tools.zip.ZipFile(zipName);
//				// zipEntry指解压后的条目
//				org.apache.tools.zip.ZipEntry ze = null;
//				java.util.Enumeration e = zf.getEntries();
//				while (e.hasMoreElements()) {
//					ze = (org.apache.tools.zip.ZipEntry) e.nextElement();
//					// 条目-->文件
//					File zfile = new File(toPath + File.separator
//							+ ze.getName());
//					//文件是路径文件
//					if (ze.isDirectory()) {// 该条目是否是路径
//						if (!zfile.exists())// 是路径，那么zfile（此时它是路径文件）路径是否已经存在？
//							zfile.mkdirs();// 不存在 -->创建
//					}else{// 否则该条目是非路径文件
//						File fpath = zfile.getParentFile();// 获得解压后该文件的父文件
//						if (!fpath.exists())// 父文件（它一定是路径）路径是否存在
//							fpath.mkdirs();// 不存在，创建
//						//创建输入流
//						InputStream in = zf.getInputStream(ze);
//						// 创建输出流
//						FileOutputStream out = new FileOutputStream(zfile);
//						byte ch[] = new byte[256];
//						int j;
//						while ((j = in.read(ch)) != -1)
//							out.write(ch, 0, j);
//						out.close();
//						in.close();
//						//文件如果还是zip，则解压
//						if (zfile.getAbsolutePath().endsWith(".zip")) {
//							zipToFile( zfile.getAbsolutePath(),
//									zfile.getParent());
//							//这里需要进行删除该子zip文件的操作吗？不需要，务必注意，不懂？
//							//那什么是迭代操作？恩 ，就是这个意思!!!
//							//deleteFile(zfile);//,删除的是子zip文件
//						}
//					}
//				}
//				zf.close();
//				deleteFiles(new File[]{new File(zipName)});//删除传进来的zip文件
//				//deleteFile(new File(zipName));
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("解压失败！");
//		}
//	}
	
	
	
	//本方法是获得src下所有被编译后文件的的路径！即获得src下被编译后的某文件所在路径
	//如果是propertie文件而且和本类在同一包下，你可以直接获得properties的输入流，使用下面的方法
	//InputStream in=FileOperation.class.getResourceAsStream("hotLable.properties");
	
	//另外 必须注意：resouceName的名字也是有要求的：
	//分两种情况:
	//如果该文件在src根下，则resourceName名字可以是“struts.xml”，还可以是“log4j.xml”,还可以是“a.txt”或者“b.java”。
	//如果该文件不在src根下，而是在包下，则resouceName,需是“包名/文件名”
	
	//其实如果是包下的文件，并且同该方法所在的类处于同一包下，则resouceName是可以简写为文件名，
	
	//首先提出一个问题：src下的文件被编译之后位于那里？WebRoot下的文件被编译之后又是位于那里？
	//假定：项目名字是OA,src文件夹被放置在“F:\workspace1\OA”下
	
	//细心的同志们还会发现：
	//类FileOperation.java 路径是“F:\workspace1\OA\src\com\ utils\FileOperation.java”;
	//但它被编译之后的文件FileOperation.class所在路径是
	//"F:\workspace1\OA\WebRoot\WEB-INF\classes\com\ utils\FileOperation.class" 
	//同理位于src下的配置文件，例如xxx.properties或者xxx.hbm.xml或者applicationContext-xxx.xml等
	//被编译之后的路径同上
	
	//而位于F:\workspace1\OA\WebRoot下的js、images、jsp等文件夹以及这些文件夹下的所有子文件夹及文件..
	//的位置与编译之前完全相同。
	
	//此时我们再看一个问题，当执行代码时需要获得某配置文件的路径
	//那么该路径究竟是编译前还是编译后的？
	//回答：编译后的.
	public static String getUrlOfsrcOfCompile(String resourceName) {
		if (null == resourceName || "".equals(resourceName))
			return null;//如果文件名字为""或者为null 则直接返回
		
		Class<?> clazz = FileUtil.class;
		java.net.URL url;
		
		//.class和配置文件的url获取方法不同！
		if("java".equals(getFileSuffix(resourceName))){//如果以.java为后缀!则将其该为.class后缀！
			resourceName=getFilePrefix(resourceName)+".class";
		}
		url = clazz.getResource(resourceName);//从包下获取资源
		if(null==url)//从根下获取资源
			url=clazz.getClassLoader().getResource(resourceName);
		if(null==url){
			System.out.println("文件不存在;\n或者你resourceName不规范:\n" +
					"  如果是src根下的资源请直接给出这个资源的名字，例如struts.xml;\n" +
					"  如果是包下的资源，请给出包名+文件名，例如com/split/hotLable.properties;");
			return null;
		}
		//System.out.println(url);//  file:/F:/workspace1/OA/WebRoot/WEB-INF/classes/com/utils/FileOperation.class
		String absolute = url.getPath();
		//System.out.println(absolute);//  /F:/workspace1/OA/WebRoot/WEB-INF/classes/com/utils/FileOperation.class
		return absolute.substring(1);// 去掉开头的“/”
	}
	
	public static String getAppDir(){
		 String dir=getUrlOfsrcOfCompile(FileUtil.class.getSimpleName()+".java");
		 for(File file=new File(dir);file!=null;file=file.getParentFile()){
			 String fileName=file.getName();
			 if(fileName.equals("bin")){
				 return file.getParentFile().getAbsolutePath();
			 }
			 if(fileName.equals("WebRoot")||fileName.equals("WebContent")){
				 return file.getParentFile().getParentFile().getAbsolutePath();
			 }
		 }
		return null;
	}
	
	//获得webroot下文件及文件夹的url
	//resourceName是相对于WebRoot的路径，例如： 
	//WebRoot下的photo文件夹下的newsPhoto文件夹：“/photo/newsPhoto”
	
	//但是如果你的程序是跑在服务器中的则有更简单的方法供你选择
	//ServletActionContext.getServletContext().getRealPath("/photo/newsPhoto");
	//上面的方法获得的是WebRoot下的文件夹photo的文件下的newPhoto文件夹路径！
	public static String getWebRootUrl(String resourceName){
		String path=getUrlOfsrcOfCompile("FileUtils.class");
		//System.out.println(path);
		if('/'!=resourceName.charAt(0))
			resourceName="/"+resourceName;
		int index=path.indexOf("/WEB-INF/classes/");
		//System.out.println(index);
		return path.substring(0,index)+resourceName;
	}
	
	//读取properties文件，并将其key-value放置于map中！
	//注意propertiesName的名字问题：
	
	//分两种情况:
	//如果该文件在src根下，则resourceName名字可以是“struts.xml”，还可以是“log4j.xml”,还可以是“a.txt”或者“b.java”。
	//如果该文件不在src根下，而是在包下，则resouceName,需是“包名/文件名”或者“/包名/文件名”
	
	//其实如果是包下的文件，并且同该方法所在的类处于同一包下，则resouceName是可以简写为文件名，
	public static Map readProperties(String propertiesName){
		Map map=new HashMap();
		Properties properties =new Properties();
		String path=getUrlOfsrcOfCompile(propertiesName);
		System.out.println(path);
		try {
			//读取.properties文件
			InputStream in=new FileInputStream(path);
			//从流中读取键值对，放到properties中！
			properties.load(in);
		} catch (IOException e) {
			System.out.println("加载properties文件失败");
			e.printStackTrace();
		}
		//获得properties中所有的键，枚举类型
		Enumeration enu=properties.keys();
		while(enu.hasMoreElements()){
			//获得一个key
			String key=enu.nextElement().toString();
			//获得 该key 对应的  value
			String value=properties.getProperty(key);
			//将 key 和 value 以utf-8的方式进行编码转换
			try {
				key=new String(key.getBytes("ISO8859-1"),"UTF-8");
				value=new String(value.getBytes("ISO8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("编码失败");
				e.printStackTrace();
			}
			//放入map
			map.put(key,value);
		}
		return map;
	}
	
	// 获得文件夹下所有符合某要求的所有文件,例如，文件的名字后缀是“.avi”等
	public static File[] getAnyFile(String dir, String req) {
		// dir = "F:/workspace1/dom4j/WebRoot/WEB-INF/classes";
		// req = "com";
		File file = new File(dir);// 文件夹对象，从该文件夹下找出符合要求的文件。
		File array[];// 用于 存放符合要求的文件.
		File temp[] = file.listFiles();// 获取dir下的所有文件（路径文件和非路径文件）
		File dic[] =null;// 用于存放 dir下符合要求的路径文件下的非路径文件！
		File nonDic[];// 用于存放 dir下符合要求的非路径文件！
		
		ArrayList list=new ArrayList();
		for (int i = 0; i < temp.length; i++) {// 如果是目录则。。
			File d[]=null;
			if (temp[i].isDirectory()) {// 如果是目录则。。
				d=getAnyFile(temp[i].getAbsolutePath(), req);
				File f=null;
				for(int j=0;j<d.length;j++){
					f=d[j];
					list.add(f);
					
				}
			}
		}
		//System.out.println(list);
		dic=new File[list.size()];
		for(int i=0;i<list.size();i++){
			dic[i]=(File)list.get(i);
		}
		
		// 不是目录，那么就是有后缀的文件，那么过滤这些文件
		nonDic = file.listFiles(new DirFilter(req));
		array = new File[dic.length + nonDic.length];
		System.arraycopy(dic, 0, array, 0, dic.length);
		System.arraycopy(nonDic, 0, array, dic.length, nonDic.length);
		return array;
	}
	// 文件过滤类
	static class DirFilter implements FilenameFilter {
		String str;
		DirFilter(String str) {
			this.str = str;
		}
		public boolean accept(File dir, String name) {
			if (null == str || "".equals(str))// 如果没有任何规格要求那么返回true
				return true;
			String f = new File(name).getName();// 否则...
			return f.indexOf(str) != -1;
		}
	}
	
	public static String toLeftSlash(String path){
		if(null==path) return path;
		return path.replaceAll("\\\\", "/");
	}
	
	
	public static void main(String args[]) throws IOException{
		//
		//F:\\svnSpace\\lyl\\doc\\99_temp\\accessory\\5_105.html
		//System.out.println(getFileContent(new File("F:\\svnSpace\\lyl\\doc\\99_temp\\accessory\\5_105.html")));
		
		//System.out.println(createHtmlFile(null,null));
		
		//Collection<MimeType> mimeTypes = MimeUtil.getMimeTypes(new File("C:/Users/Edward\\Desktop\\01_spring3.0-bean注解.htm"));  
		//System.out.println(mimeTypes);
		
		//System.out.println(createUploadFileName("aaa.html",""));
		
		//File file=new File("C:/Users/Edward\\Desktop\\a\\");
		
		//File parent=file.getParentFile();
		//parent.mkdirs();
		//file.createNewFile();
		
		//System.out.println(file.getParentFile());
		
		
		//System.out.println(file.getName());
		//System.out.println(getSmpleFileName("C:/Users/Edward\\Desktop\\a\\a"));
		
		//if(!file.exists()) file.createNewFile();;
		//FileUtil.deleteFile("C:\\Users\\Edward\\Desktop\\新建文件夹\\新建文件夹 (2)");
		
		System.out.println(getAppDir());
	}
}
