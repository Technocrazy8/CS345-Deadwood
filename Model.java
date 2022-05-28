/**
 * Based on the observer design pattern, this class outlines the subject's functions for our models
 * Since our design is specific (1 GUI object observing models, 1 controller observing 1 GUI object),
 * and since the project won't be developped upon in the future, this implementation will specifically
 * work for our deadwood project with no concern for adaptability as a pure observer/subject template.
 * 
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Model {

    public class ListenerPackage {
        public int subjectID;
        public GUI listener;
        public Getter getter;
        public ListenerPackage(int subjectID, GUI listener, Getter getter) {
            this.subjectID = subjectID;
            this.listener = listener;
            this.getter = getter;
        }
    }

    List<ListenerPackage> listeners = new ArrayList<>();
    
    // Functional interfaces to pass lambda functions as parameters
    public interface Getter<T> {
        T fetch();
    }

    // Adds listeners to model
    public void subscribe(GUI listener, int subjectID, Getter getter) {

        ListenerPackage listenerP = new ListenerPackage(subjectID, listener, getter);

        if (isListening(listenerP)) {
            return;
        }
        System.out.println("Subscribed!");
        listeners.add(listenerP);
    }

    public void unsubscribe(GUI listener, Getter getter) {
        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i).listener == listener && listeners.get(i).getter == getter) {
                listeners.remove(i);
            }
        }
    }

    // Checks if listerner already exists
    public boolean isListening(ListenerPackage listenerP) {

        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i).listener == listenerP.listener && listeners.get(i).getter == listenerP.getter) {
                return true;
            }
        }
        return false;
    }

    // Notifies all listeners listening for a particular variable
    public void notify(Getter getter) {
        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i).getter == getter) {
                listeners.get(i).listener.update(listeners.get(i).subjectID, getter);
            }
        }
    }
}
