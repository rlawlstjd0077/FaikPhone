package com.faikphone.server.controller;

import com.faikphone.server.MessageManager;
import com.faikphone.server.Util;
import com.faikphone.server.dao.ChangeDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dsm_025 on 2017-03-30.
 */
public class RealPhoneController extends HttpServlet{
    private final String SERVER_KEY = "";
    private final String url = "https://fcm.googleapis.com/fcm/send";
    private ChangeDAO dao;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("서버 접속");
        dao = ChangeDAO.getInstance();
        PrintWriter writer = response.getWriter();
        switch (request.getParameter("type").toString()){
            case "register" :
                writer.println(registerRealPhone(request.getParameter("token")));
                writer.close();
                break;
            case "send_message":
                writer.println(sendMessage(request.getParameter("token"), request.getParameter("message")));
                writer.close();
                break;
            case "reset_conn":
                writer.println(resetConnection(request.getParameter("token")));
                writer.close();
                break;
            case "reset_code":
                writer.println(resetCode(request.getParameter("token")));
                writer.close();
                break;
        }
    }

    private String sendMessage(String token, String message){
        MessageManager manager = new MessageManager(token);
        boolean response = manager.doRequest("5x1", "9:50", message);
        return response == true ?
                Util.makeSuccessResponse("메시지 요청 성공") : Util.makeErrorResponse("메시지 요청 실패");
    }

    private String registerRealPhone(String token){
        dao.insertRealPhoneToken(token);
        return dao.getConnFromRealToken(token).getCode();
    }

    private String resetConnection(String token){
        return dao.resetConnection(token) == true ?
                Util.makeSuccessResponse("리셋 성공") : Util.makeErrorResponse("리셋 실패. 등록 되어있지 않음");
    }

    private String resetCode(String token){
        return dao.resetConnection(token) == true ?
                Util.makeSuccessResponse("리셋 성공") : Util.makeErrorResponse("리셋 실패. 등록 되어있지 않음");
    }
}
