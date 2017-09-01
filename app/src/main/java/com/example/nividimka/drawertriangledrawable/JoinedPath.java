package com.example.nividimka.drawertriangledrawable;

import android.graphics.Path;
import android.graphics.PathMeasure;

public class JoinedPath {

    private final PathMeasure measureFirst;
    private final PathMeasure measureSecond;
    private final float lengthFirst;
    private final float lengthSecond;

    public JoinedPath(Path pathFirst, Path pathSecond) {
        measureFirst = new PathMeasure(pathFirst, false);
        measureSecond = new PathMeasure(pathSecond, false);
        lengthFirst = measureFirst.getLength();
        lengthSecond = measureSecond.getLength();
    }

    public void getPointOnLine(float percent, float[] coords) {
        if (percent <= .5f) {
            percent *= 2;
            measureFirst.getPosTan(lengthFirst * percent, coords, null);
        } else {
            percent -= .5f;
            percent *= 2;
            measureSecond.getPosTan(lengthSecond * percent, coords, null);
        }
    }
}