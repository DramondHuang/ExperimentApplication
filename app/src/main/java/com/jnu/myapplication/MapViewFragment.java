package com.jnu.myapplication;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    BaiduMap myBaiduMap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SDKInitializer.setAgreePrivacy(BookMainActivity.getContext(),true);
        SDKInitializer.initialize(BookMainActivity.getContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
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
        myBaiduMap = mMapView.getMap();
        myBaiduMap.setMapStatus(mMapStatusUpdate);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在Fragment执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        Handler handler = new Handler() {
            public void handleMessage(@NonNull Message msg) {
                //主线程接受子线程发送的信息
                ArrayList<Coordinate> Handlerget = new ArrayList<>();
                Handlerget = (ArrayList<Coordinate>) msg.obj;
                Toast.makeText(BookMainActivity.getContext(), "获取Json数据成功", Toast.LENGTH_SHORT).show();
                addMarker(Handlerget);
            }

            ;
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                ShopLoader dataLoader = new ShopLoader();
                String shopJsonData = null;
                try {
                    shopJsonData = dataLoader.download("http://file.nidama.net/class/mobile_develop/data/bookstore2022.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<Coordinate> locations = dataLoader.parsonJson(shopJsonData);

                //使用Handler向主线程发送消息
                Message msg = new Message();
                msg.obj = locations;
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();

        //对 marker 添加点击相应事件
        myBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(), "Marker Clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

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

    public void addMarker(ArrayList<Coordinate> coordinates) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.school);
        //给Json中的地点添加marker
        //准备 marker 的图片
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coordinate = coordinates.get(i);
            LatLng latLng = new LatLng(coordinate.latitude, coordinate.longitude);
            //构建TextOptions对象
            OverlayOptions myTextOptions = new TextOptions()
                    .text(coordinate.name) //文字内容
                    .bgColor(0xAAFFFF00) //背景色
                    .fontSize(35) //字号
                    .fontColor(0xFFFF00FF) //文字颜色
                    .rotate(0) //旋转角度
                    .position(latLng);
            //在地图上显示文字覆盖物
            myBaiduMap.addOverlay(myTextOptions);
            //添加图标
            //准备 marker option 添加 marker 使用
            MarkerOptions mymarkerOptions = new MarkerOptions().icon(bitmap).position(latLng);
            //获取添加的 marker 这样便于后续的操作
            myBaiduMap.addOverlay(mymarkerOptions);
        }
    }
}



