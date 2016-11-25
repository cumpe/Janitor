/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.ChatSerializer
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  net.minecraft.server.v1_7_R4.IChatBaseComponent
 *  net.minecraft.server.v1_7_R4.NBTTagCompound
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutChat
 *  net.minecraft.server.v1_7_R4.PlayerConnection
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package net.darepvp.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UtilActionMessage {
    private List<AMText> Text = new ArrayList<AMText>();

    public AMText addText(String Message) {
        AMText Text = new AMText(Message);
        this.Text.add(Text);
        return Text;
    }

    public String getFormattedMessage() {
        String Chat = "[\"\",";
        for (AMText Text : this.Text) {
            Chat = String.valueOf(Chat) + Text.getFormattedMessage() + ",";
        }
        Chat = Chat.substring(0, Chat.length() - 1);
        Chat = String.valueOf(Chat) + "]";
        return Chat;
    }

    public void sendToPlayer(Player Player2) {
        IChatBaseComponent base = ChatSerializer.a((String)this.getFormattedMessage());
        PacketPlayOutChat packet = new PacketPlayOutChat(base, 1);
        ((CraftPlayer)Player2).getHandle().playerConnection.sendPacket((Packet)packet);
    }

    public class AMText {
        private String Message;
        private Map<String, Map.Entry<String, String>> Modifiers;

        public AMText(String Text) {
            this.Message = "";
            this.Modifiers = new HashMap<String, Map.Entry<String, String>>();
            this.Message = Text;
        }

        public String getMessage() {
            return this.Message;
        }

        public String getFormattedMessage() {
            String Chat = "{\"text\":\"" + this.Message + "\"";
            for (String Event2 : this.Modifiers.keySet()) {
                Map.Entry<String, String> Modifier = this.Modifiers.get(Event2);
                Chat = String.valueOf(Chat) + ",\"" + Event2 + "\":{\"action\":\"" + Modifier.getKey() + "\",\"value\":" + Modifier.getValue() + "}";
            }
            Chat = String.valueOf(Chat) + "}";
            return Chat;
        }

        public /* varargs */ AMText addHoverText(String ... Text) {
            String Event2 = "hoverEvent";
            String Key = "show_text";
            String Value = "";
            if (Text.length == 1) {
                Value = "{\"text\":\"" + Text[0] + "\"}";
            } else {
                Value = "{\"text\":\"\",\"extra\":[";
                String[] arrstring = Text;
                int n = arrstring.length;
                int n2 = 0;
                while (n2 < n) {
                    String Message = arrstring[n2];
                    Value = String.valueOf(Value) + "{\"text\":\"" + Message + "\"},";
                    ++n2;
                }
                Value = Value.substring(0, Value.length() - 1);
                Value = String.valueOf(Value) + "]}";
            }
            AbstractMap.SimpleEntry<String, String> Values2 = new AbstractMap.SimpleEntry<String, String>(Key, Value);
            this.Modifiers.put(Event2, Values2);
            return this;
        }

        public AMText addHoverItem(ItemStack Item) {
            String Event2 = "hoverEvent";
            String Key = "show_item";
            String Value = CraftItemStack.asNMSCopy((ItemStack)Item).getTag().toString();
            AbstractMap.SimpleEntry<String, String> Values2 = new AbstractMap.SimpleEntry<String, String>(Key, Value);
            this.Modifiers.put(Event2, Values2);
            return this;
        }

        public AMText setClickEvent(ClickableType Type, String Value) {
            String Event2 = "clickEvent";
            String Key = Type.Action;
            AbstractMap.SimpleEntry<String, String> Values2 = new AbstractMap.SimpleEntry<String, String>(Key, "\"" + Value + "\"");
            this.Modifiers.put(Event2, Values2);
            return this;
        }
    }

    public static enum ClickableType {
        RunCommand("run_command"),
        SuggestCommand("suggest_command"),
        OpenURL("open_url");
        
        public String Action;

        private ClickableType(String Action, int n2, String string2) {
            this.Action = Action;
        }
    }

}

