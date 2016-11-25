/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.util.NumberConversions
 *  org.bukkit.util.Vector
 */
package net.darepvp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.darepvp.util.UtilBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public final class UtilCheat {
    private static final List<Material> INSTANT_BREAK = new ArrayList<Material>();
    private static final List<Material> FOOD = new ArrayList<Material>();
    private static final List<Material> INTERACTABLE = new ArrayList<Material>();
    private static final Map<Material, Material> COMBO = new HashMap<Material, Material>();
    public static final String SPY_METADATA = "ac-spydata";
    private static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$Material;

    static {
        INSTANT_BREAK.add(Material.RED_MUSHROOM);
        INSTANT_BREAK.add(Material.RED_ROSE);
        INSTANT_BREAK.add(Material.BROWN_MUSHROOM);
        INSTANT_BREAK.add(Material.YELLOW_FLOWER);
        INSTANT_BREAK.add(Material.REDSTONE);
        INSTANT_BREAK.add(Material.REDSTONE_TORCH_OFF);
        INSTANT_BREAK.add(Material.REDSTONE_TORCH_ON);
        INSTANT_BREAK.add(Material.REDSTONE_WIRE);
        INSTANT_BREAK.add(Material.LONG_GRASS);
        INSTANT_BREAK.add(Material.PAINTING);
        INSTANT_BREAK.add(Material.WHEAT);
        INSTANT_BREAK.add(Material.SUGAR_CANE);
        INSTANT_BREAK.add(Material.SUGAR_CANE_BLOCK);
        INSTANT_BREAK.add(Material.DIODE);
        INSTANT_BREAK.add(Material.DIODE_BLOCK_OFF);
        INSTANT_BREAK.add(Material.DIODE_BLOCK_ON);
        INSTANT_BREAK.add(Material.SAPLING);
        INSTANT_BREAK.add(Material.TORCH);
        INSTANT_BREAK.add(Material.CROPS);
        INSTANT_BREAK.add(Material.SNOW);
        INSTANT_BREAK.add(Material.TNT);
        INSTANT_BREAK.add(Material.POTATO);
        INSTANT_BREAK.add(Material.CARROT);
        INTERACTABLE.add(Material.STONE_BUTTON);
        INTERACTABLE.add(Material.LEVER);
        INTERACTABLE.add(Material.CHEST);
        FOOD.add(Material.COOKED_BEEF);
        FOOD.add(Material.COOKED_CHICKEN);
        FOOD.add(Material.COOKED_FISH);
        FOOD.add(Material.GRILLED_PORK);
        FOOD.add(Material.PORK);
        FOOD.add(Material.MUSHROOM_SOUP);
        FOOD.add(Material.RAW_BEEF);
        FOOD.add(Material.RAW_CHICKEN);
        FOOD.add(Material.RAW_FISH);
        FOOD.add(Material.APPLE);
        FOOD.add(Material.GOLDEN_APPLE);
        FOOD.add(Material.MELON);
        FOOD.add(Material.COOKIE);
        FOOD.add(Material.BREAD);
        FOOD.add(Material.SPIDER_EYE);
        FOOD.add(Material.ROTTEN_FLESH);
        FOOD.add(Material.POTATO_ITEM);
        COMBO.put(Material.SHEARS, Material.WOOL);
        COMBO.put(Material.IRON_SWORD, Material.WEB);
        COMBO.put(Material.DIAMOND_SWORD, Material.WEB);
        COMBO.put(Material.STONE_SWORD, Material.WEB);
        COMBO.put(Material.WOOD_SWORD, Material.WEB);
    }

    public static boolean isSafeSetbackLocation(Player player) {
        if ((UtilCheat.isInWeb(player) || UtilCheat.isInWater(player) || !UtilCheat.cantStandAtSingle(player.getLocation().getBlock())) && !player.getEyeLocation().getBlock().getType().isSolid()) {
            return true;
        }
        return false;
    }

    public static double getXDelta(Location one, Location two) {
        return Math.abs(one.getX() - two.getX());
    }

    public static double getZDelta(Location one, Location two) {
        return Math.abs(one.getZ() - two.getZ());
    }

    public static double getDistance3D(Location one, Location two) {
        double toReturn = 0.0;
        double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }

    public static double getVerticalDistance(Location one, Location two) {
        double toReturn = 0.0;
        double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        double sqrt = Math.sqrt(ySqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }

    public static double getHorizontalDistance(Location one, Location two) {
        double toReturn = 0.0;
        double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        double sqrt = Math.sqrt(xSqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }

    public static boolean cantStandAtBetter(Block block) {
        boolean overAir1;
        Block otherBlock = block.getRelative(BlockFace.DOWN);
        boolean center1 = otherBlock.getType() == Material.AIR;
        boolean north1 = otherBlock.getRelative(BlockFace.NORTH).getType() == Material.AIR;
        boolean east1 = otherBlock.getRelative(BlockFace.EAST).getType() == Material.AIR;
        boolean south1 = otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.AIR;
        boolean west1 = otherBlock.getRelative(BlockFace.WEST).getType() == Material.AIR;
        boolean northeast1 = otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.AIR;
        boolean northwest1 = otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.AIR;
        boolean southeast1 = otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.AIR;
        boolean southwest1 = otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.AIR;
        boolean bl = overAir1 = otherBlock.getRelative(BlockFace.DOWN).getType() == Material.AIR || otherBlock.getRelative(BlockFace.DOWN).getType() == Material.WATER || otherBlock.getRelative(BlockFace.DOWN).getType() == Material.LAVA;
        if (center1 && north1 && east1 && south1 && west1 && northeast1 && southeast1 && northwest1 && southwest1 && overAir1) {
            return true;
        }
        return false;
    }

    public static boolean cantStandAtSingle(Block block) {
        Block otherBlock = block.getRelative(BlockFace.DOWN);
        boolean center = otherBlock.getType() == Material.AIR;
        return center;
    }

    public static boolean cantStandAtWater(Block block) {
        boolean sw;
        Block otherBlock = block.getRelative(BlockFace.DOWN);
        boolean isHover = block.getType() == Material.AIR;
        boolean n = otherBlock.getRelative(BlockFace.NORTH).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        boolean s = otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.WATER || otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_WATER;
        boolean e = otherBlock.getRelative(BlockFace.EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_WATER;
        boolean w = otherBlock.getRelative(BlockFace.WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_WATER;
        boolean ne = otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_WATER;
        boolean nw = otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_WATER;
        boolean se = otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        boolean bl = sw = otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_WATER;
        if (n && s && e && w && ne && nw && se && sw && isHover) {
            return true;
        }
        return false;
    }

    public static boolean canStandWithin(Block block) {
        boolean solid;
        boolean isSand = block.getType() == Material.SAND;
        boolean isGravel = block.getType() == Material.GRAVEL;
        boolean bl = solid = block.getType().isSolid() && !block.getType().name().toLowerCase().contains("door") && !block.getType().name().toLowerCase().contains("fence") && !block.getType().name().toLowerCase().contains("bars") && !block.getType().name().toLowerCase().contains("sign");
        if (!(isSand || isGravel || solid)) {
            return true;
        }
        return false;
    }

    public static Vector getRotation(Location one, Location two) {
        double dx = two.getX() - one.getX();
        double dy = two.getY() - one.getY();
        double dz = two.getZ() - one.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(dy, distanceXZ) * 180.0 / 3.141592653589793);
        return new Vector(yaw, pitch, 0.0f);
    }

    public static double clamp180(double theta) {
        if ((theta %= 360.0) >= 180.0) {
            theta -= 360.0;
        }
        if (theta < -180.0) {
            theta += 360.0;
        }
        return theta;
    }

    public static int getLevelForEnchantment(Player player, String enchantment) {
        try {
            Enchantment theEnchantment = Enchantment.getByName((String)enchantment);
            ItemStack[] arritemStack = player.getInventory().getArmorContents();
            int n = arritemStack.length;
            int n2 = 0;
            while (n2 < n) {
                ItemStack item = arritemStack[n2];
                if (item.containsEnchantment(theEnchantment)) {
                    return item.getEnchantmentLevel(theEnchantment);
                }
                ++n2;
            }
        }
        catch (Exception e) {
            return -1;
        }
        return -1;
    }

    public static boolean cantStandAt(Block block) {
        if (!UtilCheat.canStand(block) && UtilCheat.cantStandClose(block) && UtilCheat.cantStandFar(block)) {
            return true;
        }
        return false;
    }

    public static boolean cantStandAtExp(Location location) {
        return UtilCheat.cantStandAt(new Location(location.getWorld(), UtilCheat.fixXAxis(location.getX()), location.getY() - 0.01, (double)location.getBlockZ()).getBlock());
    }

    public static boolean cantStandClose(Block block) {
        if (!(UtilCheat.canStand(block.getRelative(BlockFace.NORTH)) || UtilCheat.canStand(block.getRelative(BlockFace.EAST)) || UtilCheat.canStand(block.getRelative(BlockFace.SOUTH)) || UtilCheat.canStand(block.getRelative(BlockFace.WEST)))) {
            return true;
        }
        return false;
    }

    public static boolean cantStandFar(Block block) {
        if (!(UtilCheat.canStand(block.getRelative(BlockFace.NORTH_WEST)) || UtilCheat.canStand(block.getRelative(BlockFace.NORTH_EAST)) || UtilCheat.canStand(block.getRelative(BlockFace.SOUTH_WEST)) || UtilCheat.canStand(block.getRelative(BlockFace.SOUTH_EAST)))) {
            return true;
        }
        return false;
    }

    public static boolean canStand(Block block) {
        if (!block.isLiquid() && block.getType() != Material.AIR) {
            return true;
        }
        return false;
    }

    public static boolean isFullyInWater(Location player) {
        double touchedX = UtilCheat.fixXAxis(player.getX());
        if (new Location(player.getWorld(), touchedX, player.getY(), (double)player.getBlockZ()).getBlock().isLiquid() && new Location(player.getWorld(), touchedX, (double)Math.round(player.getY()), (double)player.getBlockZ()).getBlock().isLiquid()) {
            return true;
        }
        return false;
    }

    public static double fixXAxis(double x) {
        double touchedX = x;
        double rem = touchedX - (double)Math.round(touchedX) + 0.01;
        if (rem < 0.3) {
            touchedX = NumberConversions.floor((double)x) - 1;
        }
        return touchedX;
    }

    public static boolean isHoveringOverWater(Location player, int blocks) {
        int i = player.getBlockY();
        while (i > player.getBlockY() - blocks) {
            Block newloc = new Location(player.getWorld(), (double)player.getBlockX(), (double)i, (double)player.getBlockZ()).getBlock();
            if (newloc.getType() != Material.AIR) {
                return newloc.isLiquid();
            }
            --i;
        }
        return false;
    }

    public static boolean isHoveringOverWater(Location player) {
        return UtilCheat.isHoveringOverWater(player, 25);
    }

    public static boolean isInstantBreak(Material m) {
        return INSTANT_BREAK.contains((Object)m);
    }

    public static boolean isFood(Material m) {
        return FOOD.contains((Object)m);
    }

    public static boolean isSlab(Block block) {
        Material type = block.getType();
        switch (UtilCheat.$SWITCH_TABLE$org$bukkit$Material()[type.ordinal()]) {
            case 44: 
            case 45: 
            case 127: 
            case 128: {
                return true;
            }
        }
        return false;
    }

    public static boolean isStair(Block block) {
        Material type = block.getType();
        switch (UtilCheat.$SWITCH_TABLE$org$bukkit$Material()[type.ordinal()]) {
            case 54: 
            case 68: 
            case 111: 
            case 116: 
            case 130: 
            case 136: 
            case 137: 
            case 138: 
            case 158: {
                return true;
            }
        }
        return false;
    }

    public static boolean isInteractable(Material m) {
        return INTERACTABLE.contains((Object)m);
    }

    public static boolean sprintFly(Player player) {
        if (!player.isSprinting() && !player.isFlying()) {
            return false;
        }
        return true;
    }

    public static boolean isOnLilyPad(Player player) {
        Block block = player.getLocation().getBlock();
        Material lily = Material.WATER_LILY;
        if (block.getType() != lily && block.getRelative(BlockFace.NORTH).getType() != lily && block.getRelative(BlockFace.SOUTH).getType() != lily && block.getRelative(BlockFace.EAST).getType() != lily && block.getRelative(BlockFace.WEST).getType() != lily) {
            return false;
        }
        return true;
    }

    public static boolean isSubmersed(Player player) {
        if (player.getLocation().getBlock().isLiquid() && player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid()) {
            return true;
        }
        return false;
    }

    public static boolean isInWater(Player player) {
        if (!(player.getLocation().getBlock().isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid())) {
            return false;
        }
        return true;
    }

    public static boolean isInWeb(Player player) {
        if (player.getLocation().getBlock().getType() != Material.WEB && player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.WEB && player.getLocation().getBlock().getRelative(BlockFace.UP).getType() != Material.WEB) {
            return false;
        }
        return true;
    }

    public static boolean isClimbableBlock(Block block) {
        if (block.getType() != Material.VINE && block.getType() != Material.LADDER && block.getType() != Material.WATER && block.getType() != Material.STATIONARY_WATER) {
            return false;
        }
        return true;
    }

    public static boolean isOnVine(Player player) {
        if (player.getLocation().getBlock().getType() == Material.VINE) {
            return true;
        }
        return false;
    }

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (Exception var1_1) {
            return false;
        }
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        }
        catch (Exception var1_1) {
            return false;
        }
    }

    public static boolean blocksNear(Player player) {
        return UtilCheat.blocksNear(player.getLocation());
    }

    public static boolean blocksNear(Location loc) {
        boolean nearBlocks = false;
        for (Block block2 : UtilBlock.getSurrounding(loc.getBlock(), true)) {
            if (block2.getType() == Material.AIR) continue;
            nearBlocks = true;
            break;
        }
        for (Block block2 : UtilBlock.getSurrounding(loc.getBlock(), false)) {
            if (block2.getType() == Material.AIR) continue;
            nearBlocks = true;
            break;
        }
        Location a = loc;
        a.setY(a.getY() - 0.5);
        if (a.getBlock().getType() != Material.AIR) {
            nearBlocks = true;
        }
        if (UtilCheat.isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER})) {
            nearBlocks = true;
        }
        return nearBlocks;
    }

    public static boolean slabsNear(Location loc) {
        boolean nearBlocks = false;
        for (Block bl2 : UtilBlock.getSurrounding(loc.getBlock(), true)) {
            if (!bl2.getType().equals((Object)Material.STEP) && !bl2.getType().equals((Object)Material.DOUBLE_STEP) && !bl2.getType().equals((Object)Material.WOOD_DOUBLE_STEP) && !bl2.getType().equals((Object)Material.WOOD_STEP)) continue;
            nearBlocks = true;
            break;
        }
        for (Block bl2 : UtilBlock.getSurrounding(loc.getBlock(), false)) {
            if (!bl2.getType().equals((Object)Material.STEP) && !bl2.getType().equals((Object)Material.DOUBLE_STEP) && !bl2.getType().equals((Object)Material.WOOD_DOUBLE_STEP) && !bl2.getType().equals((Object)Material.WOOD_STEP)) continue;
            nearBlocks = true;
            break;
        }
        if (UtilCheat.isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.STEP, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP})) {
            nearBlocks = true;
        }
        return nearBlocks;
    }

    public static boolean isBlock(Block block, Material[] materials) {
        Material type = block.getType();
        Material[] arrmaterial = materials;
        int n = arrmaterial.length;
        int n2 = 0;
        while (n2 < n) {
            Material m = arrmaterial[n2];
            if (m == type) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public static String[] getCommands(String command) {
        return command.replaceAll("COMMAND\\[", "").replaceAll("]", "").split(";");
    }

    public static String removeWhitespace(String string) {
        return string.replaceAll(" ", "");
    }

    public static boolean hasArmorEnchantment(Player player, Enchantment e) {
        ItemStack[] arritemStack = player.getInventory().getArmorContents();
        int n = arritemStack.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack is = arritemStack[n2];
            if (is != null && is.containsEnchantment(e)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public static String listToCommaString(List<String> list) {
        StringBuilder b = new StringBuilder();
        int i = 0;
        while (i < list.size()) {
            b.append(list.get(i));
            if (i < list.size() - 1) {
                b.append(",");
            }
            ++i;
        }
        return b.toString();
    }

    public static long lifeToSeconds(String string) {
        if (string.equals("0") || string.equals("")) {
            return 0;
        }
        String[] lifeMatch = new String[]{"d", "h", "m", "s"};
        int[] lifeInterval = new int[]{86400, 3600, 60, 1};
        long seconds = 0;
        int i = 0;
        while (i < lifeMatch.length) {
            Matcher matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(string);
            while (matcher.find()) {
                seconds += (long)(Integer.parseInt(matcher.group(1)) * lifeInterval[i]);
            }
            ++i;
        }
        return seconds;
    }

    public static double[] cursor(Player player, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = UtilCheat.getRotation(playerLoc, entityLoc);
        double deltaYaw = UtilCheat.clamp180(playerRotation.getX() - expectedRotation.getX());
        double deltaPitch = UtilCheat.clamp180(playerRotation.getY() - expectedRotation.getY());
        double horizontalDistance = UtilCheat.getHorizontalDistance(playerLoc, entityLoc);
        double distance = UtilCheat.getDistance3D(playerLoc, entityLoc);
        double offsetX = deltaYaw * horizontalDistance * distance;
        double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return new double[]{Math.abs(offsetX), Math.abs(offsetY)};
    }

    public static double getAimbotoffset(Location playerLocLoc, double playerEyeHeight, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = playerLocLoc.add(0.0, playerEyeHeight, 0.0);
        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = UtilCheat.getRotation(playerLoc, entityLoc);
        double deltaYaw = UtilCheat.clamp180(playerRotation.getX() - expectedRotation.getX());
        double horizontalDistance = UtilCheat.getHorizontalDistance(playerLoc, entityLoc);
        double distance = UtilCheat.getDistance3D(playerLoc, entityLoc);
        double offsetX = deltaYaw * horizontalDistance * distance;
        return offsetX;
    }

    public static double getAimbotoffset2(Location playerLocLoc, double playerEyeHeight, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = playerLocLoc.add(0.0, playerEyeHeight, 0.0);
        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = UtilCheat.getRotation(playerLoc, entityLoc);
        double deltaPitch = UtilCheat.clamp180(playerRotation.getY() - expectedRotation.getY());
        double distance = UtilCheat.getDistance3D(playerLoc, entityLoc);
        double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return offsetY;
    }

    public static double[] getOffsetsOffCursor(Player player, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = UtilCheat.getRotation(playerLoc, entityLoc);
        double deltaYaw = UtilCheat.clamp180(playerRotation.getX() - expectedRotation.getX());
        double deltaPitch = UtilCheat.clamp180(playerRotation.getY() - expectedRotation.getY());
        double horizontalDistance = UtilCheat.getHorizontalDistance(playerLoc, entityLoc);
        double distance = UtilCheat.getDistance3D(playerLoc, entityLoc);
        double offsetX = deltaYaw * horizontalDistance * distance;
        double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return new double[]{Math.abs(offsetX), Math.abs(offsetY)};
    }

    public static double getOffsetOffCursor(Player player, LivingEntity entity) {
        double offset = 0.0;
        double[] offsets = UtilCheat.getOffsetsOffCursor(player, entity);
        offset += offsets[0];
        return offset += offsets[1];
    }

    static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$Material() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$org$bukkit$Material;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[Material.values().length];
        try {
            arrn[Material.ACACIA_STAIRS.ordinal()] = 165;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[Material.ACTIVATOR_RAIL.ordinal()] = 159;
        }
        catch (NoSuchFieldError v2) {}
        try {
            arrn[Material.AIR.ordinal()] = 1;
        }
        catch (NoSuchFieldError v3) {}
        try {
            arrn[Material.ANVIL.ordinal()] = 147;
        }
        catch (NoSuchFieldError v4) {}
        try {
            arrn[Material.APPLE.ordinal()] = 177;
        }
        catch (NoSuchFieldError v5) {}
        try {
            arrn[Material.ARROW.ordinal()] = 179;
        }
        catch (NoSuchFieldError v6) {}
        try {
            arrn[Material.BAKED_POTATO.ordinal()] = 310;
        }
        catch (NoSuchFieldError v7) {}
        try {
            arrn[Material.BEACON.ordinal()] = 140;
        }
        catch (NoSuchFieldError v8) {}
        try {
            arrn[Material.BED.ordinal()] = 272;
        }
        catch (NoSuchFieldError v9) {}
        try {
            arrn[Material.BEDROCK.ordinal()] = 8;
        }
        catch (NoSuchFieldError v10) {}
        try {
            arrn[Material.BED_BLOCK.ordinal()] = 27;
        }
        catch (NoSuchFieldError v11) {}
        try {
            arrn[Material.BIRCH_WOOD_STAIRS.ordinal()] = 137;
        }
        catch (NoSuchFieldError v12) {}
        try {
            arrn[Material.BLAZE_POWDER.ordinal()] = 294;
        }
        catch (NoSuchFieldError v13) {}
        try {
            arrn[Material.BLAZE_ROD.ordinal()] = 286;
        }
        catch (NoSuchFieldError v14) {}
        try {
            arrn[Material.BOAT.ordinal()] = 250;
        }
        catch (NoSuchFieldError v15) {}
        try {
            arrn[Material.BONE.ordinal()] = 269;
        }
        catch (NoSuchFieldError v16) {}
        try {
            arrn[Material.BOOK.ordinal()] = 257;
        }
        catch (NoSuchFieldError v17) {}
        try {
            arrn[Material.BOOKSHELF.ordinal()] = 48;
        }
        catch (NoSuchFieldError v18) {}
        try {
            arrn[Material.BOOK_AND_QUILL.ordinal()] = 303;
        }
        catch (NoSuchFieldError v19) {}
        try {
            arrn[Material.BOW.ordinal()] = 178;
        }
        catch (NoSuchFieldError v20) {}
        try {
            arrn[Material.BOWL.ordinal()] = 198;
        }
        catch (NoSuchFieldError v21) {}
        try {
            arrn[Material.BREAD.ordinal()] = 214;
        }
        catch (NoSuchFieldError v22) {}
        try {
            arrn[Material.BREWING_STAND.ordinal()] = 119;
        }
        catch (NoSuchFieldError v23) {}
        try {
            arrn[Material.BREWING_STAND_ITEM.ordinal()] = 296;
        }
        catch (NoSuchFieldError v24) {}
        try {
            arrn[Material.BRICK.ordinal()] = 46;
        }
        catch (NoSuchFieldError v25) {}
        try {
            arrn[Material.BRICK_STAIRS.ordinal()] = 110;
        }
        catch (NoSuchFieldError v26) {}
        try {
            arrn[Material.BROWN_MUSHROOM.ordinal()] = 40;
        }
        catch (NoSuchFieldError v27) {}
        try {
            arrn[Material.BUCKET.ordinal()] = 242;
        }
        catch (NoSuchFieldError v28) {}
        try {
            arrn[Material.BURNING_FURNACE.ordinal()] = 63;
        }
        catch (NoSuchFieldError v29) {}
        try {
            arrn[Material.CACTUS.ordinal()] = 82;
        }
        catch (NoSuchFieldError v30) {}
        try {
            arrn[Material.CAKE.ordinal()] = 271;
        }
        catch (NoSuchFieldError v31) {}
        try {
            arrn[Material.CAKE_BLOCK.ordinal()] = 93;
        }
        catch (NoSuchFieldError v32) {}
        try {
            arrn[Material.CARPET.ordinal()] = 168;
        }
        catch (NoSuchFieldError v33) {}
        try {
            arrn[Material.CARROT.ordinal()] = 143;
        }
        catch (NoSuchFieldError v34) {}
        try {
            arrn[Material.CARROT_ITEM.ordinal()] = 308;
        }
        catch (NoSuchFieldError v35) {}
        try {
            arrn[Material.CARROT_STICK.ordinal()] = 315;
        }
        catch (NoSuchFieldError v36) {}
        try {
            arrn[Material.CAULDRON.ordinal()] = 120;
        }
        catch (NoSuchFieldError v37) {}
        try {
            arrn[Material.CAULDRON_ITEM.ordinal()] = 297;
        }
        catch (NoSuchFieldError v38) {}
        try {
            arrn[Material.CHAINMAIL_BOOTS.ordinal()] = 222;
        }
        catch (NoSuchFieldError v39) {}
        try {
            arrn[Material.CHAINMAIL_CHESTPLATE.ordinal()] = 220;
        }
        catch (NoSuchFieldError v40) {}
        try {
            arrn[Material.CHAINMAIL_HELMET.ordinal()] = 219;
        }
        catch (NoSuchFieldError v41) {}
        try {
            arrn[Material.CHAINMAIL_LEGGINGS.ordinal()] = 221;
        }
        catch (NoSuchFieldError v42) {}
        try {
            arrn[Material.CHEST.ordinal()] = 55;
        }
        catch (NoSuchFieldError v43) {}
        try {
            arrn[Material.CLAY.ordinal()] = 83;
        }
        catch (NoSuchFieldError v44) {}
        try {
            arrn[Material.CLAY_BALL.ordinal()] = 254;
        }
        catch (NoSuchFieldError v45) {}
        try {
            arrn[Material.CLAY_BRICK.ordinal()] = 253;
        }
        catch (NoSuchFieldError v46) {}
        try {
            arrn[Material.COAL.ordinal()] = 180;
        }
        catch (NoSuchFieldError v47) {}
        try {
            arrn[Material.COAL_BLOCK.ordinal()] = 170;
        }
        catch (NoSuchFieldError v48) {}
        try {
            arrn[Material.COAL_ORE.ordinal()] = 17;
        }
        catch (NoSuchFieldError v49) {}
        try {
            arrn[Material.COBBLESTONE.ordinal()] = 5;
        }
        catch (NoSuchFieldError v50) {}
        try {
            arrn[Material.COBBLESTONE_STAIRS.ordinal()] = 68;
        }
        catch (NoSuchFieldError v51) {}
        try {
            arrn[Material.COBBLE_WALL.ordinal()] = 141;
        }
        catch (NoSuchFieldError v52) {}
        try {
            arrn[Material.COCOA.ordinal()] = 129;
        }
        catch (NoSuchFieldError v53) {}
        try {
            arrn[Material.COMMAND.ordinal()] = 139;
        }
        catch (NoSuchFieldError v54) {}
        try {
            arrn[Material.COMMAND_MINECART.ordinal()] = 331;
        }
        catch (NoSuchFieldError v55) {}
        try {
            arrn[Material.COMPASS.ordinal()] = 262;
        }
        catch (NoSuchFieldError v56) {}
        try {
            arrn[Material.COOKED_BEEF.ordinal()] = 281;
        }
        catch (NoSuchFieldError v57) {}
        try {
            arrn[Material.COOKED_CHICKEN.ordinal()] = 283;
        }
        catch (NoSuchFieldError v58) {}
        try {
            arrn[Material.COOKED_FISH.ordinal()] = 267;
        }
        catch (NoSuchFieldError v59) {}
        try {
            arrn[Material.COOKIE.ordinal()] = 274;
        }
        catch (NoSuchFieldError v60) {}
        try {
            arrn[Material.CROPS.ordinal()] = 60;
        }
        catch (NoSuchFieldError v61) {}
        try {
            arrn[Material.DARK_OAK_STAIRS.ordinal()] = 166;
        }
        catch (NoSuchFieldError v62) {}
        try {
            arrn[Material.DAYLIGHT_DETECTOR.ordinal()] = 153;
        }
        catch (NoSuchFieldError v63) {}
        try {
            arrn[Material.DEAD_BUSH.ordinal()] = 33;
        }
        catch (NoSuchFieldError v64) {}
        try {
            arrn[Material.DETECTOR_RAIL.ordinal()] = 29;
        }
        catch (NoSuchFieldError v65) {}
        try {
            arrn[Material.DIAMOND.ordinal()] = 181;
        }
        catch (NoSuchFieldError v66) {}
        try {
            arrn[Material.DIAMOND_AXE.ordinal()] = 196;
        }
        catch (NoSuchFieldError v67) {}
        try {
            arrn[Material.DIAMOND_BARDING.ordinal()] = 328;
        }
        catch (NoSuchFieldError v68) {}
        try {
            arrn[Material.DIAMOND_BLOCK.ordinal()] = 58;
        }
        catch (NoSuchFieldError v69) {}
        try {
            arrn[Material.DIAMOND_BOOTS.ordinal()] = 230;
        }
        catch (NoSuchFieldError v70) {}
        try {
            arrn[Material.DIAMOND_CHESTPLATE.ordinal()] = 228;
        }
        catch (NoSuchFieldError v71) {}
        try {
            arrn[Material.DIAMOND_HELMET.ordinal()] = 227;
        }
        catch (NoSuchFieldError v72) {}
        try {
            arrn[Material.DIAMOND_HOE.ordinal()] = 210;
        }
        catch (NoSuchFieldError v73) {}
        try {
            arrn[Material.DIAMOND_LEGGINGS.ordinal()] = 229;
        }
        catch (NoSuchFieldError v74) {}
        try {
            arrn[Material.DIAMOND_ORE.ordinal()] = 57;
        }
        catch (NoSuchFieldError v75) {}
        try {
            arrn[Material.DIAMOND_PICKAXE.ordinal()] = 195;
        }
        catch (NoSuchFieldError v76) {}
        try {
            arrn[Material.DIAMOND_SPADE.ordinal()] = 194;
        }
        catch (NoSuchFieldError v77) {}
        try {
            arrn[Material.DIAMOND_SWORD.ordinal()] = 193;
        }
        catch (NoSuchFieldError v78) {}
        try {
            arrn[Material.DIODE.ordinal()] = 273;
        }
        catch (NoSuchFieldError v79) {}
        try {
            arrn[Material.DIODE_BLOCK_OFF.ordinal()] = 94;
        }
        catch (NoSuchFieldError v80) {}
        try {
            arrn[Material.DIODE_BLOCK_ON.ordinal()] = 95;
        }
        catch (NoSuchFieldError v81) {}
        try {
            arrn[Material.DIRT.ordinal()] = 4;
        }
        catch (NoSuchFieldError v82) {}
        try {
            arrn[Material.DISPENSER.ordinal()] = 24;
        }
        catch (NoSuchFieldError v83) {}
        try {
            arrn[Material.DOUBLE_PLANT.ordinal()] = 172;
        }
        catch (NoSuchFieldError v84) {}
        try {
            arrn[Material.DOUBLE_STEP.ordinal()] = 44;
        }
        catch (NoSuchFieldError v85) {}
        try {
            arrn[Material.DRAGON_EGG.ordinal()] = 124;
        }
        catch (NoSuchFieldError v86) {}
        try {
            arrn[Material.DROPPER.ordinal()] = 160;
        }
        catch (NoSuchFieldError v87) {}
        try {
            arrn[Material.EGG.ordinal()] = 261;
        }
        catch (NoSuchFieldError v88) {}
        try {
            arrn[Material.EMERALD.ordinal()] = 305;
        }
        catch (NoSuchFieldError v89) {}
        try {
            arrn[Material.EMERALD_BLOCK.ordinal()] = 135;
        }
        catch (NoSuchFieldError v90) {}
        try {
            arrn[Material.EMERALD_ORE.ordinal()] = 131;
        }
        catch (NoSuchFieldError v91) {}
        try {
            arrn[Material.EMPTY_MAP.ordinal()] = 312;
        }
        catch (NoSuchFieldError v92) {}
        try {
            arrn[Material.ENCHANTED_BOOK.ordinal()] = 320;
        }
        catch (NoSuchFieldError v93) {}
        try {
            arrn[Material.ENCHANTMENT_TABLE.ordinal()] = 118;
        }
        catch (NoSuchFieldError v94) {}
        try {
            arrn[Material.ENDER_CHEST.ordinal()] = 132;
        }
        catch (NoSuchFieldError v95) {}
        try {
            arrn[Material.ENDER_PEARL.ordinal()] = 285;
        }
        catch (NoSuchFieldError v96) {}
        try {
            arrn[Material.ENDER_PORTAL.ordinal()] = 121;
        }
        catch (NoSuchFieldError v97) {}
        try {
            arrn[Material.ENDER_PORTAL_FRAME.ordinal()] = 122;
        }
        catch (NoSuchFieldError v98) {}
        try {
            arrn[Material.ENDER_STONE.ordinal()] = 123;
        }
        catch (NoSuchFieldError v99) {}
        try {
            arrn[Material.EXPLOSIVE_MINECART.ordinal()] = 324;
        }
        catch (NoSuchFieldError v100) {}
        try {
            arrn[Material.EXP_BOTTLE.ordinal()] = 301;
        }
        catch (NoSuchFieldError v101) {}
        try {
            arrn[Material.EYE_OF_ENDER.ordinal()] = 298;
        }
        catch (NoSuchFieldError v102) {}
        try {
            arrn[Material.FEATHER.ordinal()] = 205;
        }
        catch (NoSuchFieldError v103) {}
        try {
            arrn[Material.FENCE.ordinal()] = 86;
        }
        catch (NoSuchFieldError v104) {}
        try {
            arrn[Material.FENCE_GATE.ordinal()] = 109;
        }
        catch (NoSuchFieldError v105) {}
        try {
            arrn[Material.FERMENTED_SPIDER_EYE.ordinal()] = 293;
        }
        catch (NoSuchFieldError v106) {}
        try {
            arrn[Material.FIRE.ordinal()] = 52;
        }
        catch (NoSuchFieldError v107) {}
        try {
            arrn[Material.FIREBALL.ordinal()] = 302;
        }
        catch (NoSuchFieldError v108) {}
        try {
            arrn[Material.FIREWORK.ordinal()] = 318;
        }
        catch (NoSuchFieldError v109) {}
        try {
            arrn[Material.FIREWORK_CHARGE.ordinal()] = 319;
        }
        catch (NoSuchFieldError v110) {}
        try {
            arrn[Material.FISHING_ROD.ordinal()] = 263;
        }
        catch (NoSuchFieldError v111) {}
        try {
            arrn[Material.FLINT.ordinal()] = 235;
        }
        catch (NoSuchFieldError v112) {}
        try {
            arrn[Material.FLINT_AND_STEEL.ordinal()] = 176;
        }
        catch (NoSuchFieldError v113) {}
        try {
            arrn[Material.FLOWER_POT.ordinal()] = 142;
        }
        catch (NoSuchFieldError v114) {}
        try {
            arrn[Material.FLOWER_POT_ITEM.ordinal()] = 307;
        }
        catch (NoSuchFieldError v115) {}
        try {
            arrn[Material.FURNACE.ordinal()] = 62;
        }
        catch (NoSuchFieldError v116) {}
        try {
            arrn[Material.GHAST_TEAR.ordinal()] = 287;
        }
        catch (NoSuchFieldError v117) {}
        try {
            arrn[Material.GLASS.ordinal()] = 21;
        }
        catch (NoSuchFieldError v118) {}
        try {
            arrn[Material.GLASS_BOTTLE.ordinal()] = 291;
        }
        catch (NoSuchFieldError v119) {}
        try {
            arrn[Material.GLOWING_REDSTONE_ORE.ordinal()] = 75;
        }
        catch (NoSuchFieldError v120) {}
        try {
            arrn[Material.GLOWSTONE.ordinal()] = 90;
        }
        catch (NoSuchFieldError v121) {}
        try {
            arrn[Material.GLOWSTONE_DUST.ordinal()] = 265;
        }
        catch (NoSuchFieldError v122) {}
        try {
            arrn[Material.GOLDEN_APPLE.ordinal()] = 239;
        }
        catch (NoSuchFieldError v123) {}
        try {
            arrn[Material.GOLDEN_CARROT.ordinal()] = 313;
        }
        catch (NoSuchFieldError v124) {}
        try {
            arrn[Material.GOLD_AXE.ordinal()] = 203;
        }
        catch (NoSuchFieldError v125) {}
        try {
            arrn[Material.GOLD_BARDING.ordinal()] = 327;
        }
        catch (NoSuchFieldError v126) {}
        try {
            arrn[Material.GOLD_BLOCK.ordinal()] = 42;
        }
        catch (NoSuchFieldError v127) {}
        try {
            arrn[Material.GOLD_BOOTS.ordinal()] = 234;
        }
        catch (NoSuchFieldError v128) {}
        try {
            arrn[Material.GOLD_CHESTPLATE.ordinal()] = 232;
        }
        catch (NoSuchFieldError v129) {}
        try {
            arrn[Material.GOLD_HELMET.ordinal()] = 231;
        }
        catch (NoSuchFieldError v130) {}
        try {
            arrn[Material.GOLD_HOE.ordinal()] = 211;
        }
        catch (NoSuchFieldError v131) {}
        try {
            arrn[Material.GOLD_INGOT.ordinal()] = 183;
        }
        catch (NoSuchFieldError v132) {}
        try {
            arrn[Material.GOLD_LEGGINGS.ordinal()] = 233;
        }
        catch (NoSuchFieldError v133) {}
        try {
            arrn[Material.GOLD_NUGGET.ordinal()] = 288;
        }
        catch (NoSuchFieldError v134) {}
        try {
            arrn[Material.GOLD_ORE.ordinal()] = 15;
        }
        catch (NoSuchFieldError v135) {}
        try {
            arrn[Material.GOLD_PICKAXE.ordinal()] = 202;
        }
        catch (NoSuchFieldError v136) {}
        try {
            arrn[Material.GOLD_PLATE.ordinal()] = 149;
        }
        catch (NoSuchFieldError v137) {}
        try {
            arrn[Material.GOLD_RECORD.ordinal()] = 332;
        }
        catch (NoSuchFieldError v138) {}
        try {
            arrn[Material.GOLD_SPADE.ordinal()] = 201;
        }
        catch (NoSuchFieldError v139) {}
        try {
            arrn[Material.GOLD_SWORD.ordinal()] = 200;
        }
        catch (NoSuchFieldError v140) {}
        try {
            arrn[Material.GRASS.ordinal()] = 3;
        }
        catch (NoSuchFieldError v141) {}
        try {
            arrn[Material.GRAVEL.ordinal()] = 14;
        }
        catch (NoSuchFieldError v142) {}
        try {
            arrn[Material.GREEN_RECORD.ordinal()] = 333;
        }
        catch (NoSuchFieldError v143) {}
        try {
            arrn[Material.GRILLED_PORK.ordinal()] = 237;
        }
        catch (NoSuchFieldError v144) {}
        try {
            arrn[Material.HARD_CLAY.ordinal()] = 169;
        }
        catch (NoSuchFieldError v145) {}
        try {
            arrn[Material.HAY_BLOCK.ordinal()] = 167;
        }
        catch (NoSuchFieldError v146) {}
        try {
            arrn[Material.HOPPER.ordinal()] = 156;
        }
        catch (NoSuchFieldError v147) {}
        try {
            arrn[Material.HOPPER_MINECART.ordinal()] = 325;
        }
        catch (NoSuchFieldError v148) {}
        try {
            arrn[Material.HUGE_MUSHROOM_1.ordinal()] = 101;
        }
        catch (NoSuchFieldError v149) {}
        try {
            arrn[Material.HUGE_MUSHROOM_2.ordinal()] = 102;
        }
        catch (NoSuchFieldError v150) {}
        try {
            arrn[Material.ICE.ordinal()] = 80;
        }
        catch (NoSuchFieldError v151) {}
        try {
            arrn[Material.INK_SACK.ordinal()] = 268;
        }
        catch (NoSuchFieldError v152) {}
        try {
            arrn[Material.IRON_AXE.ordinal()] = 175;
        }
        catch (NoSuchFieldError v153) {}
        try {
            arrn[Material.IRON_BARDING.ordinal()] = 326;
        }
        catch (NoSuchFieldError v154) {}
        try {
            arrn[Material.IRON_BLOCK.ordinal()] = 43;
        }
        catch (NoSuchFieldError v155) {}
        try {
            arrn[Material.IRON_BOOTS.ordinal()] = 226;
        }
        catch (NoSuchFieldError v156) {}
        try {
            arrn[Material.IRON_CHESTPLATE.ordinal()] = 224;
        }
        catch (NoSuchFieldError v157) {}
        try {
            arrn[Material.IRON_DOOR.ordinal()] = 247;
        }
        catch (NoSuchFieldError v158) {}
        try {
            arrn[Material.IRON_DOOR_BLOCK.ordinal()] = 72;
        }
        catch (NoSuchFieldError v159) {}
        try {
            arrn[Material.IRON_FENCE.ordinal()] = 103;
        }
        catch (NoSuchFieldError v160) {}
        try {
            arrn[Material.IRON_HELMET.ordinal()] = 223;
        }
        catch (NoSuchFieldError v161) {}
        try {
            arrn[Material.IRON_HOE.ordinal()] = 209;
        }
        catch (NoSuchFieldError v162) {}
        try {
            arrn[Material.IRON_INGOT.ordinal()] = 182;
        }
        catch (NoSuchFieldError v163) {}
        try {
            arrn[Material.IRON_LEGGINGS.ordinal()] = 225;
        }
        catch (NoSuchFieldError v164) {}
        try {
            arrn[Material.IRON_ORE.ordinal()] = 16;
        }
        catch (NoSuchFieldError v165) {}
        try {
            arrn[Material.IRON_PICKAXE.ordinal()] = 174;
        }
        catch (NoSuchFieldError v166) {}
        try {
            arrn[Material.IRON_PLATE.ordinal()] = 150;
        }
        catch (NoSuchFieldError v167) {}
        try {
            arrn[Material.IRON_SPADE.ordinal()] = 173;
        }
        catch (NoSuchFieldError v168) {}
        try {
            arrn[Material.IRON_SWORD.ordinal()] = 184;
        }
        catch (NoSuchFieldError v169) {}
        try {
            arrn[Material.ITEM_FRAME.ordinal()] = 306;
        }
        catch (NoSuchFieldError v170) {}
        try {
            arrn[Material.JACK_O_LANTERN.ordinal()] = 92;
        }
        catch (NoSuchFieldError v171) {}
        try {
            arrn[Material.JUKEBOX.ordinal()] = 85;
        }
        catch (NoSuchFieldError v172) {}
        try {
            arrn[Material.JUNGLE_WOOD_STAIRS.ordinal()] = 138;
        }
        catch (NoSuchFieldError v173) {}
        try {
            arrn[Material.LADDER.ordinal()] = 66;
        }
        catch (NoSuchFieldError v174) {}
        try {
            arrn[Material.LAPIS_BLOCK.ordinal()] = 23;
        }
        catch (NoSuchFieldError v175) {}
        try {
            arrn[Material.LAPIS_ORE.ordinal()] = 22;
        }
        catch (NoSuchFieldError v176) {}
        try {
            arrn[Material.LAVA.ordinal()] = 11;
        }
        catch (NoSuchFieldError v177) {}
        try {
            arrn[Material.LAVA_BUCKET.ordinal()] = 244;
        }
        catch (NoSuchFieldError v178) {}
        try {
            arrn[Material.LEASH.ordinal()] = 329;
        }
        catch (NoSuchFieldError v179) {}
        try {
            arrn[Material.LEATHER.ordinal()] = 251;
        }
        catch (NoSuchFieldError v180) {}
        try {
            arrn[Material.LEATHER_BOOTS.ordinal()] = 218;
        }
        catch (NoSuchFieldError v181) {}
        try {
            arrn[Material.LEATHER_CHESTPLATE.ordinal()] = 216;
        }
        catch (NoSuchFieldError v182) {}
        try {
            arrn[Material.LEATHER_HELMET.ordinal()] = 215;
        }
        catch (NoSuchFieldError v183) {}
        try {
            arrn[Material.LEATHER_LEGGINGS.ordinal()] = 217;
        }
        catch (NoSuchFieldError v184) {}
        try {
            arrn[Material.LEAVES.ordinal()] = 19;
        }
        catch (NoSuchFieldError v185) {}
        try {
            arrn[Material.LEAVES_2.ordinal()] = 163;
        }
        catch (NoSuchFieldError v186) {}
        try {
            arrn[Material.LEVER.ordinal()] = 70;
        }
        catch (NoSuchFieldError v187) {}
        try {
            arrn[Material.LOCKED_CHEST.ordinal()] = 96;
        }
        catch (NoSuchFieldError v188) {}
        try {
            arrn[Material.LOG.ordinal()] = 18;
        }
        catch (NoSuchFieldError v189) {}
        try {
            arrn[Material.LOG_2.ordinal()] = 164;
        }
        catch (NoSuchFieldError v190) {}
        try {
            arrn[Material.LONG_GRASS.ordinal()] = 32;
        }
        catch (NoSuchFieldError v191) {}
        try {
            arrn[Material.MAGMA_CREAM.ordinal()] = 295;
        }
        catch (NoSuchFieldError v192) {}
        try {
            arrn[Material.MAP.ordinal()] = 275;
        }
        catch (NoSuchFieldError v193) {}
        try {
            arrn[Material.MELON.ordinal()] = 277;
        }
        catch (NoSuchFieldError v194) {}
        try {
            arrn[Material.MELON_BLOCK.ordinal()] = 105;
        }
        catch (NoSuchFieldError v195) {}
        try {
            arrn[Material.MELON_SEEDS.ordinal()] = 279;
        }
        catch (NoSuchFieldError v196) {}
        try {
            arrn[Material.MELON_STEM.ordinal()] = 107;
        }
        catch (NoSuchFieldError v197) {}
        try {
            arrn[Material.MILK_BUCKET.ordinal()] = 252;
        }
        catch (NoSuchFieldError v198) {}
        try {
            arrn[Material.MINECART.ordinal()] = 245;
        }
        catch (NoSuchFieldError v199) {}
        try {
            arrn[Material.MOB_SPAWNER.ordinal()] = 53;
        }
        catch (NoSuchFieldError v200) {}
        try {
            arrn[Material.MONSTER_EGG.ordinal()] = 300;
        }
        catch (NoSuchFieldError v201) {}
        try {
            arrn[Material.MONSTER_EGGS.ordinal()] = 99;
        }
        catch (NoSuchFieldError v202) {}
        try {
            arrn[Material.MOSSY_COBBLESTONE.ordinal()] = 49;
        }
        catch (NoSuchFieldError v203) {}
        try {
            arrn[Material.MUSHROOM_SOUP.ordinal()] = 199;
        }
        catch (NoSuchFieldError v204) {}
        try {
            arrn[Material.MYCEL.ordinal()] = 112;
        }
        catch (NoSuchFieldError v205) {}
        try {
            arrn[Material.NAME_TAG.ordinal()] = 330;
        }
        catch (NoSuchFieldError v206) {}
        try {
            arrn[Material.NETHERRACK.ordinal()] = 88;
        }
        catch (NoSuchFieldError v207) {}
        try {
            arrn[Material.NETHER_BRICK.ordinal()] = 114;
        }
        catch (NoSuchFieldError v208) {}
        try {
            arrn[Material.NETHER_BRICK_ITEM.ordinal()] = 322;
        }
        catch (NoSuchFieldError v209) {}
        try {
            arrn[Material.NETHER_BRICK_STAIRS.ordinal()] = 116;
        }
        catch (NoSuchFieldError v210) {}
        try {
            arrn[Material.NETHER_FENCE.ordinal()] = 115;
        }
        catch (NoSuchFieldError v211) {}
        try {
            arrn[Material.NETHER_STALK.ordinal()] = 289;
        }
        catch (NoSuchFieldError v212) {}
        try {
            arrn[Material.NETHER_STAR.ordinal()] = 316;
        }
        catch (NoSuchFieldError v213) {}
        try {
            arrn[Material.NETHER_WARTS.ordinal()] = 117;
        }
        catch (NoSuchFieldError v214) {}
        try {
            arrn[Material.NOTE_BLOCK.ordinal()] = 26;
        }
        catch (NoSuchFieldError v215) {}
        try {
            arrn[Material.OBSIDIAN.ordinal()] = 50;
        }
        catch (NoSuchFieldError v216) {}
        try {
            arrn[Material.PACKED_ICE.ordinal()] = 171;
        }
        catch (NoSuchFieldError v217) {}
        try {
            arrn[Material.PAINTING.ordinal()] = 238;
        }
        catch (NoSuchFieldError v218) {}
        try {
            arrn[Material.PAPER.ordinal()] = 256;
        }
        catch (NoSuchFieldError v219) {}
        try {
            arrn[Material.PISTON_BASE.ordinal()] = 34;
        }
        catch (NoSuchFieldError v220) {}
        try {
            arrn[Material.PISTON_EXTENSION.ordinal()] = 35;
        }
        catch (NoSuchFieldError v221) {}
        try {
            arrn[Material.PISTON_MOVING_PIECE.ordinal()] = 37;
        }
        catch (NoSuchFieldError v222) {}
        try {
            arrn[Material.PISTON_STICKY_BASE.ordinal()] = 30;
        }
        catch (NoSuchFieldError v223) {}
        try {
            arrn[Material.POISONOUS_POTATO.ordinal()] = 311;
        }
        catch (NoSuchFieldError v224) {}
        try {
            arrn[Material.PORK.ordinal()] = 236;
        }
        catch (NoSuchFieldError v225) {}
        try {
            arrn[Material.PORTAL.ordinal()] = 91;
        }
        catch (NoSuchFieldError v226) {}
        try {
            arrn[Material.POTATO.ordinal()] = 144;
        }
        catch (NoSuchFieldError v227) {}
        try {
            arrn[Material.POTATO_ITEM.ordinal()] = 309;
        }
        catch (NoSuchFieldError v228) {}
        try {
            arrn[Material.POTION.ordinal()] = 290;
        }
        catch (NoSuchFieldError v229) {}
        try {
            arrn[Material.POWERED_MINECART.ordinal()] = 260;
        }
        catch (NoSuchFieldError v230) {}
        try {
            arrn[Material.POWERED_RAIL.ordinal()] = 28;
        }
        catch (NoSuchFieldError v231) {}
        try {
            arrn[Material.PUMPKIN.ordinal()] = 87;
        }
        catch (NoSuchFieldError v232) {}
        try {
            arrn[Material.PUMPKIN_PIE.ordinal()] = 317;
        }
        catch (NoSuchFieldError v233) {}
        try {
            arrn[Material.PUMPKIN_SEEDS.ordinal()] = 278;
        }
        catch (NoSuchFieldError v234) {}
        try {
            arrn[Material.PUMPKIN_STEM.ordinal()] = 106;
        }
        catch (NoSuchFieldError v235) {}
        try {
            arrn[Material.QUARTZ.ordinal()] = 323;
        }
        catch (NoSuchFieldError v236) {}
        try {
            arrn[Material.QUARTZ_BLOCK.ordinal()] = 157;
        }
        catch (NoSuchFieldError v237) {}
        try {
            arrn[Material.QUARTZ_ORE.ordinal()] = 155;
        }
        catch (NoSuchFieldError v238) {}
        try {
            arrn[Material.QUARTZ_STAIRS.ordinal()] = 158;
        }
        catch (NoSuchFieldError v239) {}
        try {
            arrn[Material.RAILS.ordinal()] = 67;
        }
        catch (NoSuchFieldError v240) {}
        try {
            arrn[Material.RAW_BEEF.ordinal()] = 280;
        }
        catch (NoSuchFieldError v241) {}
        try {
            arrn[Material.RAW_CHICKEN.ordinal()] = 282;
        }
        catch (NoSuchFieldError v242) {}
        try {
            arrn[Material.RAW_FISH.ordinal()] = 266;
        }
        catch (NoSuchFieldError v243) {}
        try {
            arrn[Material.RECORD_10.ordinal()] = 341;
        }
        catch (NoSuchFieldError v244) {}
        try {
            arrn[Material.RECORD_11.ordinal()] = 342;
        }
        catch (NoSuchFieldError v245) {}
        try {
            arrn[Material.RECORD_12.ordinal()] = 343;
        }
        catch (NoSuchFieldError v246) {}
        try {
            arrn[Material.RECORD_3.ordinal()] = 334;
        }
        catch (NoSuchFieldError v247) {}
        try {
            arrn[Material.RECORD_4.ordinal()] = 335;
        }
        catch (NoSuchFieldError v248) {}
        try {
            arrn[Material.RECORD_5.ordinal()] = 336;
        }
        catch (NoSuchFieldError v249) {}
        try {
            arrn[Material.RECORD_6.ordinal()] = 337;
        }
        catch (NoSuchFieldError v250) {}
        try {
            arrn[Material.RECORD_7.ordinal()] = 338;
        }
        catch (NoSuchFieldError v251) {}
        try {
            arrn[Material.RECORD_8.ordinal()] = 339;
        }
        catch (NoSuchFieldError v252) {}
        try {
            arrn[Material.RECORD_9.ordinal()] = 340;
        }
        catch (NoSuchFieldError v253) {}
        try {
            arrn[Material.REDSTONE.ordinal()] = 248;
        }
        catch (NoSuchFieldError v254) {}
        try {
            arrn[Material.REDSTONE_BLOCK.ordinal()] = 154;
        }
        catch (NoSuchFieldError v255) {}
        try {
            arrn[Material.REDSTONE_COMPARATOR.ordinal()] = 321;
        }
        catch (NoSuchFieldError v256) {}
        try {
            arrn[Material.REDSTONE_COMPARATOR_OFF.ordinal()] = 151;
        }
        catch (NoSuchFieldError v257) {}
        try {
            arrn[Material.REDSTONE_COMPARATOR_ON.ordinal()] = 152;
        }
        catch (NoSuchFieldError v258) {}
        try {
            arrn[Material.REDSTONE_LAMP_OFF.ordinal()] = 125;
        }
        catch (NoSuchFieldError v259) {}
        try {
            arrn[Material.REDSTONE_LAMP_ON.ordinal()] = 126;
        }
        catch (NoSuchFieldError v260) {}
        try {
            arrn[Material.REDSTONE_ORE.ordinal()] = 74;
        }
        catch (NoSuchFieldError v261) {}
        try {
            arrn[Material.REDSTONE_TORCH_OFF.ordinal()] = 76;
        }
        catch (NoSuchFieldError v262) {}
        try {
            arrn[Material.REDSTONE_TORCH_ON.ordinal()] = 77;
        }
        catch (NoSuchFieldError v263) {}
        try {
            arrn[Material.REDSTONE_WIRE.ordinal()] = 56;
        }
        catch (NoSuchFieldError v264) {}
        try {
            arrn[Material.RED_MUSHROOM.ordinal()] = 41;
        }
        catch (NoSuchFieldError v265) {}
        try {
            arrn[Material.RED_ROSE.ordinal()] = 39;
        }
        catch (NoSuchFieldError v266) {}
        try {
            arrn[Material.ROTTEN_FLESH.ordinal()] = 284;
        }
        catch (NoSuchFieldError v267) {}
        try {
            arrn[Material.SADDLE.ordinal()] = 246;
        }
        catch (NoSuchFieldError v268) {}
        try {
            arrn[Material.SAND.ordinal()] = 13;
        }
        catch (NoSuchFieldError v269) {}
        try {
            arrn[Material.SANDSTONE.ordinal()] = 25;
        }
        catch (NoSuchFieldError v270) {}
        try {
            arrn[Material.SANDSTONE_STAIRS.ordinal()] = 130;
        }
        catch (NoSuchFieldError v271) {}
        try {
            arrn[Material.SAPLING.ordinal()] = 7;
        }
        catch (NoSuchFieldError v272) {}
        try {
            arrn[Material.SEEDS.ordinal()] = 212;
        }
        catch (NoSuchFieldError v273) {}
        try {
            arrn[Material.SHEARS.ordinal()] = 276;
        }
        catch (NoSuchFieldError v274) {}
        try {
            arrn[Material.SIGN.ordinal()] = 240;
        }
        catch (NoSuchFieldError v275) {}
        try {
            arrn[Material.SIGN_POST.ordinal()] = 64;
        }
        catch (NoSuchFieldError v276) {}
        try {
            arrn[Material.SKULL.ordinal()] = 146;
        }
        catch (NoSuchFieldError v277) {}
        try {
            arrn[Material.SKULL_ITEM.ordinal()] = 314;
        }
        catch (NoSuchFieldError v278) {}
        try {
            arrn[Material.SLIME_BALL.ordinal()] = 258;
        }
        catch (NoSuchFieldError v279) {}
        try {
            arrn[Material.SMOOTH_BRICK.ordinal()] = 100;
        }
        catch (NoSuchFieldError v280) {}
        try {
            arrn[Material.SMOOTH_STAIRS.ordinal()] = 111;
        }
        catch (NoSuchFieldError v281) {}
        try {
            arrn[Material.SNOW.ordinal()] = 79;
        }
        catch (NoSuchFieldError v282) {}
        try {
            arrn[Material.SNOW_BALL.ordinal()] = 249;
        }
        catch (NoSuchFieldError v283) {}
        try {
            arrn[Material.SNOW_BLOCK.ordinal()] = 81;
        }
        catch (NoSuchFieldError v284) {}
        try {
            arrn[Material.SOIL.ordinal()] = 61;
        }
        catch (NoSuchFieldError v285) {}
        try {
            arrn[Material.SOUL_SAND.ordinal()] = 89;
        }
        catch (NoSuchFieldError v286) {}
        try {
            arrn[Material.SPECKLED_MELON.ordinal()] = 299;
        }
        catch (NoSuchFieldError v287) {}
        try {
            arrn[Material.SPIDER_EYE.ordinal()] = 292;
        }
        catch (NoSuchFieldError v288) {}
        try {
            arrn[Material.SPONGE.ordinal()] = 20;
        }
        catch (NoSuchFieldError v289) {}
        try {
            arrn[Material.SPRUCE_WOOD_STAIRS.ordinal()] = 136;
        }
        catch (NoSuchFieldError v290) {}
        try {
            arrn[Material.STAINED_CLAY.ordinal()] = 161;
        }
        catch (NoSuchFieldError v291) {}
        try {
            arrn[Material.STAINED_GLASS.ordinal()] = 97;
        }
        catch (NoSuchFieldError v292) {}
        try {
            arrn[Material.STAINED_GLASS_PANE.ordinal()] = 162;
        }
        catch (NoSuchFieldError v293) {}
        try {
            arrn[Material.STATIONARY_LAVA.ordinal()] = 12;
        }
        catch (NoSuchFieldError v294) {}
        try {
            arrn[Material.STATIONARY_WATER.ordinal()] = 10;
        }
        catch (NoSuchFieldError v295) {}
        try {
            arrn[Material.STEP.ordinal()] = 45;
        }
        catch (NoSuchFieldError v296) {}
        try {
            arrn[Material.STICK.ordinal()] = 197;
        }
        catch (NoSuchFieldError v297) {}
        try {
            arrn[Material.STONE.ordinal()] = 2;
        }
        catch (NoSuchFieldError v298) {}
        try {
            arrn[Material.STONE_AXE.ordinal()] = 192;
        }
        catch (NoSuchFieldError v299) {}
        try {
            arrn[Material.STONE_BUTTON.ordinal()] = 78;
        }
        catch (NoSuchFieldError v300) {}
        try {
            arrn[Material.STONE_HOE.ordinal()] = 208;
        }
        catch (NoSuchFieldError v301) {}
        try {
            arrn[Material.STONE_PICKAXE.ordinal()] = 191;
        }
        catch (NoSuchFieldError v302) {}
        try {
            arrn[Material.STONE_PLATE.ordinal()] = 71;
        }
        catch (NoSuchFieldError v303) {}
        try {
            arrn[Material.STONE_SPADE.ordinal()] = 190;
        }
        catch (NoSuchFieldError v304) {}
        try {
            arrn[Material.STONE_SWORD.ordinal()] = 189;
        }
        catch (NoSuchFieldError v305) {}
        try {
            arrn[Material.STORAGE_MINECART.ordinal()] = 259;
        }
        catch (NoSuchFieldError v306) {}
        try {
            arrn[Material.STRING.ordinal()] = 204;
        }
        catch (NoSuchFieldError v307) {}
        try {
            arrn[Material.SUGAR.ordinal()] = 270;
        }
        catch (NoSuchFieldError v308) {}
        try {
            arrn[Material.SUGAR_CANE.ordinal()] = 255;
        }
        catch (NoSuchFieldError v309) {}
        try {
            arrn[Material.SUGAR_CANE_BLOCK.ordinal()] = 84;
        }
        catch (NoSuchFieldError v310) {}
        try {
            arrn[Material.SULPHUR.ordinal()] = 206;
        }
        catch (NoSuchFieldError v311) {}
        try {
            arrn[Material.THIN_GLASS.ordinal()] = 104;
        }
        catch (NoSuchFieldError v312) {}
        try {
            arrn[Material.TNT.ordinal()] = 47;
        }
        catch (NoSuchFieldError v313) {}
        try {
            arrn[Material.TORCH.ordinal()] = 51;
        }
        catch (NoSuchFieldError v314) {}
        try {
            arrn[Material.TRAPPED_CHEST.ordinal()] = 148;
        }
        catch (NoSuchFieldError v315) {}
        try {
            arrn[Material.TRAP_DOOR.ordinal()] = 98;
        }
        catch (NoSuchFieldError v316) {}
        try {
            arrn[Material.TRIPWIRE.ordinal()] = 134;
        }
        catch (NoSuchFieldError v317) {}
        try {
            arrn[Material.TRIPWIRE_HOOK.ordinal()] = 133;
        }
        catch (NoSuchFieldError v318) {}
        try {
            arrn[Material.VINE.ordinal()] = 108;
        }
        catch (NoSuchFieldError v319) {}
        try {
            arrn[Material.WALL_SIGN.ordinal()] = 69;
        }
        catch (NoSuchFieldError v320) {}
        try {
            arrn[Material.WATCH.ordinal()] = 264;
        }
        catch (NoSuchFieldError v321) {}
        try {
            arrn[Material.WATER.ordinal()] = 9;
        }
        catch (NoSuchFieldError v322) {}
        try {
            arrn[Material.WATER_BUCKET.ordinal()] = 243;
        }
        catch (NoSuchFieldError v323) {}
        try {
            arrn[Material.WATER_LILY.ordinal()] = 113;
        }
        catch (NoSuchFieldError v324) {}
        try {
            arrn[Material.WEB.ordinal()] = 31;
        }
        catch (NoSuchFieldError v325) {}
        try {
            arrn[Material.WHEAT.ordinal()] = 213;
        }
        catch (NoSuchFieldError v326) {}
        try {
            arrn[Material.WOOD.ordinal()] = 6;
        }
        catch (NoSuchFieldError v327) {}
        try {
            arrn[Material.WOODEN_DOOR.ordinal()] = 65;
        }
        catch (NoSuchFieldError v328) {}
        try {
            arrn[Material.WOOD_AXE.ordinal()] = 188;
        }
        catch (NoSuchFieldError v329) {}
        try {
            arrn[Material.WOOD_BUTTON.ordinal()] = 145;
        }
        catch (NoSuchFieldError v330) {}
        try {
            arrn[Material.WOOD_DOOR.ordinal()] = 241;
        }
        catch (NoSuchFieldError v331) {}
        try {
            arrn[Material.WOOD_DOUBLE_STEP.ordinal()] = 127;
        }
        catch (NoSuchFieldError v332) {}
        try {
            arrn[Material.WOOD_HOE.ordinal()] = 207;
        }
        catch (NoSuchFieldError v333) {}
        try {
            arrn[Material.WOOD_PICKAXE.ordinal()] = 187;
        }
        catch (NoSuchFieldError v334) {}
        try {
            arrn[Material.WOOD_PLATE.ordinal()] = 73;
        }
        catch (NoSuchFieldError v335) {}
        try {
            arrn[Material.WOOD_SPADE.ordinal()] = 186;
        }
        catch (NoSuchFieldError v336) {}
        try {
            arrn[Material.WOOD_STAIRS.ordinal()] = 54;
        }
        catch (NoSuchFieldError v337) {}
        try {
            arrn[Material.WOOD_STEP.ordinal()] = 128;
        }
        catch (NoSuchFieldError v338) {}
        try {
            arrn[Material.WOOD_SWORD.ordinal()] = 185;
        }
        catch (NoSuchFieldError v339) {}
        try {
            arrn[Material.WOOL.ordinal()] = 36;
        }
        catch (NoSuchFieldError v340) {}
        try {
            arrn[Material.WORKBENCH.ordinal()] = 59;
        }
        catch (NoSuchFieldError v341) {}
        try {
            arrn[Material.WRITTEN_BOOK.ordinal()] = 304;
        }
        catch (NoSuchFieldError v342) {}
        try {
            arrn[Material.YELLOW_FLOWER.ordinal()] = 38;
        }
        catch (NoSuchFieldError v343) {}
        $SWITCH_TABLE$org$bukkit$Material = arrn;
        return $SWITCH_TABLE$org$bukkit$Material;
    }
}

