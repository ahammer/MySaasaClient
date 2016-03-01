package com.adamhammer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Adam on 2/27/2016.
 */
public class Stars {
    final static int MAX_STARS = 100;
    List<Point3D> stars = new ArrayList<Point3D>();

    public Stars() {
        for (int i=0;i<MAX_STARS;i++) {
            stars.add(new Point3D());
        }
    }

    public List<Point3D> getStars() {
        return stars;
    }

    class Point3D {
        final double x,y,z;
        private Random mRandom = new Random();

        Point3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point3D() {
            x= getRandomPoint();
            y= getRandomPoint();
            z= getRandomPoint();
        }

        private double getRandomPoint() {
            return (mRandom.nextFloat()-0.5)*2;
        }

    }
}
