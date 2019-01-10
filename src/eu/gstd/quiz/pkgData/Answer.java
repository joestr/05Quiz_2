/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.gstd.quiz.pkgData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import xyz.joestr.databasex.DatabaseWrapable;

/**
 *
 * @author schueler
 */
public class Answer extends TripleStringed implements DatabaseWrapable {

    private String quizId;
    private Integer questionId;
    private Integer id;
    private String text;

    public Answer() { }

    public Answer(String quizId, Integer questionId, Integer id, String text) {
        this.quizId = quizId;
        this.questionId = questionId;
        this.id = id;
        this.text = text;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.quizId);
        hash = 23 * hash + Objects.hashCode(this.questionId);
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.text);
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
        final Answer other = (Answer) obj;
        if (!Objects.equals(this.quizId, other.quizId)) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (!Objects.equals(this.questionId, other.questionId)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Answer{" + "quizId=" + quizId + ", questionId=" + questionId + ", id=" + id + ", text=" + text + '}';
    }

    @Override
    public String databaseTableName() {
        return "antwort";
    }

    @Override
    public Collection databaseColumnNames() {
        Collection<String> result = new ArrayList<>();
        result.add("tid");
        result.add("fnr");
        result.add("anr");
        result.add("atext");
        return result;
    }

    @Override
    public Collection classFieldNames() {
        Collection<String> result = new ArrayList<>();
        result.add("quizId");
        result.add("questionId");
        result.add("id");
        result.add("text");
        return result;
    }

    @Override
    public Collection classFieldClasses() {
        Collection<Class> result = new ArrayList<>();
        result.add(String.class);
        result.add(Integer.class);
        result.add(Integer.class);
        result.add(String.class);
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
        return "-";
    }
    
}
