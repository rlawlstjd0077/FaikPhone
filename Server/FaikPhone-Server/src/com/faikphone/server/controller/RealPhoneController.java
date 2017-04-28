package com.faikphone.server.controller;

import com.faikphone.server.MessageManager;
import com.faikphone.server.Util;
import com.faikphone.server.dao.ChangeDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dsm_025 on 2017-03-30.
 */
public class RealPhoneController extends HttpServlet{
    private ChangeDAO dao;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("서버 접속");
        dao = ChangeDAO.getInstance();
        PrintWriter writer = response.getWriter();
        String type = request.getParameter("type").toString();
        System.out.println(type);
        if(type != null) {
            switch (type) {
                case "register":
                    writer.write(registerRealPhone(request.getParameter("token")));
                    break;
                case "send_message":
                    writer.write(sendMessage(request.getParameter("token"), request.getParameter("message")));
                    break;
                case "reset_conn":
                    writer.write(resetConnection(request.getParameter("token")));
                    break;
                case "reset_code":
                    writer.write(resetCode(request.getParameter("token")));
                    break;
                case "reset_all":
                    writer.write(resetAll(request.getParameter("token")));
                    break;
                case "test_msg":
                    MessageManager manager = new MessageManager("e68ZgGUqx84:APA91bHSnVWo-_lZJ6vbDnOoXpGbwvwHgG_QYJZ0s5qQyi5rPWyYQHwyCPhJGrrPbvZ687WnPefYLmm_5I4NeGg7iPTa9yikzaLjnFLcbDBWhqoGoR7DCcDH2sg5CVuhuUwBMVSDWPcP");
                    manager.doRequest("Test");
                    break;
                default:
                    System.out.println("Invalid Request");
                    writer.println("Invalid Request");
            }
        }else{
            System.out.println("Invalied Request");
            writer.println("Invalid Request");
        }
        writer.flush();
        writer.close();
    }

    private String registerRealPhone(String token){
        return dao.insertRealPhoneToken(token) == true ?
                Util.makeCodeResponse("register_response", dao.getAuthCode(token)):
                Util.makeErrorResponse("register_response", "Device Already Registered");
    }

    private String sendMessage(String token, String message){
        MessageManager manager = new MessageManager(token);
        boolean response = manager.doRequest(message);
        return response == true ?
                Util.makeSuccessResponse("send_message_response", "Message Request Success") :
                Util.makeErrorResponse("send_message_response", "Message Request Failed");
    }

    private String resetConnection(String token){
        return dao.resetConnection(token, true) == true ?
                Util.makeSuccessResponse("reset_conn_response", "Reset Success") : Util.makeErrorResponse("reset_conn_response", "Reset Failed");
    }

    private String resetCode(String token){
        return dao.resetCode(token) == true ?
                Util.makeCodeResponse("reset_code_response", dao.getAuthCode(token)) :
                Util.makeErrorResponse("reset_code_response", "Reset Failed");
    }

    private String resetAll(String token){
        return dao.resetAll(token, true) == true ?
                Util.makeSuccessResponse("reset_all_response", "Reset Success") :
                Util.makeErrorResponse("reset_all_response", "Reset Failed");
    }
}
