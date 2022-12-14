package de.buw.se4de;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.nio.FloatBuffer;

import org.joml.*;
import org.joml.Math;
import org.joml.Intersectionf;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class Model {
    private ArrayList<Triangle> tris = new ArrayList<>();
    private Vector3f bb_min = new Vector3f(Float.MAX_VALUE);
    private Vector3f bb_max = new Vector3f(Float.MIN_VALUE); 

    private int texID;
    private boolean hidden;

    public Vector3f position = new Vector3f();
    public boolean rotated = false;
    private Matrix4f projMatrix = new Matrix4f();
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f modelMatrix = new Matrix4f();
    private Matrix4f modelViewMatrix = new Matrix4f();

    public int len = 0;
    public boolean locked = false;
    public boolean clickable = false;
    public boolean colored = false;
    public Vector3f color = new Vector3f(1.0f, 1.0f, 1.0f);

    // FloatBuffer for transferring matrices to OpenGL
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    void loadModel(String path, boolean h) {
        BufferedImage image = TextureLoader.loadImage("./app/src/main/resources/"+path+".png");
        SDFReader.openModel(path, tris);
        texID = TextureLoader.loadTexture(image);
        hidden = h;
        modelMatrix.translation(0.0f, 0.0f, 0.0f);
    }

    private Vector3f start = new Vector3f();
    private Vector3f end = new Vector3f();

    void setBB() {
        for (Triangle tri : tris) {
            if (tri.v0.pos_x-0.01f < bb_min.x) bb_min.x = tri.v0.pos_x-0.01f; if (tri.v0.pos_x+0.01f > bb_max.x) bb_max.x = tri.v0.pos_x+0.01f;
            if (tri.v0.pos_y-0.01f < bb_min.y) bb_min.y = tri.v0.pos_y-0.01f; if (tri.v0.pos_y+0.01f > bb_max.y) bb_max.y = tri.v0.pos_y+0.01f;
            if (tri.v0.pos_z-0.01f < bb_min.z) bb_min.z = tri.v0.pos_z-0.01f; if (tri.v0.pos_z+0.01f > bb_max.z) bb_max.z = tri.v0.pos_z+0.01f;
            if (tri.v1.pos_x-0.01f < bb_min.x) bb_min.x = tri.v1.pos_x-0.01f; if (tri.v1.pos_x+0.01f > bb_max.x) bb_max.x = tri.v1.pos_x+0.01f;
            if (tri.v1.pos_y-0.01f < bb_min.y) bb_min.y = tri.v1.pos_y-0.01f; if (tri.v1.pos_y+0.01f > bb_max.y) bb_max.y = tri.v1.pos_y+0.01f;
            if (tri.v1.pos_z-0.01f < bb_min.z) bb_min.z = tri.v1.pos_z-0.01f; if (tri.v1.pos_z+0.01f > bb_max.z) bb_max.z = tri.v1.pos_z+0.01f;
            if (tri.v2.pos_x-0.01f < bb_min.x) bb_min.x = tri.v2.pos_x-0.01f; if (tri.v2.pos_x+0.01f > bb_max.x) bb_max.x = tri.v2.pos_x+0.01f;
            if (tri.v2.pos_y-0.01f < bb_min.y) bb_min.y = tri.v2.pos_y-0.01f; if (tri.v2.pos_y+0.01f > bb_max.y) bb_max.y = tri.v2.pos_y+0.01f;
            if (tri.v2.pos_z-0.01f < bb_min.z) bb_min.z = tri.v2.pos_z-0.01f; if (tri.v2.pos_z+0.01f > bb_max.z) bb_max.z = tri.v2.pos_z+0.01f;
        }
    }

    void updatePosition(float x, float y, float z, boolean rotate) {
        
        position.x = x;
        position.y = y;
        position.z = z;
        modelMatrix.translation(x, y, z);
        if (rotated) {
            modelMatrix.rotateY((float)Math.PI*0.5f);
        } else {
            modelMatrix.rotateY(0.0f);
        }
        if (rotate) {
            rotated = !rotated;
        }
    }

    float intersect() {
        float dist = Float.MAX_VALUE;

        if (locked) return -1.0f;
        
        Vector3f ori = new Vector3f();
        Vector3f dir = new Vector3f();

        Matrix4f MVP = new Matrix4f();
        MVP = projMatrix.mul(modelViewMatrix, MVP);
        MVP.unprojectRay(640.0f, 360.0f, new int[] {0, 0, 1280, 720}, ori, dir);

        dir.normalize();

        Vector2f res = new Vector2f();

        Vector3f end = new Vector3f(ori);
        end = end.add(dir.mul(5.0f));

        this.start = ori;
        this.end = end;

        int status;
        status = Intersectionf.intersectLineSegmentAab(ori, end, bb_min, bb_max, res);

        if (status == Intersectionf.OUTSIDE) return dist; 
        return res.x;
    }

    void draw(Camera cam, FloatBuffer l_pos) {

        if (hidden) return;

        glShadeModel(GL_SMOOTH);

        // Build the projection matrix. Watch out here for integer division
        // when computing the aspect ratio!
        projMatrix.setPerspective((float) Math.toRadians(60), 1280.0f / 720.0f, 0.01f, 100.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glLoadMatrixf(projMatrix.get(fb));

        // Set lookat view matrix
        viewMatrix.setLookAt(cam.pos.x, cam.pos.y, cam.pos.z, cam.look_at.x, cam.look_at.y, cam.look_at.z, 0.0f, 1.0f, 0.0f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // modelMatrix.translation(0.0f, 0.0f, 0.0f);
        glLoadMatrixf(viewMatrix.mul(modelMatrix, modelViewMatrix).get(fb));

        FloatBuffer spec = BufferUtils.createFloatBuffer(4);
        FloatBuffer col = BufferUtils.createFloatBuffer(4);

        spec.put(0, 1.0f);
        spec.put(1, 1.0f);
        spec.put(2, 1.0f);
        spec.put(3, 1.0f);

        col.put(0, 0.8f);
        col.put(1, 0.8f);
        col.put(2, 0.8f);
        col.put(3, 1.0f);

        glMaterialfv(GL_FRONT, GL_SPECULAR, spec);
        glMaterialf(GL_FRONT, GL_SHININESS, 0.4f);
        glMaterialf(GL_FRONT, GL_EMISSION, 0);
        glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, col);

        glBindTexture(GL_TEXTURE_2D, texID);

        if (colored)
            glDisable(GL_LIGHTING);

        glBegin(GL_TRIANGLES);
        for (Triangle t : tris) {
            glColor3f(color.x, color.y, color.z);
            glTexCoord2f(t.v0.u, t.v0.v);
            glNormal3f(t.v0.norm_x, t.v0.norm_y, t.v0.norm_z);
            glVertex3f(t.v0.pos_x , t.v0.pos_y , t.v0.pos_z);

            glColor3f(color.x, color.y, color.z);
            glTexCoord2f(t.v1.u, t.v1.v);
            glNormal3f(t.v1.norm_x, t.v1.norm_y, t.v1.norm_z);
            glVertex3f(t.v1.pos_x , t.v1.pos_y , t.v1.pos_z);
            
            glColor3f(color.x, color.y, color.z);
            glTexCoord2f(t.v2.u, t.v2.v);
            glNormal3f(t.v2.norm_x, t.v2.norm_y, t.v2.norm_z);
            glVertex3f(t.v2.pos_x , t.v2.pos_y , t.v2.pos_z);
        }
        glEnd();
        
        if (colored)
            glEnable(GL_LIGHTING);
    }

    void drawBB(Camera cam) {

        glShadeModel(GL_SMOOTH);

        // Build the projection matrix. Watch out here for integer division
        // when computing the aspect ratio!
        projMatrix.setPerspective((float) Math.toRadians(40), 1280.0f / 720.0f, 0.01f, 100.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(projMatrix.get(fb));

        // Set lookat view matrix
        viewMatrix.setLookAt(cam.pos.x, cam.pos.y, cam.pos.z, cam.look_at.x, cam.look_at.y, cam.look_at.z, 0.0f, 1.0f, 0.0f);
        glMatrixMode(GL_MODELVIEW);

        // modelMatrix.translation(0.0f, 0.0f, 0.0f);
        glLoadMatrixf(viewMatrix.mul(modelMatrix, modelViewMatrix).get(fb));

        glDisable(GL_LIGHTING);
        glBegin(GL_LINES);
            glVertex3f(bb_min.x, bb_min.y, bb_min.z);
            glVertex3f(bb_max.x, bb_min.y, bb_min.z);
            glVertex3f(bb_min.x, bb_min.y, bb_min.z);
            glVertex3f(bb_min.x, bb_max.y, bb_min.z);
            glVertex3f(bb_min.x, bb_min.y, bb_min.z);
            glVertex3f(bb_min.x, bb_min.y, bb_max.z);
            glVertex3f(bb_max.x, bb_max.y, bb_max.z);
            glVertex3f(bb_min.x, bb_max.y, bb_max.z);
            glVertex3f(bb_max.x, bb_max.y, bb_max.z);
            glVertex3f(bb_max.x, bb_min.y, bb_max.z);
            glVertex3f(bb_max.x, bb_max.y, bb_max.z);
            glVertex3f(bb_max.x, bb_max.y, bb_min.z);

            glVertex3f(start.x, start.y, start.z);
            glVertex3f(end.x, end.y, end.z);
        glEnd();
        glEnable(GL_LIGHTING);
    }
}
