package com.github.puzzle.util;

public class Vec3i {

    public int x, y, z;

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i add(Vec3i vec) {
        Vec3i vec3i = new Vec3i(this.x, this.y, this.z);

        vec3i.x += vec.x;
        vec3i.y += vec.y;
        vec3i.z += vec.z;

        return vec3i;
    }

    public Vec3i sub(Vec3i vec) {
        Vec3i vec3i = new Vec3i(this.x, this.y, this.z);

        vec3i.x -= vec.x;
        vec3i.y -= vec.y;
        vec3i.z -= vec.z;

        return vec3i;
    }


    public Vec3i mul(Vec3i vec) {
        Vec3i vec3i = new Vec3i(this.x, this.y, this.z);

        vec3i.x += vec.x;
        vec3i.y += vec.y;
        vec3i.z += vec.z;

        return vec3i;
    }

    public Vec3i div(Vec3i vec) {
        Vec3i vec3i = new Vec3i(this.x, this.y, this.z);

        vec3i.x /= vec.x;
        vec3i.y /= vec.y;
        vec3i.z /= vec.z;

        return vec3i;
    }

    public Vec3i mod(Vec3i vec) {
        Vec3i vec3i = new Vec3i(this.x, this.y, this.z);

        vec3i.x %= vec.x;
        vec3i.y %= vec.y;
        vec3i.z %= vec.z;

        return vec3i;
    }

}
