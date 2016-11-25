package net.darepvp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

public class TxtFile {
    private File File;
    private String Name;
    private List<String> Lines = new ArrayList<String>();

    public TxtFile(JavaPlugin Plugin2, String Path2, String Name) {
        this.File = new File(Plugin2.getDataFolder() + Path2);
        this.File.mkdirs();
        this.File = new File(Plugin2.getDataFolder() + Path2, String.valueOf(Name) + ".txt");
        try {
            this.File.createNewFile();
        }
        catch (IOException var4_4) {
            // empty catch block
        }
        this.Name = Name;
        this.readTxtFile();
    }

    public void clear() {
        this.Lines.clear();
    }

    public void addLine(String line) {
        this.Lines.add(line);
    }

    public void write() {
        try {
            FileWriter fw = new FileWriter(this.File, false);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String Line : this.Lines) {
                bw.write(Line);
                bw.newLine();
            }
            bw.close();
            fw.close();
        }
        catch (Exception fw) {
            // empty catch block
        }
    }

    public void readTxtFile() {
        this.Lines.clear();
        try {
            String Line;
            FileReader fr = new FileReader(this.File);
            BufferedReader br = new BufferedReader(fr);
            while ((Line = br.readLine()) != null) {
                this.Lines.add(Line);
            }
            br.close();
            fr.close();
        }
        catch (Exception exx) {
            exx.printStackTrace();
        }
    }

    public String getName() {
        return this.Name;
    }

    public String getText() {
        String text = "";
        int i = 0;
        while (i < this.Lines.size()) {
            String line = this.Lines.get(i);
            text = String.valueOf(text) + line + (this.Lines.size() - 1 == i ? "" : "\n");
            ++i;
        }
        return text;
    }

    public List<String> getLines() {
        return this.Lines;
    }
}

