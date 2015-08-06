public class SharedDoc {
    private DigitalDoc shrDoc;
    private User       usrAuthor;
    private User       usrShare;

    public SharedDoc(User author, User withShare, DigitalDoc doc) 
        throws IllegalArgumentException {
        // Parametri costruttore invalidi
        if (author == null || withShare == null || doc == null)
            throw new IllegalArgumentException("Invalid argument(s)");

        this.shrDoc    = doc;
        this.usrAuthor = author;
        this.usrShare  = withShare;
    }

    public boolean isAuthor(String nick) {
        return this.usrAuthor.getNick().equals(nick);
    }

    public boolean isUser(String nick) {
        return this.usrShare.getNick().equals(nick);
    }

    public User getAuthor() {
        return this.usrAuthor;
    }

    public User getUser() {
        return this.usrShare;
    }

    public User getShrDoc() {
        return this.shrDoc;
    }
}