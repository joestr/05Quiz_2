/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.gstd.quiz.pkgData.JSON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author schueler
 */
public class JSONQuiz {

    private String id = "";
    private String description = "";
    private List<JSONQuestion> questions = new ArrayList<>();

    public JSONQuiz() { }
    
    public JSONQuiz(String id, String description) {
        
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<JSONQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<JSONQuestion> questions) {
        this.questions = questions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JSONQuiz other = (JSONQuiz) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.questions, other.questions)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Quiz{" + "id=" + id + ", description=" + description + ", questions=" + questions + '}';
    }
}
