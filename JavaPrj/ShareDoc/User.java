/**
 *  User.java
 *  Autore: Danilo Cianfrone, matricola 501292
 *  Il codice, in ogni sua parte, è opera originale dell'autore.
 */
public abstract class User {

    private String  nick;
    private int     pass;

    public User(String nick, int pass) 
        throws IllegalArgumentException {
        // Parametro attuale non valido.
        if (nick == null)
            throw new IllegalArgumentException("Parameter \'nick\' is null");

        this.nick = nick;
        this.pass = pass;
    }

    public String getNick() {
        return this.nick;
    }

    public boolean validCred(String nick, int pass) {
        return this.nick.equals(nick) && (this.pass == pass);
    }

    public boolean equals(User obj) {
        return this.nick.equals(obj.getNick());
    }

    public boolean isOp();
}

public class Operator extends User {
    public Operator(String nick, int pass) {
        super(nick, pass);
    }

    public boolean isOp() {
        return true;
    }
}

public class Client extends User {
    public Client(String nick, int pass) {
        super(nick, pass);
    }

    public boolean isOp() {
        return false;
    }
}