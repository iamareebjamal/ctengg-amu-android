package amu.areeb.zhcet.ui.fragment;
import amu.areeb.zhcet.R;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.text.TextUtils;
import java.util.Calendar;
import android.support.design.widget.FloatingActionButton;
import amu.areeb.zhcet.utils.Utils;

public class HomeFragment extends Fragment
{
	TextInputLayout facNoTil;
	AppCompatEditText facNoEdt;
	TextInputLayout enNoTil;
	AppCompatEditText enNoEdt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		
		facNoTil = (TextInputLayout) v.findViewById(R.id.facNoWrapper);
		facNoEdt = (AppCompatEditText) v.findViewById(R.id.facNo);
		facNoTil.setHint("Faculty Number");
		
		enNoTil = (TextInputLayout) v.findViewById(R.id.enNoWrapper);
		enNoEdt = (AppCompatEditText) v.findViewById(R.id.enNo);
		enNoTil.setHint("Enrolment Number");
		
		setupFields();
		
		FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_done);
		fab.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v)
				{
					// TODO: Implement this method
					setProfile(v);
				}
		});
		
		return v;
	}
	
	public void setupFields(){
		facNoEdt.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8)});
		enNoEdt.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(6)});

		facNoEdt.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event){
					if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
						//if the enter key was pressed, then hide the keyboard and do whatever needs doing.
						final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						enNoEdt.requestFocus();
						enNoEdt.postDelayed(new Runnable(){

								@Override
								public void run()
								{
									// TODO: Implement this method
									imm.showSoftInput(enNoEdt, InputMethodManager.SHOW_FORCED);

								}


							}, 1);
						return true;

					} /*else if (keyCode == KeyEvent.KEYCODE_BACK) {
					 MainActivity.this.finish();
					 }*/
					return false;
				}

			});
		enNoEdt.setOnKeyListener(new View.OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event){
					if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
						//if the enter key was pressed, then hide the keyboard and do whatever needs doing.
						setProfile(v);
						return true;
					} /*else if (keyCode == KeyEvent.KEYCODE_BACK) {
					 MainActivity.this.finish();
					 }*/
					return false;
				}

			});
	}
	
	
	
	public void setProfile(View view){
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
		String enNo = enNoEdt.getText().toString();
		String facNo = facNoEdt.getText().toString();
		facNoTil.setErrorEnabled(false);
		enNoTil.setErrorEnabled(false);
		if(!Utils.isFacultyNumber(facNo)){
			facNoTil.setError("Invalid Faculty Number");
			return;
		}
		if(!Utils.isEnrolmentNumber(enNo)){
			enNoTil.setError("Invalid Enrolment Number");
			return;
		}
	}

}
