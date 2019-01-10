/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.gstd.quiz.pkgData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import xyz.joestr.databasex.DatabaseWrapable;

/**
 *
 * @author schueler
 */
public class Question extends TripleStringed implements DatabaseWrapable {

    private String quizId;
    private Integer id;
    private String text;
    private Integer correctAnswer;

    public Question() { }

    public Question(String quizId, Integer id, String text, Integer correctAnswer) {
        this.quizId = quizId;
        this.id = id;
        this.text = text;
        this.correctAnswer = correctAnswer;
    }
    
    

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Question{" + "quizId=" + quizId + ", id=" + id + ", text=" + text + ", correctAnswer=" + correctAnswer + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.quizId);
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.text);
        hash = 83 * hash + Objects.hashCode(this.correctAnswer);
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
        final Question other = (Question) obj;
        if (!Objects.equals(this.quizId, other.quizId)) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.correctAnswer, other.correctAnswer)) {
            return false;
        }
        return true;
    }

    @Override
    public String databaseTableName() {
        return "frage";
    }

    @Override
    public Collection databaseColumnNames() {
        Collection<String> result = new ArrayList<>();
        result.add("tid");
        result.add("fnr");
        result.add("ftext");
        result.add("anrok");
        return result;
    }

    @Override
    public Collection classFieldNames() {
        Collection<String> result = new ArrayList<>();
        result.add("quizId");
        result.add("id");
        result.add("text");
        result.add("correctAnswer");
        return result;
    }

    @Override
    public Collection classFieldClasses() {
        Collection<Class> result = new ArrayList<>();
        result.add(String.class);
        result.add(Integer.class);
        result.add(String.class);
        result.add(Integer.class);
        return result;
    }

    @Override
    public String getString1() {
        return this.id.toString();
    }

    @Override
    public String getString2() {
        return this.text;
    }

    @Override
    public String getString3() {
        return this.correctAnswer.toString();
    }
    
}
