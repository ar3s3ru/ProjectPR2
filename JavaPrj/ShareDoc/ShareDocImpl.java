/**
  *  ShareDocImpl.java
  *  Autore: Danilo Cianfrone, matricola 501292
  *  Il codice, in ogni sua parte, è opera originale dell'autore.
  */
public class ShareDocImpl implements ShareDoc {
    private Vector<DigitalDoc> docs;
    private Queue<SharedDoc>   shares;
    private Set<User>          users;

    // --------------------------------------------------------------------------- //
    public ShareDocImpl(String op_nick, int op_pass) 
        throws IllegalArgumentException {
    // --------------------------------------------------------------------------- //
        // Se il nick del costruttore è null, solleva
        // IllegalArgumentException.
        if (op_nick == null)
            throw new IllegalArgumentException("Nick operator is null");

        this.docs   = new Vector<>();
        this.users  = new HashSet<>();
        this.shares = new LinkedList<>();

        // Aggiunge un operatore all'insieme degli utenti.
        this.users.add(new Operator(op_nick, op_pass));
    }

    // --------------------------------------------------------------------------- //
    public boolean addUser(String name, int password) {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        // Se name è uguale a null, non viene aggiunto nulla
        // all'insieme degli utenti, ritorna false.
        if (name == null)
            return false;

        // Aggiunge un nuovo utente alla lista, ritorna
        // true in caso di successo, false altrimenti.
        // La proprietà richiesta dalla specifica è preservata
        // dalla classe astratta Set di this.user
        return this.users.add(new Client(name, password));
    }

    // --------------------------------------------------------------------------- //
    public void removeUser(String name) {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        // Esegue la procedura solo se name è un oggetto valido.
        if (name != null) {
            // Iteratore sulla lista dei documenti.
            Iterator<DigitalDoc> itDocs = this.docs.listIterator();

            // Rimuove l'utente indicato da 'name' dall'insieme
            // degli utenti registrati.
            // (Viene creato un nuovo oggetto User con nick 'name'
            //  e password temporanea 0).
            boolean removed = this.users.remove(new Client(name, 0));

            // Entra nel loop solo se effettivamente l'utente
            // è stato rimosso dall'insieme (indicato da removed).
            while (removed && itDocs.hasNext()) {
                // Riferimento all'elemento corrente.
                DigitalDoc current = itDocs.next();
                // Se ci sono documenti digitali dell'utente rimosso,
                // eliminali tutti.
                if (current.getUser().equals(name))
                    itDocs.remove();
            }
        }
    }

    // --------------------------------------------------------------------------- //
    public boolean addDoc(String user, String doc, int password) {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        // Se gli argomenti non sono validi, non avvengono modifiche
        // alla lista dei documenti.
        if (user == null || doc == null)
            return false;

        Iterator<User> itUsers = this.users.listIterator();
        boolean        found   = false;

        
        while (!found && itUsers.hasNext()) {
            // Cerca l'utente.
            User current = itUser.next();

            if (current.getNick().equals(user)) {
                // L'utente è stato trovato.
                if (!current.validCred(user, password))
                    // Le credenziali non sono valide.
                    return false;
                else
                    // Trovato e credenziali valide.
                    found = true;
            }
        }

        // Se l'utente viene confermato essere esistente e corretto,
        // controlla che il titolo del documento non sia già presente.
        if (found) {
            for (DigitalDoc current : this.docs) {
                // Documento trovato, ritorna false.
                if (doc.equals(current.getDoc()))
                    return false;
            }

            // TODO: Prendi il testo.
            String docText = null;
            // Aggiungi elemento.
            return this.docs.add(new DigitalDoc(user, doc, docText));
        }
        // Utente inserito non valido, non viene esteso l'insieme.
        else 
            return false;
    }

    // --------------------------------------------------------------------------- //
    public boolean removeDoc(String user, String doc, int password) {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        // Parametri attuali non validi, ritorna false.
        if (user == null || doc == null)
            return false;

        Iterator<User> itUsers = this.users.listIterator();
        boolean        found   = false;

        while (!found && itUsers.hasNext()) {
            // Cerca l'utente.
            User current = itUser.next();

            if (current.getNick().equals(user)) {
                // L'utente è stato trovato.
                if (!current.validCred(user, password))
                    // Le credenziali non sono valide.
                    return false;
                else
                    // Trovato e credenziali valide.
                    found = true;
            }
        }

        // Rimuove il documento digitale desiderato dalla lista dei
        // documenti.
        return found && this.docs.remove(new DigitalDoc(user, doc));
    }

    // --------------------------------------------------------------------------- //
    public void readDoc(String user, String doc, int password)
        throws WrongIDException {
        // TODO: invariante di rappresentazione
    // --------------------------------------------------------------------------- //
        // Esegue la procedura solo nel caso di parametri validi.
        if (user != null && doc != null) {
            User usrCheck = new User(user, password);

            if (!this.users.contains(usrCheck))
                throw new WrongIDException("User not registered");

            for (User current : this.users) {
                if (current.equals(usrCheck) && !current.validCred(user, password))
                    throw new WrongIDException("Invalid credentials");
            }

            // TODO: controllare che l'autore il titolo del documento sia presente
            //       all'interno della lista e che l'utente abbia accesso al documento.
            DigitalDoc gotDoc = null;
            boolean    found1 = false;
            boolean    found2 = false;

            for (DigitalDoc current : this.docs) {
                // Cerca il documento nell'insieme dei documenti.
                if (current.getDoc().equals(doc) && current.getUser().equals(user)) {
                    // Documento trovato.
                    gotDoc = current;
                    found1 = true;
                }

                if (found1) 
                    // Interrompi il ciclo se il documento è stato trovato.
                    break;
            }

            if (!found1)
                throw new WrongIDException("Document not found");

            if ((found1 != null || found2 != null) && gotDoc != null)
                // Leggi sta merda.
        }
    }

    // --------------------------------------------------------------------------- //
    public void shareDoc(String fromName, String toName, String doc, int password) 
        throws WrongIDException {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        // Parametri attuali errati
        if (fromName != null && toName != null && doc != null) {
            // Oggetti temporanei per la ricerca.
            User tempAuth = new Client(fromName, password);
            User tempShr  = new Client(toName, 0);
            // Riferimenti agli oggetti reali.
            User realAuth = null;
            User realShr  = null;

            // Controlla che gli utenti siano effettivamente registrati
            if (!this.users.contains(tempAuth) || !this.users.contains(tempShr))
                throw new WrongIDExcpetion("Author or Other user (or both) not registered");

            // Itera sull'insieme degli utenti per cercare i riferimenti agli
            // oggetti originali e controllare le credenziali dell'autore.
            // Il ciclo termina quando entrambi i riferimenti sono stati trovati.
            for (User current : this.users) {
                if (current.equals(tempAuth)) {
                    // Copia il riferimento originale dell'autore
                    realAuth = current;
                    // Controlla le credenziali dell'autore
                    if (!current.validCred(fromName, password))
                        throw new WrongIDException("Invalid author credentials");
                }
                // Copia il riferimento originale dell'utente nella condivisione
                if (current.equals(tempShr))
                    realShr = current;
                // Se entrambi i riferimenti sono stati risolti, esci dal ciclo
                if (realAuth != null && realShr != null)
                    break;
            }

            if (realAuth == null || realShr == null)
                throw new WrongIDException("Original references not found");

            // Riferimenti temporanei e originali al documento digitale, insieme
            // all'indice nella lista dei documenti (inizializzato a -1)
            DigitalDoc tempDoc = new DigitalDoc(fromName, doc);
            DigitalDoc realDoc = null;
            int indexRealDoc   = -1;

            // Prima cerca il documento digitale nell'insieme, poi copia il riferimento.
            // In caso di successo, aggiunge un nuovo elemento condiviso nella coda delle
            // notifiche di condivisione.
            // Altrimenti, solleva eccezione.
            if ((indexRealDoc = this.docs.indexOf(tempDoc)) != -1
                && (realDoc = this.docs.get(indexRealDoc)) != null)
                // Aggiunge una nuova notifica.
                this.shares.add(new SharedDoc(realAuth, realShr, realDoc));
            else
                // Errore, documento non trovato.
                throw new WrongIDException("Doc specified not found");
        }
    }

    // --------------------------------------------------------------------------- //
    public String getNext(String user, int password) 
        throws EmptQueueException, WrongIDException {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //

    }
}