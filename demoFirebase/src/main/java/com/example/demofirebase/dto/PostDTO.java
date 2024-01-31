package com.example.demofirebase.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDTO {

    private String id;
    private String title;
    private String content;
    // private List lista para subir las imagenes
    private List<String> tags;


}
