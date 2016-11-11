package amu.areeb.zhcet.utils;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class ShareUtil {
    private File ss;
    private Context context;
    private RecyclerView rv;
    private String name;
    private ProgressDialog pd;

    public ShareUtil(Context context, RecyclerView rv) {
        this.context = context;
        this.rv = rv;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sendBitmap(Bitmap b) {
        ss = new File(context.getFilesDir(), "attendance.png");
        ss.setReadable(true, false);
        try {
            FileOutputStream fOut = new FileOutputStream(ss);
            b.compress(Bitmap.CompressFormat.PNG, 50, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
        }
    }

    public void createChooser() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(ss));
        intent.setType("image/png");
        try {
            context.startActivity((Intent.createChooser(intent, "Share Attendance")));
        } catch (ActivityNotFoundException e) {

        }
    }

    public Bitmap getRecyclerViewScreenshot(RecyclerView view) {
        int size = view.getAdapter().getItemCount();
        RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
        view.getAdapter().onBindViewHolder(holder, 0);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
        Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth() + 20, holder.itemView.getMeasuredHeight() * size + size * 10 + 70,
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
        int iHeight = 70;
        bigCanvas.drawText(name, view.getMeasuredWidth() / 2 + 10, 50, paintText);
        holder.itemView.setDrawingCacheEnabled(true);
        holder.itemView.buildDrawingCache();
        bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 10f, iHeight, paint);
        holder.itemView.setDrawingCacheEnabled(false);
        holder.itemView.destroyDrawingCache();
        iHeight += holder.itemView.getMeasuredHeight() + 10;
        for (int i = 1; i < size; i++) {
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();
            bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 10f, iHeight, paint);
            iHeight += holder.itemView.getMeasuredHeight() + 10;
            holder.itemView.setDrawingCacheEnabled(false);
            holder.itemView.destroyDrawingCache();
        }
        return bigBitmap;
    }

    public void openShareDialog(ProgressDialog pd) {
        this.pd = pd;
        pd.show();
        new ShareAttendance().execute();
    }

    private class ShareAttendance extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... strings) {
            sendBitmap(getRecyclerViewScreenshot(rv));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(null);
            pd.dismiss();
            createChooser();
        }
    }
}
