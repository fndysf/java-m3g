package org.karlsland.m3g.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.karlsland.m3g.AnimationTrack;
import org.karlsland.m3g.KeyframeSequence;
import org.karlsland.m3g.Transform;

public class TestTransformable {

    static {
        System.loadLibrary ("jni-opengl");
    }
    
	@Test
	public void testInitialize() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();
		
		float[] angleAxis   = new float[4];
		float[] scale       = new float[3];
		float[] translation = new float[3];
		Transform t         = new Transform ();
		
		trans.getScale       (scale);
		trans.getTranslation (translation);
		trans.getOrientation (angleAxis);
		trans.getTransform   (t);
	        
		assertEquals (0, angleAxis[0]  , 0.00001f);
		assertEquals (0, angleAxis[1]  , 0.00001f);  // 仕様書ではundefined.
		assertEquals (0, angleAxis[2]  , 0.00001f);  // 仕様書ではundefined.
		assertEquals (0, angleAxis[3]  , 0.00001f);  // 仕様書ではundefined.
		assertEquals (1, scale[0]      , 0.00001f);
		assertEquals (1, scale[1]      , 0.00001f);
		assertEquals (1, scale[2]      , 0.00001f);
		assertEquals (0, translation[0], 0.00001f);
		assertEquals (0, translation[1], 0.00001f);
		assertEquals (0, translation[2], 0.00001f);
		
        float[] matrix = new float[16];
        t.get (matrix);
        float[] expected = {1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1};
        
        assertArrayEquals (expected, matrix, 0.00001f);
	}
	
	@Test
	public void testFinalize() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();
		trans = null;
		System.gc();
	}


	@Test
	public void testPostRotate() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();

		float[] angleAxis = new float[4];
		trans.postRotate     (45, 1,1,0);
		trans.getOrientation (angleAxis);
		
		assertEquals (45         , angleAxis[0], 0.0001f);
		assertEquals (0.70710678f, angleAxis[1], 0.0001f);
		assertEquals (0.70710678f, angleAxis[2], 0.0001f);
		assertEquals (0          , angleAxis[3], 0.0001f);
	}

	@Test
	public void testPreRotate() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();

		float[] angleAxis = new float[4];
		trans.preRotate      (30, 0,0,1);
		trans.getOrientation (angleAxis);
		assertEquals (30 , angleAxis[0], 0.0001f);
		assertEquals (0   , angleAxis[1], 0.0001f);
		assertEquals (0   , angleAxis[2], 0.0001f);
		assertEquals (1   , angleAxis[3], 0.0001f);
	}


	@Test
	public void testSetOrientation() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();

		float[] angleAxis = new float[4];
		trans.setOrientation (45, 1,1,0);
		trans.getOrientation (angleAxis);
		assertEquals (45         , angleAxis[0], 0.0001f);
		assertEquals (0.70710678f, angleAxis[1], 0.0001f);
		assertEquals (0.70710678f, angleAxis[2], 0.0001f);
		assertEquals (0          , angleAxis[3], 0.0001f);
	}

	@Test
	public void testScale() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();

		float[] scale = new float[3];

		trans.scale    (1,2,3);
		trans.getScale (scale);
		assertEquals (1, scale[0], 0.00001f);
		assertEquals (2, scale[1], 0.00001f);
		assertEquals (3, scale[2], 0.00001f);
	
		trans.scale    (2,2,2);
		trans.getScale (scale);
		assertEquals (2, scale[0], 0.00001f);
		assertEquals (4, scale[1], 0.00001f);
		assertEquals (6, scale[2], 0.00001f);
	}

	@Test
	public void testSetScale() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();

		float[] scale = new float[3];

		trans.setScale (1,1,1);
		trans.getScale (scale);
		assertEquals (1, scale[0], 0.00001f);
		assertEquals (1, scale[1], 0.00001f);
		assertEquals (1, scale[2], 0.00001f);

		trans.setScale (2,2,2);
		trans.getScale (scale);
		assertEquals (2, scale[0], 0.00001f);
		assertEquals (2, scale[1], 0.00001f);
		assertEquals (2, scale[2], 0.00001f);
	}


	@Test
	public void testSetTranslation() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();

		float[] translation = new float[3];

		trans.setTranslation (1,1,1);
		trans.getTranslation (translation);
		assertEquals (1, translation[0], 0.00001f);
		assertEquals (1, translation[1], 0.00001f);
		assertEquals (1, translation[2], 0.00001f);
        
		trans.setTranslation (2,2,2);
		trans.getTranslation (translation);
		assertEquals (2, translation[0], 0.00001f);
		assertEquals (2, translation[1], 0.00001f);
		assertEquals (2, translation[2], 0.00001f);
	}

	@Test
	public void testTranslate() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();

		float[] translation = new float[3];
		trans.translate (1,2,3);
		trans.getTranslation (translation);
		assertEquals (1, translation[0], 0.00001f);
		assertEquals (2, translation[1], 0.00001f);
		assertEquals (3, translation[2], 0.00001f);

        trans.translate (2,2,2);
        trans.getTranslation (translation);
        assertEquals (3, translation[0], 0.00001f);
        assertEquals (4, translation[1], 0.00001f);
        assertEquals (5, translation[2], 0.00001f);
	}


	@Test
	public void testSetTransform() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();

		Transform t1 = new Transform ();
		float[] expected = {1,2,3,4, 5,6,7,8, 9,10,11,12, 13,14,15,16};
		t1.set (expected);
		trans.setTransform (t1);
        
		Transform t2 = new Transform ();
		trans.getTransform (t2);
		float[] matrix = new float[16];
		t2.get (matrix);
		
		assertArrayEquals (expected, matrix, 0.00001f);
	}
	
	@Test
	public void testGetCompositeTransform() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();
	
		float[] matrix = {1,2,3,4, 5,6,7,8, 9,10,11,12, 13,14,15,16};
		Transform t = new Transform ();
		t.set (matrix);
		
		trans.setScale       (1,2,3);
		trans.setOrientation (45, 1,1,0);
		trans.setTranslation (1,2,3);
		trans.setTransform   (t);
		
		trans.getCompositeTransform(t);
		t.get(matrix);
		
		// 値は表示値そのものを使用した
		float[] expected = {28.818f,32.4645f,36.1109f,39.7574f, 
								21.182f,23.5355f,25.8891f,28.2426f, 
								62.5919f,68.2132f,73.8345f,79.4558f, 
								13,14,15,16};
		assertArrayEquals(expected, matrix, 0.0001f);
		
	}

	@Test
	public void testToString() {
		ConcreteTransformable trans = new ConcreteTransformable ();
		trans.initialize();

		trans.toString();
	}
	
	@Test
	public void testAddAnimationTrack () {
		KeyframeSequence keySeq1 = new KeyframeSequence(2, 3, KeyframeSequence.LINEAR);
		AnimationTrack   anim1   = new AnimationTrack(keySeq1, AnimationTrack.SCALE);
		KeyframeSequence keySeq2 = new KeyframeSequence(2, 4, KeyframeSequence.SLERP);
		AnimationTrack   anim2   = new AnimationTrack(keySeq2, AnimationTrack.ORIENTATION);
		KeyframeSequence keySeq3 = new KeyframeSequence(2, 3, KeyframeSequence.LINEAR);
		AnimationTrack   anim3   = new AnimationTrack(keySeq3, AnimationTrack.TRANSLATION);
		ConcreteTransformable trans = new ConcreteTransformable();
		trans.initialize();
	
		trans.addAnimationTrack(anim1);
		trans.addAnimationTrack(anim2);
		trans.addAnimationTrack(anim3);
		
		assertEquals(3    , trans.getAnimationTrackCount());
		assertEquals(anim1, trans.getAnimationTrack(0));
		assertEquals(anim2, trans.getAnimationTrack(1));
		assertEquals(anim3, trans.getAnimationTrack(2));

	}
}
