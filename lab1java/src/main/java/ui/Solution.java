package ui;

/**
 * Kod preuzet od vlastite prošlogodišnje laboratorijske vježbe na predmetu Uvod u umjetnu inteligenciju.
 * Manje stavke ispravljene, ostatak funkcionalnosti ostao većinski isti.
 * Moj JMBAG je 0036515289
 */
public class Solution {

    public static void main(String[] args) {
        AlgorithmImpl desiredAlg;
        if(args.length == 4 || args.length == 6) {
            desiredAlg = new AlgorithmImpl(args);
            System.out.println(desiredAlg.solve());
        }else if(args.length == 5) {
            desiredAlg = new AlgorithmImpl(args[1],args[3],args[4]);
            System.out.println(desiredAlg.check());
        }else
            throw new InvalidEntryException("Invalid number of arguments!");

    }

}
