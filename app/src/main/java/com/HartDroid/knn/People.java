package com.HartDroid.knn;

public class People {
    private double classes;
    private double age;
    private double sex;
    private double survived;
    private double distance;

    public People(double classes, double age, double sex,double survived){
        this.classes = classes;
        this.age = age;
        this.sex = sex;
        this.survived = survived;
    }

    public double getClasses() {
        return classes;
    }

    public void setClasses(double classes) {
        this.classes = classes;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getSex() {
        return sex;
    }

    public void setSex(double sex) {
        this.sex = sex;
    }

    public double getSurvived() {
        return survived;
    }

    public void setSurvived(double survived) {
        this.survived = survived;
    }
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    public String toString(){
        return classes+" "+age+" "+sex+" "+survived+" "+distance;
    }
}
