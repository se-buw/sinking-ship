package de.buw.se4de;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Player {

    Camera cam = new Camera();
    float mv_speed = 1.0f;
    float height = 1.8f;

    float yaw = -90.0f;
    float pitch = 0.0f;
    float mouse_sens = 0.2f;
    // TODO: add sinking ship stuff

    public void process_input(long window, float delta) {
        Vector3f front = new Vector3f(cam.front);
        Vector3f right = new Vector3f(cam.front);
        right = right.cross(0.0f, 1.0f, 0.0f);

        front.y = 0.0f;
        right.y = 0.0f;
        front = front.normalize();
        right = right.normalize();

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            cam.pos.add(front.mul(mv_speed * delta));
        } 
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            cam.pos.add(front.mul(mv_speed * delta * -1.0f));
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            cam.pos.add(right.mul(mv_speed * delta * -1.0f));
        } 
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            cam.pos.add(right.mul(mv_speed * delta));
        }

        cam.pos.y = height;

        cam.look_at.x = cam.pos.x + cam.front.x;
        cam.look_at.y = cam.pos.y + cam.front.y;
        cam.look_at.z = cam.pos.z + cam.front.z;
    }

    public void process_mouse(float xoffset, float yoffset) {
        xoffset *= mouse_sens;
        yoffset *= mouse_sens;

        yaw   += xoffset;
        pitch += yoffset;

        if (pitch > 89.0f)
            pitch = 89.0f;
        if (pitch < -89.0f)
            pitch = -89.0f;

        cam.front.x =(float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        cam.front.y =(float) (Math.sin(Math.toRadians(pitch)));
        cam.front.z =(float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        cam.front = cam.front.normalize();

        cam.look_at.x = cam.pos.x + cam.front.x;
        cam.look_at.y = cam.pos.y + cam.front.y;
        cam.look_at.z = cam.pos.z + cam.front.z;
    }

    public void process_hold(ArrayList<Model> interactable) {
        Model m = find_interactable(interactable);
        if (m == null) return;

        Vector3f new_pos = new Vector3f(cam.pos);
        Vector3f dir = new Vector3f(cam.front);
        new_pos = new_pos.add(dir.mul(0.5f));

        m.updatePosition(new_pos.x, new_pos.y, new_pos.z);
    }

    public Camera get_cam() {
        return cam;
    }

    public Model find_interactable(ArrayList<Model> interactables) {
        float dist = Float.MAX_VALUE;
        Model m = null;

        for (Model model : interactables) {
            float t = model.intersect();
            if (t < dist && t > 0.0f) {
                dist = t;
                m = model;
            }
        }

        return m;
    }
}
