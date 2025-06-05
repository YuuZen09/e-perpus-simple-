package com.example.testapi.model.login;

import com.google.gson.annotations.SerializedName;

public class Login{

	@SerializedName("data")
	private LoginData loginData;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public LoginData getData(){
		return loginData;
	}

	public String getMessage(){
		return message;
	}

	public boolean isStatus(){
		return status;
	}
}