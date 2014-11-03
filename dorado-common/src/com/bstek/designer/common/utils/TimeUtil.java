package com.bstek.designer.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by robin on 14-9-25.
 */
public class TimeUtil
{
    private static final int MILLIS_PER_MINUTE = 60 * 1000;

    public static void main(String[] args){
        long currentTime=System.currentTimeMillis();
        Date date=new Date(currentTime);
        SimpleDateFormat sf=new SimpleDateFormat("YYYY-MM-DD");
        System.out.print(sf.format(date));
    }
}
