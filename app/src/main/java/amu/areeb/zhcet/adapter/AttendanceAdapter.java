package amu.areeb.zhcet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import amu.areeb.zhcet.R;
import amu.areeb.zhcet.model.Attendance;
import amu.areeb.zhcet.utils.Utils;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceHolder> {

    private List<Attendance> attendanceList;
    private Context context;
    private ArrayList<String> colors = new ArrayList<String>(Utils.COLORS.length);
    private int lastPosition = -1;

    public AttendanceAdapter(Context ctx, List<Attendance> list) {
        //lastPosition=-1;
        attendanceList = list;
        context = ctx;
        for (int i = 0; i < Utils.COLORS.length; i++) {
            colors.add(Utils.COLORS[i]);
        }
        Collections.shuffle(colors);
    }

    @Override
    public AttendanceAdapter.AttendanceHolder onCreateViewHolder(ViewGroup viewGroup, int p2) {
        // TODO: Implement this method
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_card, viewGroup, false);
        return new AttendanceHolder(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(AttendanceAdapter.AttendanceHolder ah, int i) {
        Attendance a = attendanceList.get(i);
        ah.subject.setText(a.course);
        ah.attended.setText("Attended : " + a.attended);
        ah.total.setText("Total : " + a.total);
        ah.perc.setText(a.percentage + "%");
        if (a.remark.length() > 0) {
            ah.remark.setVisibility(View.VISIBLE);
            ah.remark.setText(a.remark);
            GradientDrawable shape = new GradientDrawable();
            shape.setColor(Color.parseColor("#44eeeeee"));
            shape.setCornerRadius(ah.remark.getWidth() + 100);
            if (Build.VERSION.SDK_INT >= 16)
                ah.remark.setBackground(shape);
            else
                ah.remark.setBackgroundDrawable(shape);
        } else
            ah.remark.setVisibility(View.GONE);
        ah.date.setText(a.date);
        ah.card.setCardBackgroundColor(Color.parseColor(colors.get(i)));
        setAnimation(ah.card, i);
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
    public void onViewDetachedFromWindow(AttendanceAdapter.AttendanceHolder holder) {
        // TODO: Implement this method
        holder.card.clearAnimation();
        //lastPosition=-1;
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return attendanceList.size();
    }

    @Override
    public long getItemId(final int position) {
        return getItemId(position);
    }

    public class AttendanceHolder extends RecyclerView.ViewHolder {
        protected TextView subject;
        protected TextView attended;
        protected TextView total;
        protected TextView perc;
        protected TextView remark;
        protected TextView date;
        protected CardView card;

        public AttendanceHolder(View v) {
            super(v);
            subject = (TextView) v.findViewById(R.id.subject);
            attended = (TextView) v.findViewById(R.id.attended);
            total = (TextView) v.findViewById(R.id.total);
            perc = (TextView) v.findViewById(R.id.perc);
            remark = (TextView) v.findViewById(R.id.remark);
            date = (TextView) v.findViewById(R.id.date);
            card = (CardView) v;
        }
    }
}
