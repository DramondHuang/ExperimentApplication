package com.jnu.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataSaver {
    public void SaveBook(Context context, ArrayList<Book> data){
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("booklist.dat", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public ArrayList<Book> LoadBook(Context context){
        ArrayList<Book> data = new ArrayList<>();
        try {
            FileInputStream fileInputStream = context.openFileInput("booklist.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            data = (ArrayList<Book>)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void SaveCoordinate(Context context, String data){
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("coordinatelist.txt", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public String LoadCoordinate(Context context){
        String data="" ;
        try {
            FileInputStream fileInputStream = context.openFileInput("coordinatelist.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            data = objectInputStream.readObject().toString();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
