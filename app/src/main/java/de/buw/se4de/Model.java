package de.buw.se4de;

import java.util.ArrayList;
import java.nio.FloatBuffer;

import org.joml.*;
import org.joml.Math;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class Model {
    private ArrayList<Triangle> tris = new ArrayList<>();

    private Matrix4f projMatrix = new Matrix4f();
    private Matrix4x3f viewMatrix = new Matrix4x3f();
    private Matrix4x3f modelMatrix = new Matrix4x3f();
    private Matrix4x3f modelViewMatrix = new Matrix4x3f();

    // FloatBuffer for transferring matrices to OpenGL
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    void loadModel(String path) {
        tris = OBJReader.open(path);
    }

    void draw(Camera cam) {

        glShadeModel(GL_FLAT);

        // Build the projection matrix. Watch out here for integer division
        // when computing the aspect ratio!
        projMatrix.setPerspective((float) Math.toRadians(40), 1280.0f / 720.0f, 0.01f, 100.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(projMatrix.get(fb));

        // Set lookat view matrix
        viewMatrix.setLookAt(cam.pos.x, cam.pos.y, cam.pos.z, cam.look_at.x, cam.look_at.y, cam.look_at.z, 0.0f, 1.0f, 0.0f);
        glMatrixMode(GL_MODELVIEW);

        modelMatrix.translation(0.0f, 0.0f, 0.0f);
        glLoadMatrixf(viewMatrix.mul(modelMatrix, modelViewMatrix).get4x4(fb));

        FloatBuffer spec = BufferUtils.createFloatBuffer(4);
        FloatBuffer col = BufferUtils.createFloatBuffer(4);

        spec.put(0, 0.2f);
        spec.put(1, 0.2f);
        spec.put(2, 0.2f);
        spec.put(3, 1.0f);

        col.put(0, 0.8f);
        col.put(1, 0.8f);
        col.put(2, 0.8f);
        col.put(3, 1.0f);

        glMaterialfv(GL_FRONT, GL_SPECULAR, spec);
        glMaterialf(GL_FRONT, GL_SHININESS, 0.1f);
        glMaterialf(GL_FRONT, GL_EMISSION, 0);
        glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, col);

        glBegin(GL_TRIANGLES);
        for (Triangle t : tris) {
            glNormal3f(t.v0.norm_x, t.v0.norm_y, t.v0.norm_z);
            glVertex3f(t.v0.pos_x , t.v0.pos_y , t.v0.pos_z);
            glNormal3f(t.v1.norm_x, t.v1.norm_y, t.v1.norm_z);
            glVertex3f(t.v1.pos_x , t.v1.pos_y , t.v1.pos_z);
            glNormal3f(t.v2.norm_x, t.v2.norm_y, t.v2.norm_z);
            glVertex3f(t.v2.pos_x , t.v2.pos_y , t.v2.pos_z);
        }
        glEnd();
    }
}
