package ui;

import model.Score;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


// Score application
public class NotesApp {
    private static final String JSON_STORE = "./data/score.json";
    private Score sco;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: run the notes application
    public NotesApp() throws FileNotFoundException {
        runNotes();
    }

    // MODIFIES: this
    // EFFECTS: initializes a score
    private void init() {
        sco = new Score();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runNotes() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }



    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\te -> edit properties");
        System.out.println("\tv -> view notes");
        System.out.println("\tn -> add notes");
        System.out.println("\tr -> add rest");
        System.out.println("\tt -> change tempo");
        System.out.println("\ts -> shift score");
        System.out.println("\tsv -> save score");
        System.out.println("\tl -> load score");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("n")) {
            addNotes();
        } else if (command.equals("r")) {
            addRest();
        } else if (command.equals("e")) {
            editScore();
        } else if (command.equals("v")) {
            System.out.println(sco.getNotesStringArray());
        } else if (command.equals("t")) {
            changeTempo();
        } else if (command.equals("s")) {
            shiftTone();
        } else if (command.equals("sv")) {
            saveScore();
        } else if (command.equals("l")) {
            loadScore();
        } else {
            System.out.println("Selection not valid...");
        }
    }


    // MODIFIES: this
    // EFFECTS: edit score properties that's not notes
    private void editScore() {
        String command = null;
        boolean keepGoing = true;

        while (keepGoing) {
            displayProperties();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepGoing = false;
            } else {
                processEditCommand(command);
            }
        }

        System.out.println("\nreturning to main menu!");

    }

    // EFFECTS: display menu of edit properties
    private void displayProperties() {
        System.out.println("\nSelect from:");
        System.out.println("\tv -> view properties");
        System.out.println("\tc -> edit clef");
        System.out.println("\tn -> edit name");
        System.out.println("\tt -> edit tone");
        System.out.println("\ttp -> edit tempo");
        System.out.println("\ts -> edit signature");
        System.out.println("\tb -> back to main menu");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processEditCommand(String command) {
        if (command.equals("c")) {
            editClef();
        } else if (command.equals("v")) {
            System.out.println(sco.getProperties());
        } else if (command.equals("n")) {
            editName();
        } else if (command.equals("t")) {
            editTone();
        } else if (command.equals("tp")) {
            editTempo();
        } else if (command.equals("s")) {
            editSignature();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: edit clef
    private void editClef() {
        System.out.println("enter the clef(h/m/l):");
        char clef = input.next().charAt(0);
        sco.setClef(clef);
    }

    // MODIFIES: this
    // EFFECTS: edit tempo
    private void editTempo() {
        System.out.println("enter the tempo:");
        int n = input.nextInt();
        sco.setTempo(n);
    }

    // MODIFIES: this
    // EFFECTS: edit time signature
    private void editSignature() {
        System.out.println("enter the signature:");
        String s = input.next();
        sco.setTimeSignature(s);
    }

    // MODIFIES: this
    // EFFECTS: edit tone
    private void editTone() {
        System.out.println("enter the tone:");
        String n = input.next();
        sco.setTone(n);
    }

    // MODIFIES: this
    // EFFECTS: edit name
    private void editName() {
        System.out.println("enter the name:");
        String n = input.next();
        sco.setName(n);
    }

    // MODIFIES: this
    // EFFECTS: add a note to the score
    private void addNotes() {
        System.out.println("Enter the note:");
        String key = input.next();
        String[] note = {key};
        sco.setNotes(note);
        System.out.println(sco.getNotesStringArray());
    }

    // MODIFIES: this
    // EFFECTS: add a rest to the score
    private void addRest() {
        System.out.println("Enter the beat:");
        String bt = "S%" + input.next() + "00";
        String[] note = {bt};
        sco.setNotes(note);
        System.out.println(sco.getNotesStringArray());
    }

    // MODIFIES: this
    // EFFECTS: change the tempo of the score
    private void changeTempo() {
        System.out.println("Enter the new tempo in rpm:");
        int rpm = input.nextInt();
        sco.setTempo(rpm);
        System.out.println("set the tempo to:" + sco.getTempo());
    }

    // MODIFIES: this
    // EFFECTS: change the score by shifting the tonality
    private void shiftTone() {
        System.out.println("How do you want to shift the tone? enter number.");
        int n = input.nextInt();
        sco.reTune(n);
        System.out.println(sco.getNotesStringArray());
    }

    // EFFECTS: saves the score to file
    private void saveScore() {
        try {
            jsonWriter.open();
            jsonWriter.write(sco);
            jsonWriter.close();
            System.out.println("Saved " + sco.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: load score from file
    private void loadScore() {
        try {
            sco = jsonReader.read();
            System.out.println("Loaded " + sco.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
    public static void main(String[] args) {
        try {
            new NotesApp();
        } catch (FileNotFoundException e) {
            System.out.println("unable to run application: file not found");
        }
    }
}
