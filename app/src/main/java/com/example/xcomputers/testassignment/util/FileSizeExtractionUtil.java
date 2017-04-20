package com.example.xcomputers.testassignment.util;

/**
 * Created by xComputers on 31/03/2017.
 */

import java.util.Locale;

/**
 * Utility class for calculating the size of a given file
 * in the proper units
 */
public class FileSizeExtractionUtil {

    private static final double KB = 1024d;
    private static final double MB = KB * 1024d;
    private static final double GB = MB * 1024d;
    private static final double TB = GB * 1024d;

    private static final String BYTE = "B";
    private static final String KILOBYTE = "kB";
    private static final String MEGABYTE = "MB";
    private static final String GIGABYTE = "GB";
    private static final String TERABYTE = "TB";

    /**
     * Utility method that calculates the file size
     *
     * @param size the size of the file in bytes
     * @return User friendly string representation of the size along with the proper units
     */
    public static String extract(long size) {

        String fileSize = null;
        double formattedSize = 0;

        if (size < KB) {
            formattedSize = size;
            fileSize = BYTE;
        } else if (size >= KB && size < MB) {
            formattedSize = size / KB;
            fileSize = KILOBYTE;
        } else if (size >= MB && size < GB) {
            formattedSize = size / MB;
            fileSize = MEGABYTE;
        } else if (size >= GB && size < TB) {
            formattedSize = size / GB;
            fileSize = GIGABYTE;
        } else if (size >= TB) {
            formattedSize = size / TB;
            fileSize = TERABYTE;
        }
        return String.format(Locale.getDefault(), "%.1f %s", formattedSize, fileSize);
    }
}
