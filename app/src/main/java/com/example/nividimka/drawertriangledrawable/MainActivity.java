package com.example.nividimka.drawertriangledrawable;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.Gravity.START;

public class MainActivity extends Activity {

    private DrawerTriangleDrawable drawerTriangleDrawable;
    private float offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ImageView imageView = findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();

        drawerTriangleDrawable = new DrawerTriangleDrawable(resources);
        drawerTriangleDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerTriangleDrawable);

        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;

                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    drawerTriangleDrawable.setFlip(true);
                } else if (slideOffset <= .005) {
                    drawerTriangleDrawable.setFlip(false);
                }

                drawerTriangleDrawable.setAnimatePercent(offset);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(START)) {
                    drawer.closeDrawer(START);
                } else {
                    drawer.openDrawer(START);
                }
            }
        });

        final TextView styleButton = findViewById(R.id.indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override
            public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.rounded) //
                        : resources.getString(R.string.squared));

                rounded = !rounded;
                drawerTriangleDrawable.setRounded(rounded);
            }
        });
    }
}