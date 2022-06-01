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

public class ParseXML {

   // building a document from the XML file
   // returns a Document object after loading the book.xml file.
   public Document getDocFromFile(String filename)
         throws ParserConfigurationException {
      {

         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = dbf.newDocumentBuilder();
         Document doc = null;

         try {
            doc = db.parse(filename);
         } catch (Exception ex) {
            System.out.println("XML parse failure");
            ex.printStackTrace();
         }
         return doc;
      } // exception handling

   }

   public LinkedList<Set> readBoard(Document d) { //inits the board tiles
      Element root = d.getDocumentElement();

      LinkedList<Set> boardSets = new LinkedList<Set>();

      NodeList sets = root.getElementsByTagName("set");

      for (int i = 0; i < sets.getLength(); i++) {
         Set set = new Set();
         set.setTileID(i);
         // reads data from the nodes
         Node node = sets.item(i);
         NamedNodeMap setAttributes = node.getAttributes();
         String setname = setAttributes.getNamedItem("name").getNodeValue();
         LinkedList<String> coordinates = new LinkedList<String>();
         set.setName(setname);

         // reads data

         NodeList children = node.getChildNodes();

         for (int j = 0; j < children.getLength(); j++) {
            Node sub = children.item(j);
            if ("takes".equals(sub.getNodeName())) {
               NodeList takers = sub.getChildNodes();
               for (int k = 0; k < takers.getLength(); k++) {
                  if ("take".equals(takers.item(k).getNodeName())) {
                     LinkedList<String> takecoords = new LinkedList<String>();
                     String number = takers.item(k).getAttributes().item(0).getNodeValue();
                     NodeList tcord = takers.item(k).getChildNodes();
                     Node area = tcord.item(0);
                     NamedNodeMap aCs = area.getAttributes();
                     String c = tcord.item(0).getNodeName();
                     String x = aCs.getNamedItem("x").getTextContent();
                     String y = aCs.getNamedItem("y").getTextContent();
                     String h = aCs.getNamedItem("h").getTextContent();
                     String w = aCs.getNamedItem("w").getTextContent();
                     String[] coordset = {x,y,h,w};
                     set.addTakecoords(coordset);
                     set.setShotCapacity(Integer.parseInt(number));
                  }
               }

            }else if("area".equals(sub.getNodeName())){
              int x= Integer.parseInt(sub.getAttributes().getNamedItem("x").getTextContent());
              int y= Integer.parseInt(sub.getAttributes().getNamedItem("y").getTextContent());
              int h= Integer.parseInt(sub.getAttributes().getNamedItem("h").getTextContent());
              int w= Integer.parseInt(sub.getAttributes().getNamedItem("w").getTextContent());
                coordinates.add(""+x);
                coordinates.add(""+y);
                coordinates.add(""+h);
                coordinates.add(""+w);
                coordinates.add(setname);
                set.setCoords(coordinates);
            }else if ("parts".equals(sub.getNodeName())) {
               NodeList parts = sub.getChildNodes();
               for (int k = 0; k < parts.getLength(); k++) {
                  Node part = parts.item(k);
                  if (part.getNodeName().equals("part")) {
                    LinkedList<String> cords = new LinkedList<String>();
                     NamedNodeMap partattributes = part.getAttributes();
                     NodeList subs = part.getChildNodes();
                     for(int p=0;p<subs.getLength();p++){
                       Node temp = subs.item(p);
                       if("area".equals(temp.getNodeName())){
                         cords.add(temp.getAttributes().getNamedItem("x").getTextContent());
                         cords.add(temp.getAttributes().getNamedItem("y").getTextContent());
                         cords.add(temp.getAttributes().getNamedItem("h").getTextContent());
                         cords.add(temp.getAttributes().getNamedItem("w").getTextContent());
                       }
                     }
                     String name = "" + partattributes.getNamedItem("name").getTextContent();
                     String minrank = "" + partattributes.getNamedItem("level").getTextContent();
                     String desc = subs.item(3).getTextContent();
                     Role role = new Role();
                     role.setCoords(cords);
                     role.setDesc(desc);
                     role.setTitle(name);
                     role.setRank(Integer.parseInt(minrank));
                     role.toggleExtra();
                     set.addRole(role);
                  }
               }
               set.setActCapacity(set.getRoleCount());
            }
         }
         boardSets.add(set);
      }

      NodeList forTrailer = root.getElementsByTagName("trailer");
      Set trailer = new Set();
      trailer.setName("trailer");
      trailer.setScene(null);
      trailer.setShotCapacity(-1);
      trailer.setActCapacity(8);
      trailer.noRoles();
      trailer.setTileID(10);
      LinkedList<String> coordinates = new LinkedList<String>();
      for (int i = 0; i < forTrailer.getLength(); i++) {
         NodeList children = forTrailer.item(i).getChildNodes();
         for (int j = 0; j < children.getLength(); j++) {
            Node sub = children.item(j);
            if ("neighbors".equals(sub.getNodeName())) {
               NodeList nlist = sub.getChildNodes();
               for (int k = 0; k < nlist.getLength(); k++) {
                  Node neighbor = nlist.item(k);
                  if (neighbor.getNodeName().equals("neighbor")) {
                     NamedNodeMap atts = neighbor.getAttributes();
                     for (int p = 0; p < boardSets.size(); p++) {
                        if (boardSets.get(p).getName().equals(atts.getNamedItem("name").getTextContent())) {
                           trailer.addNeighbor(boardSets.get(p));
                        }
                     }
                  }
               }
            }else if("area".equals(sub.getNodeName())){
                int x = Integer.parseInt(sub.getAttributes().getNamedItem("x").getTextContent());
                int y = Integer.parseInt(sub.getAttributes().getNamedItem("y").getTextContent())+50;
                int h = Integer.parseInt(sub.getAttributes().getNamedItem("h").getTextContent())-80;
                int w = Integer.parseInt(sub.getAttributes().getNamedItem("w").getTextContent())-15;
                coordinates.add(""+x);
                coordinates.add(""+y);
                coordinates.add(""+h);
                coordinates.add(""+w);
                coordinates.add("trailer");
                trailer.setCoords(coordinates);
            }
         }
      }

      boardSets.add(trailer);

      NodeList forOffice = root.getElementsByTagName("office");
      Set office = new Set();
      office.setName("office");
      office.setScene(null);
      office.setShotCapacity(-1);
      office.setActCapacity(8);
      office.noRoles();
      office.createCreditLegend();
      office.createMoneyLegend();
      office.setTileID(11);
      LinkedList<String> ofCoords = new LinkedList<String>();
      for (int i = 0; i < forOffice.getLength(); i++) {
         NodeList children = forOffice.item(i).getChildNodes();
         for (int j = 0; j < children.getLength(); j++) {
            Node sub = children.item(j);
            if ("neighbors".equals(sub.getNodeName())) {
               NodeList nlist = sub.getChildNodes();
               for (int k = 0; k < nlist.getLength(); k++) {
                  Node neighbor = nlist.item(k);
                  if (neighbor.getNodeName().equals("neighbor")) {
                     NamedNodeMap atts = neighbor.getAttributes();
                     for (int p = 0; p < boardSets.size(); p++) {
                        if (boardSets.get(p).getName().equals(atts.getNamedItem("name").getTextContent())) {
                           office.addNeighbor(boardSets.get(p));
                        }
                     }
                  }
               }
            } else if ("upgrades".equals(sub.getNodeName())) {
               NodeList upgradeChildren = sub.getChildNodes();
               int upcount = upgradeChildren.getLength();
               for (int k = 0; k < upcount; k++) {
                  Node upgraNode = upgradeChildren.item(k);
                  if (upgraNode.getNodeName().equals("upgrade")) {
                     NamedNodeMap atts = upgraNode.getAttributes();
                     String type = atts.getNamedItem("currency").getTextContent();
                     String amount = atts.getNamedItem("amt").getTextContent();
                     String level = atts.getNamedItem("level").getTextContent();
                     String[] tuple = { type, level, amount };
                     office.addToLegend(tuple);
                  }
               }
            }else if("area".equals(sub.getNodeName())){
                int x = Integer.parseInt(sub.getAttributes().getNamedItem("x").getTextContent());
                int y = Integer.parseInt(sub.getAttributes().getNamedItem("y").getTextContent())+50;
                int h = Integer.parseInt(sub.getAttributes().getNamedItem("h").getTextContent())-80;
                int w = Integer.parseInt(sub.getAttributes().getNamedItem("w").getTextContent()) -30;
                ofCoords.add(""+x);
                ofCoords.add(""+y);
                ofCoords.add(""+h);
                ofCoords.add(""+w);
                ofCoords.add("office");
                office.setCoords(ofCoords);
            }
         }
      }

      boardSets.add(office);

      // for getting neighbors
      for (int i = 0; i < sets.getLength(); i++) {
         Node node = sets.item(i);
         NodeList children = node.getChildNodes();
         NamedNodeMap setAttributes = node.getAttributes();
         String setname = setAttributes.getNamedItem("name").getNodeValue();
         Set parentSet = null;
         for (int j = 0; j < boardSets.size(); j++) {
            if (boardSets.get(j).getName().equals(setname)) {
               parentSet = boardSets.get(j);
            }
         }

         for (int j = 0; j < children.getLength(); j++) {
            Node sub = children.item(j);

            if ("neighbors".equals(sub.getNodeName())) {
               NodeList nlist = sub.getChildNodes();
               for (int k = 0; k < nlist.getLength(); k++) {

                  Node neighbor = nlist.item(k);
                  if (neighbor.getNodeName().equals("neighbor")) {
                     NamedNodeMap atts = neighbor.getAttributes();
                     for (int p = 0; p < boardSets.size(); p++) {
                        if (boardSets.get(p).getName().equals(atts.getNamedItem("name").getTextContent())) {
                           parentSet.addNeighbor(boardSets.get(p));
                        }
                     }
                  }
               }
            }
         }
      }

      // FOR PRINTING AND ERROR CHECKING
      for (int i = 0; i < boardSets.size(); i++) {
         Set set = boardSets.get(i);
         // System.out.println("Set name: " + set.getName() + "\nRole count: " +
         // set.getRoleCount() + "\nSet actor cap: " + set.getActCapacity());
         // System.out.println("Roles: ");
         for (int j = 0; j < set.getRoleCount(); j++) {
            Role curr = set.getRoles().get(j);
            // System.out.println(" Name: " + curr.getTitle() + " Desc: " +
            // curr.getDescription() + " Rank: " + curr.getRank());
         }
         // System.out.println("neighbors: ");
         for (int j = 0; j < set.getNeighbors().size(); j++) {
            // System.out.println(" "+set.getNeighbors().get(j).getName());
         }
      }
      // System.out.println("Total set count: " + boardSets.size());

      return boardSets;
   }

   public LinkedList<Scene> readCards(Document d) {

      Element root = d.getDocumentElement();
      NodeList cardNodes = root.getElementsByTagName("card");
      LinkedList<Scene> scenes = new LinkedList<Scene>();

      for (int i = 0; i < cardNodes.getLength(); i++) {

         Node currentCardNode = cardNodes.item(i);
         NamedNodeMap cardAttributes = currentCardNode.getAttributes();

         NodeList cardChildren = currentCardNode.getChildNodes();

         String title = cardAttributes.getNamedItem("name").getNodeValue();
         String description = "";

         String image = cardAttributes.getNamedItem("img").getNodeValue();

         int budget = Integer.parseInt(cardAttributes.getNamedItem("budget").getNodeValue());
         LinkedList<Role> parts = new LinkedList<Role>();
         int actorCapacity;


         for (int j = 0; j < cardChildren.getLength(); j++) {

            Node cardChild = cardChildren.item(j);

            if (cardChild.getNodeName().compareTo("scene") == 0) {
               description = cardChild.getTextContent();

            } else if (cardChild.getNodeName().compareTo("part") == 0) {
               Role newRole = new Role();
               NamedNodeMap roleAttributes = cardChild.getAttributes();
               NodeList rolechildren = cardChild.getChildNodes();

               newRole.setTitle(roleAttributes.getNamedItem("name").getNodeValue());

               newRole.setRank(Integer.parseInt(roleAttributes.getNamedItem("level").getNodeValue()));
               newRole.setDesc(rolechildren.item(3).getTextContent());


               for(int k=0;k<rolechildren.getLength();k++){
                 Node kid = rolechildren.item(k);
                 if("area".equals(kid.getNodeName())){
                   LinkedList<String> coordinates = new LinkedList<String>();
                   coordinates.add(kid.getAttributes().getNamedItem("x").getTextContent());
                   coordinates.add(kid.getAttributes().getNamedItem("y").getTextContent());
                   coordinates.add(kid.getAttributes().getNamedItem("h").getTextContent());
                   coordinates.add(kid.getAttributes().getNamedItem("w").getTextContent());
                   newRole.setCoords(coordinates);
                 }
               }

               parts.add(newRole);
            }
         }

         actorCapacity = parts.size();

         scenes.add(new Scene(title, description, budget, parts, actorCapacity, image));
      }

      return scenes;
   }

}
