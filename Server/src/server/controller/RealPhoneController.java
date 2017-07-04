package server.controller;

import org.json.JSONObject;
import server.Util;
import server.dao.ChangeDAO;
import server.manager.MessageManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by dsm_025 on 2017-03-30.
 */
public class RealPhoneController extends HttpServlet {
    private ChangeDAO dao;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("서버 접속");
        dao = ChangeDAO.getInstance();
        PrintWriter writer = response.getWriter();
        String type = request.getParameter("type").toString();
        System.out.println(type + " request from " + request.getParameter("token") + "to RealPhone Controller");

        if (type != null) {
            switch (type) {
                case "register":
                    writer.write(registerRealPhone(request.getParameter("token"), request.getParameter("pnum")));
                    break;
                case "send_message":
                    String result = "";
                    String line = null;
                    try {
                        BufferedReader reader = request.getReader();
                        while ((line = reader.readLine()) != null)
                            result += line;
                    } catch (Exception e) { /*report an error*/ }
                    JSONObject object = new JSONObject(result);
                    writer.write(sendMessage(object, request.getParameter("token")));
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
        } else {
            System.out.println("Invalied Request");
            writer.println("Invalid Request");
        }
        writer.flush();
        writer.close();
    }

    private String registerRealPhone(String token, String phoneNum) {
        System.out.println("Dasd");
        return dao.insertRealPhoneToken(token, phoneNum) == true ?
                Util.makeCodeResponse("register_response", dao.getAuthCode(token)) :
                Util.makeErrorResponse("register_response", "Device Already Registered");
    }

    private String sendMessage(JSONObject json, String token) {
        MessageManager manager = new MessageManager(dao.getConnFromRealToken(token).getFakeToken());
        boolean response = manager.sendMessage(json.toString());
        return response == true ?
                Util.makeSuccessResponse("send_call_response", "Message Request Success") :
                Util.makeErrorResponse("send_call_response", "Message Request Failed");
    }

    private String resetConnection(String token) {
        return dao.resetConnection(token, true) == true ?
                Util.makeSuccessResponse("reset_conn_response", "Reset Success") : Util.makeErrorResponse("reset_conn_response", "Reset Failed");
    }

    private String resetCode(String token) {
        return dao.resetCode(token) == true ?
                Util.makeCodeResponse("reset_code_response", dao.getAuthCode(token)) :
                Util.makeErrorResponse("reset_code_response", "Reset Failed");
    }

    private String resetAll(String token) {
        return dao.resetAll(token, true) == true ?
                Util.makeSuccessResponse("reset_all_response", "Reset Success") :
                Util.makeErrorResponse("reset_all_response", "Reset Failed");
    }
}
