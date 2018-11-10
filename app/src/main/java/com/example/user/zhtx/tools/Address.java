package com.example.user.zhtx.tools;

/**
 * Created by user on 2018/10/21.
 *
 * 这个类专门存放后台的地址，用okhttp找地址时直接用这里的，改起来方便
 */



public class Address {
    public static  final String address = "http://172.17.144.112:8080";

    /*----------------------登录----------------------------*/
    public static final String longin=address+"/txzh/login";
        //得到用户信息
    public static final String getUser=address+"/txzh/getUser";

    //测试
    public static final String Test="http://172.17.146.163:8080/SpringMvcTest1/getUser";

    /*----------------------注册----------------------------*/
        //检查用户是否已注册
    public static final String CheckPhone=address+"/txzh/checkPhone";
    public static final String Register=address+"/txzh/insert";

    /*----------------------找回密码----------------------------*/
        //重置密码
    public static final String ResetPassword = "";
    public static final String CheckPhone2=address+"/txzh/checkPhone2";
}
