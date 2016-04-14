package com.jp.beans;

/**
 * Created by jp on 26/11/15.
 */
public class Post {
    private String name;
    private String descricao;
    private int photoId;
    private boolean curtir = false;

    public Post(String name, String descricao, int photoId, boolean curtir) {
        this.name = name;
        this.descricao = descricao;
        this.photoId = photoId;
        this.curtir = curtir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public boolean isCurtir() {
        return curtir;
    }

    public void setCurtir(boolean curtir) {
        this.curtir = curtir;
    }
}
