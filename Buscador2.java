/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.neo4jlimpio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import static org.neo4j.driver.GraphDatabase.driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import static org.neo4j.driver.Values.parameters;

/**
 *
 * @author davic
 */
public class Buscador2 extends JFrame {
    static DefaultListModel modelo;
    static JList area;
    static JButton botoneliminar2;
    static JButton asignar;
    static JButton designar;
    static JButton actualizar;
    
    static Driver driver2 = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
     public Buscador2(){
        
        componentes();
        
        setSize(800,900);
        setVisible(true);
        setTitle("Buscador");
       setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
             
        
    }
     
     public void componentes(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.gray);
        
        
        botoneliminar2 = new JButton("Eliminar Profesor");
        botoneliminar2.setBounds(250,60,200,30);
        add(botoneliminar2);
        
        actualizar = new JButton("Actualizar Datos");
        actualizar.setBounds(20,60,200,30);
        add(actualizar);
        
        
        asignar = new JButton("Asignar asignatura");
        asignar.setBounds(590, 45, 200, 20);
        add(asignar);
        
        designar = new JButton("Eliminar de asignatura");
        designar.setBounds(590,75,200,20);
        add(designar);
        
     
       
        modelo = new DefaultListModel();
        area = new JList();
        area.setBounds(20, 100, 750, 750);
        area.setModel(modelo);
        area.setVisible(true);
      add(area);
      
       
        add(panel);
 
            asignar.addActionListener(crear);
            actualizar.addActionListener(cargardatos);
        botoneliminar2.addActionListener(eliminarnodo);
        
        designar.addActionListener(eliminarrelacion);
        
    }
     

          static ActionListener cargardatos = new ActionListener() {
         Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        public void actionPerformed(ActionEvent e){
         
       
         List<Record> listaprofes;
             area.removeAll();
            modelo.removeAllElements();
            area.setModel(modelo);
            List<Record> listaasign;
            List<Record> listatotal;
            listaasign = almacenarasignaturasp();
            for(int i=0;i<listaasign.size();i++){
               Record p = listaasign.get(i);

                String añadiral = quitarComillas(p.get("u.nombre").toString());
               listaprofes =almacenarprofesor(añadiral);
             
               modelo.addElement(" ");
               String añadir = quitarComillas(p.get("id(u)")+" "+p.get("u.nombre")+" "+p.get("u.curso").toString());
               modelo.addElement(" "+añadir);
               


                 for(int j=0;j<listaprofes.size();j++){
                   Record r = listaprofes.get(j); 

                  String añadira=quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));

                    modelo.addElement(añadira);
                }   
            }
            modelo.addElement("-------------------------");
            listatotal = almacenartodosprofes();
            for(int u=0;u<listatotal.size();u++){
                Record r=listatotal.get(u);
                String addprofesor = quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));
                modelo.addElement((addprofesor));
            }
            area.setModel(modelo);
     }
        
         public  List<Record> almacenarprofesor( String asignatura){
         
            Session session = driver.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Profesor),(j:Asignatura{nombre:$asignatura}) WHERE (u)-[:Imparte]->(j) RETURN id(u),u.nombre,u.apellido",parameters("asignatura",asignatura));
            List<Record> lista = resultado.list();
            int numeroElementos = lista.size();
          
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;           
        }
         
         public  List<Record> almacenartodosprofes(){
         
            Session session = driver.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Profesor) RETURN id(u),u.nombre,u.apellido");
            List<Record> lista = resultado.list();
            int numeroElementos = lista.size();
          
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;           
        }
         
         
         public List<Record> almacenarasignaturasp(){
          
             Session session = driver.session();
             Transaction tx = session.beginTransaction();
             Result resultado = (Result) tx.run("MATCH (u:Asignatura) RETURN id(u),u.nombre,u.curso");
             List<Record> lista = resultado.list();
           int numeroElementos = lista.size();
          
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;
         } 
        
        };
            
            
          static ActionListener crear = new ActionListener(){
        Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        public void actionPerformed(ActionEvent e){
           
            
                String texto = area.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
             String id = texto3[0];
                        String ida = (String)JOptionPane.showInputDialog("ID asignatura");
                        
                       System.out.println(id+""+ida);    
            crearrelacion(id,ida);
            
            
               
            
            
            
        }
        
        
       public void crearrelacion(String id,String ida){
         
           try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                  
                     
                    Result result = tx.run( "MATCH (o:Profesor),(c:Asignatura)"+
                            "WHERE id(o)="+id+"and id(c)="+ida+" "+
                    "CREATE (o)-[:Imparte]->(c)"
                          );
                    tx.commit();
                    return result.single().get( 0 ).asString();
                }
            } );
            
        }
         
          
           
        }
    };
          
         static ActionListener eliminarnodo = new ActionListener(){
        Driver driver =GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        @Override
        public void actionPerformed (ActionEvent e){
           
            
                String texto = area.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
            String id = texto3[0];
             int i = area.getSelectedIndex();
            modelo.removeElementAt(i);           
          borrarnodop(id);     
            
            
        
        }
        
         public void borrarnodop(String id)
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                  String cypher = "MATCH (a:Profesor) WHERE id(a)="+id+" DETACH DELETE a";
                  
                    Result result = tx.run(cypher);
                    tx.commit();
                    return result.single().get( 0 ).asString();
                }
            } );
            session.close();
            System.out.println( greeting );
        }
    }
         
    };
         
         static ActionListener eliminarrelacion = new ActionListener(){
        Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        public void actionPerformed(ActionEvent e){
            
            
                String texto = area.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
            String id = texto3[0];
            System.out.println(id);
              String ida = (String)JOptionPane.showInputDialog("ID asignatura");
            eliminarrelacionp(id,ida);
                        int i = area.getSelectedIndex();
            modelo.removeElementAt(i);  
               
            
            
          
            
            
            
        }
        
       public void eliminarrelacionp(String id,String ida){
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                  
                     
                    Result result = tx.run( "MATCH (o:Profesor)-[r:Imparte]->(c:Asignatura)"+
                            "WHERE id(o)="+id+"and id(c)="+ida+""+
                    "DETACH DELETE r");
                    tx.commit();
                    return result.single().get( 0 ).asString();
                }
            } );
            
        }
        }
    };
         
         public static String quitarComillas(String a){
           String resultado = "";
           for(int i=0;i<a.length();i++){
               if(a.charAt(i)!='"'){
                   resultado+=a.charAt(i);
               }
           }
           return resultado;
       }
         
         
     }


