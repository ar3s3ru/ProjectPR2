/**
 *  DigitalDoc.java
 *  Autore: Danilo Cianfrone, matricola 501292
 *  Il codice, in ogni sua parte, è opera originale dell'autore.
 */
public class DigitalDoc {

    private String doc;
    private String user;
    private String text;

    public DigitalDoc(String doc, String user, String text) 
        throws IllegalArgumentException {
        
        if (doc == null || user == null)
            throw new IllegalArgumentException("Invalid arguments");

        this.doc  = doc;
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return this.user;
    }

    public String getDoc() {
        return this.doc;
    }

    public String getText() {
        return this.text;
    }

    public boolean equals(DigitalDoc obj) {
        return this.doc.equals(obj.getDoc()) && this.user.equals(obj.getUser());
    }
}