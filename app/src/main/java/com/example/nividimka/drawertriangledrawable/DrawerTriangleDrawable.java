package com.example.nividimka.drawertriangledrawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;

import static android.graphics.Color.BLACK;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.Cap.BUTT;
import static android.graphics.Paint.Cap.ROUND;
import static android.graphics.Paint.SUBPIXEL_TEXT_FLAG;
import static android.graphics.Paint.Style.STROKE;
import static android.graphics.PixelFormat.TRANSLUCENT;
import static android.support.v4.widget.DrawerLayout.DrawerListener;

/**
 * A drawable that rotates between a drawer icon and a triangle based on animatePercent.
 */
public class DrawerTriangleDrawable extends Drawable {


    private final static float DIMEN_DP = 23.5f;

    private final static float STROKE_WIDTH_DP = 2;

    private BridgingLine[] lines;

    private final Rect bounds;
    private final Paint linePaint;
    private boolean rounded;

    private boolean flip;
    private float animatePercent;

    private final float density;
    private final float halfStrokeWidthPixel;

    public DrawerTriangleDrawable(Resources resources) {
        density = resources.getDisplayMetrics().density;
        float strokeWidthPixel = STROKE_WIDTH_DP * density;
        halfStrokeWidthPixel = strokeWidthPixel / 2;
        linePaint = new Paint(SUBPIXEL_TEXT_FLAG | ANTI_ALIAS_FLAG);
        linePaint.setStrokeCap(rounded ? ROUND : BUTT);
        linePaint.setColor(BLACK);
        linePaint.setStyle(STROKE);
        linePaint.setStrokeWidth(strokeWidthPixel);
        int dimen = (int) (DIMEN_DP * density);
        bounds = new Rect(0, 0, dimen, dimen);
        lines = BridgingLine.getArrowToTriangle(density, isRounded(), halfStrokeWidthPixel);
    }


    @Override
    public int getIntrinsicHeight() {
        return bounds.height();
    }

    @Override
    public int getIntrinsicWidth() {
        return bounds.width();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (flip) {
            canvas.save();
            canvas.scale(1f, -1f, getIntrinsicWidth() / 2, getIntrinsicHeight() / 2);
        }
        for (int i = 0; i < lines.length; i++) {
            lines[i].draw(canvas, animatePercent, linePaint);
        }

        if (flip) canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        linePaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        linePaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return TRANSLUCENT;
    }

    public void setStrokeColor(int color) {
        linePaint.setColor(color);
        invalidateSelf();
    }

    /**
     * Sets the rotation of this drawable based on {@code animatePercent} between 0 and 1. Usually driven
     * via {@link DrawerListener#onDrawerSlide(View, float)}'s {@code slideOffset} animatePercent.
     */
    public void setAnimatePercent(float animatePercent) {
        if (animatePercent > 1 || animatePercent < 0) {
            throw new IllegalArgumentException("Value must be between 1 and zero inclusive!");
        }
        this.animatePercent = animatePercent;
        invalidateSelf();
    }

    /**
     * When false, rotates from 3 o'clock to 9 o'clock between a drawer icon and a back arrow.
     * When true, rotates from 9 o'clock to 3 o'clock between a back arrow and a drawer icon.
     */
    public void setFlip(boolean flip) {
        this.flip = flip;
        invalidateSelf();
    }

    public boolean isRounded() {
        return rounded;
    }

    public void setRounded(boolean rounded) {
        this.rounded = rounded;
        lines = BridgingLine.getArrowToTriangle(density, isRounded(), halfStrokeWidthPixel);
        linePaint.setStrokeCap(rounded ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        invalidateSelf();
    }

    /**
     * Scales the paths to the given screen density. If the density matches the
     * {@link DrawerTriangleDrawable#PATH_GEN_DENSITY}, no scaling needs to be done.
     */
}