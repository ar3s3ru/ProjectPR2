/**
 *  ShareDocImpl.java (version 2)
 *  Autore: Danilo Cianfrone, matricola 501292
 *  Il codice, in ogni sua parte, è opera originale dell'autore.
 */
public class ShareDocImpl implements ShareDoc {
    private Map<User, DigitalDoc> docs;
    private Queue<SharedDoc>      shares;
    private Set<User>             users;

    public ShareDocImpl(String op_nick, int op_pass)
        throws IllegalArgumentException {

        if (op_nick == null)
            throw new IllegalArgumentException("Operator nick is null");

        this.users  = new HashSet<>();
        this.docs   = new TreeMap<>();
        this.shares = new LinkedList<>();

        this.users.add(new Operator(op_nick, op_pass));
    }

    public boolean addUser(String name, int password) {

        if (name == null)
            return false;

        return this.users.add(new Client(name, password));
    }

    public void removeUser(String name) {
        if (name != null) {
            User    tempUsr = new Client(name, 0);
            boolean removed = this.users.remove(tempUsr);

            if (removed) {
                while (this.docs.containsKey(tempUsr))
                    this.docs.remove(tempUsr);
            }
        }
    }

    public boolean addDoc(String user, String doc, int password) {
        if (user == null || doc == null)
            return false;

        User tempUsr = null;

        for (User current : this.users) {
            if (current.getNick().equals(user)) {
                tempUsr = current;
                if (!current.validCred(user, password))
                    return false;
                else break;
            }
        }

        return (this.docs.putIfAbsent(tempUsr, new DigitalDoc(doc)) != null ? 
            true : false);
    }

    public boolean removeDoc(String user, String doc, int password) {
        if (user == null || doc == null)
            return false;

        DigitalDoc tempDoc = null;
        User       tempUsr = null;

        for (User current : this.users) {
            if (current.getNick().equals(user)) {
                tempUsr = current;
                if (!current.validCred(user, password))
                    return false;
                else break;
            }
        }

        // TODO: funzione di rimozione della chiave.

    }

    public void readDoc(String user, String doc, int password)
        throws WrongIDException {

        if (user != null && doc != null) {
            DigitalDoc tempDoc = null;
            User       tempUsr = null;

            for (User current : this.users) {
                if (current.getNick().equals(user)) {
                    if (!current.validCred(user, password))
                        throw new WrongIDException("Invalid credentials");
                    else {
                        tempUsr = current;
                        break;
                    }
                }
            }


        }
    }
}