package com.example.xcomputers.testassignment.util;

/**
 * Created by xComputers on 31/03/2017.
 */

public class FileSizeExtractionUtil {
    private static final long KB = 1024;
    private static final long MB = 1048576;
    private static final long GB = 1073741824;
    private static final long TB = 1099511627776L;

    private static final String BYTE = " B";
    private static final String KILOBYTE = " kB";
    private static final String MEGABYTE = " MB";
    private static final String GIGABYTE = " GB";
    private static final String TERABYTE = " TB";

    public static String extract(long size){
        String fileSize = null;
        if(size < KB){
            fileSize = String.valueOf(size).concat(BYTE);
        }else if(size >= KB && size < MB)
            fileSize = String.valueOf(size/KB).concat(KILOBYTE);
        else if(size >= MB && size < GB){
            fileSize = String.valueOf(size/MB).concat(MEGABYTE);
        } else if(size >= GB && size < TB){
            fileSize = String.valueOf(size/GB).concat(GIGABYTE);
        } else if( size >= TB){
            fileSize = String.valueOf(size/TB).concat(TERABYTE);
        }
        return fileSize;
    }
}
