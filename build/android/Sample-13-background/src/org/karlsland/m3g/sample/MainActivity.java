package org.karlsland.m3g.sample;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity {
	private GLSurfaceView view;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GLSurfaceView(this);
        view.setRenderer(new M3GRenderer());
        setContentView(view);
    }
}