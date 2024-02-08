/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.biblioteca.addbooks;
import com.biblioteca.addbooks.entities.Repository;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author kkraft
 */
public class AddBooks {
    public boolean autornome = true;
    public boolean stacktrace = true;
    String nome = "";
    String sobrenome = "";   
    
    public static String parseCommand() throws IOException{
            int content = System.in.read();
            String finalstring = "";
            while(content != -1 & content != 10){
                //System.out.print(content);
                //System.out.println((char)content);
                finalstring += String.valueOf((char) content);
                content = System.in.read();
            }
            return finalstring;
    }     
    static void menu(){
            System.out.println("\n\n\n-------AddBooks------- \n ---made by kkraft--- \n");
            System.out.println("[1]Adicionar Livro");
            System.out.println("[2]Editar Informações De Livro");
            System.out.println("[3]Adicionar Autor");
            System.out.println("[4]Editar Informações De Autor");  
            System.out.println("[5]Sair");
    }
    
    
    public void addToAutor(String toadd){
        if(autornome){
            nome += toadd;
        }else{
            sobrenome += toadd;
        }
    } 
    public void addToAutor(char toadd){
        if(autornome){
            nome += toadd;
        }else{
            sobrenome += toadd;
        }
    }     
   
    public String formatAutor(String nomeraw){
        boolean firstexecution = true;
        boolean spacebefore = false;
        
        for(char c:nomeraw.toCharArray()){
            if(firstexecution){
                addToAutor(String.valueOf(c).toUpperCase());
                firstexecution = false;
                continue;
            }
    
            if(spacebefore){
                addToAutor(String.valueOf(c).toUpperCase());
                spacebefore = false;
                continue;
            }
    
            if(c == ' ' & autornome == false){
                addToAutor(c);                   
                spacebefore = true;             
                continue;
            } else if(autornome & c == ' '){
                autornome=false;
                spacebefore = true;
                continue;
            }
    
            addToAutor(c); // Se nenhum if disparar
        }
        return sobrenome + "," + nome;
    }
    
    public boolean addBook(){
        String isbn;
        String titulo;
        String sinopse;
        String autor;
        String autor_id;
        
        try{
            System.out.println("\n Opção 1 Selecionada");
            System.out.println("Insira o ISBN do livro");
            isbn = parseCommand();
            System.out.println("Insira o titulo do livro");
            titulo = parseCommand();
            System.out.println("Insira a sinopse do livro");
            sinopse = parseCommand();
            System.out.println("Insira o autor do livro");
            autor = formatAutor(parseCommand());
            try{
                List<Integer> autores_mesmo_nome = Repository.Singleton.autoresComMesmoNome(autor);
                
                if(autores_mesmo_nome.isEmpty()){
                    System.out.println("Este autor não esta cadastrado, redirecionando para o cadastro de autor");
                    addAutor();
                }else if(autores_mesmo_nome.size() == 1){
                    autor_id = autores_mesmo_nome.get(0).toString();
                    try{
                        Repository.Singleton.addBook(isbn, titulo, sinopse, autor_id);
                        System.out.println("\nLivro adicionado ao database\n");
                    }
                    catch(SQLException e){
                        if(stacktrace){
                            System.out.println("SQLException ao chamar o metodo addbook");
                            System.out.println("INSERT INTO Book VALUES(" + isbn + ",\"" + titulo + "\",\"" + sinopse + "\"," + autor_id );
                            e.printStackTrace();
                        }else{
                            System.out.println("Erro ao registrar o livro, tente denovo");
                        }
                    }
                }else if(autores_mesmo_nome.size() > 1){
                    Integer currentloop = -1;
                    System.out.println("Há mais de um autor com este nome, verifique seus catalogos abaixo e escolha o correto");                    
                    for(Integer i:autores_mesmo_nome){
                        currentloop++;
                        System.out.println("["+currentloop.toString()+ "]localhost/autor?autor=" + i);
                    }
                    String resultado = parseCommand();
                    autor_id = autores_mesmo_nome.get(Integer.valueOf(resultado).intValue()).toString();
                    try{
                        Repository.Singleton.addBook(isbn, titulo, sinopse, autor_id);
                        System.out.println("Livro adicionado ao database");
                    }
                    catch(SQLException e){
                         if(stacktrace){
                            System.out.println("SQLException ao chamar o metodo addbook");
                            e.printStackTrace();
                        }else{
                            System.out.println("Erro ao registrar o livro, tente denovo");
                        }
                    }
                }
            }catch(SQLException e){
                 if(stacktrace){
                            System.out.println("SQLException ao chamar o metodo autoresMesmoNome");
                            e.printStackTrace();
                 }else{
                    System.out.println("Erro ao buscar autores, tente denovo");
                  }
            }            
            
            System.out.println(autor);
        }
        catch(IOException e){
            
        }
            return true;            
    } 
   
    
    public void addAutor(){
        System.out.println("Opção 3 Selecionada");
        System.out.print("\nInsira o nome do autor:");
        try {
            String nome = formatAutor(parseCommand());
            System.out.println(nome);
            Repository.Singleton.addAutor(nome);
        } catch (Exception e) {
            if(stacktrace){
                System.out.println("Erro no addAutor");
                e.printStackTrace();
            }else{
                System.out.println("Erro ao adicionar o autor, tente novamente");                
            }
          }
    }
    public static void main(String[] args) {
        try {
            BeanManager beanm = new BeanManager();            
            AddBooks addbooks = new AddBooks();   
            menu();
            String opcao= parseCommand();            
            if(opcao.equals("1")){
                addbooks.addBook();
                menu();
                opcao = parseCommand();                
            }else if(opcao.equals("3")){
                addbooks.addAutor();       
                menu();    
                opcao = parseCommand();                
            }else if(opcao.equals("5")){
                Repository.Singleton.closeConnection();
            }            
        } catch (IOException ex) {}
    }
}
