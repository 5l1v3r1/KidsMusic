package com.greendev.bebekninnileri.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.greendev.bebekninnileri.R;

import java.util.ArrayList;

public class MusicListAdapter extends ArrayAdapter<String>{

    private Context context;
    private ArrayList<String> mealSurahList = new ArrayList<String>();

    private LayoutInflater inflater=null;

    public MusicListAdapter(Context context, ArrayList<String> mealSurahList) {
        super(context,R.layout.item_surahlist,mealSurahList);
        this.mealSurahList=mealSurahList;
        this.context=context;

    }

    public class Holder
    {
        TextView tvTrSurahName;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_surahlist, null, true);

        holder.tvTrSurahName=(TextView) rowView.findViewById(R.id.surahTrItem);
        holder.tvTrSurahName.setText(mealSurahList.get(position));

        return rowView;
    }
}