package com.example.tranguyen.gameappproject;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Hunt implements Serializable {

    private int id;
    private String name;
    private Date startDate;
    private Date expiryDate;
    private Date timeLimit;
    private Boolean noTimeLimit;
    private String winningCode;

   // private String authKey;

    public Hunt(int id, String name, Date startDate, Date expiryDate, Date timeLimit, Boolean noTimeLimit, String winningCode){
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.timeLimit = timeLimit;
        this.noTimeLimit = noTimeLimit;
        this.winningCode = winningCode;
        //this.authKey = auth_key;

    }

//    public Hunt(){
//
//    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public Date getTimeLimit() {
        return timeLimit;
    }
    public Boolean getNoTimeLimit() {
        return noTimeLimit;
    }
    public String getWinningCode() {
        return winningCode;
    }

//    public String getAuthKey() {
//        return authKey;
//    }





}
