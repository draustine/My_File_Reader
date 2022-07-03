package com.draustine.myfilereader;

public class Numbers {
    private static String ordinal = "";
    public static void main (String[] args) {


    }

    public static String getOrdinal(int num){
        String val = "";
        String input = String.valueOf(num);
        String last = input.substring(input.length() - 1);
        int iLast = Integer.parseInt(last);

        if (num == 1 || (num > 20 && iLast == 1)) {
            val = String.valueOf(num) + "st";
        } else if (num == 2 || (num > 20 && iLast == 2)) {
            val = String.valueOf(num) + "nd";
        } else if (num == 3 || (num > 20 && iLast == 3)) {
            val = String.valueOf(num) + "rd";
        } else {
            val = String.valueOf(num) + "th";
        }

        return val;
    }


}
