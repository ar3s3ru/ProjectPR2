/**
  *  User.java
  *  Autore: Danilo Cianfrone, matricola 501292
  *  Il codice, in ogni sua parte, è opera originale dell'autore.
  */
public abstract class User {
    // OVERVIEW: classe che descrive un utente del sistema di condivisione
    //           di documenti digitali.
    // N.B. un oggetto di tale classe è immutabile.

    // =========================================================================== //

    private String nick;
    private int    pass;

    public LinkedList<SharedDoc> sharedWith;
    public LinkedList<SharedDoc> sharedTo;
    public Vector<DigitalDoc>    docs;

    // --------------------------------------------------------------------------- //
    public User(String nick, int pass) 
        throws IllegalArgumentException {
    // --------------------------------------------------------------------------- //
        // Parametro attuale non valido.
        if (nick == null)
            throw new IllegalArgumentException("Parameter \'nick\' is null");

        this.nick   = nick;
        this.pass   = pass;
        
        this.sharedWith = new LinkedList<>();
        this.sharedTo   = new LinkedList<>();
        this.docs       = new Vector<>();
    }

    // EFFECTS: ritorna il nome dell'utente.
    // RETURN:  this.nick
    // --------------------------------------------------------------------------- //
    public String getNick() {
    // --------------------------------------------------------------------------- //
        return this.nick;
    }

    // RETURN: true se pass è la password dell'utente, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean validPass(int pass) {
    // --------------------------------------------------------------------------- //
        return (this.pass == pass);
    }

    // EFFECTS: confronta l'oggetto con un oggetto User obj, e stabilisce se sono
    //          lo stesso oggetto facendo un confronto tra il nome dei due User.
    // RETURN:  true se i due oggetti hanno lo stesso nick, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean equals(User obj) {
    // --------------------------------------------------------------------------- //
        return this.nick.equals(obj.getNick());
    }

    public abstract boolean isOp();
    public abstract boolean isDeletable();
}
