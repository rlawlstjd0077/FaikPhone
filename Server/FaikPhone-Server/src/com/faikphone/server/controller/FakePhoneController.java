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
        System.out.println("서버 접속");
        dao = ChangeDAO.getInstance();
        PrintWriter writer = response.getWriter();
        switch (request.getParameter("type").toString()) {
            case "register":
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
        boolean response = manager.doRequest("5x1", "9:50", message);
        return response == true ?
                Util.makeSuccessResponse("메시지 요청 성공") : Util.makeErrorResponse("메시지 요청 실패");
    }

    private String registerFakePhone(String token, String code){
        return dao.insertFaikPhoneToken(token, code) == true ?
                Util.makeSuccessResponse("인증 성공") : Util.makeErrorResponse("인증 실패");
    }

    private String resetAll(String token){
        return dao.resetAll(token, false) == true ?
                Util.makeCodeResponse("리셋 성공") : Util.makeErrorResponse("리셋 실패. 등록 되어있지 않음");
    }

    private String resetConnection(String token){
        return dao.resetConnection(token, false) == true ?
                Util.makeSuccessResponse("리셋 성공") : Util.makeErrorResponse("리셋 실패. 등록 되어있지 않음");
    }
}
