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
public class Quiz extends TripleStringed implements DatabaseWrapable {

    private String id = "";
    private String description = "";

    public Quiz() { }
    
    public Quiz(String id, String description) {
        
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

    @Override
    public String toString() {
        return "Quiz{" + "id=" + id + ", description=" + description + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.description);
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
        final Quiz other = (Quiz) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public String databaseTableName() {
        return "test";
    }

    @Override
    public Collection databaseColumnNames() {
        Collection<String> result = new ArrayList<>();
        result.add("tid");
        result.add("tname");
        return result;
    }

    @Override
    public Collection classFieldNames() {
        Collection<String> result = new ArrayList<>();
        result.add("id");
        result.add("description");
        return result;
    }

    @Override
    public Collection classFieldClasses() {
        Collection<Class> result = new ArrayList<>();
        result.add(String.class);
        result.add(String.class);
        return result;
    }

    @Override
    public String getString1() {
        return this.id;
    }

    @Override
    public String getString2() {
        return this.description;
    }

    @Override
    public String getString3() {
        return "-";
    }
    
}
