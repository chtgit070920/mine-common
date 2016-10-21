package com.mime.common.rsa;

import com.mine.common.RsaUtil;

import java.io.File;
import java.io.FileInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


/**
 * @author Edward
 * @Description 加密/解密  ren 命令
 */
public class CryptTest extends TestCase {

	private String plainText;//待加密的文本

	private String encryptDir="d:\\_mip\\license";//密码存储目录
	private final String encryptFileName=".license";//密文文件名

	protected void setUp() throws Exception {
		super.setUp();
		//服务器：
		//崔红涛：50-7B-9D-29-A3-2E
		//卢泓阳：48-E2-44-7A-DF-85
		//网尚玉：9C-D2-1E-B3-67-0D
		//周杰：C8-1F-66-32-77-EE
		//司昊天：18-3D-A2-D9-96-CB
		//李广彪：78-0C-B8-E2-66-34
		//小鹏：5C-26-0A-77-FA-B8
		this.plainText="5C-26-0A-77-FA-B8".replaceAll("-", "").replaceAll(":", "");
	}

	public void test0()throws Exception{
		System.out.println("明文："+plainText);
		byte[] bytes_1 = plainText.getBytes("UTF-8");

		//私钥加密
		RSAPrivateKey privateKey= RsaUtil.loadPrivateKey(new FileInputStream(priKeyPath));
		byte[] bytes_2 =RsaUtil.encrypt(privateKey, bytes_1);
		System.out.println("密文："+new String(bytes_2));

		String encryptPath=encryptDir+File.separator+plainText+File.separator+encryptFileName;

		//密文64编码并写入文件
		RsaUtil.writeToFile(encryptPath, bytes_2);

		//密文从文件中提取并64位解码
		byte[] bytes_3=RsaUtil.getFromFile(encryptPath);

		//公钥解密
		RSAPublicKey publicKey=RsaUtil.loadPublicKey(new FileInputStream(pubKeyPath));
		byte[] bytes_4=RsaUtil.decrypt(publicKey, bytes_3);
		String originText=new String(bytes_4, "UTF-8");
		System.out.println("原文："+originText+"（原文"+(plainText.equals(originText)?"==":"!=")+"明文）。");

		System.out.println();
		System.out.println("友情提示，密文（经64位转码）存储路径："+encryptPath);
	}

}