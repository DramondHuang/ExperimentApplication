package com.jnu.myapplication;

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

public class MapViewFragment extends Fragment {
    MapView mMapView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
    //在使用SDK各组件之前初始化context信息，传入ApplicationContext
    //注意该方法要再setContentView方法之前实现
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView)view.findViewById(R.id.bmapView);
        LatLng jnu =  new LatLng(22.255925,113.541112);
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

        //添加文字标志
        //构建TextOptions对象
        OverlayOptions mTextOptions = new TextOptions()
                .text("暨南大学珠海校区") //文字内容
                .bgColor(0xAAFFFF00) //背景色
                .fontSize(35) //字号
                .fontColor(0xFFFF00FF) //文字颜色
                .rotate(0) //旋转角度
                .position(jnu);
        //在地图上显示文字覆盖物
        Overlay mText = myBaiduMap.addOverlay(mTextOptions);

        //添加图标
        //准备 marker 的图片
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.school);
        //准备 marker option 添加 marker 使用
        MarkerOptions markerOptions = new MarkerOptions().icon(bitmap).position(jnu);
        //获取添加的 marker 这样便于后续的操作
        Marker marker = (Marker) myBaiduMap.addOverlay(markerOptions);

        //对 marker 添加点击相应事件
        myBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(), "暨珠被点击了！", Toast.LENGTH_SHORT).show();
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

}
