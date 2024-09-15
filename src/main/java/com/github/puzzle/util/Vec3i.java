package com.github.puzzle.util;

import com.badlogic.gdx.math.MathUtils;

public record Vec3i(int x, int y, int z) {

    public Vec3i add(Vec3i vec) {

        return new Vec3i(
                this.x + vec.x,
                this.y + vec.y,
                this.z + vec.z
        );
    }

    public Vec3i sub(Vec3i vec) {
        return new Vec3i(
                this.x - vec.x,
                this.y - vec.y,
                this.z - vec.z
        );
    }


    public Vec3i mul(Vec3i vec) {
        return new Vec3i(
                this.x * vec.x,
                this.y * vec.y,
                this.z * vec.z
        );
    }

    public Vec3i div(Vec3i vec) {
        return new Vec3i(
                this.x / vec.x,
                this.y / vec.y,
                this.z / vec.z
        );
    }

    public Vec3i mod(Vec3i vec) {
        return new Vec3i(
                this.x % vec.x,
                this.y % vec.y,
                this.z % vec.z
        );
    }

    public int distance(Vec3i vec) {
        return ((vec.x - this.x < 0) ? -(vec.x - this.x) : vec.x - this.x) +
               ((vec.y - this.y < 0) ? -(vec.y - this.y) : vec.y - this.y) +
               ((vec.z - this.z < 0) ? -(vec.z - this.z) : vec.z - this.z);
               // d = abs(x2-x1)+abs(y2-y1)+abs(x2-x1)
               // might be faster than using Math.abs?
    }
}
