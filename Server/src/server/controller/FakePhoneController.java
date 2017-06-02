package server.controller;

import server.manager.MessageManager;
import server.Util;
import server.dao.ChangeDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by dsm_025 on 2017-03-30.
 */
public class FakePhoneController extends HttpServlet {
    ChangeDAO dao;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dao = ChangeDAO.getInstance();
        PrintWriter writer = response.getWriter();
        String type = request.getParameter("type").toString();
        System.out.println(type + " request from " + request.getParameter("token") + " to FakePhone Controller");
        switch (request.getParameter("type").toString()) {
            case "register":
                writer.print(registerFakePhone(request.getParameter("token"), request.getParameter("code")));
                break;
            case "send_message":
                writer.println(sendMessage(request.getParameter("token"), request.getParameter("message")));
                break;
            case "reset_all":
                writer.println(resetAll(request.getParameter("token")));
                break;
            case "reset_conn":
                writer.println(resetConnection(request.getParameter("token")));
                break;
            case "check_conn":
                writer.println(checkConnection(request.getParameter("token")));
                break;
            default:
                System.out.println("Invalid Request");
                writer.println("Invalid Request");
        }
        writer.close();
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
                Util.makeSuccessResponse("register_response", dao.getPhoneNum(token)) :
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

    private String checkConnection(String token){
        String phoneNum;
        return (phoneNum = dao.getPhoneNum(token)) != null ?
                Util.makeCodeResponse("check_connection_response", phoneNum) :
                Util.makeErrorResponse("check_connection_response", "Not Registered");
    }
}
