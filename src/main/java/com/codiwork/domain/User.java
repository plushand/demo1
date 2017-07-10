package com.codiwork.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class User extends AbstractEntity{

	@Column(nullable=false, length=20, unique=true)
	@JsonProperty
	private String userId;
	@JsonIgnore
	private String password;
	@JsonProperty
	private String name;
	@JsonProperty
	private String email;
	
	@Override
	public String toString() {
		return "User [" + super.toString() + ", userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
	}

	public boolean matchPassword(String newPassword){
		if (newPassword == null){
			return false;
		}
		return newPassword.equals(password);
	}
	public boolean matchId(Long newId){
		if (newId == null){
			return false;
		}
		return newId.equals(getId());
	}
	
	public void update(User newUser) {
		// TODO Auto-generated method stub
		this.password = newUser.password;
		this.name = newUser.name;
		this.email = newUser.email;
	}
}
