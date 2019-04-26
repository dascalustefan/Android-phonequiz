package com.example.beggining;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class customadaperlistquiz extends ArrayAdapter<Quizanswer> implements View.OnClickListener{

    private ArrayList<Quizanswer> dataSet;
    Context mContext;
    private int idResursa;

    @Override
    public void onClick(View v) {

    }

    // View lookup cache
    private static class ViewHolder {
        TextView quizname;
        TextView right;
        TextView left;
        TextView datetaken;
        TextView numbers_taken;
    }

    public customadaperlistquiz(ArrayList<Quizanswer> data, Context context,int resource) {
        super(context,resource, data);
        this.dataSet = data;
        this.mContext=context;
        idResursa = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(idResursa, null);
        // Get the data item for this position
        final Quizanswer quizanswer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;
        Button btn = view.findViewById(R.id.take_quiz);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(),Quizstarter.class);
                it.putExtra("fisier", ""+quizanswer.getFilename());


                getContext().startActivity(it);
            }
        });
        TextView tvNume = view.findViewById(R.id.nume);
        tvNume.setText(quizanswer.getFilename());

        TextView tvVarsta = view.findViewById(R.id.data);
        tvVarsta.setText(""+String.valueOf(quizanswer.getDatetaken())+" ");

        TextView tvMedie = view.findViewById(R.id.right);
        tvMedie.setText(""+quizanswer.getRight()+" ");

        TextView tvAbsente = view.findViewById(R.id.left);
        tvAbsente.setText(""+quizanswer.getLeft()+" ");

        return view;

    }
}