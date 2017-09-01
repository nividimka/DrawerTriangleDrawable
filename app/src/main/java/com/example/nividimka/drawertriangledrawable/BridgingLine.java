package com.example.nividimka.drawertriangledrawable;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.nividimka.drawertriangledrawable.JoinedPath;

import static java.lang.Math.sqrt;

public class BridgingLine {
    private float param, magnitude, vX, vY;
    private final JoinedPath pathA;
    private final JoinedPath pathB;
    private final float coordsB[] = {0f, 0f};
    private final float coordsA[] = {0f, 0f};
    private final boolean rounded;
    private final float halfStrokeWidthPixel;
    private final static float PATH_GEN_DENSITY = 3;

    public BridgingLine(JoinedPath pathA, JoinedPath pathB, boolean rounded, float halfStrokeWidthPixel) {
        this.pathA = pathA;
        this.pathB = pathB;
        this.rounded = rounded;
        this.halfStrokeWidthPixel = halfStrokeWidthPixel;
    }

    public void draw(Canvas canvas, float animatePercent, Paint linePaint) {
        pathA.getPointOnLine(animatePercent, coordsA);
        pathB.getPointOnLine(animatePercent, coordsB);
        if (rounded) insetPointsForRoundCaps();
        canvas.drawLine(coordsA[0], coordsA[1], coordsB[0], coordsB[1], linePaint);
    }

    private void insetPointsForRoundCaps() {
        vX = coordsB[0] - coordsA[0];
        vY = coordsB[1] - coordsA[1];

        magnitude = (float) sqrt((vX * vX + vY * vY));
        param = halfStrokeWidthPixel / magnitude;

        coordsA[0] = coordsA[0] + (vX * param);
        coordsA[1] = coordsA[1] + (vY * param);
        coordsB[0] = coordsB[0] - (vX * param);
        coordsB[1] = coordsB[1] - (vY * param);
    }


    public static BridgingLine[] getArrowToTriangle(float density, boolean rounded, float halfStrokeWidthPixel) {
        BridgingLine[] lines = new BridgingLine[3];
        Path first, second;
        JoinedPath joinedA, joinedB;

        // Top Left
        first = new Path();
        first.moveTo(5f, 20f);
        first.rCubicTo(8.125f, -16.317f, 39.753f, -27.851f, 55f, -3f);
        second = new Path();
        second.moveTo(60f, 17f);
        second.rCubicTo(16.083f, 0f, 26.853f, 16.702f, -10f, 18f);
        scalePath(first, density);
        scalePath(second, density);
        joinedA = new JoinedPath(first, second);

        //Top Right
        first = new Path();
        first.moveTo(65f, 20f);
        first.rCubicTo(4.457f, 16.75f, 1.512f, 37.982f, -23f, 43f);
        second = new Path();
        second.moveTo(42f, 63f);
        second.rCubicTo(6.083f, 0f, 2.853f, 6.702f, -32f, -3f);
        scalePath(first, density);
        scalePath(second, density);
        joinedB = new JoinedPath(first, second);
        lines[0] = new BridgingLine(joinedA, joinedB, rounded, halfStrokeWidthPixel);

        // Middle Left
        first = new Path();
        first.moveTo(5f, 35f);
        first.rCubicTo(5.042f, 20.333f, 18.625f, 6.791f, 30f, -28f);
        second = new Path();
        second.moveTo(35f, 7f);
        second.rCubicTo(16.083f, 0f, 26.853f, 16.702f, 15f, 28f);
        scalePath(first, density);
        scalePath(second, density);
        joinedA = new JoinedPath(first, second);

        //Middle Right
        first = new Path();
        first.moveTo(65f, 35f);
        first.rCubicTo(0f, 10.926f, -8.709f, 26.416f, -30f, 26f);
        second = new Path();
        second.moveTo(35f, 61f);
        second.rCubicTo(-7.5f, 0f, -23.946f, -8.211f, -25f, -51f);
        scalePath(first, density);
        scalePath(second, density);
        joinedB = new JoinedPath(first, second);
        lines[1] = new BridgingLine(joinedA, joinedB, rounded, halfStrokeWidthPixel);

        // Bottom Left
        first = new Path();
        first.moveTo(5f, 50f);
        first.rCubicTo(2.5f, 43.312f, 0.013f, 26.546f, 5f, -33f);
        second = new Path();
        second.moveTo(10f, 17f);
        second.rCubicTo(9.462f, -9.2f, 24.188f, -10.353f, 0f, -7f);
        scalePath(first, density);
        scalePath(second, density);
        joinedA = new JoinedPath(first, second);

        // Bottom Right
        first = new Path();
        first.moveTo(65f, 50f);
        first.rCubicTo(-7.021f, 10.08f, -20.584f, 19.699f, -37f, 13f);
        second = new Path();
        second.moveTo(28f, 63f);
        second.rCubicTo(-15.723f, -6.521f, -18.8f, -23.543f, -18f, -3f);
        scalePath(first, density);
        scalePath(second, density);
        joinedB = new JoinedPath(first, second);
        lines[2] = new BridgingLine(joinedA, joinedB, rounded, halfStrokeWidthPixel);
        return lines;
    }

    private static void scalePath(Path path, float density) {
        if (density == PATH_GEN_DENSITY) return;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(density / PATH_GEN_DENSITY, density / PATH_GEN_DENSITY, 0, 0);
        path.transform(scaleMatrix);
    }
}