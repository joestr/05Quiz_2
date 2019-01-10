/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.gstd.quiz.pkgMisc;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author schueler
 */
public class ImageProvider {
    
    ImageView Q;
    ImageView F;
    ImageView papirus_icon_theme_emblem_question;
    ImageView papirus_icon_theme_emblem_generic;
    ImageView papirus_icon_theme_emblem_default;
    
    public ImageProvider() {
        
        this.Q = new ImageView(new Image(getClass().getResourceAsStream("resources/Q.png")));
        this.F = new ImageView(new Image(getClass().getResourceAsStream("resources/F.png")));
        this.papirus_icon_theme_emblem_default = new ImageView(new Image(getClass().getResourceAsStream("resources/emblem-default.png")));
        this.papirus_icon_theme_emblem_generic = new ImageView(new Image(getClass().getResourceAsStream("resources/emblem-generic.png")));
        this.papirus_icon_theme_emblem_question = new ImageView(new Image(getClass().getResourceAsStream("resources/emblem-question.png")));
    }
    
    @Deprecated
    public ImageView getQ() {
        
        return this.Q;
    }
    
    @Deprecated
    public ImageView getF() {
        
        return this.F;
    }

    public ImageView getPapirus_icon_theme_emblem_question() {
        
        return new ImageView(new Image(getClass().getResourceAsStream("resources/emblem-question.png")));
    }

    public ImageView getPapirus_icon_theme_emblem_generic() {
        
        return new ImageView(new Image(getClass().getResourceAsStream("resources/emblem-generic.png")));
    }

    public ImageView getPapirus_icon_theme_emblem_default() {
        
        return new ImageView(new Image(getClass().getResourceAsStream("resources/emblem-default.png")));
    }
    
    public ImageView getPapirus_icon_theme_emblem_unreadable() {
        
        return new ImageView(new Image(getClass().getResourceAsStream("resources/emblem-unreadable.png")));
    }
}
