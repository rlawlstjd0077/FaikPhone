package com.faikphone.client.data;

import android.os.Environment;
import android.os.SystemClock;

import com.faikphone.client.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dsm_025 on 2017-04-27.
 */

public class Logger {
    private static final Logger logger = new Logger();
    private File logFile;

    private Logger(){
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES);
        logFile = new File(path, "/" + "log.txt");
    }

    public static Logger getInstance(){
        return logger;
    }



    public void write(String message){
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        message = dayTime.format(new Date(time)) + " " + message;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(message);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Log File을 비움
     */
    public void clearLog(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(logFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();
    }

    public void read() {
        try {
            BufferedReader read = new BufferedReader(new FileReader(logFile));
            String str;
            while((str = read.readLine()) != null) {
                System.out.println(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
