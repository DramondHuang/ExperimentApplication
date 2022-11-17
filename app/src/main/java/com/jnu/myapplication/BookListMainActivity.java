package com.jnu.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BookListMainActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private RecyclerAdapter myAdapter;
    private int mPosition;

    ArrayList<Book> Booklist=new ArrayList<Book>();
    Book Book1= new Book("软件项目管理案例教程（第4版）", R.drawable.book_2);
    Book Book2= new Book("创新工程实践", R.drawable.book_no_name);
    Book Book3= new Book("信息安全数学基础（第2版）", R.drawable.book_1);

    public int getPosition() {

        return mPosition;

    }
    public class Book{
        int cover_id;
        String title;

        public Book(String t,int id){
            title=t;
            cover_id=id;
        }

        public int getCoverResource(){
            return cover_id;
        }

        public String getTitle(){
            return title;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        String name=data.getExtras().getString("book_name");
        switch (resultCode) {
            case 1:         // 子窗口ChildActivity的回传数据
                Book addedbook=new Book(name,R.drawable.book_no_name);
                Booklist.add(mPosition,addedbook);
                myAdapter.notifyDataSetChanged();
                break;

            case 2:
                Booklist.get(mPosition).title=name;
                myAdapter.notifyDataSetChanged();
            default:
                //其它窗口的回传数据
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);
        initData();
        myRecyclerView=(RecyclerView) findViewById(R.id.recycle_view_books);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.setAdapter(myAdapter=new RecyclerAdapter());

        registerForContextMenu(findViewById(R.id.recycle_view_books));
    }
    protected void initData(){

        Booklist.add(Book1);
        Booklist.add(Book2);
        Booklist.add(Book3);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        // 如果要给多个 view 注册上下文菜单，可以根据 v 参数来判断

        MenuInflater inflator = new MenuInflater(this);
        inflator.inflate(R.menu.menu1, menu);
    }
    protected List getListBooks(){
        return Booklist;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add:
                Intent intent=new Intent(this,Add_Activity.class);
                Toast.makeText(this,"add",Toast.LENGTH_SHORT).show();
                intent.putExtra("Action","Add");
                startActivityForResult(intent,0);
                break;

            case R.id.delete:
                Toast.makeText(this,"delete",Toast.LENGTH_SHORT).show();
                Booklist.remove(mPosition);
                myAdapter.notifyDataSetChanged();
                break;

            case R.id.edit:
                Intent intent2=new Intent(this,Add_Activity.class);
                Toast.makeText(this,"add",Toast.LENGTH_SHORT).show();
                intent2.putExtra("Action","Edit");
                intent2.putExtra("name",Booklist.get(mPosition).title);
                startActivityForResult(intent2,0);
                break;
        }
        return super.onContextItemSelected(item);
    }


    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(
                    BookListMainActivity.this).inflate(R.layout.recycler_items,parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            holder.tv.setText(Booklist.get(position).title);
            holder.iv.setImageResource(Booklist.get(position).cover_id);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mPosition = holder.getAdapterPosition();//将当前position赋值保存
                    return false;
                }
            });
        }

        @Override
        public int getItemCount(){
            return Booklist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            ImageView iv;

            public MyViewHolder(View inflate) {
                super(inflate);
                tv=(TextView) inflate.findViewById(R.id.text_view_book_title);
                iv=(ImageView) inflate.findViewById(R.id.image_view_book_cover);
            }
        }
    }
}