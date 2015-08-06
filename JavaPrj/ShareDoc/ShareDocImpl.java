/**
 *  ShareDocImpl.java
 *  Autore: Danilo Cianfrone, matricola 501292
 *  Il codice, in ogni sua parte, è opera originale dell'autore.
 */
public class ShareDocImpl implements ShareDoc {
    private Vector<DigitalDoc> docs;
    private Queue<SharedDoc>   shares;
    private Set<User>          users;

    public ShareDocImpl(String op_nick, int op_pass) 
        throws IllegalArgumentException {
            // Se il nick del costruttore è null, solleva
            // IllegalArgumentException.
            if (op_nick == null)
                throw new IllegalArgumentException("Nick operator is null");

            // Inizializza l'insieme degli utenti e aggiunge
            // un nuovo utente operatore.
            this.users = new HashSet<>();
            this.users.add(new Operator(op_nick, op_pass));
            // Inizializza la lista dei documenti.
            this.docs = new Vector<>();
    }

    public boolean addUser(String name, int password) {
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

    public void removeUser(String name) {
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

    public boolean addDoc(String user, String doc, int password) {
        // Se gli argomenti non sono validi, non avvengono modifiche
        // alla lista dei documenti.
        if (user == null || doc == null)
            return false;

        // Controlla che le credenziali dell'utente siano valide.
        for (User current : this.users) {
            if (current.getNick().equals(user))
                if (!current.validCred(user, password)) 
                    return false;
                else break;
        }

        // Controlla che il documento non sia già presente nella lista
        // dei documenti inseriti; se ci sono, ritorna false e non inserisce,
        // altrimenti inserisce e ritorna true.
        return this.docs.add(new DigitalDoc(doc, user));
    }

    public boolean removeDoc(String user, String doc, int password) {
        // Parametri attuali non validi, ritorna false.
        if (user == null || doc == null)
            return false;

        for (User current : this.users) {
            if (current.getNick().equals(user))
                if (!current.validCred(user, password) || !current.isOp())
                    return false;
                else break;
        }

        // Rimuove il documento digitale desiderato dalla lista dei
        // documenti.
        return this.docs.remove(new DigitalDoc(user, doc));
    }

    public void readDoc(String user, String doc, int password)
        throws WrongIDException {
        // Esegue la procedura solo nel caso di parametri validi.
        if (user != null && doc != null) {
            User usrCheck = new User(user, password);

            if (!this.users.contains(toCheck))
                throw new WrongIDException("User not registered");

            for (User current : this.users) {
                if (current.equals(usrCheck) && !current.validCred(user, password))
                    throw new WrongIDException("Invalid credentials");
            }

            // TODO: controllare che l'autore il titolo del documento sia presente
            //       all'interno della lista e che l'utente abbia accesso al documento.
            
        }
    }

    public void shareDoc(String fromName, String toName, String doc, int password) 
        throws WrongIDException {
        // Parametri attuali errati
        if (fromName != null && toName != null && doc != null) {
            // Oggetti temporanei per la ricerca.
            User tempAuth = new Client(fromName, 0);
            User tempShr  = new Client(toName, 0);
            // Riferimenti agli oggetti reali.
            User realAuth = null;
            User realShr  = null;

            // Controlla che gli utenti siano effettivamente registrati
            if (!this.users.contains(tempAuth) && !this.users.contains(tempShr))
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

    public String getNext(String user, int password) 
        throws EmptQueueException, WrongIDException {

    }
}