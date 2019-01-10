/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.gstd.quiz.pkgData.JSON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 *
 * @author schueler
 */
public class JSONAnswer {

    private int id;
    private String text;
    private boolean correct;

    public JSONAnswer() { }

    public JSONAnswer(int id, String text, boolean correct) {
        this.id = id;
        this.text = text;
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.id;
        hash = 41 * hash + Objects.hashCode(this.text);
        hash = 41 * hash + (this.correct ? 1 : 0);
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
        final JSONAnswer other = (JSONAnswer) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.correct != other.correct) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Answer{" + "id=" + id + ", text=" + text + ", correct=" + correct + '}';
    }
}
