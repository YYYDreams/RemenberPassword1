package com.example.common;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String UserName;
	private String Password;
	
	public User(){}
	
	public User(String username,String password){
		this.UserName=username;
		this.Password=password;
	}
	
	public void setUserName(String username){
		this.UserName=username;
	}
	
	public void setPassword(String password){
		this.Password=password;
	}
	
	public String getUserName(){
		return UserName;
	}
	
	public String getPassword(){
		return Password;
	}
}
