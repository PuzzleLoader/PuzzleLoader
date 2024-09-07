package com.github.puzzle.util;

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

}
