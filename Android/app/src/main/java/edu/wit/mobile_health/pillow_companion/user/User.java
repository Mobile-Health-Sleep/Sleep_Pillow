package edu.wit.mobile_health.pillow_companion.user;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class User {
    private int height;
    private int weight;
    private int age;

    public User(int height, int weight, int age){
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    public int getHeight(){
        return this.height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int getWeight(){
        return this.weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public int getAge(){
        return this.age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void exportToJson(){
        JSONObject temp = new JSONObject();

        try{
            temp.put("height", getHeight());
            temp.put("weight", getWeight());
            temp.put("age", getAge());
        }catch(org.json.JSONException e){
            e.printStackTrace();
        }

        try{
            FileWriter JsonWriter = new FileWriter("data/data/edu.wit.mobile_health.pillow_companion/files/User.json");
            JsonWriter.append(temp.toString());
            JsonWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
