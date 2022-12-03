package com.jnu.myapplication;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MapViewFragment extends Fragment {
    MapView mMapView = null;
    //储存地点
    ArrayList<Coordinate> coordinates = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        LatLng jnu = new LatLng(22.255925, 113.541112);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(jnu)
                //放大地图到20倍
                .zoom(20)
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        BaiduMap myBaiduMap = mMapView.getMap();
        myBaiduMap.setMapStatus(mMapStatusUpdate);

//        //添加文字标志，
//        LatLng northgate =  new LatLng(22.255453,113.54145);
//        //构建TextOptions对象
//        OverlayOptions mTextOptions = new TextOptions()
//                .text("暨珠实验楼") //文字内容
//                .bgColor(0xAAFFFF00) //背景色
//                .fontSize(35) //字号
//                .fontColor(0xFFFF00FF) //文字颜色
//                .rotate(0) //旋转角度
//                .position(northgate);
//        //在地图上显示文字覆盖物
//        Overlay mText = myBaiduMap.addOverlay(mTextOptions);
//
//        //添加图标
//        //准备 marker 的图片
//        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.school);
//        //准备 marker option 添加 marker 使用
//        MarkerOptions markerOptions = new MarkerOptions().icon(bitmap).position(jnu);
//        //获取添加的 marker 这样便于后续的操作
//        Marker marker = (Marker) myBaiduMap.addOverlay(markerOptions);
//


        //sd卡中读取并解析Json
        String result = getFileFromSdcard("/Android/data/com.jnu.myapplication/files/Download/dxtj.json");
        try {
            // 整个最大的JSON数组
            JSONObject jsonObjectALL = new JSONObject(result);
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

        //给Json中的地点添加marker
        //准备 marker 的图片
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.school);
        for (int i = 0; i < coordinates.size(); i++) {
            LatLng latLng = new LatLng(coordinates.get(i).latitude, coordinates.get(i).longitude);
            //构建TextOptions对象
            OverlayOptions myTextOptions = new TextOptions()
                    .text(coordinates.get(i).name) //文字内容
                    .bgColor(0xAAFFFF00) //背景色
                    .fontSize(35) //字号
                    .fontColor(0xFFFF00FF) //文字颜色
                    .rotate(0) //旋转角度
                    .position(latLng);
            //在地图上显示文字覆盖物
            Overlay myText = myBaiduMap.addOverlay(myTextOptions);
            //添加图标
            //准备 marker option 添加 marker 使用
            MarkerOptions mymarkerOptions = new MarkerOptions().icon(bitmap).position(latLng);
            //获取添加的 marker 这样便于后续的操作
            Marker mymarker = (Marker) myBaiduMap.addOverlay(mymarkerOptions);
        }

        //对 marker 添加点击相应事件
        myBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(), "Marker按键响应！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在Fragment执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        //在Fragment执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在Fragment执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    //读取文件
    public String getFileFromSdcard(String fileName) {
        FileInputStream inputStream = null;
        // 缓存的流，和磁盘无关，不需要关闭
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                inputStream = new FileInputStream(file);
                int len = 0;
                byte[] data = new byte[1024];
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return new String(outputStream.toByteArray());
    }
}

