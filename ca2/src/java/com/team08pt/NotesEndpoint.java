package com.team08pt;

import com.team08pt.business.NoteBean;
import com.team08pt.model.Note;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
//    private static Map<String, List<Session>> sessionMap = new HashMap<>();
//    private static List<Note> notes;
    @Inject
    private NotesDisplaySession noteDisplaySession;

    @EJB
    private NoteBean noteBean;

    @OnOpen
    public void open(@PathParam("category") String category, Session sess) {
        this.category = category;
        this.session = sess;
        sendPostedNotes();
    }

    @OnClose
    public void close() {
        noteDisplaySession.remove(category, session);
        System.out.println(">>> remove session id: " + session.getId() + ", category: " + category);
    }

    private void sendPostedNotes() {
        if (noteDisplaySession.getNotes() == null) {
            noteDisplaySession.setNotes(noteBean.findAll());
        }
        noteDisplaySession.add(category, session);
        System.out.println(">>> added session id: " + session.getId() + ", category: " + category);
        noteDisplaySession.sendPostedNotes(category, session);
    }

    @OnMessage
    public void message(String text) {
//        String msg = Json.createObjectBuilder()
//                .add("text", text)
//                .build()
//                .toString();
//
//        for (Session s : session.getOpenSessions()) {
//            try {
//                s.getBasicRemote().sendText(msg);
//            } catch (IOException ex) {
//                try {
//                    s.close();
//                } catch (IOException e) {
//                }
//            }
//        }
    }
}
