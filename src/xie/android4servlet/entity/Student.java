package xie.android4servlet.entity;

import java.io.Serializable;

import android.R.integer;

public class Student implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5453674613937713355L;
	
	private Long id;	
	private String name;	
	private int age;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Student(Long id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}
	public Student() {
		super();
	}
	
	
	

}
