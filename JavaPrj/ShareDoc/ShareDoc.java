/**
  * ShareDoc.java
  * Autore: Danilo Cianfrone, matricola 501292
  * Il codice, in ogni sua parte, è opera originale dell'autore.
  */
public interface ShareDoc {
    // OVERVIEW: modulo Java che simula la gestione di un sistema di condivisione di
    //           documenti digitali.
    //           Ogni utente può inserire, rimuovere e leggere i propri documenti
    //           digitali; può inoltre condividerli con altri utenti.

    // =========================================================================== //

    // REQUIRES: name != null
    // MODIFIES: this.users
    // EFFECTS:  aggiunge (name, password) alla lista degli utenti, se non è già
    //           presente un utente con lo stesso nome.
    // RETURN:   true se l'utente viene aggiunto, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean addUser(String name, int password);
    // --------------------------------------------------------------------------- //

    // REQUIRES: name != null
    // MODIFIES: this.users && this.shares && this.docs
    // EFFECTS:  Elimina l’utente (se presente) e tutti i suoi documenti digitali.
    // --------------------------------------------------------------------------- //
    public void removeUser(String name);
    // --------------------------------------------------------------------------- //

    // REQUIRES: user != null && doc != null
    // MODIFIES: this.docs
    // EFFECTS:  Aggiunge al sistema il documento digitale identificato dal nome.
    // RETURN:   true se il documento viene aggiunto, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean addDoc(String user, String doc, int password);
    // --------------------------------------------------------------------------- //

    // REQUIRES: user != null && doc != null
    // MODIFIES: this.docs && this.shares
    // EFFECTS:  Rimuove dal sistema il documento digitale identificato dal nome. 
    // RETURN:   Restituisce true se l’operazione ha successo,
    //           false se fallisce perchè non esiste un documento con quel nome.
    // --------------------------------------------------------------------------- //
    public boolean removeDoc(String user, String doc, int password);
    // --------------------------------------------------------------------------- //

    // REQUIRES: user != null && doc != null
    // EFFECTS:  Legge il documento digitale identificato dal nome.
    // THROWS:   WrongIDException se user non é il nome di un utente registrato,
    //           o se non esiste un documento con quel nome,
    //           o se la password non é corretta.
    // --------------------------------------------------------------------------- //
    public void readDoc(String user, String doc, int password)
        throws WrongIDException;
    // --------------------------------------------------------------------------- //

    // REQUIRES: fromName != null && toName != null && doc != null
    // MODIFIES: this.docs && this.shares
    // EFFECTS:  Notifica una condivisione di documento.
    // THROWS:   WrongIDException se fromName o toName non sono nomi 
    //           di utenti registrati, o se non esiste un documento con quel nome,
    //           o se la password non é corretta.
    // --------------------------------------------------------------------------- //
    public void shareDoc(String fromName, String toName, String doc, int password) 
        throws WrongIDException;
    // --------------------------------------------------------------------------- //

    // REQUIRES: user != null
    // MODIFIES: this.shares && this.docs
    // EFFECTS:  Restituisce il nome del documento condiviso cancellandolo dalla coda 
    //           delle notifiche di condivisione.
    // THROWS:   EmptyQueueException se non ci sono notifiche;
    //           WrongIDException se user non é il nome di un utente registrato,
    //           o se la password non é corretta.
    // --------------------------------------------------------------------------- //
    public String getNext(String user, int password) 
        throws EmptyQueueException, WrongIDException;
    // --------------------------------------------------------------------------- //
}