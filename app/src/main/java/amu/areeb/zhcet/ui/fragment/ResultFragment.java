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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amu.areeb.zhcet.R;
import amu.areeb.zhcet.adapter.ResultAdapter;
import amu.areeb.zhcet.api.StudentService;
import amu.areeb.zhcet.model.Result;
import amu.areeb.zhcet.model.StudentResult;
import amu.areeb.zhcet.ui.MainActivity;
import amu.areeb.zhcet.utils.Random;
import amu.areeb.zhcet.utils.ShareUtil;
import amu.areeb.zhcet.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultFragment extends Fragment implements Callback<StudentResult> {

    private Random rnd = new Random(Utils.COLORS.length);
    private ProgressDialog pd;
    private RecyclerView rv;
    private ResultAdapter mAdapter;
    private StudentResult studentResult;
    private List<Result> resultList = new ArrayList<>();
    private LinearLayout emptyList, hidden;
    private View root;

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_result, container, false);

        emptyList = (LinearLayout) root.findViewById(R.id.empty);
        ImageView bin = (ImageView) emptyList.findViewById(R.id.bin);
        GradientDrawable circle = new GradientDrawable();
        circle.setCornerRadius(bin.getHeight() + 500);
        circle.setColor(Color.parseColor("#33aaaaaa"));

        hidden = (LinearLayout) root.findViewById(R.id.hidden);
        hidden.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= 16)
            bin.setBackground(circle);
        else
            bin.setBackgroundDrawable(circle);

        rv = (RecyclerView) root.findViewById(R.id.recycler);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ResultAdapter(getActivity(), resultList);
        rv.setAdapter(mAdapter);
        Snackbar.make(rv, "Click on + above to enter Faculty Number and Enrolment Number", Snackbar.LENGTH_LONG).show();

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

        studentResult = Utils.loadResult(getActivity());
        if (studentResult != null)
            manageResult(studentResult.results);

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
                if (studentResult != null) {
                    ShareUtil shareUtil = new ShareUtil(getContext(), rv);
                    shareUtil.setName(studentResult.name);
                    shareUtil.openShareDialog(pd);
                }
                return false;
            }
        });
    }

    private void fab() {
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);

        final TextInputLayout fac_til = new TextInputLayout(getActivity());
        fac_til.setHint("Enter your Faculty Number");
        final AppCompatEditText fac_edt = new AppCompatEditText(getActivity());
        fac_til.addView(fac_edt);
        fac_til.setPadding(20, 60, 20, 20);

        final TextInputLayout en_til = new TextInputLayout(getActivity());
        en_til.setHint("Enter your Enrolment Number");
        final AppCompatEditText en_edt = new AppCompatEditText(getActivity());
        en_til.addView(en_edt);
        en_til.setPadding(20, 60, 20, 20);

        ll.addView(fac_til);
        ll.addView(en_til);

        fac_edt.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(9)});
        en_edt.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(6)});
        if (studentResult != null) {
            fac_edt.setText(studentResult.faculty_number);
            en_edt.setText(studentResult.enrolment);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        alert.setView(ll);

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

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#333333"));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                String fac = fac_edt.getText().toString().trim();
                String en = en_edt.getText().toString().trim();
                if (!Utils.isFacultyNumber(fac)) {
                    fac_til.setError("Invalid Faculty Number");
                }

                if (!Utils.isEnrolmentNumber(en)) {
                    en_til.setError("Invalid Enrolment Number");
                }

                if (Utils.isEnrolmentNumber(en) && Utils.isFacultyNumber(fac)) {
                    getResult(fac, en);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(p1.getWindowToken(), 0);
                    dialog.dismiss();
                }
            }
        });
    }

    public void getResult(String fac_no, String en_no) {
        pd.show();
        ConnectivityManager conn = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        if (net != null) {
            StudentService.getResultCall(fac_no, en_no).enqueue(this);
        } else {
            pd.dismiss();
            Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    private void entry(final List<Result> results) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            manageResult(results);
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
                hidden.setVisibility(View.VISIBLE);
                resultList.clear();
                mAdapter.notifyDataSetChanged();
                mAdapter.resetAnimation();
            }

            @Override
            public void onAnimationEnd(Animator p1) {

                TextView name = (TextView) hidden.findViewById(R.id.name);
                TextView fac = (TextView) hidden.findViewById(R.id.fac);
                TextView enrolment = (TextView) hidden.findViewById(R.id.enrolment);
                TextView cpi = (TextView) hidden.findViewById(R.id.cpi);
                TextView spi = (TextView) hidden.findViewById(R.id.spi);
                TextView credits = (TextView) hidden.findViewById(R.id.credits);

                name.setText(studentResult.name);
                fac.setText(studentResult.faculty_number);
                enrolment.setText(studentResult.enrolment);
                cpi.setText("CPI : " + studentResult.cpi);
                spi.setText("SPI : " + studentResult.spi);
                credits.setText("Credits : " + studentResult.ec);

                for (Result r : results) {
                    resultList.add(r);
                    mAdapter.notifyItemInserted(resultList.size());
                }

                ValueAnimator va = ObjectAnimator.ofInt(root, "backgroundColor", to, Color.parseColor("#eeeeee"));
                va.setEvaluator(new ArgbEvaluator());
                va.setRepeatCount(0);
                va.setRepeatMode(ValueAnimator.REVERSE);
                va.setDuration(500);
                va.start();

                try {
                    Snackbar.make(rv, studentResult.name, Snackbar.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).setUsername(studentResult.name);
                    ((MainActivity) getActivity()).setDetail(Utils.getDetail(studentResult.faculty_number));
                    Utils.saveResult(getContext(), studentResult);
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

    private void manageResult(List<Result> results) {
        emptyList.setVisibility(View.GONE);
        hidden.setVisibility(View.VISIBLE);
        resultList.clear();

        TextView name = (TextView) hidden.findViewById(R.id.name);
        TextView fac = (TextView) hidden.findViewById(R.id.fac);
        TextView enrolment = (TextView) hidden.findViewById(R.id.enrolment);
        TextView cpi = (TextView) hidden.findViewById(R.id.cpi);
        TextView spi = (TextView) hidden.findViewById(R.id.spi);
        TextView credits = (TextView) hidden.findViewById(R.id.credits);

        name.setText(studentResult.name);
        fac.setText(studentResult.faculty_number);
        enrolment.setText(studentResult.enrolment);
        cpi.setText("CPI : " + studentResult.cpi);
        spi.setText("SPI : " + studentResult.spi);
        credits.setText("Credits : " + studentResult.ec);

        mAdapter.notifyDataSetChanged();
        mAdapter.resetAnimation();
        for (Result r : results) {
            resultList.add(r);
            mAdapter.notifyItemInserted(resultList.size());
        }

        try {
            Snackbar.make(rv, studentResult.name, Snackbar.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).setUsername(studentResult.name);
            ((MainActivity) getActivity()).setDetail(Utils.getDetail(studentResult.faculty_number));
            Utils.saveResult(getContext(), studentResult);
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResponse(Call<StudentResult> call, Response<StudentResult> response) {
        pd.dismiss();
        studentResult = response.body();
        if (!studentResult.error) {
            entry(studentResult.results);
        } else
            Toast.makeText(getActivity(), studentResult.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call<StudentResult> call, Throwable t) {
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
