package amu.areeb.zhcet.ui.fragment;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amu.areeb.zhcet.R;
import amu.areeb.zhcet.adapter.AttendanceAdapter;
import amu.areeb.zhcet.api.StudentService;
import amu.areeb.zhcet.model.Attendance;
import amu.areeb.zhcet.model.StudentAttendance;
import amu.areeb.zhcet.ui.MainActivity;
import amu.areeb.zhcet.utils.Random;
import amu.areeb.zhcet.utils.ShareUtil;
import amu.areeb.zhcet.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceFragment extends Fragment implements Callback<StudentAttendance> {

    private Random rnd = new Random(Utils.COLORS.length);
    private ProgressDialog pd;
    private RecyclerView rv;
    private AttendanceAdapter mAdapter;
    private StudentAttendance studentAttendance;
    private List<Attendance> attendanceList = new ArrayList<>();
    private LinearLayout emptyList;
    private View root;

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_attendance, container, false);

        emptyList = (LinearLayout) root.findViewById(R.id.empty);
        ImageView bin = (ImageView) emptyList.findViewById(R.id.bin);
        GradientDrawable circle = new GradientDrawable();
        circle.setCornerRadius(bin.getHeight() + 500);
        circle.setColor(Color.parseColor("#33aaaaaa"));
        if (Build.VERSION.SDK_INT >= 16)
            bin.setBackground(circle);
        else
            bin.setBackgroundDrawable(circle);

        rv = (RecyclerView) root.findViewById(R.id.recycler);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new AttendanceAdapter(getActivity(), attendanceList);
        rv.setAdapter(mAdapter);
        Snackbar.make(rv, "Click on + above to enter Faculty Number", Snackbar.LENGTH_LONG).show();

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                fab();
            }
        });

        studentAttendance = Utils.loadAttendance(getActivity());
        if (studentAttendance != null)
            manageAttendance(studentAttendance.attendance);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.attendance, menu);
        final MenuItem share = menu.add("Share");
        share.setIcon(R.drawable.vector_share);
        if (Build.VERSION.SDK_INT >= 11)
            share.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem p1) {
                if (studentAttendance != null) {
                    ShareUtil shareUtil = new ShareUtil(getContext(), rv);
                    shareUtil.setName(studentAttendance.name);
                    shareUtil.openShareDialog(pd);
                }
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
        if (studentAttendance != null)
            edt.setText(studentAttendance.fac);
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
                getAttendance(feed);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(p1.getWindowToken(), 0);
                dialog.dismiss();
            }
        });
    }

    public void getAttendance(String fac_no) {
        pd.show();
        ConnectivityManager conn = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        if (net != null) {
            StudentService.getAttendanceCall(fac_no).enqueue(this);
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
        int cx = root.getRight();
        int cy = root.getBottom();
        final int to = Color.parseColor(Utils.COLORS[rnd.getNext()]);
        root.setBackgroundColor(to);
        root.bringToFront();
        float radius = (float) Math.hypot(root.getMeasuredHeight(), root.getMeasuredWidth());
        Animator anim = ViewAnimationUtils.createCircularReveal(root, cx, cy, 0, radius);
        anim.setDuration(700);
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator p1) {
                emptyList.setVisibility(View.GONE);
                attendanceList.clear();
                mAdapter.notifyDataSetChanged();
                mAdapter.resetAnimation();
            }

            @Override
            public void onAnimationEnd(Animator p1) {
                for (Attendance a : att) {
                    attendanceList.add(a);
                    mAdapter.notifyItemInserted(attendanceList.size());
                }

                ValueAnimator va = ObjectAnimator.ofInt(root, "backgroundColor", to, Color.parseColor("#eeeeee"));
                va.setEvaluator(new ArgbEvaluator());
                va.setRepeatCount(0);
                va.setRepeatMode(ValueAnimator.REVERSE);
                va.setDuration(500);
                va.start();

                try {
                    Snackbar.make(rv, studentAttendance.name, Snackbar.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).setUsername(studentAttendance.name);
                    ((MainActivity) getActivity()).setDetail(Utils.getDetail(studentAttendance.fac));
                    Utils.saveAttendance(getContext(), studentAttendance);
                } catch (NullPointerException e) { }
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
        attendanceList.clear();
        mAdapter.notifyDataSetChanged();
        mAdapter.resetAnimation();
        for (Attendance a : att) {
            attendanceList.add(a);
            mAdapter.notifyItemInserted(attendanceList.size());
        }

        try {
            Snackbar.make(rv, studentAttendance.name, Snackbar.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).setUsername(studentAttendance.name);
            ((MainActivity) getActivity()).setDetail(Utils.getDetail(studentAttendance.fac));
            Utils.saveAttendance(getContext(), studentAttendance);
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResponse(Call<StudentAttendance> call, Response<StudentAttendance> response) {
        pd.dismiss();
        studentAttendance = response.body();
        if (!studentAttendance.error) {
            entry(studentAttendance.attendance);
        } else
            Toast.makeText(getActivity(), studentAttendance.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call<StudentAttendance> call, Throwable t) {
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
