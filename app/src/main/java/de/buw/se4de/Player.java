package de.buw.se4de;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Player {

    Camera cam = new Camera();
    float mv_speed = 1.0f;
    float height = 1.8f;

    float yaw = -90.0f;
    float pitch = 0.0f;
    float mouse_sens = 0.2f;

    grid playerGrid = new grid();
    grid enemyGrid = new grid();

    private boolean rotate = false;
    private boolean r_rel = true;
    private boolean place = false;
    private boolean e_rel = true;

    private boolean gameStarted = false;
    private boolean playerTurn = true;
    public boolean gameEnded = false;
    public boolean victory = false;

    private boolean lockPlayer = false;

    public void process_input(long window, float delta) {
        if (lockPlayer) return;
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
        if (glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS) {
            rotate = true;
            r_rel = false;
        }
        if (glfwGetKey(window, GLFW_KEY_R) == GLFW_RELEASE) {
            r_rel = true;
        }
        if (glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS) {
            place = true;
            e_rel = false;
        }
        if (glfwGetKey(window, GLFW_KEY_E) == GLFW_RELEASE) {
            e_rel = true;
        }

        cam.pos.y = height;

        cam.look_at.x = cam.pos.x + cam.front.x;
        cam.look_at.y = cam.pos.y + cam.front.y;
        cam.look_at.z = cam.pos.z + cam.front.z;
    }

    public void process_mouse(float xoffset, float yoffset) {
        if (lockPlayer) return;
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
        if (lockPlayer) return;
        Model m = find_interactable(interactable);
        if (m == null) return;

        float curr_h = m.position.y;

        Vector3f new_pos = new Vector3f(cam.pos);
        Vector3f dir = new Vector3f(cam.front);

        if (dir.y > -0.1f) return;
        float t = (curr_h - new_pos.y) / dir.y;

        new_pos = new_pos.add(dir.mul(t));

        int cell = find_cell(new_pos, true);

        m.updatePosition(new_pos.x, new_pos.y, new_pos.z, rotate&&r_rel);
        if (rotate&&r_rel) {
            rotate = false;
        }

        boolean valid = true;

        if (cell == -1) valid = false;

        // bounds checking
        if (m.rotated) {
            if (cell%10+m.len-1 > 9) valid = false;
            if (valid&&!playerGrid.checkValidPlacement(9-cell / 10, cell % 10, 9-cell / 10, cell % 10 + m.len-1)) valid = false;
        } else {
            if (9-cell/10+m.len-1 > 9) valid = false;
            if (valid&&!playerGrid.checkValidPlacement(9-cell / 10, cell % 10, 9-cell / 10 + m.len-1, cell % 10)) valid = false;
        }

        if (place&&e_rel) {
            place = false;
            if (valid) {
                m.locked = true;
                placeShip(cell, m.rotated, m.len);
                playerGrid.shipAmount -= 1;
                if (playerGrid.shipAmount == 0) startGame();
            }
        }
    }

    public void process_click(ArrayList<Model> clickable) {
        if (lockPlayer) return;
        if (!gameStarted || !playerTurn) return;
        Model m = find_interactable(clickable);
        if (m == null || !m.clickable) return;

        int cell = find_cell(m.position, false);
        String row = Character.toString((char)(74 - cell/10));

        if (enemyGrid.cells[9-cell/10][cell%10].dead) {
            return;
        }

        enemyGrid.shoot(row, cell%10);

        if (!enemyGrid.cells[9-cell/10][cell%10].hasShip) {
            playerTurn = false;
        }

        if (enemyGrid.aliveCells == 0) {
            gameEnded = true;
            victory = true;
        }
    }

    public void do_step(ArrayList<Model> clickable) {
        for (Model m : clickable) {
            int cell = find_cell(m.position, false);
            cell c = playerGrid.cells[9-cell/10][cell%10];
            int amount = (c.dead?1:0) + (c.shotShip?1:0) + (c.sunkShip?1:0);
            switch (amount) {
                case 0: 
                    m.color = new Vector3f(0.0f, 0.0f, 1.0f);
                    break;
                case 1: 
                    m.color = new Vector3f(1.0f, 1.0f, 1.0f);
                    break;
                case 2: 
                    m.color = new Vector3f(1.0f, 0.0f, 0.0f);
                    break;
                case 3: 
                    m.color = new Vector3f(0.0f, 0.0f, 0.0f);
                    break;
            }
        }

        if (gameEnded) {
            lockPlayer = true;
        }

        if (gameStarted && !playerTurn) {
            // int hit[] = playerGrid.randomShoot();
            int hit[] = playerGrid.hunt_target_shoot();
            if (!playerGrid.cells[hit[0]][hit[1]].hasShip) {
                playerTurn = true;
            }
            if (playerGrid.aliveCells == 0) {
                gameEnded = true;
            }
        }
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

    private int find_cell(Vector3f pos, boolean is_player) {

        float cell_size = 0.05f;
        float x_start = 0.275f;
        if (!is_player) x_start = -0.275f - 9.0f*cell_size;
        float y_start = 2.0f;

        if (pos.x < x_start || pos.x > x_start + 9.0f * cell_size || pos.z < y_start || pos.z > y_start + 9.0f * cell_size) return -1;

        float x = pos.x - x_start;
        float y = pos.z - y_start;

        if ((x % cell_size)/cell_size < 0.5f) {
            x += x_start - cell_size*((x % cell_size)/cell_size);
        } else {
            x += x_start + cell_size - (cell_size*((x % cell_size)/cell_size));
        }

        if ((y % cell_size)/cell_size < 0.5f) {
            y += y_start - cell_size*((y % cell_size)/cell_size);
        } else {
            y += y_start + cell_size - (cell_size*((y % cell_size)/cell_size));
        }

        pos.x = x;
        pos.z = y;

        int i = 9 - (int)((pos.x - x_start + (cell_size*0.5f))/cell_size);
        int j = (int)((pos.z - y_start + (cell_size*0.5f))/cell_size);

        return j*10+i;
    }

    private void placeShip(int cell, boolean rotated, int len) {
        String row = Character.toString((char)(74 - cell/10));
        if (!rotated) {
            playerGrid.changingCellsCol(row, len, cell%10);
        } else {
            playerGrid.changingCellsRow(cell%10, len, row);
        }
    }

    private void startGame() {
        enemyGrid.PlacingEnemyShip();
        gameStarted = true;
    }
}
