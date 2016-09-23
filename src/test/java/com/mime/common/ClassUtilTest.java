package com.mime.common;

import org.junit.Test;

/**
 * Created by Edward on 2016/9/23.
 */
public class ClassUtilTest {

    @Test
    public void test(){

        System.out.println(int[].class.getName());//[I
        System.out.println(byte[].class.getName());//[B
        System.out.println(boolean[].class.getName());//[Z
        System.out.println(char[].class.getName());//[C
        System.out.println(double[].class.getName());//[D
        System.out.println(float[].class.getName());//[F
        System.out.println(long[].class.getName());//[J
        System.out.println(short[].class.getName());//[S

        System.out.println(Integer[].class.getName());//[Ljava.lang.Integer;
        System.out.println(Byte[].class.getName());//[Ljava.lang.Byte;
        System.out.println(Boolean[].class.getName());//[Ljava.lang.Boolean;
        System.out.println(Character[].class.getName());//[Ljava.lang.Character;
        System.out.println(Double[].class.getName());//[Ljava.lang.Double;
        System.out.println(Float[].class.getName());//[Ljava.lang.Float;
        System.out.println(Long[].class.getName());//[Ljava.lang.Long;
        System.out.println(Short[].class.getName());//[Ljava.lang.Short;

        System.out.println(String.class.getName());//java.lang.String
        System.out.println(String [].class.getName());//[Ljava.lang.String;
    }
}
