package server.controller;
import server.Util;
import server.dao.ChangeDAO;
import server.manager.MessageManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by dsm_025 on 2017-03-30.
 */
public class RealPhoneController extends HttpServlet {
    private ChangeDAO dao;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("서버 접속");
        dao = ChangeDAO.getInstance();
        PrintWriter writer = response.getWriter();
        String type = request.getParameter("type").toString();
        System.out.println(type + " request from " + request.getParameter("token") + "to RealPhone Controller");
        if(type != null) {
            switch (type) {
                case "register":
                    writer.write(registerRealPhone(request.getParameter("token")));
                    break;
                case "send_message":
                    if(request.getParameter("event").equals("call")) {
                        writer.write(sendCall(request.getParameter("token"), request.getParameter("name"), request.getParameter("number")));
                    }else if(request.getParameter("event").equals("")){

                    }
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

    private String sendCall(String token, String name, String number){
        MessageManager manager = new MessageManager(dao.getConnFromRealToken(token).getFakeToken());
        boolean response = manager.sendCall(name, number);
        return response == true ?
                Util.makeSuccessResponse("send_call_response", "Message Request Success") :
                Util.makeErrorResponse("send_call_response", "Message Request Failed");
    }

    private String sendMessage(String token, String name, String number, String content){
        MessageManager manager = new MessageManager(dao.getConnFromRealToken(token).getFakeToken());
        boolean response = manager.sendMessage(name, number, content);
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
