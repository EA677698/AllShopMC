package me.phantomknight.allshop.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
public class DoubleChestsUtil {

    public static Chest getRightChest(Chest chest){
        Chest temp;
        if(isChestDoubleChest(chest)) {
            String data = chest.getBlockData().getAsString();
            if(data.substring(data.indexOf("type=")+5,data.indexOf(",",data.indexOf("type="))).equals("left")){
                return chest;
            }
            temp = getNextChest(chest, "left");
            if(isChestDoubleChest(temp)){
                data = temp.getBlockData().getAsString();
                if(data.substring(data.indexOf("type=")+5,data.indexOf(",",data.indexOf("type="))).equals("left")){
                    return temp;
                }
            } else {
                temp = getNextChest(chest, "left");
                if(isChestDoubleChest(temp)){
                    data = temp.getBlockData().getAsString();
                    if(data.substring(data.indexOf("type=")+5,data.indexOf(",",data.indexOf("type="))).equals("left")){
                        return temp;
                    }
                }
            }
        }
        return null;
    }

    public static Location getBackChestLocation(String direction, Location chestLocation){
        switch (direction.toUpperCase()){
            case "NORTH":
                return chestLocation.add(0,0,1);
            case "EAST":
                return chestLocation.add(-1,0,0);
            case "SOUTH":
                return chestLocation.add(0,0,-1);
            default:
                return chestLocation.add(1,0,0);
        }
    }

    public static Chest getLeftChest(Chest chest){
        Chest temp;
        if(isChestDoubleChest(chest)) {
            String data = chest.getBlockData().getAsString();
            if(data.substring(data.indexOf("type=")+5,data.indexOf(",",data.indexOf("type="))).equals("right")){
                return chest;
            }
            temp = getNextChest(chest, "right");
            if(isChestDoubleChest(temp)){
                data = temp.getBlockData().getAsString();
                if(data.substring(data.indexOf("type=")+5,data.indexOf(",",data.indexOf("type="))).equals("right")){
                    return temp;
                }
            } else {
                temp = getNextChest(chest, "right");
                if(isChestDoubleChest(temp)){
                    data = temp.getBlockData().getAsString();
                    if(data.substring(data.indexOf("type=")+5,data.indexOf(",",data.indexOf("type="))).equals("right")){
                        return temp;
                    }
                }
            }
        }
        return null;
    }

    public static Chest getNextChest(Chest chest, String chestSide){
        Chest two;
        String data = chest.getBlockData().getAsString();
        String direction = data.substring(data.indexOf("facing=")+7,data.indexOf(","));
        String face;
        float pitch1 = chest.getLocation().getPitch();
        float yaw1 = chest.getLocation().getYaw();
        float pitch2;
        float yaw2;
        if(chestSide.equalsIgnoreCase("right")){
            face = getNextClockWiseDirection(direction);
        } else {
            face = getNextCounterClockWiseDirection(direction);
        }
        if (chest.getBlock().getRelative(BlockFace.valueOf(face)).getType() == Material.CHEST) {
            two = (Chest) chest.getBlock().getRelative(BlockFace.valueOf(face)).getState();
            pitch2 = two.getLocation().getPitch();
            yaw2 = two.getLocation().getYaw();
            if (pitch1 == pitch2 && yaw1 == yaw2) {
                return two;
            }
        }
        return null;
    }

    private static String getNextClockWiseDirection(String direction){
        switch (direction.toUpperCase()){
            case "NORTH":
                return "EAST";
            case "EAST":
                return "SOUTH";
            case "SOUTH":
                return "WEST";
            default:
                return "NORTH";
        }
    }

    private static String getNextCounterClockWiseDirection(String direction){
        switch (direction.toUpperCase()){
            case "NORTH":
                return "WEST";
            case "WEST":
                return "SOUTH";
            case "SOUTH":
                return "EAST";
            default:
                return "NORTH";
        }
    }


    @SuppressWarnings({"unused", "MismatchedReadAndWriteOfArray"})
    public static Chest[] getDoubleChest(Chest chest){
        Chest[] doubleChest = new Chest[2];
        doubleChest[0] = getLeftChest(chest);
        doubleChest[1] = getRightChest(chest);
        return new Chest[2];
    }

    @SuppressWarnings("unused")
    public static DoubleChest getDoubleChestInventory(Chest chest){
        if (chest instanceof Chest) {
            Inventory inventory = chest.getInventory();
            if (inventory instanceof DoubleChestInventory) {
                return (DoubleChest) inventory.getHolder();
            }
        }
        return null;
    }

    public static boolean isChestDoubleChest(Chest chest){
        if (chest instanceof Chest) {
            Inventory inventory = chest.getInventory();
            return inventory instanceof DoubleChestInventory;
        }
        return false;
    }
}
