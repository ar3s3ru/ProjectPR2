/**
  *  ShareDocImpl.java
  *  Autore: Danilo Cianfrone, matricola 501292
  *  Il codice, in ogni sua parte, è opera originale dell'autore.
  */
import java.util.List;
import java.util.Scanner;
import java.util.LinkedList;

public class ShareDocImpl implements ShareDoc {
    // Variabili d'istanza che denotano il login.
    private boolean isLogged;
    private User    usrLogged;
    // Variabili d'istanza che denotano lista utenti e lista dei documenti presenti.
    private List<User>   users;
    private List<String> name_docs;
    private List<String> name_usrs;

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
        this.name_usrs = new LinkedList<>();

        this.users.add(new Operator(op_nick, op_pass));
        this.name_usrs.add(op_nick);
    }

    // --------------------------------------------------------------------------- //
    public boolean addUser(String name, int password) {
    // --------------------------------------------------------------------------- //
        // Se name è uguale a null, o non vi è un login attivo,
        // oppure il login attivo non è di un operatore, ritorna false.
        if (name == null || !this.isLogged || !this.usrLogged.isOp())
            return false;

        // Aggiunge un nuovo utente alla lista, ritorna
        // true in caso di successo, false altrimenti.
        User toAdd = new Client(name, password);
        // Controlla che l'utente non sia già presente nella collezione.
        boolean isContained = this.name_usrs.contains(name);

        /*
        for (User current : this.users) {
            // L'utente è già presente, segnala...
            if (current.equals(toAdd)) {
                // ...ed esce dal ciclo.
                isContained = true;
                break;
            }
        }
        */

        // Esegue le add() solo se l'utente non è presente.
        return !isContained && this.users.add(toAdd) && this.name_usrs.add(name);
    }

    // --------------------------------------------------------------------------- //
    public void removeUser(String name) {
    // --------------------------------------------------------------------------- //
        // Esegue la procedura solo se name è un nome registrato e valido,
        // e vi sia loggato un operatore.
        if (name != null && this.isLogged && this.usrLogged.isOp() &&
            this.name_usrs.contains(name)) {
            // Riferimento all'utente da eliminare.
            User    toDelete = null;
            boolean gotUser  = false;

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

            // L'utente è stato trovato, esegue clean-up se l'utente
            // è eliminabile.
            if (gotUser && toDelete.isDeletable()) {
                // Itera nell'insieme dei documenti condivisi del quale l'autore
                // è utente beneficiario, ed elimina i riferimenti nella lista
                // dell'utente autore.
                for (SharedDoc current : toDelete.sharedWith) {
                    current.getAuthor().sharedTo.remove(current);
                }
                // Elimina i riferimenti dei documenti condivisi dall'utente
                // dagli insiemi degli utenti con i quali 
                // i documenti son stati condivisi.
                for (SharedDoc current : toDelete.sharedTo) {
                    current.getUser().sharedWith.remove(current);
                }
                // Elimina documenti registrati dell'utente dalla lista dei
                // documenti inseriti.
                for (DigitalDoc current : toDelete.docs) {
                    this.name_docs.remove(current.getDoc());
                }
                // Elimina il riferimento all'utente nell'insieme degli utenti.
                this.users.remove(toDelete);
                this.name_usrs.remove(name);
            }
        }
    }

    // --------------------------------------------------------------------------- //
    public boolean addDoc(String user, String doc, int password) {
    // --------------------------------------------------------------------------- //
        // Se gli argomenti non sono validi, non avvengono modifiche
        // alla lista dei documenti.
        // Inoltre, c'è bisogno di un login da parte di un utente Client,
        // non Operator.
        if (user == null   || doc == null ||
            !this.isLogged || this.usrLogged.isOp())
            return false;

        // Credenziali invalide, ritorna false.
        if (!this.usrLogged.getNick().equals(user) || !this.usrLogged.validPass(password))
            return false;

        // Controlla che il nome del documento non sia stato già utilizzato
        // per altri documenti.
        if (this.name_docs.contains(doc)) {
            return false;
        }

        // Aggiunge il documento alla lista dei documenti dell'utente.
        Scanner input = new Scanner(System.in);
        System.out.print("Insert text here: ");
        String textToGet = input.nextLine();

        return (this.name_docs.add(doc)) &&
               (this.usrLogged.docs.add(new DigitalDoc(doc, user, textToGet)));
    }

    // --------------------------------------------------------------------------- //
    public boolean removeDoc(String user, String doc, int password) {
    // --------------------------------------------------------------------------- //
        // Parametri attuali non validi, ritorna false.
        if (user == null   || doc == null ||
            !this.isLogged || this.usrLogged.isOp())
            return false;

        DigitalDoc toDelete     = null;
        boolean    isDocPresent = false;

        for (DigitalDoc current : this.usrLogged.docs) {
            // Scorri nella lista dei documenti dell'utente per cercare
            // il documento di nome "doc".
            if (current.getDoc().equals(doc)) {
                // Documento trovato, copia riferimento ed esci dal ciclo.
                toDelete     = current;
                isDocPresent = true;
                break;
            }
        }

        if (!isDocPresent) {
            // Documento non trovato, ritorna false.
            return false;
        }

        // Cerca nelle notifiche di condivisione la presenza
        // del documento.
        for (SharedDoc current : this.usrLogged.sharedTo) {
            // Trovata una notifica di condivisione.
            if (current.getShrDoc().equals(toDelete)) {
                // Elimina dalla coda di condivisione
                // del cliente.
                current.getUser().sharedWith.remove(current);
                // Elimina dalla coda di condivisione del
                // servente (autore).
                this.usrLogged.sharedTo.remove(current);
            }
        }

        // Rimuovi il nome del documento dall'insieme dei nomi dei documenti
        // e dalla lista dei documenti dell'utente.
        return (this.name_docs.remove(doc)) &&
               (this.usrLogged.docs.remove(toDelete));
    }

    // --------------------------------------------------------------------------- //
    public void readDoc(String user, String doc, int password)
        throws WrongIDException {
    // --------------------------------------------------------------------------- //
        // Esegue la procedura solo nel caso di parametri validi.
        if (user != null && doc != null && 
            this.isLogged && !this.usrLogged.isOp()) {

            if (!this.usrLogged.getNick().equals(user) ||
                !this.usrLogged.validPass(password))
                // Credenziali invalide, l'utente non è loggato.
                throw new WrongIDException("Insert invalid credentials");

            DigitalDoc toRead   = null;
            boolean    docFound = false;
            // Cerca il documento nella lista dei documenti dell'utente.
            for (DigitalDoc current : this.usrLogged.docs) {
                if (current.getDoc().equals(doc)) {
                    toRead   = current;
                    docFound = true;
                    break;
                }
            }

            if (!docFound) {
                // Cerca nell'insieme di documenti condivisi nel quale l'utente
                // è cliente.
                for (SharedDoc current : this.usrLogged.sharedWith) {
                    // Documento condiviso trovato, copia riferimento.
                    if (current.getShrDoc().getDoc().equals(doc)) {
                        toRead   = current.getShrDoc();
                        docFound = true;
                        break;
                    }
                }
            }

            // Legge il documento solo se è stato trovato.
            if (docFound) {
                // Legge il documento
                System.out.print("Document: ");
                System.out.println(toRead.getDoc());

                System.out.print("Text: ");
                System.out.println(toRead.getText());
            }
            // Documento non trovato, solleva eccezione.
            else
                throw new WrongIDException("Document not found");
        }
    }

    // --------------------------------------------------------------------------- //
    public void shareDoc(String fromName, String toName, String doc, int password) 
        throws WrongIDException {
    // --------------------------------------------------------------------------- //
        // Parametri attuali errati
        if (fromName != null && toName != null && doc != null &&
            this.isLogged && !this.usrLogged.isOp()) {
            
            if (!this.usrLogged.getNick().equals(fromName) ||
                !this.usrLogged.validPass(password))
                // Le credenziali non sono quelle dell'utente loggato.
                throw new WrongIDException("Invalid user credentials");

            // Riferimenti all'utente cliente della condivisione.
            User    clientRefer = null;
            boolean clientFound = false;

            for (User current : this.users) {
                // Utente cliente trovato.
                if (current.getNick().equals(toName)) {
                    // Copia i riferimenti ed esci dal ciclo.
                    clientRefer = current;
                    clientFound = true;
                    break;
                }
            }

            if (!clientFound)
                // Cliente non trovato, solleva eccezione.
                throw new WrongIDException("Client user not found");

            // Riferimenti al documento digitale da condividere.
            DigitalDoc shrDoc   = null;
            boolean    foundDoc = false;

            for (DigitalDoc current : this.usrLogged.docs) {
                // Documento da condividere trovato.
                if (current.getDoc().equals(doc)) {
                    // Copia i riferimenti.
                    shrDoc   = current;
                    foundDoc = true;
                    break;
                }
            }

            if (!foundDoc)
                // Documento non trovato, solleva eccezione.
                throw new WrongIDException("Document not found");

            // Crea nuovo elemento condiviso.
            SharedDoc toShare = new SharedDoc(usrLogged, clientRefer, shrDoc);
            // Inserisci documento condiviso nelle code di condivisione
            // del servente e del cliente.
            this.usrLogged.sharedTo.add(toShare);
            clientRefer.sharedWith.add(toShare);
        }
    }

    // --------------------------------------------------------------------------- //
    public String getNext(String user, int password) 
        throws EmptyQueueException, WrongIDException {
        // TODO: invariante di rappresentazione.
    // --------------------------------------------------------------------------- //
        if (user == null)
            // Nome utente non valido.
            throw new WrongIDException("User is null");

        if (!this.isLogged)
            // Nessun login corrente.
            throw new WrongIDException("No login session in current");

        if (this.usrLogged.isOp())
            // Il login corrente è di un operatore.
            throw new WrongIDException("Operator is logged in");

        if (!this.usrLogged.getNick().equals(user) || 
            !this.usrLogged.validPass(password))
            // Credenziali non valide.
            throw new WrongIDException("User credentials are not valid");

        // Copia il riferimento all'elemento in testa alla coda di condivisione
        // ed elimina dalla coda stessa.
        SharedDoc toGet = this.usrLogged.sharedTo.poll();

        if (toGet == null)
            // Se il riferimento è null, la coda è vuota.
            throw new EmptyQueueException("Empty queue");

        // Rimuovi la condivisione anche dalla coda del cliente.
        toGet.getUser().sharedWith.remove(toGet);
        // Ritorna il titolo del documento condiviso eliminato.
        return toGet.getShrDoc().getDoc();
    }

    // EFFECTS: verifica che l'invariante di rappresentazione sia valida.
    // RETURN:  true se la repInvariant è valida, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean repOk() {
    // --------------------------------------------------------------------------- //
        if ((isLogged && usrLogged != null) || (!isLogged && usrLogged == null)) {
            if (users.size() <= 0) {
                return false;
            }

            int documentsScanned = 0;
            int usersScanned     = 0;

            for (User user : users) {
                // Ogni utente dev'essere registrato in name_usrs.
                if (name_usrs.contains(user.getNick())) {
                    ++usersScanned;
                } else {
                    return false;
                }

                // Il nome di ogni documento digitale deve essere registrato anche
                // in name_docs.
                for (DigitalDoc doc : user.docs) {
                    // Documento non registrato in name_docs.
                    if (!name_docs.contains(doc.getDoc()))
                        return false;
                    // Incrementa contatore documenti analizzati.
                    ++documentsScanned;
                }

                for (SharedDoc shwith : user.sharedWith) {
                    // Ogni documento in condivisione cliente deve essere registrato.
                    // Il cliente del documento condiviso dev'essere l'utente corrente.
                    // L'autore del documento condiviso dev'essere registrato.
                    if (!name_docs.contains(shwith.getShrDoc().getDoc()) ||
                        !shwith.getUser().equals(user)                   ||
                        !users.contains(shwith.getAuthor()))
                        return false;
                }

                for (SharedDoc shto : user.sharedTo) {
                    // Ogni documento in condivisione servente deve essere registrato.
                    // L'autore del documento condiviso dev'essere l'utente corrente.
                    // L'utente cliente del documento condiviso dev'essere registrato.
                    if (!name_docs.contains(shto.getShrDoc().getDoc()) ||
                        !shto.getAuthor().equals(user)                 ||
                        !users.contains(shto.getUser()))
                        return false;
                }
            }

            // Controlla che il numero di utenti e documenti analizzati siano
            // uguali rispettivamente al numero di utenti e documenti registrati.
            return (documentsScanned == name_docs.size())
                    && (usersScanned == name_usrs.size());
        } else {
            return false;
        }
    }

    // EFFECTS: effettua il login dell'utente.
    // RETURNS: true se lo stato del sistema è LOGIN, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean logIn(String nick, int pass) {
        // --------------------------------------------------------------------------- //
        // Precondizioni verificate.
        if (nick != null && !this.isLogged) {
            // Itera sulla collezione di utenti.
            for (User current : this.users) {
                // Utente trovato.
                if (current.getNick().equals(nick)) {
                    // Credenziali valide, accedi.
                    if (current.validPass(pass)) {
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

    // EFFECTS: effettua il logout del sistema.
    // RETURNS: true se lo stato del sistema è LOGOUT, false altrimenti.
    // --------------------------------------------------------------------------- //
    public boolean logOut() {
        // --------------------------------------------------------------------------- //
        // Se vi è un login attivo...
        if (this.isLogged) {
            // ...esci dal login.
            this.usrLogged = null;
            this.isLogged  = false;
            // Logout effettuato.
            return true;
        }
        // Il logout non è stato effettuato.
        return false;
    }

    // EFFECTS: stampa lo stato del sistema.
    //          (Funzione di debug)
    // --------------------------------------------------------------------------- //
    public void printStat () {
        // --------------------------------------------------------------------------- //
        if (this.isLogged && this.usrLogged.isOp()) {
            System.out.println("[ Operator " + this.usrLogged.getNick() + " logged ]");
            System.out.println("User list:");

            for (User current : this.users) {
                System.out.print("  -> " + current.getNick());

                if (current.isOp()) {
                    System.out.println(" [Operator]");
                    continue;
                } else {
                    System.out.println();
                }

                System.out.print("    >> Docs: ");

                if (current.docs.size() > 0) {
                    boolean gotFirst = false;
                    for (DigitalDoc doc : current.docs) {
                        if (!gotFirst) {
                            System.out.println(doc.getDoc());
                            gotFirst = true;
                        } else {
                            System.out.println("             " + doc.getDoc());
                        }
                    }
                } else {
                    System.out.println("[Empty]");
                }

                System.out.print("    >> Shared client: ");

                if (current.sharedWith.size() > 0) {
                    boolean gotFirst = false;
                    for (SharedDoc shwith : current.sharedWith) {
                        if (!gotFirst) {
                            System.out.print(shwith.getShrDoc().getDoc());
                            System.out.println(" (author: " + shwith.getAuthor().getNick() + ")");
                            gotFirst = true;
                        } else {
                            System.out.print("                      ");
                            System.out.print(shwith.getShrDoc().getDoc());
                            System.out.println(" (author: " + shwith.getAuthor().getNick() + ")");
                        }
                    }
                } else {
                    System.out.println("[Empty]");
                }

                System.out.print("    >> Shared server: ");

                if (current.sharedTo.size() > 0) {
                    boolean gotFirst = false;
                    for (SharedDoc shto : current.sharedTo) {
                        if (!gotFirst) {
                            System.out.print(shto.getShrDoc().getDoc());
                            System.out.println(" (with: " + shto.getUser().getNick() + ")");
                            gotFirst = true;
                        } else {
                            System.out.print("                      ");
                            System.out.print(shto.getShrDoc().getDoc());
                            System.out.println(" (with: " + shto.getUser().getNick() + ")");
                        }
                    }
                } else {
                    System.out.println("[Empty]");
                }
            }
        } else {
            System.out.println("[!] Operator not logged, access denied.");
        }
    }
}