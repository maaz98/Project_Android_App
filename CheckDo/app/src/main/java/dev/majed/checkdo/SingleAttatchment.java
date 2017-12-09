package dev.majed.checkdo;


import java.io.Serializable;

public class SingleAttatchment implements Serializable {
    int type;
    String source;

    public SingleAttatchment( int type, String source) {

        this.type = type;
        this.source = source;

    }
/*
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }*/

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
