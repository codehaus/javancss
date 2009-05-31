import java.awt.*;
import java.applet.Applet;
import java.io.*;
import java.util.Vector;

class Daten {
    private static    String ae = "�";
    private static    String AE = "A";
    private static    String oe = "�";
    private static    String OE = "�";
    private static    String ue = "�";
    private static    String UE = "�";
    private static    String SS = "�";

    private static int _jahr = 1963;
    private static int _spieltag = 1;
    private static String _sDatei;
    private static int _anzGamer;
    private static String _asGamer[] = new String[4];
    private static String _asSpVerein[] = new String[4];
    private static int _aPlaetze[] = new int[4];

    private static String _asSpieler[] = new String[717];
    private static String _asVerein[]  = new String[154];
    private static String _asTrainer[] = new String[69];

    private static int _aaSpDaten[][] = new int[696][12];
   private static int _aaN[][] = new int[5][65];
   private static int _aT[] = new int[65];
   private static String _asT[] = new String[65];


    private static int _aSk[] = new int[154];

    private static String _asKlasse[] = new String[6];
    private static String _asStatus[] = new String[6];

    private static boolean _lesenDateien () {
        DataInputStream dipDaten;
        //FileInputStream

        try {
            dipDaten = new DataInputStream(
                                new FileInputStream("../dateien/spieler.txt"));
            for(int nr = 0; nr < 696; nr++) {
                _asSpieler[nr] = dipDaten.readLine();
            }
            dipDaten.close();
            dipDaten = null;

            dipDaten = new DataInputStream(
                                new FileInputStream("../dateien/vereine.txt"));
            for(int nr = 0; nr < 154; nr++) {
                _asVerein[nr] = dipDaten.readLine();
            }
            dipDaten.close();
            dipDaten = null;

            dipDaten = new DataInputStream(
                                new FileInputStream("../dateien/vereined.txt"));
            for(int nr = 64; nr < 153; nr++) {
                String sZahl = dipDaten.readLine();
            _aSk[nr] = Integer.parseInt(sZahl);
            }
            dipDaten.close();
            dipDaten = null;

            dipDaten = new DataInputStream(
                                new FileInputStream("../dateien/spielerd.txt"));
            for(int spieler = 0; spieler < 696; spieler++) {
                for(int eigensch = 0; eigensch < 12; eigensch++) {
                    String sZahl = dipDaten.readLine();
               _aaSpDaten[spieler][eigensch] = Integer.parseInt(sZahl);
                }
            }
            dipDaten.close();
            dipDaten = null;

            return(false);
        } catch (Exception e) {
            // Achtung, hier mu� gepanict werden
            return(true);
        }
    }

    public static boolean init() {
        _asKlasse[0] = "Amateur";
        _asKlasse[1] = "Ersatzspieler";
        _asKlasse[2] = "Mitl"+ae+"ufer";
        _asKlasse[3] = "Stammspieler";
        _asKlasse[4] = "Nationalspieler";
        _asKlasse[5] = "Superstar";
        _asStatus[0] = "   ";
        _asStatus[1] = " > ";
        _asStatus[2] = " . ";
        _asStatus[3] = " * ";
        _asStatus[4] = " R ";
        _asStatus[5] = " V ";

        if (_lesenDateien()) {
            // Fehler
            return(true);
        }

        //           frmAltesSpiel.Show 1
       //If g_AltesSpielJa = True Then
      //    Call laden
      //    GoTo Hauptprogramm2
       //End If

         for(int i = 0; i < 64; i++) {
          _aaN[0][i] = i+1;
       }
       for(int gamer = 0; gamer < 4; gamer++) {
            _aPlaetze[gamer] = 10000;
        }

        return(false);
    }

    public static void putDatei(String sDatei) {
//        static boolean bNurEinmal = true;

//        if (bNurEinmal == false) {
            // panic
//            System.out.println("panic in putDatei");
//        }

//        bNurEinmal = false;
        this._sDatei = sDatei;        // bug report
    }

    public static void doPlan() {
      _aT[0] = 1; _aT[1] = 2; _aT[2] = 3; _aT[3] = 4; _aT[4] = 41; _aT[5] = 5;
      _aT[6] = -1;
      _aT[7] = 6; _aT[8] = 7; _aT[9] = 42; _aT[10] = 8; _aT[11] = 51;
      _aT[12] = 9;
      _aT[13] = -2; _aT[14] = 10; _aT[15] = 11; _aT[16] = 43; _aT[17] = 12;
      _aT[18] = 52;
      _aT[19] = 13; _aT[20] = -3; _aT[21] = 14; _aT[22] = 15; _aT[23] = 44;
      _aT[24] = 16;
      _aT[25] = 53; _aT[26] = 17; _aT[27] = -4; _aT[28] = 18; _aT[29] = 19;
      _aT[31] = 20; _aT[32] = 21; _aT[33] = 22; _aT[34] = 23; _aT[35] = 45;
      _aT[36] = 24; _aT[37] = -5; _aT[38] = 25; _aT[39] = 26; _aT[40] = 46;
      _aT[41] = 27; _aT[42] = 54; _aT[43] = 28; _aT[44] = -6; _aT[45] = 29;
      _aT[46] = 30; _aT[47] = 47; _aT[48] = 31; _aT[49] = 55; _aT[50] = 32;
      _aT[51] = 33; _aT[52] = -7; _aT[53] = 34; _aT[54] = 35;
      _aT[55] = 56; _aT[56] = 36; _aT[57] = -8; _aT[58] = 37; _aT[59] = 38;
      _aT[60] = 57;
      _aT[61] = 48; _aT[62] = 39; _aT[63] = 40;
      _asT[1] = " 1. Bundesligaspieltag";
      _asT[2] = " 2. Bundesligaspieltag";
      _asT[5] = " Europapokal:  1. Runde";
      _asT[10] = " Europapokal:  2. Runde";
      _asT[17] = " Europapokal:  3. Runde";
      _asT[24] = " Europapokal:  Achtelfinale";
      _asT[36] = " Europapokal:  Viertelfinale";
      _asT[41] = " Europapokal:  Halbfinale";
      _asT[48] = " Europapokal:  Finale";
      _asT[62] = " Weltpokalfinale";
      _asT[12] = " DFB-Pokal:  1. Runde";
      _asT[19] = " DFB-Pokal:  2. Runde";
      _asT[26] = " DFB-Polal:  3. Runde";
      _asT[43] = " DFB-Pokal:  Achtelfinale";
      _asT[50] = " DFB-Pokal:  Viertelfinale";
      _asT[56] = " DFB-Pokal:  Halbfinale";
      _asT[61] = " DFB-Pokal:  Finale";
      _asT[63] = " 1. Relegationsspiel";
      _asT[64] = " 2. Relegationsspiel";
      _asT[31] = " Winterpause";
      _asT[32] = " 20. Bundesligaspieltag";
      _asT[33] = " 21. Bundelisgaspieltag";
      for(int i = 2; i < 30; i++) {
         if ((_aT[i] > 0) && (_aT[i] < 40)) {
            _asT[i] = "";
            if (_aT[i] < 12) {
               _asT[i] = " ";
            }
            _asT[i] = _asT[i] + (_aT[i] - 2) + ". bzw.";
            if (_aT[i] < 10) {
               _asT[i] = _asT[i] + " ";
            }
            _asT[i] = _asT[i] + _aT[i] + ". Bundesligaspieltag";
         }
      }
      for(int i = 33; i < 60; i++) {
         if ((_aT[i] > 0) && (_aT[i] < 40)) {
            _asT[i] = (_aT[i] - 4) + ". bzw.";
            _asT[i] = _asT[i] + _aT[i] + ". Bundesligaspieltag";
         }
      }
    }

    public static void putAnzGamer(int ANZGAMER) {
        if (!(1 <= ANZGAMER <= 4)) {
            System.out.println("panic in putAnzGamer!");
        }
        _anzGamer = ANZGAMER;
    }

    public static void putNextGamer(String sGamer) {
        //static int counterGamer = 0;

        //if (counterGamer == _anzGamer) {
        //    System.out.println("panic in putNextGamer!");
        //}
        //_asGamer[counterGamer++] = sGamer;
    }

    public static void putVerein(int gamer, int vereinsNr) {
        _asSpVerein[gamer] = _asVerein[vereinsNr];
        _aaN[0][vereinsNr] = 69 - gamer;                // warum dieses?
    }

    public static void putStadionname(int gamer, String sStadion) {
        _asSpVerein[gamer] = _asVerein[vereinsNr];
    }

    public static String[] getVereine() {
        String asTemp[] = (String[])_asVerein.clone();

        return(asTemp);
    }
}

class InputWindow extends Window {
    private Interface _if;
    private TextField _txtInput = new TextField(30);

    InputWindow(Interface ifParent, String sPrint, String sInit) {
        super(ifParent);
        _if = ifParent;

        this.add("West", new Label(sPrint));
        _txtInput.setText(sInit);
        this.add("East", _txtInput);
        this.add("South", new Button("   OK   "));
        this.pack();
    }

    public boolean action(Event evt, Object obj) {
       if (evt.target instanceof Button) {
           _if.putInput(_txtInput.getText());
           dispose();
         }
      return(true);
      }
}

class SelectFromListWindow extends Window {
    private Interface _if;
    private List _lst;

    SelectFromListWindow(Interface ifParent, String sPrint,
        String[] asListItem) {
        super(ifParent);
        _if = ifParent;

        this.add("North", new Label(sPrint));
        _lst = new List(asListItem.length, false);
        for(int zeile = 0; zeile < asListItem.length; zeile++) {
            _lst.addItem(asListItem[zeile]);
        }
        this.add("Center", _lst);
        this.add("South", new Button("   OK   "));
        this.pack();
    }

    public boolean action(Event evt, Object obj) {
       if (evt.target instanceof Button) {
           _if.putInput(_lst.getSelectedIndex());
           dispose();
         }
      return true;
      }
}

class ShowMsgBox extends Window {
    ShowMsgBox(Frame frmParent, String sPrint) {
        super(frmParent);

        this.add("North", new Label(sPrint));
        this.add("South", new Button("   OK   "));
        this.pack();
        this.show();
    }

    public boolean action(Event evt, Object obj) {
       if (evt.target instanceof Button) {
           dispose();
         }
      return(true);
      }
}

class Interface extends Frame {
    //Frame frmSimSoccer;
    //Panel    panHauptmenue;
    private String _sInput;
    private String _Input;

    Interface() {
        super("Rund ist der Ball");

        this.pack();
        this.resize(800, 600);
        this.show();
   }

    public void putInput(String sInputFromWindow) {
        _sInput = sInputFromWindow;
    }

    public void putInput(int InputFromWindow) {
        _Input = InputFromWindow;
    }

    public String input(String sPrint, String sInit) {
      InputWindow winInput = new InputWindow(this, sPrint, sInit);

        winInput.show();        // Seiteneffekt: setzt _sInput

        return(_sInput);
    }

    public int selectFromList(String sPrint, String[] asListItem) {
        SelectFromListWindow winList = new SelectFromListWindow(this, sPrint,
                                                        asListItem);

        winList.show();        // Seiteneffekt: setzt _Input

        return(_Input);
    }
}

public class SimSoccer {
    Interface _ifSimSoccer;

   private boolean _init() {
        if (Daten.init()) {
            return(true);
        }
        _ifSimSoccer = new Interface();

        // altes Spiel?
        // Dateiname erfragen
        Daten.putDatei(_ifSimSoccer.input("Name der Datei: ", ""));
        Daten.doPlan();
        // Anzahl Spieler
      do {
         int anzGamer = Integer.parseInt(
            _ifSimSoccer.input("Anzahl der Mitspieler: ", "1"));
      } while (!(1 <= anzGamer <= 4));
      Daten.putAnzGamer(anzGamer);
      // f�r jeden Spieler
      Vector vBesetzteVer = new Vector(5);
      vBesetzteVer.addElement(new Integer(-1));
      for (int spieler = 0; spieler < anzGamer; spieler++) {
          //     Name eingeben
          String sGamer = _ifSimSoccer.input("Name des"+spieler+". Spielers:",
                                      "");
          Daten.putNextGamer(sGamer);
          //     Verein Ausw�hlen
          do {
              int auswahl = _ifSimSoccer.selectFromList(sGamer+
                                      ", w�hlen Sie bitte Ihren Verein aus!",
                                      Daten.getVereine());
                if (vBesetzteVer.contains(new Integer(auswahl))) {
                    ShowMsgBox smbTemp = new ShowMsgBox(_ifSimSoccer, "Keinen oder schon besetzten Verein ausgew�hlt!");
                }
            } while (vBesetzteVer.contains(new Integer(auswahl)));
            vBesetzteVer.addElement(new Integer(auswahl));
          Daten.putVerein(spieler, auswahl);
            //     Stadionname eingeben
            Daten.putStadionname(spieler, _ifSimSoccer.input(
                "Name des Stadions (Artikel Name Stadion): ", ""));
        }
        //Ausgabe.

        return(false);
   }

   //public void start(){

   //}

    public static void main(String args[]) {
        if (_init()) {
            return;
        }
        //frmSimSoccer.add("Center", panHauptmenue);
        //frmSimSoccer.pack();
    }
}

