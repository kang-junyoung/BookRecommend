package com.jy.kang;

import java.util.ArrayList;

/**
 * Created by jykang on 15. 3. 4..
 */
public class TestClass {
    public ArrayList<Integer> getResult(){
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i=0; i<10; i++){
            result.add(i+1);
        }

        return result;
    }
}
