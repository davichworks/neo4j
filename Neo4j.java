/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.neo4jlimpio;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;


import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Value;

import static org.neo4j.driver.Values.parameters;

public class Neo4j extends JFrame  {
     static JTextField nombreprofesor;
     static JTextField apellidoprofesor;
     static JTextField apellidoalumno;
     static JTextField nombrealumno;
     static JComboBox<String> cursoalumno;
     static JTextField nombrebusqueda;
   
  
     static DefaultListModel modeloseleccion;
     
     static JList areatexto;
     
     static JList areaasignatura;
     static DefaultListModel modeloasignatura;
     
     
      public final Driver driver1= GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
     
     
      public static void main(String...args) throws Exception{
        
        Neo4j hola = new Neo4j();
        
               
       
    }
       
    
    public Neo4j(  )
    {
    
        
        componentes();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,500);
        setVisible(true);
        setTitle("MENU");

        
      
    }
    
    
            //Components JFrame
    public void componentes(){
           JPanel panel = new JPanel(new BorderLayout());
           panel.setBackground(Color.gray);
         
           JLabel etiquetaa = new JLabel("Alumno");
           JLabel etiquetap = new JLabel("Profesor");        
           
          
        
           JButton añadiralumno = new JButton("Insertar");
           JButton añadirprofesor = new JButton("Insertar");
           JButton añadirasignatura = new JButton("Añadir asignatura");
           JButton eliminarasignatura = new JButton("Eliminar asignatura");
           JButton borrar = new JButton("Eliminar");
           JButton buscar = new JButton("Buscar");
           JButton cargardatos = new JButton("Cargar Alumnos");
          JButton cargardatos2 = new JButton("Cargar Profesores");
          
         nombrealumno = new JTextField();
         apellidoalumno = new JTextField();
         cursoalumno = new JComboBox<String>();
          
         nombreprofesor = new JTextField("Nombre");
         apellidoprofesor = new JTextField("Apellido");
          
          areaasignatura = new JList();
          modeloasignatura = new DefaultListModel();
         
          
         
          areaasignatura.setBounds(200, 20, 150, 140);
          areaasignatura.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
           areaasignatura.setModel(modeloasignatura);
          add(areaasignatura);
          
          
         
          modeloseleccion = new DefaultListModel();
         
          
       
          
          
           nombrebusqueda = new JTextField("Nombre");
          
           areatexto = new JList();
           
          
           etiquetaa.setBounds(20, 25, 90,20);
           etiquetaa.setBackground(Color.red);
           add(etiquetaa);
           etiquetap.setBounds(20,125,90,20);
           add(etiquetap);
           
           nombrealumno.setBounds(70,25,90,20);
           
           nombrealumno.setText("Nombre");
           add(nombrealumno);
           apellidoalumno.setBounds(70,50,90,20);
           apellidoalumno.setText("Apellido");
           add(apellidoalumno);
           cursoalumno.setBounds(70,75,90,20);
           add(cursoalumno);
           cursoalumno.addItem("primero");
           cursoalumno.addItem("segundo");
           cursoalumno.addItem("tercero");
           cursoalumno.addItem("cuarto");
           
           
           nombreprofesor.setBounds(80,135,90,20);
           add(nombreprofesor);
           apellidoprofesor.setBounds(80,160,90,20);
           add(apellidoprofesor);
           
           añadirprofesor.setBounds(80, 185, 100, 20);
           add(añadirprofesor);
           añadiralumno.setBounds(70,100,100,20);
           add(añadiralumno);
           añadirasignatura.setBounds(200,170,150,20);
           add(añadirasignatura);
           eliminarasignatura.setBounds(200,200,150,20);
           add(eliminarasignatura);
           
        
          
           buscar.setBounds(20,250,100,20);
           add(buscar);
           
           nombrebusqueda.setBounds(130,250,100,20);
           add(nombrebusqueda);
           
           areatexto.setBounds(20,280,150,100);
           add(areatexto);
           
           borrar.setBounds(240,250,100,20);
           add(borrar);
           
           cargardatos.setBounds(20,430,170,20);
           add(cargardatos);
           cargardatos2.setBounds(200, 430, 170, 20);
          add(cargardatos2);
    
         
          
          añadirprofesor.addActionListener(añadirprofesorlis);
          añadiralumno.addActionListener(añadiralumnolis);
          
          buscar.addActionListener(buscarlis);
          borrar.addActionListener(botoneliminar);
          añadirasignatura.addActionListener(añadirasignaturalis);
          eliminarasignatura.addActionListener(eliminarasignaturalis);
          cargardatos.addActionListener(cargardatoslis);
          cargardatos2.addActionListener(cargardatosprofe);
          cursoalumno.addActionListener(mostrarasignaturas);
          
    
          
       
           add(panel);
    }


    public void close() throws Exception
    {
        driver1.close();
    }
    
   
            //Muestra asignaturas disponibles a un curso
    static ActionListener mostrarasignaturas = new ActionListener(){
      Driver driver1 = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
      @Override
      
      public void actionPerformed(ActionEvent e){
            modeloasignatura.removeAllElements();
            areaasignatura.setModel(modeloasignatura);
            String eleccion = (String) cursoalumno.getSelectedItem();
            List<Record> listar;
            if(eleccion.equals("primero")){
               listar = listarasignaturas("primero");
               System.out.println(listar.size());
                for(int i=0;i<listar.size();i++){
               Record p = listar.get(i);
               
                String asignatura = quitarComillas(p.get("u.nombre").toString());
              
               modeloasignatura.addElement(asignatura);
            
            }
                areaasignatura.setModel(modeloasignatura);
                
            }else if(eleccion.equals("segundo")){
               listar = listarasignaturas("segundo");
               
                for(int i=0;i<listar.size();i++){
               Record p = listar.get(i);
               
                String asignatura = quitarComillas(p.get("u.nombre").toString());
              
               modeloasignatura.addElement(asignatura);
            
            }
                areaasignatura.setModel(modeloasignatura);
            }else if(eleccion.equals("tercero")){
                listar = listarasignaturas("tercero");
                
                for(int i=0;i<listar.size();i++){
               Record p = listar.get(i);
               
                String asignatura = quitarComillas(p.get("u.nombre").toString());
              
               modeloasignatura.addElement(asignatura);
            
            }
                areaasignatura.setModel(modeloasignatura);
            }else if(eleccion.equals("cuarto")){
                listar = listarasignaturas("cuarto");
               
                for(int i=0;i<listar.size();i++){
               Record p = listar.get(i);
               
                String asignatura = quitarComillas(p.get("u.nombre").toString());
              
               modeloasignatura.addElement(asignatura);
            
            }
                areaasignatura.setModel(modeloasignatura);
            }else{System.out.println("error");}
        }
    
       
      public List<Record> listarasignaturas(String nombre){
            
            Session session = driver1.session();
            Transaction tx = session.beginTransaction();
            Result resultado = (Result) tx.run("MATCH(u:Asignatura{curso:$nombre}) RETURN id(u),u.nombre,u.apellido",parameters("nombre",nombre));
            List<Record> lista = resultado.list();
            
            
         
          while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;
            
        }
        
    };      
    
            //Añade un profesor
    static ActionListener añadirprofesorlis = new ActionListener() {
        
             Driver driver1 = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        @Override
        
        public void actionPerformed(ActionEvent e){
            String nombreprofe = nombreprofesor.getText();
            String apellidoprofe = apellidoprofesor.getText();
            
            if( nombreprofe.equals("Nombre") || apellidoprofe.equals("Apellido")){
                System.out.println("error");
            }else {
                   crearNodoProfesor(nombreprofe,apellidoprofe);
            }
         
        }
        
        public void crearNodoProfesor(  String nombre, String apellido )
    {
        try ( Session session = driver1.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "CREATE (p:Profesor) " +
                                                     "SET p.nombre = $nombre,p.apellido=$apellido " +
                                                     "RETURN p.nombre + ', from node ' + id(p)",
                            parameters( "nombre", nombre,"apellido",apellido ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }  
    };
      
            //Añade un alumno [FALTA EDITARLO]
    static ActionListener añadiralumnolis = new ActionListener() {
        
             Driver driver1 = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        @Override
        
        public void actionPerformed(ActionEvent e){
   
            String nombrealum = nombrealumno.getText();
            String apellidoalum = (String) apellidoalumno.getText();
            String cursoalum = (String) cursoalumno.getSelectedItem();
            String asignatura = (String) areaasignatura.getSelectedValue();
            if( nombrealum.equals("Nombre") || apellidoalum.equals("Apellido") || asignatura.equals(null)){
                System.out.println("Error");
            }else {
                     crearNodoAlumno(nombrealum,apellidoalum,cursoalum);
           
          
               crearrelacion(nombrealum,apellidoalum,cursoalum,asignatura);
            }
        
           
           
        }
        public void crearrelacion(String nombre,String apellido,String curso,String asignatura){
        
    Session session = driver1.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Alumno{nombre:$nombre,apellido:$apellido,curso:$curso}),(j:Asignatura{nombre:$asignatura}) CREATE (u)-[:Participa]->(j)",parameters("asignatura",asignatura,"nombre",nombre,"apellido",apellido,"curso",curso));
        
    
        }
        
        
        
    
     public void crearNodoAlumno(String nombre,String apellido,String curso)   
    {
        try ( Session session = driver1.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "CREATE (a:Alumno) " +
                                                     "SET a.nombre = $nombre,a.apellido=$apellido,a.curso=$curso " +
                                                     "RETURN a.nombre + ', from node ' + id(a)",
                            parameters( "nombre", nombre,"apellido",apellido,"curso",curso ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }
    };
    
    
            //Añade asignatura a un curso
    static ActionListener añadirasignaturalis = new ActionListener() {
        
             Driver driver1 = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        @Override
        
        public void actionPerformed(ActionEvent e){
            String nombreasignatura =(String)JOptionPane.showInputDialog(null,"Nombre asignatura");
            String cursoasignatura = (String) cursoalumno.getSelectedItem();
            
            if(nombreasignatura!=""){
                 crearNodoAsignatura(nombreasignatura,cursoasignatura);
            }
           
        }
        
        public void crearNodoAsignatura( final String nombre,final String curso )
    {
        try ( Session session = driver1.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "CREATE (f:Asignatura) " +
                                                     "SET f.nombre = $nombre,f.curso=$curso " +
                                                     "RETURN f.nombre + ', from node ' + id(f)",
                            parameters( "nombre", nombre,"curso",curso ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }  
    };
    
    //Elimina asignatura
     static ActionListener eliminarasignaturalis = new ActionListener() {
        
             Driver driver1 = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        @Override
        
        public void actionPerformed(ActionEvent e){
            String nombreasignatura =(String)areaasignatura.getSelectedValue();
            System.out.println(nombreasignatura);
            String cursoasignatura = (String) cursoalumno.getSelectedItem();
             System.out.println(cursoasignatura);
           borrarNodoAsignatura(nombreasignatura,cursoasignatura);
        }
        
        public void borrarNodoAsignatura( final String nombre,final String curso )
    {
        try ( Session session = driver1.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "MATCH (j:Asignatura{nombre:$nombre,curso:$curso}) DETACH DELETE j",
                            parameters( "nombre", nombre,"curso",curso ) );
                            tx.commit();
                    return result.single().get( 0 ).asString();
                   
                }
            } );
            
            System.out.println( greeting );
        }
    }  
    };
    
    
            //eliminar profe de JList
    static ActionListener botoneliminar = new ActionListener(){
        Driver driver1 =GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        @Override
        public void actionPerformed (ActionEvent e){
            String texto = areatexto.getSelectedValue().toString();
         
            
           
            String texto2[] = texto.split(" ");
            String id = texto2[0];

            int i = areatexto.getSelectedIndex();
            modeloseleccion.removeElementAt(i);
           
            areatexto.setModel(modeloseleccion);
           
            
               borrarnodop(id);
           
            
            
        }
        
         
         public void borrarnodop( final String id)
    {
        try ( Session session = driver1.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                  String cypher = "MATCH (a:Profesor) WHERE id(a)="+id+" DETACH DELETE a";
                  System.out.println(cypher);
                    Result result = tx.run(cypher);
                    tx.commit();
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }
    };
    
    
    
   
            /*
    static ActionListener asignarplis = new ActionListener() {
        Driver driver1 = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        @Override
        
    
             public void actionPerformed(ActionEvent e){
            String asign =(String)JOptionPane.showInputDialog(null,"Nombre Profesor");
      
          
                String asignatura = (String)JOptionPane.showInputDialog(null,"Asignatura");
             
            
           
         
               crearrelaciona(asign,asignatura);
           
        }
             public void crearrelaciona(final String nombrealumno,final String asignatura )
             {
       
        try ( Session session = driver1.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                  
                     
                    Result result = tx.run( "MATCH (o:Profesor{nombre:$user1}),(c:Asignatura{nombre:$user2})"+
                    "CREATE (o)-[:Imparte]->(c)",
                            parameters("user1",nombrealumno,"user2",asignatura));
                    tx.commit();
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
        }
    
    };
    */
    
            //Cargar datos asignaturas y alumnos
    static ActionListener cargardatoslis = new ActionListener() {
        
             
        
        public void actionPerformed(ActionEvent e){
          
           Buscador v = new Buscador();
           v.setVisible(true);
           
           
        }
    };
    
     static ActionListener cargardatosprofe = new ActionListener() {
        
             
        
        public void actionPerformed(ActionEvent e){
          
           Buscador2 b = new Buscador2();
           b.setVisible(true);
           
           
        }
    };
    
            //Borrar toda la base
            static ActionListener borrartodolis = new ActionListener() {
        
             Driver driver1 = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        @Override
        
        public void actionPerformed(ActionEvent e){
          
           borrartodo();
        }
        public void borrartodo(  )
    {
        try ( Session session = driver1.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run(  "MATCH (a) " +
                                           "DETACH DELETE a",
                            parameters( ));
                    tx.commit();
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }
    }; 
            
            
            //Cargar profesores en JList
            static ActionListener buscarlis = new ActionListener(){
        Driver driver1 = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        public void actionPerformed(ActionEvent e){
          
          
            String nommbre = nombrebusqueda.getText();
            List<Record> listaproducir ;
           
            areatexto.removeAll();
            modeloseleccion.removeAllElements();
            
            
                
                 listaproducir = buscarnodoProfesor(nommbre);
                
                
                for(int i =0;i<listaproducir.size();i++){
                   Record r = listaproducir.get(i);                  
                  String texto=quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));
                  
                    modeloseleccion.addElement(texto);
                 
                }
                 System.out.println(listaproducir);
                areatexto.setModel(modeloseleccion);
            
      
                           
        }
      
       
                public List<Record> buscarnodoProfesor(String nombre){
            
            Session session = driver1.session();
            Transaction tx = session.beginTransaction();
            Result resultado = (Result) tx.run("MATCH(u:Profesor{nombre:$nombre}) RETURN id(u),u.nombre,u.apellido",parameters("nombre",nombre));
            List<Record> lista = resultado.list();
            int numeroElementos = lista.size();
            Map<String,Object> mapa = null;
            
          /*  for(int i=0;i<numeroElementos;i++){
                mapa = lista.get(i).asMap();
                listaNodosp.add(mapa);
            }*/
          
          while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;
            
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