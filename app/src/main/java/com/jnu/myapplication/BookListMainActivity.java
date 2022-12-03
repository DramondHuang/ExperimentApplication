package com.jnu.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class BookListMainActivity extends AppCompatActivity {
    //private RecyclerView myRecyclerView;
    //private RecyclerAdapter myAdapter;
    private int mPosition;
    File file = new File(Environment.getExternalStorageDirectory(),
            "booklist.dat");
    DataSaver myDataSaver = new DataSaver();
    ArrayList<Book> tempbooklist;

    ArrayList<Book> Booklist = new ArrayList<Book>();
    Book Book1 = new Book("软件项目管理案例教程（第4版）", R.drawable.book_2);
    Book Book2 = new Book("创新工程实践", R.drawable.book_no_name);
    Book Book3 = new Book("信息安全数学基础（第2版）", R.drawable.book_1);

    private TabLayout mTabLayout;
    private ViewPager2 mViewPage;
    private String[] tabTitles;//tab的标题
    private List<Fragment> mDatas = new ArrayList<>();//ViewPage2的Fragment容器

    FragmentOne frgOne = new FragmentOne();
    WebViewFragment frgTwo = new WebViewFragment();
    MapViewFragment frgThree=new MapViewFragment();

    public int getPosition() {
        return mPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        myRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_books);
//        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        myRecyclerView.setAdapter(myAdapter = new RecyclerAdapter());
//        registerForContextMenu(findViewById(R.id.recycle_view_books));

        Booklist.add(Book1);
        Booklist.add(Book2);
        Booklist.add(Book3);
        myDataSaver.Save(this, Booklist);

        //找到控件
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPage = findViewById(R.id.view_page);
        //创建适配器
        MyViewPageAdapter mAdapter = new MyViewPageAdapter(this,mDatas);
        mViewPage.setAdapter(mAdapter);
        mViewPage.setUserInputEnabled(false);

        //TabLayout与ViewPage2联动关键代码
        new TabLayoutMediator(mTabLayout, mViewPage, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabTitles[position]);
            }
        }).attach();

        //ViewPage2选中改变监听
        mViewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        //TabLayout的选中改变监听
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        SDKInitializer.setAgreePrivacy(getApplicationContext(),true);
        //初始化百度地图数据
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //权限设置
        LocationClient.setAgreePrivacy(true);
    }

    protected void initData() throws IOException {

        tabTitles = new String[]{"图书", "新闻","卖家"};
        mDatas.add(frgOne);
        mDatas.add(frgTwo);
        mDatas.add(frgThree);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//
//        // 如果要给多个 view 注册上下文菜单，可以根据 v 参数来判断
//        MenuInflater inflator = new MenuInflater(this);
//        inflator.inflate(R.menu.menu1, menu);
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add:
//                Intent intent = new Intent(this, Add_Activity.class);
//                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
//                intent.putExtra("Action", "Add");
//                startActivityForResult(intent, 0);
//                break;
//
//            case R.id.delete:
//                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
//                tempbooklist = myDataSaver.Load(this);
//                tempbooklist.remove(mPosition);
//                myDataSaver.Save(this, tempbooklist);
//                //myAdapter.notifyDataSetChanged();
//                break;
//
//            case R.id.edit:
//                Intent intent2 = new Intent(this, Add_Activity.class);
//                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
//                intent2.putExtra("Action", "Edit");
//                intent2.putExtra("name", Booklist.get(mPosition).title);
//                startActivityForResult(intent2, 0);
//                break;
//        }
//        return super.onContextItemSelected(item);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String name = data.getExtras().getString("book_name");
//        tempbooklist = myDataSaver.Load(this);
//        switch (resultCode) {
//            case 0:
//                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
//            case 1:         // Add book
//                Book addedbook = new Book(name, R.drawable.book_no_name);
//                tempbooklist.add(mPosition, addedbook);
//                myDataSaver.Save(this, tempbooklist);
//                //myAdapter.notifyDataSetChanged();
//                break;
//
//            case 2:   // Edit book
//                tempbooklist.get(mPosition).title = name;
//                myDataSaver.Save(this, tempbooklist);
//                //myAdapter.notifyDataSetChanged();
//            default:
//                //其它窗口的回传数据
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }


//    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
//                    BookListMainActivity.this).inflate(R.layout.recycler_items, parent,
//                    false));
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position) {
//            tempbooklist = myDataSaver.Load(BookListMainActivity.this);
//            holder.tv.setText(tempbooklist.get(position).title);
//            holder.iv.setImageResource(tempbooklist.get(position).cover_id);
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    mPosition = holder.getAdapterPosition();//将当前position赋值保存
//                    return false;
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            tempbooklist = myDataSaver.Load(BookListMainActivity.this);
//            return tempbooklist.size();
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            TextView tv;
//            ImageView iv;
//
//            public MyViewHolder(View inflate) {
//                super(inflate);
//                tv = (TextView) inflate.findViewById(R.id.text_view_book_title);
//                iv = (ImageView) inflate.findViewById(R.id.image_view_book_cover);
//            }
//        }
//    }

    // View Page Adapter
    public class MyViewPageAdapter extends FragmentStateAdapter {
        List<Fragment> mDatas = new ArrayList<>();

        public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> mDatas) {
            super(fragmentActivity);
            this.mDatas = mDatas;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mDatas.get(position);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }
}