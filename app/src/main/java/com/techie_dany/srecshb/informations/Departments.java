package com.techie_dany.srecshb.informations;

import java.util.HashMap;
import java.util.Map;

public class Departments {

        HashMap<Integer,String> deptMap = new HashMap<>();
    public Departments(){
        deptMap.put(101,"IT-Seminar Hall");
        deptMap.put(1011,"IT-SEMINAR HALL");

        deptMap.put(102,"CSE-Seminar Hall");
        deptMap.put(1022,"CSE-SEMINAR HALL");

        deptMap.put(103,"ECE-Seminar Hall");
        deptMap.put(1033,"ECE-SEMINAR HALL");

        deptMap.put(104,"Mechanical-Seminar Hall");
        deptMap.put(1044,"MECHANICAL-SEMINAR HALL");

        deptMap.put(105,"Aero-Seminar Hall");
        deptMap.put(1011,"AERO-SEMINAR HALL");

        deptMap.put(100,"LIBRARY-Seminar Hall");
        deptMap.put(1000,"LIBRARY-SEMINAR HALL");
    }


    public String getDept(int hall){
        return deptMap.get(hall);
    }
}
