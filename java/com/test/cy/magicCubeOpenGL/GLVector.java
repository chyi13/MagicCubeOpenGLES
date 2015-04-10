package com.test.cy.magicCubeOpenGL;

/**
 * Created by Administrator on 2015/4/1.
 */
public class GLVector {

    public float[] values;
    public float vLength = 1;
    public int iLength;

    public  GLVector(){
        values = new float[]{0.0f, 0.0f, 0.0f};
        iLength = 3;
    }
    public  GLVector(float[] src){
        values = new float[src.length];
        for (int i = 0; i<src.length; i++)
           values[i] = src[i];
        iLength = src.length;
    }

    public void normalize(){
        double sum = 0;
        for (int i = 0; i<values.length; i++)
            sum+= values[i] * values[i];
         vLength = (float)Math.sqrt(sum);

        for (int i = 0; i<values.length; i++){
            values[i]/=vLength;
        }
    }

    public static GLVector add(GLVector a, GLVector b){
        float[] result = new float[a.iLength];
        for (int i = 0; i<result.length; i++){
            result[i] = a.values[i] + b.values[i];
        }
        return new GLVector(result);
    }

    public static GLVector minus(GLVector a, GLVector b){
        float[] result = new float[a.iLength];
        for (int i = 0; i<result.length; i++){
            result[i] = a.values[i] - b.values[i];
        }
        return new GLVector(result);
    }

    public static float dot(GLVector a, GLVector b){
        return a.values[0] * b.values[0] + a.values[1] * b.values[1] + a.values[2] * b.values[2];
    }
    public static GLVector cross(GLVector a, GLVector b){
        float[] c = new float[3];
        c[0] =  a.values[1] * b.values[2] - b.values[1] * a.values[2];
        c[1] = -a.values[0] * b.values[2] + b.values[0] * a.values[2];
        c[2] =  a.values[0] * b.values[1] - b.values[0] * a.values[1];
        return new GLVector(c);
    }

    public static GLVector multiplyVF(GLVector a, float b){
        GLVector temp = a;
        for (int i = 0; i<a.iLength; i++){
            temp.values[i]*=b;
        }
        return temp;
    }

}
