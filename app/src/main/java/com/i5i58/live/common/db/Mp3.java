package com.i5i58.live.common.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Lee on 2017/4/12.
 */

@Table(name = "mp3")
public class Mp3 {

    @Column(name = "id")
    private int id;

    @Column(name = "title", isId = true)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
