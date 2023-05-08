package com.spring.prototipolitigando.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="rol")
public class RolEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="rol_name")
    private String rol_name;

    public RolEntity() {
    }

    public RolEntity(long id, String rol_name) {
        this.id = id;
        this.rol_name = rol_name;
    }

    public RolEntity(String rol_name) {
        this.rol_name = rol_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRol_name() {
        return rol_name;
    }

    public void setRol_name(String rol_name) {
        this.rol_name = rol_name;
    }

    
    
}
