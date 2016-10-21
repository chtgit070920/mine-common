package com.mime.common.rsa;

import com.mine.common.RsaUtil;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;


public class RandomGeneration {

	public RandomGeneration(String privateKeyPath, String publicKeyPath)throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA"); // 创建‘密匙对’生成器
		kpg.initialize(1024); // 指定密匙长度（取值范围：512～2048）
		KeyPair kp = kpg.genKeyPair(); // 生成‘密匙对’，其中包含着一个公匙和一个私匙的信息
		PublicKey public_key = kp.getPublic(); // 获得公匙
		PrivateKey private_key = kp.getPrivate(); // 获得私匙

		RsaUtil.writeToFile(privateKeyPath, private_key.getEncoded());
		RsaUtil.writeToFile(publicKeyPath, public_key.getEncoded());
	}


}
