/**
  * SharedDoc.java
  * Autore: Danilo Cianfrone, matricola 501292
  * Il codice, in ogni sua parte, è opera originale dell'autore.
  */
public class SharedDoc {
    // OVERVIEW: classe che denota un documento digitale condiviso.
    // N.B. un oggetto di tale classe è immutabile.

    // =========================================================================== //

    private DigitalDoc shrDoc;
    private User       usrAuthor;
    private User       usrShare;

    // REQUIRES: author != null && withShare != null && doc != null
    // EFFECTS:  costruisce l'oggetto SharedDoc che denota una condivisione di doc
    //           tra author e withShare.
    // THROWS:   IllegalArgumentException, se le precondizioni sono violate.
    // --------------------------------------------------------------------------- //
    public SharedDoc(User author, User withShare, DigitalDoc doc) 
        throws IllegalArgumentException {
    // --------------------------------------------------------------------------- //
        // Parametri costruttore invalidi
        if (author == null || withShare == null || doc == null)
            throw new IllegalArgumentException("Invalid argument(s)");

        this.shrDoc    = doc;
        this.usrAuthor = author;
        this.usrShare  = withShare;
    }

    // EFFECTS: stabilisce se nick è il nickname dell'autore del documento.
    // RETURN:  true se nick è il nickname dell'autore, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean isAuthor(String nick) {
    // --------------------------------------------------------------------------- //
        return this.usrAuthor.getNick().equals(nick);
    }

    // EFFECTS: stabilisce se nick è il nickname dell'utente con il quale
    //          il documento viene condiviso.
    // RETURN:  true se nick è il nickname dell'utente, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean isUser(String nick) {
    // --------------------------------------------------------------------------- //
        return this.usrShare.getNick().equals(nick);
    }

    // RETURN: ritorna il riferimento all'autore.
    // --------------------------------------------------------------------------- //
    public User getAuthor() {
    // --------------------------------------------------------------------------- //
        return this.usrAuthor;
    }

    // RETURN: ritorna il riferimento all'utente che partecipa alla condivisione.
    // --------------------------------------------------------------------------- //
    public User getUser() {
    // --------------------------------------------------------------------------- //
        return this.usrShare;
    }

    // RETURN: ritorna il riferimento al documento digitale.
    // --------------------------------------------------------------------------- //
    public DigitalDoc getShrDoc() {
    // --------------------------------------------------------------------------- //
        return this.shrDoc;
    }
}
