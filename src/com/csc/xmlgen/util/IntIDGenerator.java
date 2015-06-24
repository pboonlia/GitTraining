package com.csc.xmlgen.util;

public  class IntIDGenerator {
    private  int sequence = 0;
    public synchronized  int generate(){
        sequence++;
        return sequence;
    }

}
