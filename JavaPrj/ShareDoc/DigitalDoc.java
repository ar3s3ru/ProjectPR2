/** 
  * DigitalDoc.java
  * Autore: Danilo Cianfrone, matricola 501292
  * Il codice, in ogni sua parte, è opera originale dell'autore.
  */
public class DigitalDoc {
    // OVERVIEW: classe che descrive un documento digitale, con nome "doc" e
    //           contenuto "text", dell'utente "user".
    // N.B. un oggetto di tale classe è immutabile.

    // =========================================================================== //

    private String doc;
    private String user;
    private String text;

    // REQUIRES: doc != null && user != null
    // EFFECTS:  Costruisce un oggetto DigitalDoc con nome doc, testo text
    //           e appartenente all'utente user.
    // THROWS:   IllegalArgumentException se le precondizioni vengono violate.
    // --------------------------------------------------------------------------- //
    public DigitalDoc(String doc, String user, String text) 
        throws IllegalArgumentException {
    // --------------------------------------------------------------------------- //
        if (doc == null || user == null)
            throw new IllegalArgumentException("Invalid arguments");

        this.doc  = doc;
        this.user = user;
        this.text = text;
    }

    // EFFECTS: Ritorna il nome dell'autore.
    // RETURN:  this.user
    // --------------------------------------------------------------------------- //
    public String getUser() {
    // --------------------------------------------------------------------------- //
        return this.user;
    }

    // EFFECTS: Ritorna il titolo del documento.
    // RETURN:  this.doc
    // --------------------------------------------------------------------------- //
    public String getDoc() {
    // --------------------------------------------------------------------------- //
        return this.doc;
    }

    // EFFECTS: Ritorna il testo del documento.
    // RETURN:  this.text
    // --------------------------------------------------------------------------- //
    public String getText() {
    // --------------------------------------------------------------------------- //
        return this.text;
    }

    // EFFECTS: Confronta due oggetti di tipo DigitalDoc e stabilisce se sono uguali
    //          o meno.
    // RETURN:  true se il titolo coincide, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean equals(DigitalDoc obj) {
    // --------------------------------------------------------------------------- //
        return this.doc.equals(obj.getDoc());
    }
}