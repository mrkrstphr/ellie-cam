package com.grcodingcompany.elliecam;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "cameras")
public class Camera extends Model {
    @Column
    public String name;

    @Column
    public String url;

    @Column
    public String username;

    @Column
    public String password;

    @Column
    public String thumbnailSnapshot;

    @Column
    public boolean status;

//    public static List<Camera> getAll() {
//        return new Select()
//                .from(Camera.class)
//                .orderBy("name ASC")
//                .execute();
//    }
//
//    public static Camera find(Long id) {
//        return new Select()
//                .from(Camera.class)
//                .where("Id = ?", id)
//                .executeSingle();
//    }
}
