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

/**
 * Created by dsm_025 on 2017-03-30.
 */
public class FakePhoneController extends HttpServlet{
    ChangeDAO dao;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dao = ChangeDAO.getInstance();
        PrintWriter writer = response.getWriter();
        switch (request.getParameter("type").toString()) {
            case "register":
                System.out.println("req register");
                writer.print(registerFakePhone(request.getParameter("token"), request.getParameter("code")));
                break;
            case "send_message":
                writer.println(sendMessage(request.getParameter("token"), request.getParameter("message")));
                writer.close();
                break;
            case "reset_all":
                writer.println(resetAll(request.getParameter("token")));
                writer.close();
            case "reset_conn":
                writer.println(resetConnection(request.getParameter("token")));
                writer.close();
        }
    }

    private String sendMessage(String token, String message){
        MessageManager manager = new MessageManager(token);
        boolean response = manager.doRequest(message);
        return response == true ?
                Util.makeSuccessResponse("send_message_response", "Message Request Success") :
                Util.makeErrorResponse("send_message_response", "Message Request Failed");
    }

    private String registerFakePhone(String token, String code){
        return dao.insertFaikPhoneToken(token, code) == true ?
                Util.makeSuccessResponse("register_response", "Certification Success") :
                Util.makeErrorResponse("register_response", "Certification Failed");
    }

    private String resetAll(String token){
        return dao.resetAll(token, false) == true ?
                Util.makeSuccessResponse("reset_all_response", "Reset Success") :
                Util.makeErrorResponse("reset_all_response", "Reset Failed");
    }

    private String resetConnection(String token){
        return dao.resetConnection(token, false) == true ?
                Util.makeSuccessResponse("reset_conn_response", "Reset Success") :
                Util.makeErrorResponse("reset_conn_response", "Reset Failed");
    }
}
