package org.karlsland.m3g.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.karlsland.m3g.AnimationTrack;
import org.karlsland.m3g.KeyframeSequence;
import org.karlsland.m3g.Object3D;
import org.karlsland.m3g.test.ConcreteObject3D;


public class TestObject3D {

    static {
        System.loadLibrary ("jni-opengl");
    }
    
	@Test
	public void testInitialize() {
		ConcreteObject3D obj = new ConcreteObject3D ();
		obj.initialize();
		
		assertEquals(0    , obj.getUserID());
		assertEquals(null , obj.getUserObject());
		assertEquals(0    , obj.getAnimationTrackCount());
	}

	@Test
	public void testFinalize() {
		ConcreteObject3D obj = new ConcreteObject3D ();
		obj.initialize();
		obj = null;
		System.gc();
	}
	
	/**
	 * Object3Dに設定できるアニメーションは存在しないので
	 * ここでは例外が発生することだけチェックしている。
	 */
	@Test (expected=IllegalArgumentException.class)
	public void testAddAnimationTrack() {
		ConcreteObject3D obj = new ConcreteObject3D ();
		obj.initialize();
		KeyframeSequence keyframeSequence = new KeyframeSequence (1, 1, KeyframeSequence.LINEAR);
		AnimationTrack animationTrack = new AnimationTrack (keyframeSequence, AnimationTrack.ALPHA);

		// ここでillegalArgumentException例外
		obj.addAnimationTrack(animationTrack);
		
		assertEquals(0    , obj.getAnimationTrackCount());
	}

	/**
	 * 抽象クラスでdupliate()はできない。
	 */
	@Test
	public void testDuplicate() {
	}

	@Test
	public void testGetReferences() {
		ConcreteObject3D obj = new ConcreteObject3D ();
		obj.initialize();

		Object3D[] refs = new Object3D[0];
		int n = obj.getReferences(refs);

		assertEquals(0, n);
	}

	@Test
	public void testGetUserID() {
		ConcreteObject3D obj = new ConcreteObject3D ();
		obj.initialize();
		
		obj.setUserID (100);
		assertEquals(100, obj.getUserID());
	}

	@Test
	public void testGetUserObject() {
		ConcreteObject3D obj = new ConcreteObject3D ();
		obj.initialize();
		
		int n = 100;
		obj.setUserObject(n);
		assertEquals(n, obj.getUserObject());
	}

	@Test
	public void testToString() {
		ConcreteObject3D obj = new ConcreteObject3D ();
		obj.initialize();
		
		obj.toString();
	}

}
