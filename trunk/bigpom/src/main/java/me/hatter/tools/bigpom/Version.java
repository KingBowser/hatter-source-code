package me.hatter.tools.bigpom;

public class Version {

    int v0;
    int v1;
    int v2;

    public Version(int v0, int v1, int v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
    }

    public static Version parse(String v) {
        String[] vv = v.split("\\.");
        int v0 = 0;
        int v1 = 0;
        int v2 = 0;
        if (vv.length > 0) {
            v0 = Integer.parseInt(vv[0]);
        }
        if (vv.length > 1) {
            v1 = Integer.parseInt(vv[1]);
        }
        if (vv.length > 2) {
            v2 = Integer.parseInt(vv[2]);
        }
        return new Version(v0, v1, v2);
    }

    public int compareTo(Version ver2) {
        if (this.v0 != ver2.v0) {
            return this.v0 - ver2.v0;
        }
        if (this.v1 != ver2.v1) {
            return this.v1 - ver2.v1;
        }
        if (this.v2 != ver2.v2) {
            return this.v2 - ver2.v2;
        }
        return 0;
    }

    public String toString() {
        return v0 + "." + v1 + "." + v2;
    }
}
