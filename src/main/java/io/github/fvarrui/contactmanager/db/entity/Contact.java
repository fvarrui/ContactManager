package io.github.fvarrui.contactmanager.db.entity;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.firestore.annotation.Exclude;

public class Contact {

	private String id;
	private String name;
	private String surname;
	private Map<String, String> phones = new HashMap<>();
	
	public Contact() {}

	public Contact(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
	}

	@Exclude
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Map<String, String> getPhones() {
		return phones;
	}

	public void setPhones(Map<String, String> phones) {
		this.phones = phones;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", surname=" + surname + ", phones=" + phones + "]";
	}

}
