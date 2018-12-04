package com.example.user.zhtx.tools;

/**
 * Created by user on 2018/10/21.
 *
 * 这个类专门存放后台的地址，用okhttp找地址时直接用这里的，改起来方便
 */



public class Address {
    public static  final String address = "http://172.17.146.102:8080";
    public static  final String title="http://172.17.146.102:8080/txzh/pic/";

    /*----------------------登录----------------------------*/
    public static final String longin=address+"/txzh/login";
        //得到用户信息
    public static final String getUser=address+"/txzh/getUser";



    /*----------------------注册----------------------------*/
        //检查用户是否已注册
    public static final String CheckPhone=address+"/txzh/checkPhone";
    public static final String Register=address+"/txzh/insert";

    /*----------------------找回密码----------------------------*/
        //重置密码
    public static final String ResetPassword = address+"/txzh/findpassword";
    public static final String CheckPhone2=address+"/txzh/checkPhone2";

    /*----------------------修改密码----------------------------*/
        //使用旧密码修改  使用验证码更改跟找回密码一致
    public static final String ChangePasswordByPwd = address+"/txzh/changepassword";

    /*----------------------修改个人信息----------------------------*/
        //普通信息
    public static final String ChangeMessage=address+"/txzh/ChangeMessage";
    //个人头像
    public static final String ChangeHead=address+"/txzh/ChangePicture";

     /*----------------------GPS----------------------------*/
        //上传个人gps位置
    public static final String SendSelfGPS = address+"/txzh/putGps";
        //得到好友GPS位置
    public static final String GetFirendsGPS = address+"/txzh/getFriendsGpsList";

    /*---------------------好友信息----------------------------*/
        //得到所有好友信息
    public static final String GetFriends = address+"/txzh/getFriendsList";

    /*---------------------群组信息----------------------------*/
        //得到群成员
    public static final String GetGroupMemberGps = address+"/txzh/groupMemberGps";

    /*---------------------可见范围----------------------------*/
        //改变看见范围
    public static final String ChangeSeeArea = address+"/txzh/groupMemberGps";


    /*---------------------发送邀请信息----------------------------*/
    public static final String setInvitation = address + "/txzh/insertInvitation";

    /*---------------------获取邀请信息----------------------------*/
    public static final String getInvitation = address + "/txzh/selectInvitation";

    /*---------------------添加好友邀请信息----------------------------*/
    public static final String deleteInvitation = address + "/txzh/deleteInvitation";

    /*---------------------创建群聊----------------------------*/
    public static final String createdGroup = address + "/txzh/insertGroup";

    /*---------------------删除群聊----------------------------*/
    public static final String deletedGroup = address + "/txzh/deleteGroup";

    /*---------------------加入群聊----------------------------*/
    public static final String joinGroup = address + "/txzh/joinGroup";

    /*---------------------查找群聊----------------------------*/
    public static final String serachGroup = address + "/txzh/searchGroup";

    /*---------------------退出群聊----------------------------*/
    public static final String exitGroup = address + "/txzh/exitGroup";

    /*---------------------添加好友----------------------------*/
    public static final String addFriends = address + "/txzh/addFriends";





}
