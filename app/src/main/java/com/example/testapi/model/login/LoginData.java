package com.example.testapi.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginData {

	@SerializedName("user_id")
	private int userId;

	@SerializedName("no_anggota")
	private String noAnggota;

	@SerializedName("name")
	private String name;

	public String getUserId(){
		return String.valueOf(userId);
	}

	public String getNoAnggota(){
		return noAnggota;
	}

	public String getName(){
		return name;
	}
}