package me.jfishing.burp.dict.content_path;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ContentPathDict {

    final private String dictFilePath = "E:\\security_weapons\\dict\\content_path.dic";
    Set<String> dictSet = new HashSet<>(20000);

    public ContentPathDict() throws IOException {
        File dictFile = new File(dictFilePath);
        BufferedReader reader = new BufferedReader(new FileReader(dictFile));
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!"".equals(line)) {
                    dictSet.add(line);
                }
            }
        } catch(Exception e) {
            throw e;
        } finally {
            reader.close();
        }

    }

    public boolean hasValue(String value) {
        return dictSet.contains(value);
    }

    public void addDict(String value) throws IOException {
        if(dictSet.contains(value) == false) {
            dictSet.add(value);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(dictFilePath, true)));
            try {
                writer.write(value + "\r\n");
            } catch(Exception e) {
                throw e;
            } finally {
                writer.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ContentPathDict dic = new ContentPathDict();
        dic.addDict("iwefiewfjewijfier");
    }

}
