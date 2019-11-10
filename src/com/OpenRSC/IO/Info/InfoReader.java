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
            }
        } catch (IOException a) {
            a.printStackTrace();
        }
        return ret;
    }
}