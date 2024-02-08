/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.addbooks.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kkraft
 */
public class Repository {
    
    public static Repository Singleton;
    boolean firstacess = true;
    Connection connection;  
    
    Connection getOrSetConnection() throws SQLException{
        if(firstacess){
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Acervo", "root", "123456");  
            return connection;
        }else{
            return connection;
        }
    }
    public void closeConnection(){
        try {
            if(firstacess){
                connection.close();
            }
        } catch (SQLException ex) {
        }
    }
    
    public void addBook(String isbn, String titulo,String sinopse,String autorid) throws SQLException {
       getOrSetConnection().createStatement().execute("INSERT INTO Book VALUES(" + isbn + ",\"" + titulo + "\",\"" + sinopse + "\"," + autorid + ")" );
    }
    
    public void addAutor(String nome) throws SQLException{
       getOrSetConnection().createStatement().execute("INSERT INTO Autor VALUES(null,\"" + nome + "\")");        
    }
    
    public List<Integer> autoresComMesmoNome(String nome) throws SQLException {
       Statement st = getOrSetConnection().createStatement();
       st.execute("SELECT id FROM Autor WHERE nome =\""+ nome + "\"" );
       ResultSet rs = st.getResultSet();
       List<Integer> listafinal = new ArrayList<Integer>();
       while(rs.next()){
           listafinal.add(Integer.valueOf(rs.getInt("id")));
       }
       return listafinal;
    }  
    
    
}
