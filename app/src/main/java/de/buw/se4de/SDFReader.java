package de.buw.se4de;

import java.util.ArrayList;

import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SDFReader {

    public static void openSDF(String path, ArrayList<Model> models, ArrayList<Model> interactables, ArrayList<Model> clickables) {
        BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("src/main/resources/"+path+".sdf"));
			String line = reader.readLine();
			while (line != null) {
				String[] line_arr = line.split(",");
                String name = line_arr[0];
                String[] positions = line_arr[1].split(" ");
                String inter = line_arr[2];
                String hidden = line_arr[3];
                int len = Integer.parseInt(line_arr[4]);
                String clickable = line_arr[5];

                float x = Float.parseFloat(positions[0]);
                float y = Float.parseFloat(positions[1]);
                float z = Float.parseFloat(positions[2]);

                Model m = new Model();
                m.loadModel(name, hidden.equals("h"));
                m.updatePosition(x, y, z, false);
                if (inter.equals("i")) {
                    m.setBB();
                    interactables.add(m);
                }
                m.len = len;
                m.clickable = clickable.equals("c");
                if (m.clickable) {
                    if (name.equals("mVictory")) {
                        m.colored = true;
                        m.color = new Vector3f(1.0f, 0.84f, 0.0f);
                    } else if (name.equals("mLost")) {
                        m.colored = true;
                        m.color = new Vector3f(1.0f, 0.0f, 0.0f);
                    } else {
                        m.setBB();
                        m.colored = true;
                        clickables.add(m);
                    }
                }

                models.add(m);

				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static void openModel(String path, ArrayList<Triangle> tris) {

        ArrayList<Float> positions = new ArrayList<>();
        ArrayList<Float> uvs = new ArrayList<>();
        ArrayList<Float> normals = new ArrayList<>();

        // really basic .obj File parser
        BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("src/main/resources/"+path+".obj"));
			String line = reader.readLine();
			while (line != null) {
				String[] line_arr = line.split(" ");
                switch(line_arr[0]) {
                    case "v":
                        positions.add(Float.parseFloat(line_arr[1]));
                        positions.add(Float.parseFloat(line_arr[2]));
                        positions.add(Float.parseFloat(line_arr[3]));
                        break;
                    case "vt":
                        uvs.add(Float.parseFloat(line_arr[1]));
                        uvs.add(Float.parseFloat(line_arr[2]));
                        break;
                    case "vn":
                        normals.add(Float.parseFloat(line_arr[1]));
                        normals.add(Float.parseFloat(line_arr[2]));
                        normals.add(Float.parseFloat(line_arr[3]));
                        break;
                    case "f":
                        String[] vertex0 = line_arr[1].split("/");
                        String[] vertex1 = line_arr[2].split("/");
                        String[] vertex2 = line_arr[3].split("/");

                        int v0_pos_off = (Integer.parseInt(vertex0[0])-1)*3;
                        int v0_uv_off = (Integer.parseInt(vertex0[1])-1)*2;
                        int v0_norm_off = (Integer.parseInt(vertex0[2])-1)*3;
                        int v1_pos_off = (Integer.parseInt(vertex1[0])-1)*3;
                        int v1_uv_off = (Integer.parseInt(vertex1[1])-1)*2;
                        int v1_norm_off = (Integer.parseInt(vertex1[2])-1)*3;
                        int v2_pos_off = (Integer.parseInt(vertex2[0])-1)*3;
                        int v2_uv_off = (Integer.parseInt(vertex2[1])-1)*2;
                        int v2_norm_off = (Integer.parseInt(vertex2[2])-1)*3;

                        Vertex v0 = new Vertex(); v0.pos_x = positions.get(v0_pos_off); v0.pos_y = positions.get(v0_pos_off+1); v0.pos_z = positions.get(v0_pos_off+2); v0.norm_x = normals.get(v0_norm_off); v0.norm_y = normals.get(v0_norm_off+1); v0.norm_z = normals.get(v0_norm_off+2); v0.u = uvs.get(v0_uv_off); v0.v = uvs.get(v0_uv_off+1);
                        Vertex v1 = new Vertex(); v1.pos_x = positions.get(v1_pos_off); v1.pos_y = positions.get(v1_pos_off+1); v1.pos_z = positions.get(v1_pos_off+2); v1.norm_x = normals.get(v1_norm_off); v1.norm_y = normals.get(v1_norm_off+1); v1.norm_z = normals.get(v1_norm_off+2); v1.u = uvs.get(v1_uv_off); v1.v = uvs.get(v1_uv_off+1);
                        Vertex v2 = new Vertex(); v2.pos_x = positions.get(v2_pos_off); v2.pos_y = positions.get(v2_pos_off+1); v2.pos_z = positions.get(v2_pos_off+2); v2.norm_x = normals.get(v2_norm_off); v2.norm_y = normals.get(v2_norm_off+1); v2.norm_z = normals.get(v2_norm_off+2); v2.u = uvs.get(v2_uv_off); v2.v = uvs.get(v2_uv_off+1);

                        Triangle t = new Triangle();
                        t.v0 = v0; t.v1 = v1; t.v2 = v2;

                        tris.add(t);

                        break;
                }

				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
