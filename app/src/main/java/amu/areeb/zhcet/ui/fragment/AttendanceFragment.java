package amu.areeb.zhcet.ui.fragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import amu.areeb.zhcet.attendance.AttendanceAdapter;
import amu.areeb.zhcet.attendance.AttendanceGetter;
import amu.areeb.zhcet.ui.MainActivity;
import amu.areeb.zhcet.R;
import amu.areeb.zhcet.utils.Utils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.graphics.*;
import android.view.*;
import java.io.*;
import java.util.List;

import android.content.*;
import android.net.*;
import android.animation.*;
import amu.areeb.zhcet.model.Attendance;
import amu.areeb.zhcet.utils.*;

public class AttendanceFragment extends Fragment
{

	private ProgressDialog pd;
	private RecyclerView rv;
	private List<Attendance> list;
	private AttendanceAdapter mAdapter;
	private LinearLayoutManager llm;
	private LinearLayout emptyList;
	private FloatingActionButton fab;
	private View v;
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		v = inflater.inflate(R.layout.fragment_attendance, container, false);
		
		emptyList = (LinearLayout) v.findViewById(R.id.empty);
		ImageView bin = (ImageView) emptyList.findViewById(R.id.bin);
		GradientDrawable circle = new GradientDrawable();
		circle.setCornerRadius(bin.getHeight()+500);
		circle.setColor(Color.parseColor("#33aaaaaa"));
		if(Build.VERSION.SDK_INT>=16)
			bin.setBackground(circle);
		else
			bin.setBackgroundDrawable(circle);
		
		rv = (RecyclerView) v.findViewById(R.id.recycler);
		rv.setHasFixedSize(true);
		llm = new LinearLayoutManager(getActivity());
		rv.setLayoutManager(llm);
		rv.setItemAnimator(new DefaultItemAnimator());
		
		list = new ArrayList<Attendance>();
		
		mAdapter = new AttendanceAdapter(getActivity(), list);
		rv.setAdapter(mAdapter);
		Snackbar.make(rv, "Click on + above to enter Faculty Number", Snackbar.LENGTH_LONG).show();
		
		pd = new ProgressDialog(getActivity());
		pd.setMessage("Please wait...");
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(false);
		
		fab = (FloatingActionButton) v.findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					fab(list, mAdapter);
				}
		});
		
		
		load();
		setHasOptionsMenu(true);
		return v;
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        //inflater.inflate(R.menu.attendance, menu);
		MenuItem share = menu.add("Share");
		share.setIcon(R.drawable.ic_share);
		if(Build.VERSION.SDK_INT >= 11)
			share.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1)
				{
					// TODO: Implement this method
					pd.show();
					new ShareAttendance().execute();
					return false;
				}
			});
    }
	
	public void fab(List<Attendance> list, AttendanceAdapter mAdapter){
		//Toast.makeText(getActivity(), Attendance.COLORS[(int)(Math.random()*Attendance.COLORS.length)], Toast.LENGTH_SHORT).show();
		
		final TextInputLayout til = new TextInputLayout(getActivity());
		til.setHint("Enter your Faculty Number");
		final AppCompatEditText edt = new AppCompatEditText(getActivity());
		til.addView(edt);
		til.setPadding(20,60,20,20);
		edt.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(9)});
		if(aGetter!=null)
			edt.setText(aGetter.getFacultyNumber());
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
		alert.setView(til);

		alert.setPositiveButton("Get", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Left for rotting

				}


			});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					p1.dismiss();
				}


			});
		final AlertDialog dialog = alert.create();
		dialog.show();
		
		edt.setOnKeyListener(new View.OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event){
					if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
						//if the enter key was pressed, then hide the keyboard and do whatever needs doing.
						dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
						return true;
					} /*else if (keyCode == KeyEvent.KEYCODE_BACK) {
					 MainActivity.this.finish();
					 }*/
					return false;
				}

			});
		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#333333"));
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					String feed = edt.getText().toString().trim();
					if(!Utils.isFacultyNumber(feed)){
						til.setError("Invalid Faculty Number");
					} else{
						getAttendance(feed);
						InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(p1.getWindowToken(), 0);
						dialog.dismiss();
					}
				}
		});
	}
	
	File ss;
	public void sendBitmap(Bitmap b){
		ss = new File(getContext().getFilesDir(), "attendance.png");
		ss.setReadable(true, false);
		try
		{
			FileOutputStream fOut = new FileOutputStream(ss);
			b.compress(Bitmap.CompressFormat.PNG, 50, fOut);
			fOut.flush();
			fOut.close();
			//ss.setReadable(false);
		}
		catch (Exception e)
		{}
	}
	
	public void createChooser(){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(ss));
		intent.setType("image/png");
		try{
			startActivity((Intent.createChooser(intent, "Share Attendance")));
		} catch(ActivityNotFoundException e){

		}
	}
	
	
	
	public String getDetail(String facNo){
		String streams[] = {"PEB", "Computer", "LEB", "Electronics", "EEB", "Electrical", "MEB", "Mechanical", "CEB", "Civil", "KEB", "Chemical", "PKB", "Petrochemical"};
		String stream = facNo.substring(2, 5);
		int index = Arrays.asList(streams).indexOf(stream);
		stream = index==-1?stream:streams[index+1];
		int year = Utils.getSmallYear() - Integer.parseInt(facNo.substring(0, 2));
		
		if(Utils.getMonth()>Calendar.AUGUST)
			year++;
		String post = "st";
		
		switch(year){
			case 1:
				post = "st";
				break;
			case 2:
				post = "nd";
				break;
			case 3:
				post = "rd";
				break;
			case 4:
				post = "th";
		}
		
		return facNo + " " + year + post + " Year" + " " + stream;
	}

	
	AttendanceGetter aGetter;
	
	public void getAttendance(String fac_no){
		aGetter = new AttendanceGetter(fac_no);
		pd.show();
		ConnectivityManager conn = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = conn.getActiveNetworkInfo();
		if (net != null){
			new GetAttendance().execute(fac_no);
		} else {
			pd.dismiss();
			Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
		}
	}
	
	Random rnd = new Random(Attendance.COLORS.length);

	@SuppressLint("NewApi")
	private void entry(final List<Attendance> att){
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
			manageAttendance(att);
			return;
		}
		int cx = v.getRight(), cy = v.getBottom();
		final int to = Color.parseColor(Attendance.COLORS[rnd.getNext()]);
		v.setBackgroundColor(to);
		v.bringToFront();
		float radius = (float) Math.hypot(v.getMeasuredHeight(),v.getMeasuredWidth());
		Animator anim = ViewAnimationUtils.createCircularReveal(v,cx,cy,0,radius);
		anim.setDuration(700);
		anim.addListener(new Animator.AnimatorListener(){

				@Override
				public void onAnimationStart(Animator p1)
				{
					// TODO: Implement this method
					emptyList.setVisibility(View.GONE);
					list.clear();
					mAdapter.notifyDataSetChanged();
					mAdapter.resetAnimation();
				}

				@Override
				public void onAnimationEnd(Animator p1)
				{
					// TODO: Implement this method
					//list.clear();
					//mAdapter.notifyDataSetChanged();
					for(Attendance a : att){
					 	list.add(a);
					 	mAdapter.notifyItemInserted(list.size());
					 }
					 
					 ValueAnimator va = ObjectAnimator.ofInt(v, "backgroundColor", to, Color.parseColor("#eeeeee"));
					 va.setEvaluator(new ArgbEvaluator());
					 va.setRepeatCount(0);
					 va.setRepeatMode(ValueAnimator.REVERSE);
					 va.setDuration(500);
					 va.start();
					 
					try{
						Snackbar.make(rv, aGetter.getName(), Snackbar.LENGTH_SHORT).show();
						((MainActivity) getActivity()).setUsername(aGetter.getName());
						((MainActivity) getActivity()).setDetail(getDetail(aGetter.getFacultyNumber()));
						save();
					} catch (NullPointerException e){}
				}

				@Override
				public void onAnimationCancel(Animator p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationRepeat(Animator p1)
				{
					// TODO: Implement this method
				}

			
		});
		anim.start();
	}
	
	public void manageAttendance(List<Attendance> att){
		emptyList.setVisibility(View.GONE);
		list.clear();
		mAdapter.notifyDataSetChanged();
		mAdapter.resetAnimation();
		for(Attendance a : att){
			list.add(a);
			mAdapter.notifyItemInserted(list.size());
		}

		try{
			Snackbar.make(rv, aGetter.getName(), Snackbar.LENGTH_SHORT).show();
			((MainActivity) getActivity()).setUsername(aGetter.getName());
			((MainActivity) getActivity()).setDetail(getDetail(aGetter.getFacultyNumber()));
			save();
		} catch (NullPointerException e){
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
		
			
		//sendBitmap(getRecyclerViewScreenshot(rv));
		//rv.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.attended).setVisibility(View.GONE);
	}
	
	public void load(){
		try
		{
			FileInputStream fis = getActivity().openFileInput("attendance.db");
			ObjectInputStream o = new ObjectInputStream(fis);
			aGetter = (AttendanceGetter) o.readObject();
			//entry(aGetter.getAttendance());
			manageAttendance(aGetter.getAttendance());
			fis.close();
		}
		catch (FileNotFoundException e)
		{
			
		}
		catch (IOException e){
			
		}
		catch (ClassNotFoundException e){
			
		}
		catch (ClassCastException e){
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void save(){
		try
		{
			FileOutputStream fos = getActivity().openFileOutput("attendance.db", Context.MODE_PRIVATE);
			ObjectOutputStream o = new ObjectOutputStream(fos);
			o.writeObject(aGetter);
			fos.close();
		}
		catch (FileNotFoundException e)
		{
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
		catch (IOException e){
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
		catch (NullPointerException e){
			
		}
		
	}
	
	public Bitmap getRecyclerViewScreenshot(RecyclerView view) {
		int size = view.getAdapter().getItemCount();
		RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
		view.getAdapter().onBindViewHolder(holder, 0);
		holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
								View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
		Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth() + 20, holder.itemView.getMeasuredHeight() * size + size*10 + 70,
											   Bitmap.Config.ARGB_8888);
		Canvas bigCanvas = new Canvas(bigBitmap);
		bigCanvas.drawColor(Color.parseColor("#eeeeee"));
		Paint paint = new Paint();
		Paint paintText = new Paint();
		paintText.setColor(Color.parseColor("#333333"));
		paintText.setTextAlign(Paint.Align.CENTER);
		paintText.setLinearText(true);
		paintText.setAntiAlias(true);
		paintText.setTextSize(50);
		//paint.setColor(Color.parseColor("#333333"));
		int iHeight = 70;
		bigCanvas.drawText(aGetter.getName(), view.getMeasuredWidth()/2+10, 50, paintText);
		holder.itemView.setDrawingCacheEnabled(true);
		holder.itemView.buildDrawingCache();
		bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 10f, iHeight, paint);
		holder.itemView.setDrawingCacheEnabled(false);
		holder.itemView.destroyDrawingCache();
		iHeight += holder.itemView.getMeasuredHeight()+10;
		for (int i = 1; i < size; i++) {
			view.getAdapter().onBindViewHolder(holder, i);
			holder.itemView.setDrawingCacheEnabled(true);
			holder.itemView.buildDrawingCache();
			bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 10f, iHeight, paint);
			iHeight += holder.itemView.getMeasuredHeight()+10;
			holder.itemView.setDrawingCacheEnabled(false);
			holder.itemView.destroyDrawingCache();
		}
		return bigBitmap;
	}
	
	private class GetAttendance extends AsyncTask<String, Void, String>
	{

        @Override
        protected String doInBackground(String... strings) {
            return aGetter.getAttendanceResult();
        }

        @Override
        protected void onPostExecute(String s)
		{
            super.onPostExecute(s);
			pd.dismiss();
			if(s.contains("Success")){
				entry(aGetter.getAttendance());
				//manageAttendance(aGetter.getAttendance());
			} else
				Toast.makeText(getActivity(), aGetter.getError(), Toast.LENGTH_SHORT).show();
        }
	}
	
	private class ShareAttendance extends AsyncTask<Void, Void, Void>
	{
        @Override
        protected Void doInBackground(Void... strings) {
			sendBitmap(getRecyclerViewScreenshot(rv));
			return null;
		}

        @Override
        protected void onPostExecute(Void v)
		{
            super.onPostExecute(null);
			pd.dismiss();
			createChooser();
        }
	}
}
