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

    private String  nick;
    private int     pass;

    // --------------------------------------------------------------------------- //
    public User(String nick, int pass) 
        throws IllegalArgumentException {
    // --------------------------------------------------------------------------- //
        // Parametro attuale non valido.
        if (nick == null)
            throw new IllegalArgumentException("Parameter \'nick\' is null");

        this.nick = nick;
        this.pass = pass;
    }

    // EFFECTS: ritorna il nome dell'utente.
    // RETURN:  this.nick
    // --------------------------------------------------------------------------- //
    public String getNick() {
    // --------------------------------------------------------------------------- //
        return this.nick;
    }

    // RETURN: true se nick e pass coincidono con this.nick e this.pass, false
    //         altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean validCred(String nick, int pass) {
    // --------------------------------------------------------------------------- //
        return this.nick.equals(nick) && (this.pass == pass);
    }

    // EFFECTS: confronta l'oggetto con un oggetto User obj, e stabilisce se sono
    //          lo stesso oggetto facendo un confronto tra il nome dei due User.
    // RETURN:  true se i due oggetti hanno lo stesso nick, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean equals(User obj) {
    // --------------------------------------------------------------------------- //
        return this.nick.equals(obj.getNick());
    }

    public boolean isOp();
}

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
}

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
}