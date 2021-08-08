package com.nicanoritorma.gradeview.dataModels;

public class Student_DataModel {
    private String student_name;
    private String student_idNum;
    private String student_grade;

    public Student_DataModel() {
    }

    public Student_DataModel(String student_name, String student_idNum, String student_grade) {
        this.student_name = student_name;
        this.student_idNum = student_idNum;
        this.student_grade = student_grade;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_idNum() {
        return student_idNum;
    }

    public void setStudent_idNum(String student_idNum) {
        this.student_idNum = student_idNum;
    }

    public String getStudent_grade() {
        return student_grade;
    }

    public void setStudent_grade(String student_grade) {
        this.student_grade = student_grade;
    }
}
