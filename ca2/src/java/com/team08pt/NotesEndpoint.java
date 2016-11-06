package com.team08pt;

import com.team08pt.business.NotesBean;
import com.team08pt.model.Notes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.core.Response;

@RequestScoped
@ServerEndpoint("/notes/{category}")
public class NotesEndpoint {

    private String category;
    private Session session;
    private static Map<String, List<String>> sessionMap = new HashMap<>();
    private static List<Notes> notes;
    
    @EJB private NotesBean noteBean;
    
    public NotesEndpoint() {
        notes = noteBean.findAll();
    }

    @OnOpen
    public void open(@PathParam("category") String category, Session sess) {
        this.category = category;
        this.session = sess;
        if (!sessionMap.containsKey(category)) {
            sessionMap.put(category, new ArrayList<>());
        }
        sessionMap.get(category).add(sess.getId());
        System.out.println(">>> added session id: " + session.getId() + ", category: " + category);
        
        try {
            //load all posted message;
            sess.getBasicRemote().sendText(loadNotes(category));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @OnClose
    public void close() {
        //TODO remove session id from map.
        sessionMap.get(category).remove(session.getId());
        System.out.println(">>> remove session id: " + session.getId() + ", category: " + category);
    }
    
    private String loadNotes(String category) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        if (notes != null) {
            notes.forEach((a) -> {
                jsonArrayBuilder.add(a.toJSON());
            });
        } else {
            notes = noteBean.findAll();
        }
        return jsonArrayBuilder.build().toString();
    }

    @OnMessage
    public void message(String time, String title, String category, String content) {
        String msg = Json.createObjectBuilder()
                .add("time", time)
                .add("title", title)
                .add("category", category)
                .add("content", content)
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
}
