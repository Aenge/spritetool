package com.OpenRSC.IO.Info;
import com.OpenRSC.Model.Format.Info;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class InfoReader {
    public Info read(File infoFile) {
        Info ret = null;
        try {
            if (infoFile.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                ret = objectMapper.readValue(infoFile,Info.class);

                //Jackson encodes strings in double quotes. Need to eliminate those.
                String name = ret.getName();
                if (name.startsWith("\"") &&
                    name.endsWith("\"")) {
                    name = name.substring(1,name.length()-1);
                    ret.setName(name);
                }
            }
        } catch (IOException a) {
            a.printStackTrace();
        }
        return ret;
    }
}