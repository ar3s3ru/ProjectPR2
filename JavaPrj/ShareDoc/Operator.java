/**
  *  Operator.java
  *  Autore: Danilo Cianfrone, matricola 501292
  *  Il codice, in ogni sua parte, è opera originale dell'autore.
  */
public class Operator extends User {
    // OVERVIEW: sottoclasse di User, usata per denotare un utente Operatore.
    //           L'utente Operatore può aggiungere o rimuovere utenti Clienti.
    // --------------------------------------------------------------------------- //
    public Operator(String nick, int pass) {
    // --------------------------------------------------------------------------- //
        super(nick, pass);
    }

    // --------------------------------------------------------------------------- //
    public boolean isOp() {
    // --------------------------------------------------------------------------- //
        return true;
    }

    // --------------------------------------------------------------------------- //
    public boolean isDeletable() {
    // --------------------------------------------------------------------------- //
        return false;
    }
}