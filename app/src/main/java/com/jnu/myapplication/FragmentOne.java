package com.jnu.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentOne extends Fragment {
    @Nullable
    private RecyclerView myRecyclerView;
    private RecyclerAdapter myAdapter;
    private int mPosition;
    DataSaver myDataSaver = new DataSaver();
    ArrayList<Book> tempbooklist;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book,container);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_books);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(myAdapter = new RecyclerAdapter());

        registerForContextMenu(view.findViewById(R.id.recycle_view_books));

        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        // 如果要给多个 view 注册上下文菜单，可以根据 v 参数来判断
        MenuInflater inflator = new MenuInflater(getActivity());
        inflator.inflate(R.menu.menu1, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(getActivity(), Add_Activity.class);
                Toast.makeText(getActivity(), "add", Toast.LENGTH_SHORT).show();
                intent.putExtra("Action", "Add");
                startActivityForResult(intent, 0);
                break;

            case R.id.delete:
                Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
                tempbooklist = myDataSaver.Load(getActivity());
                tempbooklist.remove(mPosition);
                myDataSaver.Save(getActivity(), tempbooklist);
                myAdapter.notifyDataSetChanged();
                break;

            case R.id.edit:
                Intent intent2 = new Intent(getActivity(), Add_Activity.class);
                Toast.makeText(getActivity(), "add", Toast.LENGTH_SHORT).show();
                intent2.putExtra("Action", "Edit");
                tempbooklist=myDataSaver.Load(getActivity());
                intent2.putExtra("name", tempbooklist.get(mPosition).title);
                startActivityForResult(intent2, 0);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String name = data.getExtras().getString("book_name");
        tempbooklist = myDataSaver.Load(getActivity());
        switch (resultCode) {
            case 0:
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            case 1:         // Add book
                Book addedbook = new Book(name, R.drawable.book_no_name);
                tempbooklist.add(mPosition, addedbook);
                myDataSaver.Save(getActivity(), tempbooklist);
                myAdapter.notifyDataSetChanged();
                break;

            case 2:   // Edit book
                tempbooklist.get(mPosition).title = name;
                myDataSaver.Save(getActivity(), tempbooklist);
                myAdapter.notifyDataSetChanged();
            default:
                //其它窗口的回传数据
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.recycler_items, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position) {
            tempbooklist = myDataSaver.Load(getActivity());
            holder.tv.setText(tempbooklist.get(position).title);
            holder.iv.setImageResource(tempbooklist.get(position).cover_id);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mPosition = holder.getAdapterPosition();//将当前position赋值保存
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            tempbooklist = myDataSaver.Load(getActivity());
            return tempbooklist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            ImageView iv;

            public MyViewHolder(View inflate) {
                super(inflate);
                tv = (TextView) inflate.findViewById(R.id.text_view_book_title);
                iv = (ImageView) inflate.findViewById(R.id.image_view_book_cover);
            }
        }
    }
}
