package com.example.appbanmypham.utils;

import com.example.appbanmypham.model.GioHang;
import com.example.appbanmypham.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "http://192.168.43.128/appbanhang/";
    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();

    public static User user_current = new User();
    public static String ID_RECEIVED;
    //Sử dụng làm khóa trong HashMap để lưu trữ ID của người gửi.
    public static String SENDID = "idsend";
    public static String RECEIVEDID = "idreceived";
    //Sử dụng làm khóa trong HashMap để lưu trữ nội dung tin nhắn.
    public static String MESS = "message";
    public static String DATETIME = "datetime";
    public static String PATH_CHAT = "chat";





    public static String statusOrder(int stutus){
        String result="";
        switch (stutus){
            case 0:
                result = "Đơn hàng đang được xử lí";
                break;
            case 1:
                result = "Đơn hàng đã được xác nhận";
                break;
            case 2:
                result = "Đơn hàng đã được giao cho đơn vị vận chuyển";
                break;
            case 3:
                result = "Giao thành công";
                break;
            case 4:
                result = "Đơn hàng đã bị hủy";
                break;
            default:
                result = ".........";
        }
        return result;
    }

}
