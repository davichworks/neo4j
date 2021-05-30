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
public class Buscador extends JFrame {
    
   static DefaultListModel modelo4;
   static DefaultListModel modelo1;
   static DefaultListModel modelo2;
   static DefaultListModel modelo3;
   static JButton botoneliminar;
   static JList areatexto1;
   static JList areatexto2;
   static JList areatexto3;
   static JList areatexto4;
   static String a;
   
   static JButton añadirasignatura;
   static JButton eliminarasignatura;
   
   
    public Buscador(){
        
        componentes();
        
        setSize(800,900);
        setVisible(true);
        setTitle("Buscador");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
    
            //Componentes
    public void componentes(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.gray);
        
        botoneliminar = new JButton("Eliminar");
        botoneliminar.setBounds(480,50,100,40);
        add(botoneliminar);
        
        añadirasignatura = new JButton("Añadir asignatura");
        añadirasignatura.setBounds(590, 45, 200, 20);
        add(añadirasignatura);
        
        eliminarasignatura = new JButton("Eliminar asignatura");
        eliminarasignatura.setBounds(590,75,200,20);
        add(eliminarasignatura);
        
        JButton primero = new JButton("Primero");
        primero.setBounds(20, 50, 100, 40);
        add(primero);
        
        JButton segundo = new JButton("Segundo");
        segundo.setBounds(130, 50, 100, 40);
        add(segundo);
      
        JButton tercero = new JButton("Tercero");
        tercero.setBounds(240, 50, 100, 40);
        add(tercero);
        
        JButton cuarto = new JButton("Cuarto");
        cuarto.setBounds(350, 50, 100, 40);
        add(cuarto);
        /*JPanel panelsegundo = new JPanel();
        JPanel paneltercero = new JPanel();
        JPanel panelcuarto = new JPanel();*/
        
       
       
        modelo1 = new DefaultListModel();
        areatexto1 = new JList();
        areatexto1.setBounds(20, 100, 750, 750);
        areatexto1.setModel(modelo1);
        areatexto1.setVisible(false);
        add(areatexto1);
        
         modelo2 = new DefaultListModel();
        areatexto2 = new JList();
        areatexto2.setBounds(20, 100, 750, 750);
        areatexto2.setModel(modelo1);
        areatexto2.setVisible(false);
        add(areatexto2);
        
          modelo3 = new DefaultListModel();
        areatexto3 = new JList();
        areatexto3.setBounds(20, 100, 750, 750);
        areatexto3.setModel(modelo3);
        areatexto3.setVisible(false);
        add(areatexto3);
        
         modelo4 = new DefaultListModel();
        areatexto4 = new JList();
        areatexto4.setBounds(20, 100, 750, 750);
        areatexto4.setModel(modelo4);
        areatexto4.setVisible(false);
        add(areatexto4);
       
        add(panel);
     
        primero.addActionListener(cargarprimero);
        segundo.addActionListener(cargarsegundo);
        tercero.addActionListener(cargartercero);
        cuarto.addActionListener(cargarcuarto);
        botoneliminar.addActionListener(eliminarnodo);
        añadirasignatura.addActionListener(crearrelacion);
        eliminarasignatura.addActionListener(eliminarrelacion);
        
    }
   
    
    
            //Carga asignaturas y alumnos de primero
     static ActionListener cargarprimero = new ActionListener() {
         Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        public void actionPerformed(ActionEvent e){
            areatexto1.setVisible(true);
            areatexto2.setVisible(false);
            areatexto3.setVisible(false);
            areatexto4.setVisible(false);
            a ="primero";
            List<Record> listaalumnos;
             areatexto1.removeAll();
            modelo1.removeAllElements();
            areatexto1.setModel(modelo1);
            List<Record> listaasign;
            List<Record> listatotal;
            listaasign = almacenarasignaturasp();
            for(int i=0;i<listaasign.size();i++){
               Record p = listaasign.get(i);

                String añadiral = quitarComillas(p.get("u.nombre").toString());
               listaalumnos =almacenaralumnosp(añadiral);
               modelo1.addElement(" ");
               modelo1.addElement(" "+añadiral);


                 for(int j=0;j<listaalumnos.size();j++){
                   Record r = listaalumnos.get(j); 

                  String añadira=quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));

                    modelo1.addElement(añadira);
                }   
            }
            modelo1.addElement("-------------------------");
            listatotal = almacenartodosalumnos();
            for(int u=0;u<listatotal.size();u++){
                Record r=listatotal.get(u);
                String addalumno = quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));
                modelo1.addElement((addalumno));
            }
            areatexto1.setModel(modelo1);
        }

         public  List<Record> almacenaralumnosp( String asignatura){
         
            Session session = driver.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Alumno{curso:'primero'}),(j:Asignatura{nombre:$asignatura}) WHERE (u)-[:Participa]->(j) RETURN id(u),u.nombre,u.apellido",parameters("asignatura",asignatura));
            List<Record> lista = resultado.list();
            int numeroElementos = lista.size();
          
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;           
        }
         
         public  List<Record> almacenartodosalumnos(){
         
            Session session = driver.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Alumno{curso:'primero'}) RETURN id(u),u.nombre,u.apellido");
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
             Result resultado = (Result) tx.run("MATCH (u:Asignatura{curso:'primero'}) RETURN u.nombre");
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
     
            //Carga asignaturas y alumnos de segundo
     static ActionListener cargarsegundo = new ActionListener() {
         Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        public void actionPerformed(ActionEvent e){
            areatexto1.setVisible(false);
            areatexto2.setVisible(true);
            areatexto3.setVisible(false);
            areatexto4.setVisible(false);
            a ="segundo";
            List<Record> listaalumnos;
             areatexto2.removeAll();
            modelo2.removeAllElements();
            areatexto2.setModel(modelo2);
            List<Record> listaasign;
            listaasign = almacenarasignaturass();
            List<Record> listatotal;
            for(int i=0;i<listaasign.size();i++){
               Record p = listaasign.get(i);
               
                String añadiral = quitarComillas(p.get("u.nombre").toString());
               listaalumnos =almacenaralumnoss(añadiral);
                modelo2.addElement(" ");
               modelo2.addElement(" "+añadiral);
         
               
          
                 for(int j=0;j<listaalumnos.size();j++){
                   Record r = listaalumnos.get(j); 
                    
                  String añadira=quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));
                   
                    modelo2.addElement(añadira);
                }   
            }
            modelo2.addElement("-------------------------");
            listatotal = almacenartodosalumnos();
            for(int u=0;u<listatotal.size();u++){
                Record r=listatotal.get(u);
                String addalumno = quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));
                modelo2.addElement((addalumno));
            }
            areatexto2.setModel(modelo2);
        }
            
         public  List<Record> almacenaralumnoss( String asignatura){
         
            Session session = driver.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Alumno{curso:'segundo'}),(j:Asignatura{nombre:$asignatura}) WHERE (u)-[:Participa]->(j) RETURN id(u),u.nombre,u.apellido",parameters("asignatura",asignatura));
            List<Record> lista = resultado.list();
            int numeroElementos = lista.size();
          
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;           
        }
         
         public  List<Record> almacenartodosalumnos(){
         
            Session session = driver.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Alumno{curso:'segundo'}) RETURN id(u),u.nombre,u.apellido");
            List<Record> lista = resultado.list();
            int numeroElementos = lista.size();
          
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;           
        }
         
         public List<Record> almacenarasignaturass(){
          
             Session session = driver.session();
             Transaction tx = session.beginTransaction();
             Result resultado = (Result) tx.run("MATCH (u:Asignatura{curso:'segundo'}) RETURN u.nombre");
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
     
            //Carga asignaturas y alumnos de tercero
     static ActionListener cargartercero = new ActionListener() {
         Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        public void actionPerformed(ActionEvent e){
             areatexto1.setVisible(false);
            areatexto2.setVisible(false);
            areatexto3.setVisible(true);
              areatexto4.setVisible(false);
              a="tercero";
            List<Record> listaalumnos;
             areatexto3.removeAll();
            modelo3.removeAllElements();
            areatexto3.setModel(modelo3);
            List<Record> listaasign;
            List<Record> listatotal;
            listaasign = almacenarasignaturass();
            for(int i=0;i<listaasign.size();i++){
               Record p = listaasign.get(i);
               
                String añadiral = quitarComillas(p.get("u.nombre").toString());
               listaalumnos =almacenaralumnoss(añadiral);
                modelo3.addElement(" ");
               modelo3.addElement(" "+añadiral);
          
          
                 for(int j=0;j<listaalumnos.size();j++){
                   Record r = listaalumnos.get(j); 
                    
                  String añadira=quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));
                   
                    modelo3.addElement(añadira);
                }   
            }
            modelo3.addElement("-------------------------");
            listatotal = almacenartodosalumnos();
            for(int u=0;u<listatotal.size();u++){
                Record r=listatotal.get(u);
                String addalumno = quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));
                modelo3.addElement((addalumno));
            }
            areatexto3.setModel(modelo3);
        }
            
         public  List<Record> almacenaralumnoss( String asignatura){
         
            Session session = driver.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Alumno{curso:'tercero'}),(j:Asignatura{nombre:$asignatura}) WHERE (u)-[:Participa]->(j) RETURN id(u),u.nombre,u.apellido",parameters("asignatura",asignatura));
            List<Record> lista = resultado.list();
            int numeroElementos = lista.size();
          
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;           
        }
         
         public  List<Record> almacenartodosalumnos(){
         
            Session session = driver.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Alumno{curso:'tercero'}) RETURN id(u),u.nombre,u.apellido");
            List<Record> lista = resultado.list();
            int numeroElementos = lista.size();
          
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;           
        }
         
         
         public List<Record> almacenarasignaturass(){
          
             Session session = driver.session();
             Transaction tx = session.beginTransaction();
             Result resultado = (Result) tx.run("MATCH (u:Asignatura{curso:'tercero'}) RETURN u.nombre");
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
     
            //Carga asignaturas y alumnos de cuarto
     static ActionListener cargarcuarto = new ActionListener() {
         Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        public void actionPerformed(ActionEvent e){
             areatexto1.setVisible(false);
            areatexto2.setVisible(false);
            areatexto3.setVisible(false);
            areatexto4.setVisible(true);
            a="cuarto";
            
            List<Record> listaalumnos;
            List<Record> listatotal;
             areatexto4.removeAll();
            modelo4.removeAllElements();
            areatexto4.setModel(modelo4);
            List<Record> listaasign;
            listaasign = almacenarasignaturass();
            for(int i=0;i<listaasign.size();i++){
               Record p = listaasign.get(i);
               
                String añadiral = quitarComillas(p.get("u.nombre").toString());
               listaalumnos =almacenaralumnoss(añadiral);
                modelo4.addElement(" ");
               modelo4.addElement(" "+añadiral);
              
          
                 for(int j=0;j<listaalumnos.size();j++){
                   Record r = listaalumnos.get(j); 
                    
                  String añadira=quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));
                   
                    modelo4.addElement(añadira);
                }   
            }
            modelo4.addElement("-------------------------");
            listatotal = almacenartodosalumnos();
            for(int u=0;u<listatotal.size();u++){
                Record r=listatotal.get(u);
                String addalumno = quitarComillas(r.get("id(u)")+" "+r.get("u.nombre")+" "+r.get("u.apellido"));
                modelo4.addElement((addalumno));
            }
            
            areatexto4.setModel(modelo4);
        }
            
         public  List<Record> almacenaralumnoss( String asignatura){
         
            Session session = driver.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Alumno{curso:'cuarto'}),(j:Asignatura{nombre:$asignatura}) WHERE (u)-[:Participa]->(j) RETURN id(u),u.nombre,u.apellido",parameters("asignatura",asignatura));
            List<Record> lista = resultado.list();
            int numeroElementos = lista.size();
          
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;           
        }
         
         public  List<Record> almacenartodosalumnos(){
         
            Session session = driver.session();
            Transaction tx = session.beginTransaction();
           Result resultado = (Result) tx.run("MATCH(u:Alumno{curso:'cuarto'}) RETURN id(u),u.nombre,u.apellido");
            List<Record> lista = resultado.list();
            int numeroElementos = lista.size();
          
        
         while(resultado.hasNext()){
             Record r= resultado.next();
             lista.add(r);
         }
            session.close();
            return lista;           
        }
         
         public List<Record> almacenarasignaturass(){
          
             Session session = driver.session();
             Transaction tx = session.beginTransaction();
             Result resultado = (Result) tx.run("MATCH (u:Asignatura{curso:'cuarto'}) RETURN u.nombre");
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
     
     
     
            //Elimina un alumno a 
     
     static ActionListener eliminarnodo = new ActionListener(){
        Driver driver =GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        @Override
        public void actionPerformed (ActionEvent e){
           
            if (a.equals("primero")){
                String texto = areatexto1.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
            String id = texto3[0];
             int i = areatexto1.getSelectedIndex();
            modelo1.removeElementAt(i);           
          borrarnodoa(id);     
            }else if(a.equals("segundo")){
                String texto = areatexto2.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
            String id = texto3[0];
             int i = areatexto2.getSelectedIndex();
            modelo2.removeElementAt(i);           
          borrarnodoa(id);     
            }else if(a.equals("tercero")){
                String texto = areatexto3.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
            String id = texto3[0];
             int i = areatexto3.getSelectedIndex();
            modelo3.removeElementAt(i);           
          borrarnodoa(id);     
            }else if(a.equals("cuarto")){
                String texto = areatexto4.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
            String id = texto3[0];
             int i = areatexto4.getSelectedIndex();
            modelo4.removeElementAt(i);           
          borrarnodoa(id);     
            }
            
        
                
        }
        
         public void borrarnodoa(String id)
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                  String cypher = "MATCH (a:Alumno) WHERE id(a)="+id+" DETACH DELETE a";
                  
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
            
            if (a.equals("primero")){
                String texto = areatexto1.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
            String id = texto3[0];
            System.out.println(id);
              String nombreas = (String)JOptionPane.showInputDialog("Nombre asignatura");
            eliminarrelacionn(id,nombreas,"primero");
                        int i = areatexto1.getSelectedIndex();
            modelo1.removeElementAt(i);  
               
            }else if(a.equals("segundo")){
                String texto = areatexto2.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
             String id = texto3[0];
               String nombreas = (String)JOptionPane.showInputDialog("Nombre asignatura");
             eliminarrelacionn(id,nombreas,"segundo");
                 int i = areatexto2.getSelectedIndex();
            modelo2.removeElementAt(i);  
            }else if(a.equals("tercero")){
                String texto = areatexto3.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
            String id = texto3[0];
               String nombreas = (String)JOptionPane.showInputDialog("Nombre asignatura");
             eliminarrelacionn(id,nombreas,"tercero");
              int i = areatexto3.getSelectedIndex();
            modelo3.removeElementAt(i);  
            }else if(a.equals("cuarto")){
                String texto = areatexto4.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
             String id = texto3[0];
               String nombreas = (String)JOptionPane.showInputDialog("Nombre asignatura");
             eliminarrelacionn(id,nombreas,"cuarto");
                 int i = areatexto4.getSelectedIndex();
            modelo4.removeElementAt(i);  
            }
            
          
            
            
            
        }
        
       public void eliminarrelacionn(String id,String nombre,String curso){
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                  
                     
                    Result result = tx.run( "MATCH (o:Alumno)-[r:Participa]->(c:Asignatura{nombre:$user2,curso:$curso})"+
                            "WHERE id(o)="+id+""+
                    "DELETE r",
                            parameters("user2",nombre,"curso",curso));
                    tx.commit();
                    return result.single().get( 0 ).asString();
                }
            } );
            
        }
        }
    };
    
    static ActionListener crearrelacion = new ActionListener(){
        Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "123" ) );
        public void actionPerformed(ActionEvent e){
           
            if (a.equals("primero")){
                String texto = areatexto1.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
             String id = texto3[0];
                        String nombreas = JOptionPane.showInputDialog("Nombre asignatura");
            
            crearrelacionn(id,nombreas,"primero");
            
               
            }else if(a.equals("segundo")){
               
                String texto = areatexto2.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
             String id = texto3[0];
            
             
             String nombreas = JOptionPane.showInputDialog("Nombre asignatura");
           
            
            crearrelacionn(id,nombreas,"segundo");
                
            }else if(a.equals("tercero")){
                String texto = areatexto3.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
             String id = texto3[0];
             String nombreas = (String)JOptionPane.showInputDialog("Nombre asignatura");
            
            crearrelacionn(id,nombreas,"tercero");
         
            }else if(a.equals("cuarto")){
                String texto = areatexto4.getSelectedValue().toString();
                String texto3[] = texto.split(" ");
             String id = texto3[0];
                 
             String nombreas = JOptionPane.showInputDialog("Nombre asignatura");
            
            crearrelacionn(id,nombreas,"cuarto");
            
            }
            
            
        }
        
        
       public void crearrelacionn(String id,String nombre,String curso){
         
            
         
           try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                  
                     
                    Result result = tx.run( "MATCH (o:Alumno),(c:Asignatura{nombre:$user2,curso:$curso})"+
                            "WHERE id(o)="+id+""+
                    "CREATE (o)-[:Participa]->(c)",
                            parameters("user2",nombre,"curso",curso));
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
