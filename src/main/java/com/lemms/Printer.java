package com.lemms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Printer {

    public static void print( int line_num, String error_message ) {
        System.out.println("line: " + line_num + " " + error_message);
    }

    public static void printError(
        int line_num, int char_num, String line_str, String error_message
    ) {
        System.err.println("line: " + line_num + " " + error_message);
    }

}
