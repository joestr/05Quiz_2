/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.gstd.quiz.pkgData;

/**
 *
 * @author schueler
 */
public class TripleStringedImplementation extends TripleStringed {
    
    String s1;
    String s2;
    String s3;
    
    public TripleStringedImplementation(String s1, String s2, String s3) {
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
    }
    
    public String getString1() { return this.s1; }
    public String getString2() { return this.s2; }
    public String getString3() { return this.s3; }
}
