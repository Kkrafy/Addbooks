/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.addbooks;

import com.biblioteca.addbooks.entities.Repository;

/**
 *
 * @author kkraft
 */
public class BeanManager {
 
    
    public BeanManager(){
        Repository.Singleton = new Repository();
    }
    
}
