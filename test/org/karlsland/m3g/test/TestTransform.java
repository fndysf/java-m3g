package org.karlsland.m3g.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.karlsland.m3g.Transform;
import org.karlsland.m3g.VertexArray;

public class TestTransform {

    static {
        System.loadLibrary ("jni-opengl");
    }
    
	@Test
	public void testInitialize() {
		Transform tra1     = new Transform();
		Transform tra2     = new Transform(tra1);
		float[]   expected = {1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1};
		float[]   matrix   = new float[16];

		tra1.get(matrix);
		assertArrayEquals(expected, matrix, 0.0001f);
		tra2.get(matrix);
		assertArrayEquals(expected, matrix, 0.0001f);
	}

	@Test
	public void testFinalize() {
		@SuppressWarnings("unused")
		Transform tra = new Transform();
		tra = null;
		System.gc();
	}

	@Test
	public void testTransform1() {
		Transform tra    = new Transform();
		float[]   matrix = {1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1};
		tra.set(matrix);
		float[] value    = {1,2,3,4};
		float[] expected = {10,10,10,10};
		tra.transform(value);
		
		assertArrayEquals(expected, value, 0.00001f);
		System.out.println("OUTOF");
	}
	
	/**
	 * 覚え書き：結果を書き込むfloatの配列が頂点数ｘ４より小さい時
	 * 仕様では例外を発生するべきだが、黙ってメモリ破壊している（多分落ちる）。
	 * これはC++側のインターフェースに「長さ」が無いためであるが、
	 * 不便なので将来的にインターフェースを変更しようかと思う。
	 */
	@Test
	public void testTransform2() {
		Transform   tra    = new Transform();
		float[]     matrix = {1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1};
		tra.set(matrix);
		VertexArray varry = new VertexArray(2,3,4);
		float[] values = {1,2,3, 4,5,6}; 
		varry.set(0, 2, values);
		float[] expected = {7,7,7,7,  16,16,16,16};
		float[] value    = new float[8];
		tra.transform(varry, value, true);
		
		assertArrayEquals(expected, value, 0.00001f);
	}


	@Test
	public void testInvert() {
        Transform tra      = new Transform ();
        float[]   matrix   = {1,0,0,1, 0,1,0,1, 0,0,1,1, 0,0,0,2};
        float[]   expected = {1,0,0,-0.5f, 0,1,0,-0.5f, 0,0,1,-0.5f, 0,0,0,0.5f};

        tra.set (matrix);
        tra.invert ();
        tra.get (matrix);
        
        assertArrayEquals(expected, matrix, 0.0001f);
	}

	@Test
	public void testPostMultiply() {
        Transform tra1 = new Transform ();
        Transform tra2 = new Transform ();
        float[] matrix1  = {1,0,0,1, 0,1,0,1, 0,0,1,1, 0,0,0,1};
        float[] matrix2  = {1,1,1,1, 1,1,1,1, 1,1,1,1, 1,1,1,1};
        float[] expected = {2,2,2,2, 2,2,2,2, 2,2,2,2, 1,1,1,1};
        tra1.set (matrix1);
        tra2.set (matrix2);

        tra1.postMultiply (tra2);

        float[] matrix  = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        tra1.get (matrix);
        
        assertArrayEquals(expected, matrix, 0.0001f);
	}

	@Test
	public void testPostRotate() {
        Transform tra = new Transform ();
        tra.postRotate (45, 1, 1, 0);
        
        float[] expected = {0.853553f,0.146447f,0.5f,0f, 0.146447f,0.853553f,-0.5f,0, -0.5f,0.5f,0.707107f,0, 0,0,0,1};
        float[] matrix   = {0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,999};
        tra.get (matrix);

        assertArrayEquals (expected, matrix, 0.0001f);
	}

	@Test
	public void testPostRotateQuat() {
        Transform trans = new Transform ();
        // Quaternion (45, 1, 1, 0)と同じ
        trans.postRotateQuat (0.270598f,0.270598f,0, 0.92388f);

        float[] expected = {0.853553f,0.146447f,0.5f,0f, 0.146447f,0.853553f,-0.5f,0, -0.5f,0.5f,0.707107f,0, 0,0,0,1};
        float[] matrix   = {0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,999};
        trans.get (matrix);

        assertArrayEquals (expected, matrix, 0.0001f);
	}

	@Test
	public void testPostScale() {
        Transform trans = new Transform ();
        trans.postScale (100, 200, 300);

        float[] expected = {100,0,0,0, 0,200,0,0, 0,0,300,0, 0,0,0,1};
        float[] matrix   = {0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0};
        trans.get (matrix);

        assertArrayEquals (expected, matrix, 0.0001f);
	}

	@Test
	public void testPostTranslate() {
        Transform trans = new Transform ();
        trans.postTranslate (100, 200, 300);

        float[] expected = {1,0,0,100, 0,1,0,200, 0,0,1,300, 0,0,0,1};
        float[] matrix   = {0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,999};
        trans.get (matrix);

        assertArrayEquals (expected, matrix, 0.0001f);
	}

	@Test
	public void testSetFloatArray() {
		Transform tra      = new Transform();
		float[]   expected = {1,2,3,4, 5,6,7,8, 9,10,11,12, 13,114,15,16};
		float[]   matrix   = new float[16];
		tra.set(expected);
		tra.get(matrix);
		
		assertArrayEquals(expected, matrix, 0.0001f);
	}

	@Test
	public void testSetTransform() {
		Transform tra1      = new Transform();
		Transform tra2      = new Transform();
		float[]   expected = {1,2,3,4, 5,6,7,8, 9,10,11,12, 13,114,15,16};
		float[]   matrix   = new float[16];
		tra1.set(expected);
		tra2.set(tra1);
		tra2.get(matrix);
		
		assertArrayEquals(expected, matrix, 0.0001f);
	}

	@Test
	public void testSetIdentity() {
		Transform tra      = new Transform();
		float[]   expected = {1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1};
		float[]   matrix   = new float[16];
		tra.setIdentity();
		tra.get(matrix);
		
		assertArrayEquals(expected, matrix, 0.0001f);
	}


	@Test
	public void testTranspose() {
        Transform tra  = new Transform ();
        float[]   matrix = {1,2,3,4, 5,6,7,8, 9,10,11,12, 13,14,15,16};
        tra.set (matrix);
        tra.transpose ();
        tra.get (matrix);

        float[] expected = {1,5,9,13, 2,6,10,14, 3,7,11,15, 4,8,12,16};

        assertArrayEquals (expected, matrix, 0.0001f);
	}

	@Test
	public void testToString() {
        Transform tra  = new Transform ();
        tra.toString();
	}

	
}
