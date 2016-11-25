/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.util.Vector
 */
package net.darepvp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class UtilMath {
    public static Random random = new Random();

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double trim(int degree, double d) {
        String format = "#.#";
        int i = 1;
        while (i < degree) {
            format = String.valueOf(format) + "#";
            ++i;
        }
        DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d));
    }

    public static int r(int i) {
        return random.nextInt(i);
    }

    public static double abs(double a) {
        return a <= 0.0 ? 0.0 - a : a;
    }

    public static String ArrayToString(String[] list) {
        String string = "";
        String[] arrstring = list;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String key = arrstring[n2];
            string = String.valueOf(string) + key + ",";
            ++n2;
        }
        if (string.length() != 0) {
            return string.substring(0, string.length() - 1);
        }
        return null;
    }

    public static String ArrayToString(List<String> list) {
        String string = "";
        for (String key : list) {
            string = String.valueOf(string) + key + ",";
        }
        if (string.length() != 0) {
            return string.substring(0, string.length() - 1);
        }
        return null;
    }

    public static String[] StringToArray(String string, String split) {
        return string.split(split);
    }

    public static double offset2d(Entity a, Entity b) {
        return UtilMath.offset2d(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset2d(Location a, Location b) {
        return UtilMath.offset2d(a.toVector(), b.toVector());
    }

    public static double offset2d(Vector a, Vector b) {
        a.setY(0);
        b.setY(0);
        return a.subtract(b).length();
    }

    public static double offset(Entity a, Entity b) {
        return UtilMath.offset(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset(Location a, Location b) {
        return UtilMath.offset(a.toVector(), b.toVector());
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    public static Vector getHorizontalVector(Vector v) {
        v.setY(0);
        return v;
    }

    public static Vector getVerticalVector(Vector v) {
        v.setX(0);
        v.setZ(0);
        return v;
    }

    public static String serializeLocation(Location location) {
        int X = (int)location.getX();
        int Y = (int)location.getY();
        int Z = (int)location.getZ();
        int P = (int)location.getPitch();
        int Yaw = (int)location.getYaw();
        return new String(String.valueOf(location.getWorld().getName()) + "," + X + "," + Y + "," + Z + "," + P + "," + Yaw);
    }

    public static Location deserializeLocation(String string) {
        String[] parts = string.split(",");
        World world = Bukkit.getServer().getWorld(parts[0]);
        Double LX = Double.parseDouble(parts[1]);
        Double LY = Double.parseDouble(parts[2]);
        Double LZ = Double.parseDouble(parts[3]);
        Float P = Float.valueOf(Float.parseFloat(parts[4]));
        Float Y = Float.valueOf(Float.parseFloat(parts[5]));
        Location result = new Location(world, LX.doubleValue(), LY.doubleValue(), LZ.doubleValue());
        result.setPitch(P.floatValue());
        result.setYaw(Y.floatValue());
        return result;
    }

    public static long averageLong(List<Long> list) {
        long add = 0;
        for (Long listlist : list) {
            add += listlist.longValue();
        }
        return add / (long)list.size();
    }

    public static double averageDouble(List<Double> list) {
        Double add = 0.0;
        for (Double listlist : list) {
            add = add + listlist;
        }
        return add / (double)list.size();
    }
}

