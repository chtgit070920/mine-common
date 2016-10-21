package com.mime.common.rsa;


/**
 * @author Edward
 * @Description 存储公私钥到文件
 */
public class GenerationTest extends TestCase{
	
	public void test0() throws Exception{
		new JksGeneration(super.priKeyPath,super.pubKeyPath);
		//new RandomGeneration(super.priKeyPath,super.pubKeyPath);
	}
}
