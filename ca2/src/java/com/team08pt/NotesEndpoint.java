package com.team08pt;

import com.team08pt.business.NotesBean;
import com.team08pt.model.Notes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@RequestScoped
@ServerEndpoint("/notes/{category}")
public class NotesEndpoint {

    private String category;
    private Session session;
    private static Map<String, List<Session>> sessionMap = new HashMap<>();
    private static List<Notes> notes;
    
    @EJB private NotesBean noteBean;

    @OnOpen
    public void open(@PathParam("category") String category, Session sess) {
        this.category = category;
        this.session = sess;
        if (!sessionMap.containsKey(category)) {
            sessionMap.put(category, new ArrayList<>());
        }
        sessionMap.get(category).add(sess);
        System.out.println(">>> added session id: " + session.getId() + ", category: " + category);
        
        try {
            //load all posted message;            
            sess.getBasicRemote().sendText(loadNotes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @OnClose
    public void close() {
        //TODO remove session id from map.
        sessionMap.get(category).remove(session);
        System.out.println(">>> remove session id: " + session.getId() + ", category: " + category);
    }
    
    private String loadNotes() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        if (notes == null) {
            notes = noteBean.findAll();
        }
        if (notes != null) {
            if (category.equalsIgnoreCase("all")) {
                for (int i=notes.size(); i>0; i--) {
                    jsonArrayBuilder.add(notes.get(i - 1).toJSON());
                }
            } else {
                for (int i=notes.size(); i>0; i--) {
                    Notes note = notes.get(i - 1);
                    if (note.getCategory().equalsIgnoreCase(category))
                        jsonArrayBuilder.add(note.toJSON());
                }
            }
        }
        return jsonArrayBuilder.build().toString();
    }

    @OnMessage
    public void message(String text) {
        String msg = Json.createObjectBuilder()
                .add("text", text)
                .build()
                .toString();

        for (Session s : session.getOpenSessions()) {
            try {
                s.getBasicRemote().sendText(msg);
            } catch (IOException ex) {
                try {
                    s.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    public void sendMessageToGroup(Notes note) {
        String msg = note.toJSON().toString();
        
        notes.add(note);
        if (sessionMap.containsKey(note.getCategory())) {
            sendNotes(msg, note.getCategory());
        }
        if (sessionMap.containsKey("all")) {
            sendNotes(msg, "all");
        }
    }
    
    private void sendNotes(String msg, String category) {
        for (Session s : sessionMap.get(category)) {
            try {
                s.getBasicRemote().sendText(msg);
            } catch (IOException ex) {
                try {
                    s.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
