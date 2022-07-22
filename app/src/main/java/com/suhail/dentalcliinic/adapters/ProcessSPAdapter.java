package com.suhail.dentalcliinic.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.Process;

import java.util.List;

public class ProcessSPAdapter extends BaseAdapter {
    List<Process> processes;
    Context context;

    public ProcessSPAdapter(List<Process> processes, Context context) {
        this.processes = processes;
        this.context = context;
    }


    @Override
    public int getCount() {
        return processes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View v= view;
        if (v==null){
         v=LayoutInflater.from(context).inflate(R.layout.process_sp_item_design,null,false);
         }
            TextView name=v.findViewById(R.id.txt_sp_process_adapter);
            name.setText(processes.get(i).getName());

        return v;
    }
}
