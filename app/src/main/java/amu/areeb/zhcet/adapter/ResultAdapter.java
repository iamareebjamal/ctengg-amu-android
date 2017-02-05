package amu.areeb.zhcet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import amu.areeb.zhcet.R;
import amu.areeb.zhcet.model.Result;
import amu.areeb.zhcet.utils.Utils;


public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder> {

    private List<Result> resultList;
    private Context context;
    private List<String> colors = Arrays.asList(Utils.COLORS);
    private int lastPosition = -1;

    public ResultAdapter(Context ctx, List<Result> list) {
        //lastPosition=-1;
        resultList = list;
        context = ctx;
        Collections.shuffle(colors);
    }

    @Override
    public ResultAdapter.ResultHolder onCreateViewHolder(ViewGroup viewGroup, int p2) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_card, viewGroup, false);
        return new ResultHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultAdapter.ResultHolder rh, int i) {
        Result r = resultList.get(i);

        rh.subject.setText(r.course);
        rh.sessional.setText("Sessional : " + r.sessional_marks);
        rh.exam.setText("Final : " + r.exam_marks);
        rh.total.setText("Total : " + r.total);
        rh.grace.setText("Grace : " + r.grace);
        rh.grade.setText(r.grades);

        rh.average.setText("Average : " + r.class_average);
        rh.rank.setText("Rank : " + r.subject_rank);
        rh.highest.setText("Highest : " + r.highest);
        rh.course_name.setText(r.course_name);
        rh.card.setCardBackgroundColor(Color.parseColor(colors.get(i)));
        setAnimation(rh.card, i);
    }

    public void resetAnimation() {
        lastPosition = -1;
    }

    private void setAnimation(CardView viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setDuration(200);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ResultAdapter.ResultHolder holder) {
        holder.card.clearAnimation();
        //lastPosition=-1;
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    @Override
    public long getItemId(final int position) {
        return resultList.get(position).hashCode();
    }

    class ResultHolder extends RecyclerView.ViewHolder {
        TextView subject, sessional, exam, total, grace, grade;
        TextView course_name, highest, average, rank;
        CardView card;

        public ResultHolder(View v) {
            super(v);
            subject = (TextView) v.findViewById(R.id.subject);
            sessional = (TextView) v.findViewById(R.id.sessional);
            exam = (TextView) v.findViewById(R.id.exam);
            total = (TextView) v.findViewById(R.id.total);
            grace = (TextView) v.findViewById(R.id.grace);
            grade = (TextView) v.findViewById(R.id.grade);
            course_name = (TextView) v.findViewById(R.id.subjectName);
            highest = (TextView) v.findViewById(R.id.highest);
            average = (TextView) v.findViewById(R.id.average);
            rank = (TextView) v.findViewById(R.id.rank);
            card = (CardView) v;
        }
    }
}
