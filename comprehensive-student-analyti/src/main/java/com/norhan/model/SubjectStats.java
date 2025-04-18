package com.norhan.model;

public class SubjectStats {
    private String subject;
    private double averageMarks;
    private String topStudents;
    private String lowestStudents;
    private double passRate;
    private int gradeACount;
    private int gradeBCount;
    private int gradeCCount;
    private int gradeDCount;
    private int gradeFCount;
    private String outliers;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getAverageMarks() {
        return averageMarks;
    }

    public void setAverageMarks(double averageMarks) {
        this.averageMarks = averageMarks;
    }

    public String getTopStudents() {
        return topStudents;
    }

    public void setTopStudents(String topStudents) {
        this.topStudents = topStudents;
    }

    public String getLowestStudents() {
        return lowestStudents;
    }

    public void setLowestStudents(String lowestStudents) {
        this.lowestStudents = lowestStudents;
    }

    public double getPassRate() {
        return passRate;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }

    public int getGradeACount() {
        return gradeACount;
    }

    public void setGradeACount(int gradeACount) {
        this.gradeACount = gradeACount;
    }

    public int getGradeBCount() {
        return gradeBCount;
    }

    public void setGradeBCount(int gradeBCount) {
        this.gradeBCount = gradeBCount;
    }

    public int getGradeCCount() {
        return gradeCCount;
    }

    public void setGradeCCount(int gradeCCount) {
        this.gradeCCount = gradeCCount;
    }

    public int getGradeDCount() {
        return gradeDCount;
    }

    public void setGradeDCount(int gradeDCount) {
        this.gradeDCount = gradeDCount;
    }

    public int getGradeFCount() {
        return gradeFCount;
    }

    public void setGradeFCount(int gradeFCount) {
        this.gradeFCount = gradeFCount;
    }

    public String getOutliers() {
        return outliers;
    }

    public void setOutliers(String outliers) {
        this.outliers = outliers;
    }
}
