package amu.areeb.zhcet.ui.fragment;

import amu.areeb.zhcet.R;
import amu.areeb.zhcet.attendance.AttendanceAdapter;
import amu.areeb.zhcet.attendance.AttendanceGetter;
import amu.areeb.zhcet.model.Attendance;
import amu.areeb.zhcet.ui.MainActivity;
import amu.areeb.zhcet.utils.Random;
import amu.areeb.zhcet.utils.ShareUtil;
import amu.areeb.zhcet.utils.Utils;
import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AttendanceFragment extends Fragment {

    private AttendanceGetter aGetter;
    private Random rnd = new Random(Attendance.COLORS.length);
    private ProgressDialog pd;
    private RecyclerView rv;
    private List<Attendance> list;
    private AttendanceAdapter mAdapter;
    private LinearLayout emptyList;
    private View v;

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_attendance, container, false);

        emptyList = (LinearLayout) v.findViewById(R.id.empty);
        ImageView bin = (ImageView) emptyList.findViewById(R.id.bin);
        GradientDrawable circle = new GradientDrawable();
        circle.setCornerRadius(bin.getHeight() + 500);
        circle.setColor(Color.parseColor("#33aaaaaa"));
        if (Build.VERSION.SDK_INT >= 16)
            bin.setBackground(circle);
        else
            bin.setBackgroundDrawable(circle);

        rv = (RecyclerView) v.findViewById(R.id.recycler);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());

        list = new ArrayList<>();

        mAdapter = new AttendanceAdapter(getActivity(), list);
        rv.setAdapter(mAdapter);
        Snackbar.make(rv, "Click on + above to enter Faculty Number", Snackbar.LENGTH_LONG).show();

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                fab();
            }
        });

        aGetter = Utils.load(getActivity());
        if (aGetter != null)
            manageAttendance(aGetter.getAttendance());

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.attendance, menu);
        final MenuItem share = menu.add("Share");
        share.setIcon(R.drawable.ic_share);
        if (Build.VERSION.SDK_INT >= 11)
            share.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem p1) {
                ShareUtil shareUtil = new ShareUtil(getContext(), rv);
                shareUtil.setName(aGetter.getName());
                shareUtil.openShareDialog(pd);
                return false;
            }
        });
    }

    private void fab() {
        final TextInputLayout til = new TextInputLayout(getActivity());
        til.setHint("Enter your Faculty Number");
        final AppCompatEditText edt = new AppCompatEditText(getActivity());
        til.addView(edt);
        til.setPadding(20, 60, 20, 20);
        edt.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(9)});
        if (aGetter != null)
            edt.setText(aGetter.getFacultyNumber());
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        alert.setView(til);

        alert.setPositiveButton("Get", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
            }

        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                p1.dismiss();
            }


        });
        final AlertDialog dialog = alert.create();
        dialog.show();

        edt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                    return true;
                }
                return false;
            }

        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#333333"));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                String feed = edt.getText().toString().trim();
                if (!Utils.isFacultyNumber(feed)) {
                    til.setError("Invalid Faculty Number");
                } else {
                    getAttendance(feed);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(p1.getWindowToken(), 0);
                    dialog.dismiss();
                }
            }
        });
    }

    public void getAttendance(String fac_no) {
        aGetter = new AttendanceGetter(fac_no);
        pd.show();
        ConnectivityManager conn = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        if (net != null) {
            new GetAttendance().execute(fac_no);
        } else {
            pd.dismiss();
            Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    private void entry(final List<Attendance> att) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            manageAttendance(att);
            return;
        }
        int cx = v.getRight(), cy = v.getBottom();
        final int to = Color.parseColor(Attendance.COLORS[rnd.getNext()]);
        v.setBackgroundColor(to);
        v.bringToFront();
        float radius = (float) Math.hypot(v.getMeasuredHeight(), v.getMeasuredWidth());
        Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
        anim.setDuration(700);
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator p1) {
                emptyList.setVisibility(View.GONE);
                list.clear();
                mAdapter.notifyDataSetChanged();
                mAdapter.resetAnimation();
            }

            @Override
            public void onAnimationEnd(Animator p1) {
                for (Attendance a : att) {
                    list.add(a);
                    mAdapter.notifyItemInserted(list.size());
                }

                ValueAnimator va = ObjectAnimator.ofInt(v, "backgroundColor", to, Color.parseColor("#eeeeee"));
                va.setEvaluator(new ArgbEvaluator());
                va.setRepeatCount(0);
                va.setRepeatMode(ValueAnimator.REVERSE);
                va.setDuration(500);
                va.start();

                try {
                    Snackbar.make(rv, aGetter.getName(), Snackbar.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).setUsername(aGetter.getName());
                    ((MainActivity) getActivity()).setDetail(Utils.getDetail(aGetter.getFacultyNumber()));
                    Utils.save(getContext(), aGetter);
                } catch (NullPointerException e) {
                }
            }

            @Override
            public void onAnimationCancel(Animator p1) {
            }

            @Override
            public void onAnimationRepeat(Animator p1) {
            }

        });
        anim.start();
    }

    public void manageAttendance(List<Attendance> att) {
        emptyList.setVisibility(View.GONE);
        list.clear();
        mAdapter.notifyDataSetChanged();
        mAdapter.resetAnimation();
        for (Attendance a : att) {
            list.add(a);
            mAdapter.notifyItemInserted(list.size());
        }

        try {
            Snackbar.make(rv, aGetter.getName(), Snackbar.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).setUsername(aGetter.getName());
            ((MainActivity) getActivity()).setDetail(Utils.getDetail(aGetter.getFacultyNumber()));
            Utils.save(getContext(), aGetter);
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private class GetAttendance extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return aGetter.getAttendanceResult();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s.contains("Success")) {
                entry(aGetter.getAttendance());
            } else
                Toast.makeText(getActivity(), aGetter.getError(), Toast.LENGTH_SHORT).show();
        }
    }

}
