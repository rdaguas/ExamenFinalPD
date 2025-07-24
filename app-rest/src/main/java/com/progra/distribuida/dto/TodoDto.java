package com.progra.distribuida.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TodoDto {

    private Long id;
    private Long userId;
    private String title;
    private Boolean completed;
    private String nombre;
}
