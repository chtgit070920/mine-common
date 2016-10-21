package com.mime.common.rsa;

import com.mine.common.RsaUtil;
import com.mine.common.StringUtil;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * 1、准备：请保证keystore 存在（使用jdk 命令keytool 生成 keystore）
 *           keytool -genkey -alias mip -keyalg RSA -keysize 1024 -validity 3650
 *           keytool -exportcert -alias mip -file mip.cer
 * 2、使用本类完成 从keystore中 提公私钥取 并写入文件
 */
public class JksGeneration {

	private String storePath="C:\\Users\\Edward\\.keystore";
	private String storePwd="123456";
	private String alias="mip";
	private String privatePwd="123456";

	public JksGeneration(String privateKeyPath, String publicKeyPath)throws Exception {
		this.assertNoEmpty(storePath, "keystore路径是为空，或使用jdk命令：keytool重新生成.");
		this.assertNoEmpty(storePwd, "keyStoer 密码不能为空.");
		this.assertNoEmpty(alias,"RSA公私钥条目对别名不能为空.");
		this.assertNoEmpty(privatePwd,"RSA 私钥密码不能为空.");
		this.assertNoEmpty(privateKeyPath,"RSA 私钥存储路径不能为空.");
		this.assertNoEmpty(publicKeyPath,"RSA 公钥私钥存储路径不能为空.");

		KeyStore ks=KeyStore.getInstance("JKS");
		InputStream is=JksGeneration.class.getClassLoader().getResourceAsStream(".keystore");
		ks.load(is, storePwd.toCharArray());

		PrivateKey prikey = (PrivateKey) ks.getKey(alias, privatePwd.toCharArray());
		RsaUtil.writeToFile(privateKeyPath, prikey.getEncoded());

		Certificate cert = ks.getCertificate(alias);
		PublicKey pubkey = cert.getPublicKey();
		RsaUtil.writeToFile(publicKeyPath, pubkey.getEncoded());
	}

	private void assertNoEmpty(String obj,String message){
		if(StringUtil.isEmpty(obj)){
			throw new IllegalArgumentException(message);
		}
	}
}
