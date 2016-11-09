/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team08pt;

import com.team08pt.model.Note;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.websocket.Session;

/**
 *
 * @author kyawminthu
 */
@ApplicationScoped
public class NotesDisplaySession {

    private static Map<String, List<Session>> sessionMap = new HashMap<>();
    private static List<Note> notes;

    public List<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void add(String category, Session session) {
        List<Session> allSessions = sessionMap.computeIfAbsent(category, s -> new ArrayList<Session>());
        allSessions.add(session);
    }

    public void remove(String category, Session session) {
        sessionMap.get(category).remove(session);
    }

    public void sendPostedNotes(String category, Session session) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        if (notes != null) {
            if (category.equalsIgnoreCase("all")) {
                for (int i = notes.size(); i > 0; i--) {
                    jsonArrayBuilder.add(notes.get(i - 1).toJSON());
                }
            } else {
                for (int i = notes.size(); i > 0; i--) {
                    Note note = notes.get(i - 1);
                    if (note.getCategory().equalsIgnoreCase(category)) {
                        jsonArrayBuilder.add(note.toJSON());
                    }
                }
            }

            try {
                //load all posted message;            
                session.getBasicRemote().sendText(jsonArrayBuilder.build().toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendMessageToGroup(Note note) {
        String msg = note.toJSON().toString();

        notes.add(note);
        if (sessionMap.containsKey(note.getCategory().toLowerCase())) {
            sendNotes(msg, note.getCategory().toLowerCase());
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
