package com.faikphone.server.controller;

import com.faikphone.server.dao.ChangeDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dsm_025 on 2017-03-30.
 */
public class ChangeController extends HttpServlet{
    private final String SERVER_KEY = "";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("서버 접속");
        ChangeDAO dao = ChangeDAO.getInstance();

        if(request.getParameter("from").equals("real")){
            String resMessage = request.getParameter("purpose").equals("message") ?
                    sendMessage(request.getParameter("token"), request.getParameter("message")) :
                    registerRealPhone(request.getParameter("token"));
        }else{
//            request.getParameter("purpose").equals("fake") ?
        }
    }

    private String sendMessage(String token, String message){
        String response = null;
        return response;
    }

    private String registerRealPhone(String token){
        String response = null;
        return response;
    }

    private void registerFakePhone(String token, String auth){

    }
}
