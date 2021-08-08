package com.nicanoritorma.gradeview.dataModels;

public class Course_DataModel {
    private String course_name;
    private String prof_name;
    private String YearAndBlock;
    private String college;

    public Course_DataModel() {
    }

    public Course_DataModel(String course_name, String YearAndBlock, String prof_name, String college) {
        this.course_name = course_name;
        this.prof_name = prof_name;
        this.YearAndBlock = YearAndBlock;
        this.college = college;
    }

    public Course_DataModel(String course_name, String YearAndBlock, String prof_name) {
        this.course_name = course_name;
        this.prof_name = prof_name;
        this.YearAndBlock = YearAndBlock;
    }


    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getProf_name() {
        return prof_name;
    }

    public void setProf_name(String prof_name) {
        this.prof_name = prof_name;
    }

    public String getYearAndBlock() {
        return YearAndBlock;
    }

    public void setYearAndBlock(String yearAndBlock) {
        YearAndBlock = yearAndBlock;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }
}

