package com.servlets;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.lang.System.out;

/**
 * Created by nikitos on 06.09.16.
 */
@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        String id = httpSessionEvent.getSession().getId();
        out.println("Session Destroy with id = " + id);
        String cmd = "rm /Users/nikitos/Downloads/" + id + ".stl /Users/nikitos/Downloads/" + id + ".svg";
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(cmd);
            proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream str1 = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(str1);
        BufferedReader br = new BufferedReader(isr);
        String line;


        try {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        proc.destroy();
    }
}
