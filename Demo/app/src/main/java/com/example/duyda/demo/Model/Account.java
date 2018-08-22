package com.example.duyda.demo.Model;

import io.realm.RealmObject;

public class Account extends RealmObject {
    String UserName;
    String PassWord;

    @Override
    public String toString() {
        return "Account{" +
                "UserName='" + UserName + '\'' +
                ", PassWord='" + PassWord + '\'' +
                '}';
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
}
