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

    public static String extract(long size) {
        String fileSize = null;
        double formattedSize;
        if (size < KB) {
            fileSize = String.valueOf(size).concat(BYTE);
        } else if (size >= KB && size < MB) {
            formattedSize = size / KB;
            fileSize = String.valueOf(formattedSize).concat(KILOBYTE);
        } else if (size >= MB && size < GB) {
            formattedSize = size / MB;
            fileSize = String.valueOf(formattedSize).concat(MEGABYTE);
        } else if (size >= GB && size < TB) {
            formattedSize = size / GB;
            fileSize = String.valueOf(formattedSize).concat(GIGABYTE);
        } else if (size >= TB) {
            formattedSize = size / TB;
            fileSize = String.valueOf(formattedSize).concat(TERABYTE);
        }
        return fileSize;
    }
}
