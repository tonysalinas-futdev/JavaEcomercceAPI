package com.example.Ecomercce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
public class firstTests {

    @Test
    void sumTwoNums(){
    assertEquals(4, 2+2);}

    @Test
    void twoStringsEqual(){
        assertTrue("Hola".contains("a"));
    }


    @ParameterizedTest
    @ValueSource(strings ={"hola","adios",})
    void testStr(String str){
        
        assertTrue(str.contains("a"));

    }
}
