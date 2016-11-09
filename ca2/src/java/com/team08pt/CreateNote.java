package com.team08pt;

import com.team08pt.business.NoteBean;
import com.team08pt.model.Note;
import com.team08pt.model.Users;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author zawminthant
 */
@RequestScoped
@Named
public class CreateNote implements Serializable {

    private String title;
    private List<String> categories;
    private String category;
    private String content;

    @EJB
    private NoteBean noteBean;
    
    @Inject private NotesDisplaySession notesDisplays;

    public CreateNote() {
        this.categories = new ArrayList<>();
        this.categories.add("Social");
        this.categories.add("For Sale");
        this.categories.add("Jobs");
        this.categories.add("Tuition");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    @Inject private UserSession userSession;
    public void createNote() {
        Users user = userSession.getUserModel();
        Note note = new Note(title, category, content, new Date(), user);
        noteBean.createNote(note);
        
        this.title = "";
        this.content = "";
        notesDisplays.loadNotes();
        notesDisplays.sendMessageToGroup(note);
    }
}
