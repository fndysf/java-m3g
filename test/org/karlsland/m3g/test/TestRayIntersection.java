package org.karlsland.m3g.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.karlsland.m3g.RayIntersection;

public class TestRayIntersection {

    static {
        System.loadLibrary ("jni-opengl");
    }
    
	@Test
	public void testInitialize() {
        RayIntersection ri  = new RayIntersection ();
        float[]         ray = new float[6];
        ri.getRay (ray);

        assertEquals (0.f , ray[0], 0.00001f);
        assertEquals (0.f , ray[1], 0.00001f);
        assertEquals (0.f , ray[2], 0.00001f);
        assertEquals (0.f , ray[3], 0.00001f);
        assertEquals (0.f , ray[4], 0.00001f);
        assertEquals (1.f , ray[5], 0.00001f);
        assertEquals (null, ri.getIntersected());
        assertEquals (0.f , ri.getDistance() , 0.00001f);
        assertEquals (0   , ri.getSubmeshIndex());
        assertEquals (0.f , ri.getTextureS(0), 0.00001f);
        assertEquals (0.f , ri.getTextureT(0), 0.00001f);
        assertEquals (0.f , ri.getNormalX()  , 0.00001f);
        assertEquals (0.f , ri.getNormalY()  , 0.00001f);
        assertEquals (1.f , ri.getNormalZ()  , 0.00001f);
	}

	@Test
	public void testFinalize() {
		@SuppressWarnings("unused")
		RayIntersection ri = new RayIntersection ();
       ri = null;
       System.gc();
	}

	// 交差判定のテストはここではなく
	// 別ファイルで行う（まだない）

	@Test
	public void testToString() {
		RayIntersection ri = new RayIntersection();
		ri.toString();
	}

}
