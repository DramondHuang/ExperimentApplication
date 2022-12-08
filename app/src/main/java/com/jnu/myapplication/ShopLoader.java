package com.jnu.myapplication;

import static android.content.Context.DOWNLOAD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ShopLoader{

    String result;


    public ArrayList<Coordinate> parsonJson(String json){
        //储存地点
        ArrayList<Coordinate> coordinates = new ArrayList<>();

        //sd卡中读取并解析Json
        try {
            // 整个最大的JSON数组
            JSONObject jsonObjectALL = new JSONObject(json);
            // 通过标识(shops)，获取JSON数组
            JSONArray jsonArray = jsonObjectALL.getJSONArray("shops");
            for (int i = 0; i < jsonArray.length(); i++) {
                // JSON数组里面的具体-JSON对象
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.optString("name");
                double latitude = jsonObject.optDouble("latitude");
                double longitude = jsonObject.optDouble("longitude");
                Coordinate position = new Coordinate(name, latitude, longitude);
                coordinates.add(position);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    public String download(String path) throws IOException {
        try{
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String tempLine = null;
                StringBuffer resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
                Log.i("test data",resultBuffer.toString());

                return resultBuffer.toString();
            }
        }catch(Exception exception)
        {
            Log.e("error",exception.getMessage());

        }
        return "";
    }
}
