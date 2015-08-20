/**
  *  ShareDocImpl.java
  *  Autore: Danilo Cianfrone, matricola 501292
  *  Il codice, in ogni sua parte, è opera originale dell'autore.
  */
public class ShareDocImpl implements ShareDoc {

    private boolean isLogged;
    private User    usrLogged;

    private List<User>   users;
    private List<String> name_docs;

    // --------------------------------------------------------------------------- //
    public ShareDocImpl(String op_nick, int op_pass) 
        throws IllegalArgumentException {
    // --------------------------------------------------------------------------- //
        // Se il nick del costruttore è null, solleva
        // IllegalArgumentException.
        if (op_nick == null)
            throw new IllegalArgumentException("Nick operator is null");

        this.isLogged  = false;
        this.usrLogged = null;

        this.users     = new LinkedList<>();
        this.name_docs = new LinkedList<>();

        this.users.add(new Operator(op_nick, op_pass));
    }

    public boolean logIn(String nick, int pass) {
        // Precondizioni verificate.
        if (nick != null && !this.isLogged) {
            // Itera sulla collezione di utenti.
            for (User current : this.users) {
                // Utente trovato.
                if (current.getNick().equals(nick)) {
                    // Credenziali valide, accedi.
                    if (current.validPass(pass)) {
                        this.usrLogged = current;
                        this.isLogged  = true;
                    }
                    // Esci dal ciclo.
                    break;
                }
            }
        }
        // Ritorna lo stato di login della piattaforma.
        return this.isLogged;
    }

    public boolean logOut() {
        // Se vi è un login attivo...
        if (this.isLogged) {
            // ...esci dal login.
            this.usrLogged = null;
            this.isLogged  = false;
        }
        // Ritorna lo stato di logout (negazione di login).
        return !(this.isLogged);
    }

    // --------------------------------------------------------------------------- //
    public boolean addUser(String name, int password) {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        // Se name è uguale a null, o non vi è un login attivo,
        // oppure il login attivo non è di un operatore, ritorna false.
        if (name == null || !this.isLogged || !this.usrLogged.isOp())
            return false;

        // Aggiunge un nuovo utente alla lista, ritorna
        // true in caso di successo, false altrimenti.
        Client toAdd = new Client(name, password);
        // Controlla che l'utente non sia già presente nella collezione.
        if (!this.users.contains(toAdd))
            return this.users.add(toAdd);
        else return false;
    }

    // --------------------------------------------------------------------------- //
    public void removeUser(String name) {
    // --------------------------------------------------------------------------- //
        // Esegue la procedura solo se name è un oggetto valido,
        // e vi sia loggato un operatore.
        if (name != null && this.isLogged && this.usrLogged.isOp()) {
            // Riferimento all'utente da eliminare.
            User toDelete   = null;
            boolean gotUser = false;
            // Cerca l'utente nell'insieme degli utenti.
            for (User current : this.users) {
                // Utente trovato.
                if (current.getNick().equals(name)) {
                    // Copia il riferimento ed esci dal ciclo.
                    toDelete = current;
                    gotUser  = true;
                    break;
                }
            }

            // L'utente è stato trovato, esegue clean-up.
            if (gotUser) {
                // Itera nell'insieme dei documenti condivisi del quale l'autore
                // è utente beneficiario, ed elimina i riferimenti nella lista
                // dell'utente autore.
                for (SharedDoc current : this.toDelete.sharedWith) {
                    current.getAuthor().sharedTo.remove(current);
                }
                // Elimina i riferimenti dei documenti condivisi dall'utente
                // dagli insiemi degli utenti con i quali 
                // i documenti son stati condivisi.
                for (ShareDoc current : this.toDelete.sharedTo) {
                    current.getUser().sharedWith.remove(current);
                }
                // Elimina il riferimento all'utente nell'insieme degli utenti.
                this.users.remove(toDelete);
            }
        }
    }

    // --------------------------------------------------------------------------- //
    public boolean addDoc(String user, String doc, int password) {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        // Se gli argomenti non sono validi, non avvengono modifiche
        // alla lista dei documenti.
        // Inoltre, c'è bisogno di un login da parte di un utente Client,
        // non Operator.
        if (user == null   || doc == null ||
            !this.isLogged || this.isOp())
            return false;

        // Credenziali invalide, ritorna false.
        if (!this.usrLogged.getNick(user) || !this.usrLogged.validPass(password))
            return false;

        // DA IMPLEMENTARE.
    }

    // --------------------------------------------------------------------------- //
    public boolean removeDoc(String user, String doc, int password) {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        // Parametri attuali non validi, ritorna false.
        if (user == null   || doc == null ||
            !this.isLogged || this.usrLogged.isOp())
            return false;

        // DA IMPLEMENTARE.
    }

    // --------------------------------------------------------------------------- //
    public void readDoc(String user, String doc, int password)
        throws WrongIDException {
        // TODO: invariante di rappresentazione
    // --------------------------------------------------------------------------- //
        // Esegue la procedura solo nel caso di parametri validi.
        if (user != null && doc != null && 
            this.isLogged && !this.usrLogged.isOp()) {
            // DA IMPLEMENTARE.
        }
    }

    // --------------------------------------------------------------------------- //
    public void shareDoc(String fromName, String toName, String doc, int password) 
        throws WrongIDException {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        // Parametri attuali errati
        if (fromName != null && toName != null && doc != null &&
            this.isLogged && !this.usrLogged.isOp()) {
            // DA IMPLEMENTARE.
        }
    }

    // --------------------------------------------------------------------------- //
    public String getNext(String user, int password) 
        throws EmptyQueueException, WrongIDException {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        if (user == null)
            throw new WrongIDException("User is null");

        if (!this.isLogged)
            throw new WrongIDException("No login session in current");

        if (this.usrLogged.isOp())
            throw new WrongIDException("Operator is logged in");

        if (!this.usrLogged.getNick().equals(user) || 
            !this.usrLogged.validPas(password))
            throw new WrongIDException("User credentials are not valid");

        // DA IMPLEMENTARE.
    }
}