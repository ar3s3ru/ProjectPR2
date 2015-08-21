import java.util.Scanner;

public class TestCase {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Inserisci nome operatore: ");
        String nameOp = input.nextLine();

        System.out.print("Inserisci password (numero intero): ");
        int passOp = input.nextInt();
        input.nextLine();

        ShareDocImpl platform = null;

        try {
            platform = new ShareDocImpl(nameOp, passOp);
        }
        catch (IllegalArgumentException e) {
            System.out.println("Ricevuto IllegalArgumentException: " + e);
            return;
        }

        System.out.print("Login ritorna stato: ");
        System.out.println(platform.logIn(nameOp, passOp));

        System.out.print("Logout ritorna stato: ");
        System.out.println(platform.logOut());

        System.out.print("Login errato ritorna stato: ");
        System.out.println(platform.logIn("Porcozio", 10));

        System.out.print("Logout ritorna stato: ");
        System.out.println(platform.logOut());

        System.out.println("Prova addUser senza login: " + platform.addUser("Ciao", 121));
        System.out.print("Prova addUser con login operatore: ");

        if (platform.logIn(nameOp, passOp)) {
            System.out.println(platform.addUser("Ciao", 121));

            System.out.print("Prova removeUser client: ");
            platform.removeUser("Ciao");
            if (platform.logOut()) {
                System.out.println(!platform.logIn("Ciao", 121));
            } else
                System.out.println("false -> non posso fare logout");
        } else 
            System.out.println("false");

        // Creazione utente
        String testUser;
        System.out.print("Inserisci nome utente: ");
        if (input.hasNextLine())
            testUser = input.nextLine();
        else {
            System.out.println("[!] Cannot read next line");
            return;
        }

        int testPass;
        System.out.print("Inserisci password: ");
        if (input.hasNextInt()) {
            testPass = input.nextInt();
            input.nextLine();
        }
        else {
            System.out.println("[!] Cannot read next int");
            return;
        }

        // Login operatore
        if (!platform.logIn(nameOp, passOp)) {
            System.out.println("[!] Login operatore fallito...");
            return;
        }
        // Aggiunta nuovo cliente specificato
        if (!platform.addUser(testUser, testPass)) {
            System.out.println("Creazione utente fallita...");
            return;
        }
        // Logout operatore
        if (!platform.logOut()) {
            System.out.println("[!] Logout operatore fallito...");
            return;
        }

        // Testa creazione documento.
        System.out.print("Aggiungi documento - nome: ");
        String nameDoc = input.nextLine();
        // Login cliente
        if (!platform.logIn(testUser, testPass)) {
            System.out.println("[!] Login cliente fallito...");
            return;
        }
        // Aggiunta nuovo documento
        if (!platform.addDoc(testUser, nameDoc, testPass)) {
            System.out.println("Aggiunta documento " + nameDoc + " fallita");
            return;
        } else
            System.out.println("Documento aggiunto con successo");
        // Prova la lettura del documento appena inserito
        try {
            platform.readDoc(testUser, nameDoc, testPass);
        } catch (WrongIDException e) {
            // Errore, catturata eccezione
            System.out.println("Catturato " + e);
            System.out.println("Uscita...");
            return;
        }

        // Prova removeDoc
        // Aggiunge nuovo documento di test
        if (!platform.addDoc(testUser, "testDoc", testPass)) {
            System.out.println("Aggiunta documento " + nameDoc + " fallita");
            return;
        }
        // Rimuove il documento
        if (!platform.removeDoc(testUser, "testDoc", testPass)) {
            System.out.println("Rimozione testDoc fallita...");
            return;
        }

        boolean gotExcept = false;
        // Prova a leggere il documento appena rimosso
        try {
            platform.readDoc(testUser, "testDoc", testPass);
        } catch (WrongIDException e) {
            // Catturata eccezione, test superato
            System.out.println("Catturato " + e);
            System.out.println("    Test superato!");
            gotExcept = true;
        } finally {
            if (!gotExcept) {
                // Eccezione non catturata, errore
                System.out.println("[!] Eccezione non catturata...");
                return;
            }
        }
        // Logout cliente
        if (!platform.logOut()) {
            System.out.println("[!] Logout cliente fallito...");
            return;
        }

        // Aggiunta nuovo user per test shareDoc
        // Login operatore
        if (!platform.logIn(nameOp, passOp)) {
            System.out.println("[!] Login operatore fallito...");
            return;
        }
        // Aggiunta utente
        if (!platform.addUser("testUser", 123)) {
            System.out.println("[!] Aggiunta testUser fallito...");
            return;
        }
        // Logout operatore
        if (!platform.logOut()) {
            System.out.println("[!] Logout operatore fallito...");
            return;
        }

        // Prova lettura del documento che non è di testUser
        // Login nuovo utente
        if (!platform.logIn("testUser", 123)) {
            System.out.println("[!] Login testUser fallito...");
            return;
        }
        // Lettura documento nameDoc (dell'utente iniziale)
        try {
            platform.readDoc("testUser", nameDoc, 123);
        } catch (WrongIDException e) {
            // Catturata eccezione, test superato
            System.out.println("Catturato " + e);
            System.out.println("    Test superato!");
            gotExcept = true;
        } finally {
            if (!gotExcept) {
                // Eccezione non catturata, errore
                System.out.println("[!] Eccezione non catturata...");
                return;
            }
        }
        // Logout testUser
        if (!platform.logOut()) {
            System.out.println("[!] Logout testUser fallito...");
            return;
        }
        // Login utente iniziale
        if (!platform.logIn(testUser, testPass)) {
            System.out.println("[!] Login " + testUser + " fallito...");
            return;
        }
        // Condivisione documento nameDoc
        try {
            platform.shareDoc(testUser, "testUser", nameDoc, testPass);
        } catch (WrongIDException e) {
            System.out.println("Catturato " + e);
            return;
        }
        // Logout utente iniziale e login testUser
        if (!platform.logOut() || !platform.logIn("testUser", 123)) {
            System.out.println("[!] Errore login testUser...");
            return;
        }
        // Lettura documento (dovrebbe essere condiviso)
        try {
            platform.readDoc("testUser", nameDoc, 123);
        } catch (WrongIDException e) {
            System.out.println("Catturato " + e);
            return;
        }
        // Lettura andata a buon fine, esegue logout
        if (!platform.logOut()) {
            System.out.println("[!] Logout testUser fallito...");
            return;
        }
    }
}