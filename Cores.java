/* ΟΝΟΜΑΤΕΠΩΝΥΜΟ: ΜΑΤΣΚΙΔΗΣ ΑΘΑΝΑΣΙΟΣ
 *  ΕΤΟΙΜΟΣ ΚΩΔΙΚΑΣ: ΤΟ ΔΙΑΒΑΣΜΑ ΤΟΥ ΑΡΧΕΙΟΥ ΩΣ ΟΡΙΣΜΑ ΤΟ ΟΠΟΙΟ ΠΗΡΑ ΑΠΟ https://www2.hawaii.edu/~walbritt/ics211/examples/ReadFromFile.java
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Cores {

    /* ΑΝΑΠΑΡΑΣΤΑΣΗ ΤΩΝ ΑΠΑΙΤΉΣΕΩΝ ΤΩΝ ΚΑΤΑΝΑΛΩΤΩΝ ΣΕ ΠΥΡΗΝΕΣ ΚΑΙ Η ΠΡΟΣΦΕΡΟΜΕΝΗ ΤΙΜΗ ΑΝΑ ΠΥΡΗΝΑ. */
    static class Demands {

        private int cores;
        private double pricePerCore;

        public Demands(int cores, double pricePerCore) {
            this.cores = cores;
            this.pricePerCore = pricePerCore;
        }

        public int getCores() {return cores;}

        public double getPricePerCore() {return pricePerCore;}
    }

    /* minNumberOfCoins: ΔΕΧΕΤΑΙ ΩΣ ΟΡΙΣΜΑ ΕΝΑΝ ΠΙΝΑΚΑ ΠΟΥ ΕΧΕΙ ΤΙΣ ΤΙΜΕΣ ΤΩΝ ΔΙΑΘΕΣΙΜΩΝ ΠΥΡΗΝΩΝ ΑΝ VMs
    *  ΤΟ ΜΕΓΕΘΟΣ ΑΥΤΟΥ ΤΟΥ ΠΙΝΑΚΑ ΚΑΙ ΤΟΝ ΑΡΙΘΜΟ ΤΩΝ ΠΥΡΗΝΩΝ ΠΟΥ ΑΠΑΙΤΕΙ Ο ΚΑΤΑΝΑΛΩΤΗΣ ΚΑΙ ΕΠΙΣΤΡΕΦΕΙ
    *  ΤΟΝ ΑΡΙΘΜΟ ΤΩΝ VMs. */
    public static int minNumberOfCoins(int availableCores[], int size, int value) {

        int VMs[] = new int[value + 1];

        VMs[0] = 0;

        for (int i = 1; i <= value; i++) {
            VMs[i] = Integer.MAX_VALUE;
        }

        for (int i = 1; i <= value; i++) {
            for (int j = 0; j < size; j++) {
                if (availableCores[j] <= i) {
                    int sub_res = VMs[i - availableCores[j]];
                    if (sub_res != Integer.MAX_VALUE && sub_res + 1 < VMs[i]) {
                        VMs[i] = sub_res + 1;
                    }

                }
            }
        }
        return VMs[value];
    }

    /* printVMs: ΔΕΧΕΤΑΙ ΣΑΝ ΟΡΙΣΜΑ ΕΝΑ ArrayList ΟΠΟΥ ΕΙΝΑΙ ΑΠΟΘΗΚΕΥΜΈΝΑ Ο ΑΡΙΘΜΟΣ ΤΩΝ VMs ΤΩΝ
    *  ΚΑΤΑΝΑΛΩΤΩΝ ΚΑΙ ΤΑ ΕΜΦΑΝΙΖΕΙ ΣΤΗΝ ΖΗΤΟΥΜΕΝΗ ΜΟΡΦΗ. */
    public static void printVMs(ArrayList<Integer> VMs) {
        for (int i = 0; i < VMs.size(); i++) {
            System.out.println("Client " + (i + 1) + ": " + VMs.get(i) + " VMs");
        }
    }

    /* maxValue: ΔΕΧΕΤΑΙ ΩΣ ΟΡΙΣΜΑΤΑ ΤΟΝ ΑΡΙΘΜΟ ΤΩΝ ΔΙΑΘΕΣΙΜΩΝ ΠΥΡΗΝΩΝ ΣΤΟΥΣ SERVERS, ΕΝΑΝ ΠΙΝΑΚΑ ΠΟΥ ΕΙΝΑΙ ΑΠΟΘΗΚΕΥΜΕΝΑ
    *  ΟΙ ΤΙΜΕΣ ΤΩΝ ΑΠΑΙΤΗΣΕΩΝ ΤΩΝ ΚΑΤΑΝΑΛΩΤΩΝ ΣΕ ΠΥΡΗΝΕΣ, ΕΝΑΝ ΠΙΝΑΚΑ ΜΕ ΤΙΣ ΤΙΜΕΣ ΤΟΥ ΣΥΝΟΛΙΚΟΥ ΠΟΣΟΥ ΠΡΟΣ ΠΛΗΡΩΜΗ ΓΙΑ
    *  ΚΑΘΕ ΚΑΤΑΝΑΛΩΤΗ ΚΑΙ ΤΟΝ ΑΡΙΘΜΟ ΤΩΝ ΚΑΤΑΝΑΛΩΤΩΝ ΚΑΙ ΕΠΙΣΤΡΕΦΕΙ ΤΟ ΜΕΓΙΣΤΟ ΠΟΣΟ ΠΛΗΡΩΜΗΣ. */
    public static double maxValue(int cores, int weight[], double value[], int numberOfCustomers) {

        double data[][] = new double[numberOfCustomers + 1][cores + 1];

        for (int i = 0; i <= numberOfCustomers; i++) {
            for (int j = 0; j <= cores; j++) {
                if (i == 0 || j == 0) {
                    data[i][j] = 0;
                }
                else if (weight[i - 1] <= j) {
                    data[i][j] = Math.max(value[i - 1] + data[i - 1][j - weight[i - 1]], data[i - 1][j]);
                }
                else {
                    data[i][j] = data[i - 1][j];
                }
            }
        }

        return data[numberOfCustomers][cores];
    }

    public static void main(String[] args) {
        File file = null;
        Scanner readFromFile = null;
        String line = null;

        /* ΠΕΡΙΠΤΩΣΗ ΟΠΟΥ ΔΕΝ ΥΠΑΡΧΕΙ ΚΑΝΕΝΑ ΑΡΧΕΙΟ ΩΣ ΟΡΙΣΜΑ. */
        if (args.length == 0){
            System.out.println("ERROR: Please enter the file name as the first commandline argument.");
            System.exit(1);
        }

        /* ΠΕΡΙΠΤΩΣΗ ΑΔΥΝΑΜΙΑΣ ΕΥΡΕΣΗΣ ΤΟΥ ΑΡΧΕΙΟΥ. */
        file = new File(args[0]);
        try{
            readFromFile = new Scanner(file);
        }catch (FileNotFoundException exception){
            System.out.println("ERROR: File not found for \"");
            System.out.println(args[0]+"\"");
            System.exit(1);
        }

        /* ΔΗΜΙΟΥΡΓΙΑ ΣΥΝΟΛΟΥ ΟΛΩΝ ΤΩΝ ΣΗΜΕΙΩΝ. */
        line=readFromFile.nextLine();
        int totalCores = Integer.parseInt(line);
        ArrayList<Demands> demandsList = new ArrayList<>();
        while (readFromFile.hasNextLine()){
            line=readFromFile.nextLine();
            if (line.split(" ").length > 1) {
                Demands demand = new Demands(Integer.parseInt(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]));
                demandsList.add(demand);
            }
        }

        int availableCores[] = {1, 2, 7, 11};
        int size = availableCores.length;
        ArrayList<Integer> VMs = new ArrayList<Integer>();
        for (int i = 0; i < demandsList.size(); i++) {
            VMs.add(minNumberOfCoins(availableCores, size, demandsList.get(i).getCores()));
        }
        printVMs(VMs);

        double value[] = new double[demandsList.size()];
        int weight[] = new int [demandsList.size()];
        size = value.length;
        for (int i = 0; i < demandsList.size(); i++) {
            value[i] = demandsList.get(i).getCores() * demandsList.get(i).getPricePerCore();
            weight[i] = demandsList.get(i).getCores();
        }
        System.out.println("Total amount: " + maxValue(totalCores, weight, value, size));
    }
}