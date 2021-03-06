package org.karlsland.m3g.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.karlsland.m3g.Appearance;
import org.karlsland.m3g.Group;
import org.karlsland.m3g.MorphingMesh;
import org.karlsland.m3g.Object3D;
import org.karlsland.m3g.SkinnedMesh;
import org.karlsland.m3g.Transform;
import org.karlsland.m3g.TriangleStripArray;
import org.karlsland.m3g.VertexArray;
import org.karlsland.m3g.VertexBuffer;

public class TestSkinnedMesh {

    static {
        System.loadLibrary ("jni-opengl");
    }
    
	@Test
	public void testInitialize1() {
		VertexArray        positions = new VertexArray (4, 3, 2);
		VertexBuffer       vertices  = new VertexBuffer ();
		TriangleStripArray tris      = new TriangleStripArray (0, new int[]{4});
		Appearance         app       = new Appearance ();
		Group              skeleton  = new Group ();
		short[]      positions_value = {0,1,2,3,4,5,6,7,8,9,10,11};
		positions.set (0, 4, positions_value);
		
		float   scale = 1;
		float[] bias  = {0,0,0};
		vertices.setPositions (positions, scale, bias);
		
		SkinnedMesh   mesh  = new SkinnedMesh (vertices, tris, app, skeleton);
			
		assertEquals (vertices, mesh.getVertexBuffer());
		assertEquals (1       , mesh.getSubmeshCount());
		assertEquals (tris    , mesh.getIndexBuffer(0));
		assertEquals (app     , mesh.getAppearance(0));
		assertEquals (skeleton, mesh.getSkeleton());		
	}

	 @Test
	 public void testInitialize2 () {
		 VertexArray          positions = new VertexArray (4, 3, 2);
		 VertexBuffer         vertices  = new VertexBuffer ();
		 TriangleStripArray   tris0     = new TriangleStripArray (0, new int[]{3});
		 TriangleStripArray   tris1     = new TriangleStripArray (0, new int[]{3});
		 TriangleStripArray[] trises    = {tris0, tris1};
		 Appearance           app0      = new Appearance ();
		 Appearance           app1      = new Appearance ();
		 Appearance[]         apps      = {app0, app1};
		 Group                skeleton  = new Group ();
		 float[]        positions_value = new float[]{0,0,0};
		 vertices.setPositions (positions, 1, positions_value);

		 SkinnedMesh mesh = new SkinnedMesh (vertices, trises, apps, skeleton);

		 assertEquals (vertices , mesh.getVertexBuffer());
		 assertEquals (2        , mesh.getSubmeshCount());
		 assertEquals (tris0    , mesh.getIndexBuffer(0));
		 assertEquals (tris1    , mesh.getIndexBuffer(1));
		 assertEquals (app0     , mesh.getAppearance(0));
		 assertEquals (app1     , mesh.getAppearance(1));
		 assertEquals (skeleton , mesh.getSkeleton());
	}
	    	
	@Test
	public void testFinalize() {
		 VertexArray          positions = new VertexArray (4, 3, 2);
		 VertexBuffer         vertices  = new VertexBuffer ();
		 TriangleStripArray   tris0     = new TriangleStripArray (0, new int[]{3});
		 TriangleStripArray   tris1     = new TriangleStripArray (0, new int[]{3});
		 TriangleStripArray[] trises    = {tris0, tris1};
		 Appearance           app0      = new Appearance ();
		 Appearance           app1      = new Appearance ();
		 Appearance[]         apps      = {app0, app1};
		 Group                skeleton  = new Group ();
		 float[]        positions_value = new float[]{0,0,0};
		 vertices.setPositions (positions, 1, positions_value);

		 @SuppressWarnings("unused")
		SkinnedMesh mesh = new SkinnedMesh (vertices, trises, apps, skeleton);
		 mesh = null;
		 System.gc();
	}

	@Test
	public void testGetBoneTransform() {
        VertexArray  varry       = new VertexArray (10, 3, 2);
        VertexBuffer vbuf        = new VertexBuffer ();
        int[]        indices     = {0,1,2};
        int[]        strips      = {3};
        TriangleStripArray tris  = new TriangleStripArray (indices, strips);
        Appearance         app   = new Appearance ();

        Group        bone0    = new Group ();
        Group        bone1    = new Group ();
        Group        bone2    = new Group ();
        bone1.translate (0,1,0);
        bone2.translate (1,0,0);
        bone0.addChild (bone1);
        bone0.addChild (bone2);

        float   scale = 1;
        float[] bias  = {0,0,0};
        vbuf.setPositions (varry, scale, bias);

        SkinnedMesh  mesh        = new SkinnedMesh (vbuf, tris, app, bone0);

        // vertices 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        // bone0    ----------        ----
        // bone1          ----------
        // bone2          ----                   
        mesh.addTransform (bone0, 100, 0, 4);
        mesh.addTransform (bone0, 100, 6, 2);
        mesh.addTransform (bone1, 100, 2, 4);
        mesh.addTransform (bone2, 20,  2, 2);  // ウェイトの小さな3つ目のボーンは無視

        Transform trns = new Transform ();
        float[]   m    = new float[16];

        mesh.getBoneTransform (bone1, trns);
        trns.get (m);
        assertEquals (-1, m[7], 0.00001f);   // m[1][3] = ty

        mesh.getBoneTransform (bone2, trns);
        trns.get (m);
        assertEquals (-1, m[3], 0.00001f);   // m[0][3] = tx
    }

	@Test
	public void testGetBoneVertices() {
	    VertexArray  varry       = new VertexArray (10, 3, 2);
        VertexBuffer vbuf        = new VertexBuffer ();
        int[]        indices     = {0,1,2};
        int[]        strips      = {3};
        TriangleStripArray tris  = new TriangleStripArray (indices, strips);
        Appearance         app   = new Appearance ();

        // bone0 --> bone1 --> bone2
        //             |-----> bone3
        Group        bone0    = new Group ();
        Group        bone1    = new Group ();
        Group        bone2    = new Group ();
        Group        bone3    = new Group ();
        bone1.translate (0,1,0);
        bone2.translate (1,0,0);
        bone3.translate (1,0,0);
        bone0.addChild (bone1);
        bone1.addChild (bone2);
        bone1.addChild (bone3);
  
        float   scale = 1;
        float[] bias  = {0,0,0};
        vbuf.setPositions (varry, scale, bias);

        SkinnedMesh  mesh        = new SkinnedMesh (vbuf, tris, app, bone0);

        // vertices 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        // bone0    ----------        ----
        // bone1          ----------
        // bone2          ----                   
        // bone3                      ----------                     
        mesh.addTransform (bone0, 100, 0, 4);
        mesh.addTransform (bone0, 100, 6, 2);
        mesh.addTransform (bone1, 100, 2, 4);
        mesh.addTransform (bone2, 200, 2, 2);
        mesh.addTransform (bone3, 200, 6, 4);
  
        int[]   vertex_indices = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        float[] weights        = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        int     num            = -1;

        num = mesh.getBoneVertices (bone0, vertex_indices, weights);
        assertEquals (6, num);
        assertEquals (0, vertex_indices[0]);
        assertEquals (1, vertex_indices[1]);
        assertEquals (2, vertex_indices[2]);
        assertEquals (3, vertex_indices[3]);
        assertEquals (6, vertex_indices[4]);
        assertEquals (7, vertex_indices[5]);
        assertEquals (1,         weights[0], 0.00001f);
        assertEquals (1,         weights[1], 0.00001f);
        assertEquals (0.25f,     weights[2], 0.00001f);
        assertEquals (0.25f,     weights[3], 0.00001f);
        assertEquals (0.333333f, weights[4], 0.00001f);
        assertEquals (0.333333f, weights[5], 0.00001f);

        num = mesh.getBoneVertices (bone1, vertex_indices, weights);
        assertEquals (4, num);
        assertEquals (2, vertex_indices[0]);
        assertEquals (3, vertex_indices[1]);
        assertEquals (4, vertex_indices[2]);
        assertEquals (5, vertex_indices[3]);
        assertEquals (0.25f, weights[0], 0.00001f);
        assertEquals (0.25f, weights[1], 0.00001f);
        assertEquals (1,     weights[2], 0.00001f);
        assertEquals (1,     weights[3], 0.00001f);

        num = mesh.getBoneVertices (bone2, vertex_indices, weights);
        assertEquals (2,    num);
        assertEquals (2,    vertex_indices[0]);
        assertEquals (3,    vertex_indices[1]);
        assertEquals (0.5f, weights[0], 0.00001f);
        assertEquals (0.5f, weights[1], 0.00001f);

        num = mesh.getBoneVertices (bone3, vertex_indices, weights);
        assertEquals (4, num);
        assertEquals (6, vertex_indices[0]);
        assertEquals (7, vertex_indices[1]);
        assertEquals (8, vertex_indices[2]);
        assertEquals (9, vertex_indices[3]);
        assertEquals (0.666667f, weights[0], 0.00001f);
        assertEquals (0.666667f, weights[1], 0.00001f);
        assertEquals (1,         weights[2], 0.00001f);
        assertEquals (1,         weights[3], 0.00001f);
	}
	
	@Test
	public void testToString() {
		 VertexArray          positions = new VertexArray (4, 3, 2);
		 VertexBuffer         vertices  = new VertexBuffer ();
		 TriangleStripArray   tris0     = new TriangleStripArray (0, new int[]{3});
		 TriangleStripArray   tris1     = new TriangleStripArray (0, new int[]{3});
		 TriangleStripArray[] trises    = {tris0, tris1};
		 Appearance           app0      = new Appearance ();
		 Appearance           app1      = new Appearance ();
		 Appearance[]         apps      = {app0, app1};
		 Group                skeleton  = new Group ();
		 float[]        positions_value = new float[]{0,0,0};
		 vertices.setPositions (positions, 1, positions_value);

		SkinnedMesh mesh = new SkinnedMesh (vertices, trises, apps, skeleton);
		mesh.toString();
	}

	@Test
	public void testGetReferences() {
		 VertexArray          positions = new VertexArray (4, 3, 2);
		 VertexBuffer         vertices  = new VertexBuffer ();
		 TriangleStripArray   tris0     = new TriangleStripArray (0, new int[]{3});
		 TriangleStripArray   tris1     = new TriangleStripArray (0, new int[]{3});
		 TriangleStripArray[] trises    = {tris0, tris1};
		 Appearance           app0      = new Appearance ();
		 Appearance           app1      = new Appearance ();
		 Appearance[]         apps      = {app0, app1};
		 Group                skeleton  = new Group ();
		 float[]        positions_value = new float[]{0,0,0};
		 vertices.setPositions (positions, 1, positions_value);

		SkinnedMesh mesh = new SkinnedMesh (vertices, trises, apps, skeleton);

		Object3D[] references = {null, null, null, null, null, null};
	 	int n = mesh.getReferences(references);

	 	assertEquals(6       , n);
	 	assertEquals(vertices, references[0]);
	 	assertEquals(tris0   , references[1]);
	 	assertEquals(tris1   , references[2]);
	 	assertEquals(app0    , references[3]);
	 	assertEquals(app1    , references[4]);
	 	assertEquals(skeleton, references[5]);
	}


}
