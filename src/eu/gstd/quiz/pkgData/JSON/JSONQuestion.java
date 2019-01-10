/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.gstd.quiz.pkgData.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author schueler
 */
public class JSONQuestion {

    private Integer id;
    private String text;
    private List<JSONAnswer> answers = new ArrayList<>();

    public JSONQuestion() { }

    public JSONQuestion(Integer id, String text) {
        this.id = id;
        this.text = text;
    }
    

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<JSONAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<JSONAnswer> answers) {
        this.answers = answers;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.text);
        hash = 59 * hash + Objects.hashCode(this.answers);
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
        final JSONQuestion other = (JSONQuestion) obj;
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.answers, other.answers)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Question{" + "id=" + id + ", text=" + text + ", answers=" + answers + '}';
    }
}
