package fynn.renderer;

import org.joml.Vector3f;
import org.joml.Matrix4f;


public class Camera {
    private Vector3f pos, rot;
    private float scale;
    Matrix4f viewMatrix, projectionMatrix;


    public Camera(Vector3f pos, Vector3f rot) {
        this.pos = pos;
        this.rot = rot;
        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        scale = 0.01f;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getRot() {
        return rot;
    }

    public void setRot(Vector3f rot) {
        this.rot = rot;
    }

    public void rotateY(float angle) {

        rot.y += angle;
    }

    public void scaleUp() {

        this.scale *= 1.1f;
    }

    public void scaleDown() {
        this.scale *= 0.9f;
    }


    public Matrix4f getViewMatrix() {

        viewMatrix.identity();

        viewMatrix.translate(-pos.x, -pos.y, -pos.z);

        //viewMatrix.perspective(90, 16.0f/9.0f, 1.0f, 10.0f);

        viewMatrix.rotationXYZ((float) Math.toRadians(rot.x), (float) Math.toRadians(rot.y), (float) Math.toRadians(rot.z));



        viewMatrix.scale(scale);




        return viewMatrix;

    }

}
