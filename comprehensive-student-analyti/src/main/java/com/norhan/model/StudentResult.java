package com.norhan.model;

public class StudentResult {
    private String studentId;
    private String name;
    private String subject;
    private int marks;
    private String grade;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
public String getName(){
    return name;
}
public void setName(String name){
    this.name=name;
}

public String getSubject(){
    return subject;
}
public void setSubject(String Subject){
    this.subject =Subject;
}
public int getMarks(){
    return marks;
}
public void setMarks(int marks){
    this.marks=marks;
}
public String getGrade(){
    return grade;
}
public void setGrade(String grade){
    this.grade=grade;
}
}

