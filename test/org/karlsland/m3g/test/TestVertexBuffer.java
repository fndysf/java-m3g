package org.karlsland.m3g.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.karlsland.m3g.Image2D;
import org.karlsland.m3g.Object3D;
import org.karlsland.m3g.Texture2D;
import org.karlsland.m3g.VertexArray;
import org.karlsland.m3g.VertexBuffer;

public class TestVertexBuffer {

    static {
        System.loadLibrary ("jni-opengl");
    }
    
	@Test
	public void testInitialize() {
        VertexBuffer vbuf = new VertexBuffer ();

        assertEquals (0   , vbuf.getVertexCount());
        assertEquals (null, vbuf.getPositions(null));
        assertEquals (null, vbuf.getTexCoords(0, null));
        assertEquals (null, vbuf.getNormals());
        assertEquals (null, vbuf.getColors());
        assertEquals (0xffffffff, vbuf.getDefaultColor());
	}

	@Test
	public void testFinalize() {
		 @SuppressWarnings("unused")
		VertexBuffer vbuf = new VertexBuffer ();
		 vbuf = null;
		 System.gc();
     }

	@Test
	public void testSetColors() {
		VertexBuffer vbuf   = new VertexBuffer ();
		VertexArray  colors = new VertexArray(4,3,1);
		vbuf.setColors(colors);
		assertEquals(colors, vbuf.getColors());
	}

	@Test
	public void testSetDefaultColor() {
		VertexBuffer vbuf   = new VertexBuffer ();
		vbuf.setDefaultColor(0x12345678);
		assertEquals(0x12345678, vbuf.getDefaultColor());
	}

	@Test
	public void testSetNormals() {
		VertexBuffer vbuf   = new VertexBuffer ();
		VertexArray  normals = new VertexArray(4,3,2);
		vbuf.setNormals(normals);
		assertEquals(normals, vbuf.getNormals());
	}

	@Test
	public void testSetPositions() {
		VertexBuffer vbuf      = new VertexBuffer ();
		VertexArray  positions = new VertexArray(4,3,4);
		float   scale = 2;
		float[] bias  = {3,4,5};
		vbuf.setPositions(positions, scale, bias);
		float[] scale_bias = new float[4];
		assertEquals(positions, vbuf.getPositions(scale_bias));
		assertEquals(scale  , scale_bias[0], 0.0001f);
		assertEquals(bias[0], scale_bias[1], 0.0001f);
		assertEquals(bias[1], scale_bias[2], 0.0001f);
		assertEquals(bias[2], scale_bias[3], 0.0001f);
	}

	@Test
	public void testSetTexCoords() {
		VertexBuffer vbuf      = new VertexBuffer ();
		VertexArray  tex_coords = new VertexArray(4,3,4);
		float   scale = 2;
		float[] bias  = {3,4,5};
		vbuf.setTexCoords(0, tex_coords, scale, bias);
		float[] scale_bias = new float[4];
		assertEquals(tex_coords, vbuf.getTexCoords(0, scale_bias));
		assertEquals(scale  , scale_bias[0], 0.0001f);
		assertEquals(bias[0], scale_bias[1], 0.0001f);
		assertEquals(bias[1], scale_bias[2], 0.0001f);
		assertEquals(bias[2], scale_bias[3], 0.0001f);
	}

	@Test
	public void testToString() {
		VertexBuffer vbuf      = new VertexBuffer ();
		vbuf.toString();
	}

	@Test
	public void testGetReferences() {
		VertexBuffer vbuf       = new VertexBuffer ();
		VertexArray  positions  = new VertexArray(4,3,4);
		VertexArray  normals    = new VertexArray(4,3,2);
		VertexArray  colors     = new VertexArray(4,3,1);
		VertexArray  tex_coords = new VertexArray(4,3,4);
		float   scale = 2;
		float[] bias  = {3,4,5};
		vbuf.setPositions(positions, scale, bias);
		vbuf.setNormals(normals);
		vbuf.setColors(colors);
		vbuf.setTexCoords(0, tex_coords, scale, bias);

		Object3D[] references = {null, null, null, null};
	 	int n = vbuf.getReferences(references);

	 	assertEquals(4  , n);
	 	assertEquals(positions , references[0]);
	 	assertEquals(normals   , references[1]);
	 	assertEquals(colors    , references[2]);
	 	assertEquals(tex_coords, references[3]);
	}

}
