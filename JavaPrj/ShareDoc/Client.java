/**
  *  Client.java
  *  Autore: Danilo Cianfrone, matricola 501292
  *  Il codice, in ogni sua parte, è opera originale dell'autore.
  */
public class Client extends User {
    // OVERVIEW: sottoclasse di User, usata per denotare un utente Cliente.
    //           L'utente Cliente può inserire, rimuovere e leggere i propri
    //           documenti, e condividerli con altri utenti.
    // --------------------------------------------------------------------------- //
    public Client(String nick, int pass) {
    // --------------------------------------------------------------------------- //
        super(nick, pass);
    }

    // --------------------------------------------------------------------------- //
    public boolean isOp() {
    // --------------------------------------------------------------------------- //
        return false;
    }

    // --------------------------------------------------------------------------- //
    public boolean isDeletable() {
    // --------------------------------------------------------------------------- //
        return true;
    }
}
