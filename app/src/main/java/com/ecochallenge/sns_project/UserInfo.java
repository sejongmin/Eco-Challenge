package com.ecochallenge.sns_project;

public class UserInfo {
    private String name;
    private String phoneNumber;
    private String birthDay;
    private String address;
    private String photoUrl;
    private long score;
    int rank;//11.20 추가

    public UserInfo(String name, String phoneNumber, String birthDay, String address, String photoUrl,long score){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.photoUrl = photoUrl;
        this.score = score;
    }

    public UserInfo(String name, String phoneNumber, String birthDay, String address,long score){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.score = score;
    }
    //11.20 추가
    public UserInfo(String name, String phoneNumber, String birthDay, String address, String photoUrl,long score,int rank){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.photoUrl = photoUrl;
        this.score = score;
        this.rank=rank;
    }
    public UserInfo(String name, String phoneNumber, String birthDay, String address,long score,int rank){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.score = score;
        this.rank=rank;
    }
    //11.20 추가
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getBirthDay(){
        return this.birthDay;
    }
    public void setBirthDay(String birthDay){
        this.birthDay = birthDay;
    }
    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getPhotoUrl(){
        return this.photoUrl;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }
    public long getscore(){
        return this.score;
    }
    public void setScore(String photoUrl){
        this.score = score;
    }
    public int getRank(){
        return this.rank;
    }//11.20 추가
    public void setRank(int rank){
        this.rank = rank;
    }//11.20 추가
}
