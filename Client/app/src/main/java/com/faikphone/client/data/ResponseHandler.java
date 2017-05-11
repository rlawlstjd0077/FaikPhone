package com.faikphone.client.data;

import android.content.Context;

import com.faikphone.client.AppPreferences;

/**
 * Created by dsm_025 on 2017-04-28.
 */

public class ResponseHandler {
    private Logger logger = Logger.getInstance();
    private AppPreferences preferences;

    public ResponseHandler(Context context){
        preferences = new AppPreferences(context);
    }

    public void handlerResponse(Response response, boolean state){
        if(!state){
            switch (response.getType()){
                case "send_message_response" :
                    if(response.getState()){
                        logger.write("메시지 전송 성공");
                    }else {
                        logger.write("메시지 전송 실패");
                    }
                    break;
                case "register_response":
                    if(response.getState()){
                        logger.write("등록 성공");
                        preferences.setKeyCode(response.getMessage());
                    }else{
                        logger.write("등록 실패");
                    }
                    break;
                case "reset_all_response":
                    if(response.getState()){
                        logger.write("모든 정보 리셋 성공");
                    }else{
                        logger.write("모든 정보 리셋 실패");
                    }
                    break;
                case "reset_code_response":
                    if(response.getState()){
                        logger.write("코드 리셋 성공");
                        preferences.setKeyCode(response.getMessage());
                    }else{
                        logger.write("코드 리셋 실패");
                    }
                    break;
                case "reset_conn_response":
                    if(response.getState()){
                        logger.write("연결 리셋 성공");
                    }else{
                        logger.write("연결 정보 리셋 실패");
                    }
                    break;
            }
        }else{
            //TODO 공기계도 Log를 보여줄 것인지
            switch (response.getType()){
                case "send_message_response" :
                    if(response.getState()){
                        logger.write("메시지 전송 성공");
                    }else{
                        logger.write("메시지 전송 실패");
                    }
                    break;
                case "register_response":
                    if(response.getState()){
                        logger.write("메시지 전송 성공");
                    }else{
                        logger.write("메시지 전송 실패");
                    }
                    break;
                case "reset_all_response":
                    break;
                case "reset_conn_response":
                    break;
            }
        }
    }
}
