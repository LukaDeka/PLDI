package com.lemms;
import java.io.*;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {


        //Quellcode-File auslesen
        ArrayList<String> lines = new ArrayList<>();
        InputStream is = Main.class.getClassLoader().getResourceAsStream("exampleCode.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {

            String line = null;
            while((line = br.readLine())!=null){
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(lines); //log source

    }
}