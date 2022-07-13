package me.jfishing.burp.dict.content_path;

public class ContentPathParser {

    public static String parse(String path) {
        if( path == null || path.length() < 2) {
            return null;
        }
        String[] res = path.split("/");
        if(res.length < 2) {
            return null;
        } else {
            return res[1];
        }
    }

}
