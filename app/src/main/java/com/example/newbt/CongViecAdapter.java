package com.example.newbt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CongViecAdapter extends BaseAdapter {

    private MainActivity context;
    private int layout;
    private List<CongViec> congViecList;

    public CongViecAdapter(MainActivity context, int layout, List<CongViec> congViecList) {
        this.context = context;
        this.layout = layout;
        this.congViecList = congViecList;
    }

    @Override
    public int getCount() {
        return congViecList.size();
    }

    @Override
    public Object getItem(int position) {
        return congViecList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView txtTen;
        ImageView imgDelete, imgEdit;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtTen = convertView.findViewById(R.id.textViewTenCongViec);
            holder.imgDelete = convertView.findViewById(R.id.imageViewXoa);
            holder.imgEdit = convertView.findViewById(R.id.imageViewSua);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CongViec congViec = congViecList.get(position);
        holder.txtTen.setText(congViec.getTenCV());

        // Bắt sự kiện xóa và sửa
        holder.imgEdit.setOnClickListener(v -> {
            context.DialogSuaCongViec(congViec.getTenCV(), congViec.getId());
        });

        holder.imgDelete.setOnClickListener(v -> {
            context.DialogXoaCongViec(congViec.getTenCV(), congViec.getId());
        });

        return convertView;
    }
}