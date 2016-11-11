package amu.areeb.zhcet.attendance;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import java.util.List;
import android.view.LayoutInflater;
import android.support.v7.widget.CardView;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import java.io.Serializable;
import android.widget.TextView;
import amu.areeb.zhcet.utils.Random;
import amu.areeb.zhcet.R;
import java.util.ArrayList;
import java.util.Collections;
import android.view.*;
import android.widget.*;
import android.view.animation.*;
import amu.areeb.zhcet.*;
import android.content.*;

class Attendance implements Serializable {
											/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*"#FF6D00" /*Orange "#00E676" /*Green "#536DFE" /*Indigo* "#448AFF" /*Blue "#40C4FF" /*Light Blue*/
	public static final String[] COLORS = {"#FF5252" /*Red*/, "#FF4081" /*Pink*/, "#e040fb" /*Purple*/, 
									"#00E5FF" /*Cyan*/, 
											"#1DE9B6" /*Teal*/, "#00c853" /*Light Green*/, "#F9A825" /*Yellow*/,
											"#FF6E40" /*Deep Orange*/};
	
	protected String subject, attended, total, perc, remark, date;
	public static String name;
	
	public Attendance(){}
	
	public Attendance(String subject, String attended, String total, String perc, String remark, String date){
		this.subject = subject;
		this.attended = attended;
		this.total = total;
		this.perc = perc;
		this.remark = remark;
		this.date = date;
	}
}

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceHolder>
{
	
	private List<Attendance> attendanceList;
	private Context context;
	private ArrayList<String> colors=new ArrayList<String>(Attendance.COLORS.length);
	private int lastPosition=-1;
	
	public AttendanceAdapter(Context ctx, List<Attendance> list){
		//lastPosition=-1;
		attendanceList = list;
		context=ctx;
		for(int i = 0; i < Attendance.COLORS.length; i++){
			colors.add(Attendance.COLORS[i]);
		}
		Collections.shuffle(colors);
	}
	
	public class AttendanceHolder extends RecyclerView.ViewHolder{
		protected TextView subject;
		protected TextView attended;
		protected TextView total;
		protected TextView perc;
		protected TextView remark;
		protected TextView date;
		protected CardView card;
		
		public AttendanceHolder(View v) {
			super(v);
			subject =  (TextView) v.findViewById(R.id.subject);
			attended = (TextView) v.findViewById(R.id.attended);
			total = (TextView) v.findViewById(R.id.total);
			perc = (TextView) v.findViewById(R.id.perc);
			remark = (TextView) v.findViewById(R.id.remark);
			date = (TextView) v.findViewById(R.id.date);
			card = (CardView) v;
	   }
	}
	
	@Override
	public AttendanceAdapter.AttendanceHolder onCreateViewHolder(ViewGroup viewGroup, int p2)
	{
		// TODO: Implement this method
		View itemView = LayoutInflater.from(viewGroup .getContext()).inflate(R.layout.attendance_card, viewGroup, false);
		return new AttendanceHolder(itemView);
	}

	@SuppressLint("NewApi")
	@Override
	public void onBindViewHolder(AttendanceAdapter.AttendanceHolder ah, int i){
		Attendance a = attendanceList.get(i);
		ah.subject.setText(a.subject);
		ah.attended.setText("Attended : "+a.attended);
		ah.total.setText("Total : "+a.total);
		ah.perc.setText(a.perc+"%");
		if(Float.parseFloat(a.perc)<75){
			ah.remark.setVisibility(View.VISIBLE);
			ah.remark.setText("SHORT");
			GradientDrawable shape = new GradientDrawable();
			shape.setColor(Color.parseColor("#44eeeeee"));
			shape.setCornerRadius(ah.remark.getWidth()+100);
			if(Build.VERSION.SDK_INT>=16)
				ah.remark.setBackground(shape);
			else
				ah.remark.setBackgroundDrawable(shape);
		} else
			ah.remark.setVisibility(View.GONE);
		ah.date.setText(a.date);
		ah.card.setCardBackgroundColor(Color.parseColor(colors.get(i)));
		setAnimation(ah.card,i);
	}
	
	public void resetAnimation(){
		lastPosition=-1;
	}
	
	private void setAnimation(CardView viewToAnimate, int position)
	{
		// If the bound view wasn't previously displayed on screen, it's animated
		if (position > lastPosition)
		{
			Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
			animation.setDuration(200);
			viewToAnimate.startAnimation(animation);
			lastPosition = position;
		}
	}

	@Override
	public void onViewDetachedFromWindow(AttendanceAdapter.AttendanceHolder holder)
	{
		// TODO: Implement this method
		holder.card.clearAnimation();
		//lastPosition=-1;
		super.onViewDetachedFromWindow(holder);
	}

	

	@Override
	public int getItemCount(){
		// TODO: Implement this method
		return attendanceList.size();
	}
	
	@Override
	public long getItemId(final int position) {
		return getItemId(position); 
	}
}
