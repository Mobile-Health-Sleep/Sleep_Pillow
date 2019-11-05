package edu.wit.mobile_health.pillow_companion.user;

public class user {
    private int height;
    private int weight;
    private int age;

    user(int height, int weight, int age){
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWeight(){
        return this.weight;
    }

    public int getAge(){
        return this.age;
    }
}
