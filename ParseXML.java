// Example Code for parsing XML file
// Dr. Moushumi Sharmin
// CSCI 345

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;

import java.util.*;

public class ParseXML{
   
        // building a document from the XML file
        // returns a Document object after loading the book.xml file.
        public Document getDocFromFile(String filename)
        throws ParserConfigurationException{
        {
            
                  
           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
           DocumentBuilder db = dbf.newDocumentBuilder();
           Document doc = null;
           
           try{
               doc = db.parse(filename);
           } catch (Exception ex){
               System.out.println("XML parse failure");
               ex.printStackTrace();
           }
           return doc;
        } // exception handling
        
        }  
        
        // reads data from XML file and prints data
        public void readBookData(Document d){
        
            Element root = d.getDocumentElement();
            
            NodeList books = root.getElementsByTagName("book");
            
            for (int i=0; i<books.getLength();i++){
                
                System.out.println("Printing information for book "+(i+1));
                
                //reads data from the nodes
                Node book = books.item(i);
                String bookCategory = book.getAttributes().getNamedItem("category").getNodeValue();
                System.out.println("Category = "+bookCategory);
                
                //reads data
                                             
                NodeList children = book.getChildNodes();
                
                for (int j=0; j< children.getLength(); j++){
                    
                  Node sub = children.item(j);
                
                  if("title".equals(sub.getNodeName())){
                     String bookLanguage = sub.getAttributes().getNamedItem("lang").getNodeValue();
                     System.out.println("Language = "+bookLanguage);
                     String title = sub.getTextContent();
                     System.out.println("Title = "+title);
                     
                  }
                  
                  else if("author".equals(sub.getNodeName())){
                     String authorName = sub.getTextContent();
                     System.out.println(" Author = "+authorName);
                     
                  }
                  else if("year".equals(sub.getNodeName())){
                     String yearVal = sub.getTextContent();
                     System.out.println(" Publication Year = "+yearVal);
                     
                  }
                  else if("price".equals(sub.getNodeName())){
                     String priceVal = sub.getTextContent();
                     System.out.println(" Price = "+priceVal);
                     
                  }
                                 
                
                } //for childnodes
                
                System.out.println("\n");
                
            }//for book nodes
        
        }// method

        public LinkedList<Set> readBoard(Document d){
         Element root = d.getDocumentElement();

         LinkedList<Set> boardSets = new LinkedList<Set>();
            
         NodeList sets = root.getElementsByTagName("set");

            for (int i=0; i<sets.getLength();i++){

               Set set = new Set();
                  
               System.out.println("Printing information for set "+(i+1));
               
               //reads data from the nodes
               Node node = sets.item(i);
               NamedNodeMap setAttributes = node.getAttributes();
               String setname = setAttributes.getNamedItem("name").getNodeValue();
               System.out.println("Set name= " + setname);
               set.setName(setname);

               //reads data
                                          
               NodeList children = node.getChildNodes();
               
               for (int j=0; j < children.getLength(); j++){
                  
                  Node sub = children.item(j);
                  //Nodelist parts = sub.getElementsByTagName("part");
                  
                  if("takes".equals(sub.getNodeName())){
                     System.out.println(sub.getNodeName());
                     
                     NodeList takers = sub.getChildNodes();
                     for(int k=0;k<takers.getLength();k++){
                        if("take".equals(takers.item(k).getNodeName())){
                           String number = takers.item(k).getAttributes().item(0).getNodeValue();
                           //System.out.println(k + " take amount = "+number);
                           set.setShotCapacity(Integer.parseInt(number));
                        }                        
                     }

                  }else if("parts".equals(sub.getNodeName())){
                     NodeList parts = sub.getChildNodes();
                     for(int k = 0 ;k<parts.getLength();k++){
                        Node part = parts.item(k);
                        //Role role = new Role();
                        
                        if(part.getNodeName().equals("part")){

                           NamedNodeMap partattributes = part.getAttributes();
                           NodeList subs = part.getChildNodes();

                           String name = ""+ partattributes.getNamedItem("name").getTextContent();
                           String minrank = "" + partattributes.getNamedItem("level").getTextContent();
                           String desc = subs.item(3).getTextContent();

                           System.out.println(k+" Role name: " + name+"\nmin rank: " + minrank);
                           System.out.println(desc);
                           Role role = new Role();
                           role.setDesc(desc);
                           role.setTitle(name);
                           role.setRank(Integer.parseInt(minrank));
                           set.addRole(role);
                        }
                        //set.addRole(role);
                        //set.setActCapacity(set.getRoleCount());
                        //System.out.println("Actor capacity: " + set.getRoleCount());
                     }
                     set.setActCapacity(set.getRoleCount());
                     System.out.println("Actor capacity: " + set.getRoleCount());
                  }

               }System.out.println("end set");
            }
            return boardSets;
         }
      
      public LinkedList<Scene> readCards(Document d) {
      
      Element root = d.getDocumentElement();
      NodeList cardNodes = root.getElementsByTagName("card");
      LinkedList<Scene> scenes = new LinkedList<Scene>();

      for (int i = 0; i < cardNodes.getLength(); i++) {

         Node currentCardNode = cardNodes.item(i);
         NamedNodeMap cardAttributes =  currentCardNode.getAttributes();

         NodeList cardChildren = currentCardNode.getChildNodes();

         String title = cardAttributes.getNamedItem("name").getNodeValue();
         String description = "";
         int budget = Integer.parseInt(cardAttributes.getNamedItem("budget").getNodeValue());
         LinkedList<Role> parts = new LinkedList<Role>();
         int actorCapacity;

         for (int j = 0; j < cardChildren.getLength(); j++) {

            Node cardChild = cardChildren.item(j);
            
            if (cardChild.getNodeName().compareTo("scene") == 0) {
               description = cardChild.getTextContent();

            }
            else if (cardChild.getNodeName().compareTo("part") == 0) {
               Role newRole = new Role();
               NamedNodeMap roleAttributes = cardChild.getAttributes();

               newRole.setTitle(roleAttributes.getNamedItem("name").getNodeValue());

               newRole.setRank(Integer.parseInt(roleAttributes.getNamedItem("level").getNodeValue()));
               newRole.description = cardChild.getLastChild().getTextContent();

               parts.add(newRole);
            }
         }

         actorCapacity = parts.size();
         
         scenes.add(new Scene(title, description, budget, parts, actorCapacity));
      }

      return scenes;
   }

}//class
