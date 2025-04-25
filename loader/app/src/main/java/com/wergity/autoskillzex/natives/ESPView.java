package com.wergity.autoskillzex.natives;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;

import android.os.Process;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;

public class ESPView extends View implements Runnable {
    private int tickA = 0;
    private int tickB = 0;

    private static int fps;
    private static long sleepTime;

    private static Paint linePaint;
    private static Paint filledPaint;
    private static Paint textPaint;

    private static final float MAX_SCREEN_WIDTH = 1440.0f;

    public Thread thread;

    public ESPView(Context context) {
        super(context, null, 0);

        initPaints(context);

        setWillNotDraw(false);
        setBackgroundColor(0);
        setFocusableInTouchMode(false);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        setFps(60);
        setAntiAlias(true);

        thread = new Thread(this);
        thread.start();
    }

    private void initPaints(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
        Rect windowBounds = windowMetrics.getBounds();

        int width = windowBounds.width();
        int height = windowBounds.height();

        float delta = Math.min(width, height) / MAX_SCREEN_WIDTH;
        float textSize = delta * 40.0f;

        linePaint = new Paint();
        linePaint.setColor(0);
        linePaint.setAlpha(255);
        linePaint.setStyle(Paint.Style.STROKE);

        filledPaint = new Paint();
        filledPaint.setColor(0);
        filledPaint.setAlpha(255);
        filledPaint.setTextSize(textSize);
        filledPaint.setStyle(Paint.Style.FILL);
        filledPaint.setTextAlign(Paint.Align.CENTER);

        textPaint = new Paint();
        textPaint.setColor(0);
        textPaint.setAlpha(255);
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(delta * 5.0f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public static void setAntiAlias(boolean enabled) {
        linePaint.setAntiAlias(enabled);
        textPaint.setAntiAlias(enabled);
        filledPaint.setAntiAlias(enabled);
    }

    private static int getColor(int position) {
        return switch (position) {
            case 0  -> Color.WHITE;
            case 1  -> Color.BLACK;
            case 2  -> Color.RED;
            case 3  -> Color.YELLOW;
            case 4  -> Color.GREEN;
            case 5  -> Color.CYAN;
            case 6  -> Color.BLUE;
            case 7  -> Color.MAGENTA;
            default -> Color.TRANSPARENT;
        };
    }

    public static void line(@NonNull Canvas cvs, int color, float width, float fx, float fy, float tx, float ty) {
        linePaint.setColor(getColor(color));
        linePaint.setStrokeWidth(width);

        cvs.drawLine(fx, fy, tx, ty, linePaint);
    }

    public static void text(Canvas cvs, int color, String text, float x, float y) {
        y += 2.5f;
        y += textPaint.getTextSize();

        cvs.drawText(text, x, y, textPaint);
        filledPaint.setColor(getColor(color));
        cvs.drawText(text, x, y, filledPaint);
    }

    public static void setFps(int value) {
        fps = value;
        sleepTime = 1000 / value;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        Daemon.signal(this, canvas);

        tickA++;
        if (tickA > fps) {
            tickA = 0;
        }
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        while (!thread.isInterrupted()) {
            if (tickA != tickB) {
                tickB = tickA;
                postInvalidate();
            }

            try {
                //noinspection all
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {}
        }
    }
}