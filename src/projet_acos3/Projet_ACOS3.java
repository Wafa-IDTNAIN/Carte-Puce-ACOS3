/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet_acos3;

import javax.smartcardio.*;

/**
 *
 * @author Wafa IDTNAIN et Hafssa HASSY
 */
public class Projet_ACOS3 {

    /**
     * @param args the command line arguments
     */
    private static byte [] APDU_IC_CODE = {(byte) 0x80,0x20,0x07,0x00, 0x08, 0x41, 0x43, 0x4F, 0x53, 0x54, 0x45, 0x53, 0x54};
    private static byte [] APDU_PIN_CODE = {(byte) 0x80,0x20,0x06,0x00, 0x08,0x02, 0x04, 0x06, 0x08, 0x10, 0x12, 0x14, 0x16};
    private static byte [] PIN_CODE = {0x02, 0x04, 0x06, 0x08, 0x10, 0x12, 0x14, 0x16};
    private static String [] info_proprietaire ={"Melle","IDTNAIN HAFSSA","4012001037141112","10-2028"};
    private static  byte [] Signature ={(byte)0x30,(byte)0x4e,(byte)0xcb,(byte)0x5b,(byte)0x1e,(byte)0xd8,(byte)0x9f,(byte)0xbd,0x2e,(byte)0x4e,(byte)0x7c,(byte)0x12,(byte)0xb5,0x23,(byte)0x68,(byte)0x96,(byte)0x4f,(byte)0x97,(byte)0x6c,(byte)0x76,(byte)0xa1,(byte)0x95,(byte)0x38,(byte)0xa2,(byte)0x86,(byte)0xcd,(byte)0xad,(byte)0x5e,(byte)0x88,(byte)0x9a,(byte)0x6e,(byte)0x2a,(byte)0x6b,(byte)0xf0,(byte)0x09,(byte)0x0c,(byte)0x54,(byte)0xe9,(byte)0x9a,(byte)0xac,(byte)0x08,(byte)0xb5,(byte)0x17,(byte)0x72,(byte)0xb5,(byte)0x41,(byte)0xb2,(byte)0xf7,(byte)0xaf,(byte)0x69,(byte)0xf6,(byte)0xeb,(byte)0x5a,(byte)0xda,(byte)0x39,(byte)0x42,(byte)0x18,(byte)0xed,(byte)0x0f,(byte)0xe2,(byte)0x58,(byte)0x73,(byte)0x18,(byte)0x5c }; //512 bits 
    private static String Public_Key="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALlcZOXIMfbuFSSAdWuJpsTwzBNRiLqm4szgg2JNjF70bgt9ZMs/ErdnB8KxdkUi5+PKYZ88OoJopMOblaTIus0CAwEAAQ==" ;
    
    public static void main(String[] args) throws CardException {
        
        TerminalFactory tf =TerminalFactory.getDefault();
        CardTerminals lecteurs = tf.terminals();
        CardTerminal lecteur = lecteurs.getTerminal("Gemalto Prox-DU Contact_12400327 0");
        
        Card card = null;
        System.out.println("Insérer votre carte ACOS3!!");
        
        // attente de la carte
        while(!lecteur.isCardPresent()){
            }
        
        card = lecteur.connect("*");
        if (card !=null){
            System.out.println(" Carte connectée!!");
        }  
         CardChannel canal = card.getBasicChannel();
         
         
        // envoyer le code IC pour effacer la carte
        CommandAPDU SubmitAPDU = new CommandAPDU(APDU_IC_CODE);
        ResponseAPDU rep = canal.transmit(SubmitAPDU);
        
        if (rep.getSW() == 0x9000){
            System.out.println("Ok, IC code verifié !");            
        }
        else
            System.out.println("Erreur, IC code non verifié! ( 0x"+Integer.toHexString(rep.getSW())+")");                     
        
        
        // effacer la carte
        byte[] vect_command = new byte[4];
        vect_command[0] = (byte) 0x80;
        vect_command[1] = 0x30;
        
        SubmitAPDU = new CommandAPDU(vect_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, carte effacée !");            
        }
        else
            System.out.println("Erreur, carte non effacée! ( 0x"+Integer.toHexString(rep.getSW())+")");  
         
         
        // selectionner FF02 pour définir N_OF_FILE
        byte[] select_command = new byte[7];
        select_command[0] = (byte) 0x80;
        select_command[1] = (byte)0xA4;
        select_command[2] = 0x00;
        select_command[3] = 0x00;
        select_command[4] = 0x02;
        select_command[5] = (byte) 0xFF;
        select_command[6] = 0x02;
        SubmitAPDU = new CommandAPDU(select_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, FF02 selectionné !");            
        }
        else
            System.out.println("Erreur, FF02 non selectionné! ( 0x"+Integer.toHexString(rep.getSW())+")"); 
         
         
         //envoyer le IC code pour écrire dans le fichier FF 02
        SubmitAPDU = new CommandAPDU(APDU_IC_CODE);
        rep = canal.transmit(SubmitAPDU);
        
        if (rep.getSW() == 0x9000){
            System.out.println("Ok, IC code verifié !");            
        }
        else
            System.out.println("Erreur, IC code non verifié! ( 0x"+Integer.toHexString(rep.getSW())+")");   
        
        
        // Ecrire dans FF02
        vect_command = new byte[9]; 
        vect_command[0] = (byte) 0x80;
        vect_command[1] = (byte)0xD2;
        vect_command[2] = 0x00; //1er enregistrement
        vect_command[3] = 0x00;
        vect_command[4] = 0x04;
        vect_command[5] = 0x00;
        vect_command[6] = 0x00;
        vect_command[7] = 0x03;   // N_OF_File
        vect_command[8] = 0x00;
        
        SubmitAPDU = new CommandAPDU(vect_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, écriture dans FF02!");            
        }
        else
            System.out.println("Erreur, écriture dans FF02! ( 0x"+Integer.toHexString(rep.getSW())+")"); 
        
        // on reset la carte pour prendre en consideration le nouveau N_OF_File 
        card.disconnect(true);
        card = lecteur.connect("*");    
            if (card !=null){
            System.out.println(" Carte connectée!!");
        }
        canal = card.getBasicChannel();    
        
        // selectionner FF03         
        select_command[5] = (byte)0xFF;
        select_command[6] = 0x03;
        SubmitAPDU = new CommandAPDU(select_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, 0xFF03 selectionné !");            
        }
        else
            System.out.println("Erreur, 0xFF03 non selectionné! ( 0x"+Integer.toHexString(rep.getSW())+")"); 
         
         
         //envoyer le IC code pour écrire dans le fichier FF 03
        SubmitAPDU = new CommandAPDU(APDU_IC_CODE);
        rep = canal.transmit(SubmitAPDU);
        
        if (rep.getSW() == 0x9000){
            System.out.println("Ok, IC code verifié !");            
        }
        else
            System.out.println("Erreur, IC code non verifié! ( 0x"+Integer.toHexString(rep.getSW())+")");      
        
        // Ecrire le code PIN dans FF03
        vect_command = new byte[13]; 
        vect_command[0] = (byte) 0x80;
        vect_command[1] = (byte)0xD2;
        vect_command[2] = 0x01; //2ème enregistrement
        vect_command[3] = 0x00;        
        vect_command[4] = 0x08;
        for (int i=0;i<8;i++){
            vect_command[5+i] = PIN_CODE[i];
        }
        SubmitAPDU = new CommandAPDU(vect_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, code PIN enregistré dans 0xFF03  !");            
        }
        else
            System.out.println("Erreur, code PIN non enregistré dans 0xFF03! ( 0x"+Integer.toHexString(rep.getSW())+")");  
         
         
         
        // selectionner FF04
        select_command[6] = 0x04;
        SubmitAPDU = new CommandAPDU(select_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, FF04 selectionné !");            
        }
        else
            System.out.println("Erreur, FF04 non selectionné! ( 0x"+Integer.toHexString(rep.getSW())+")");     
         
         
         // envoyer le code IC pour écrire dans FF 04
        SubmitAPDU = new CommandAPDU(APDU_IC_CODE);
        rep = canal.transmit(SubmitAPDU);
        
        if (rep.getSW() == 0x9000){
            System.out.println("Ok, IC code verifié !");            
        }
        else
            System.out.println("Erreur, IC code non verifié! ( 0x"+Integer.toHexString(rep.getSW())+")");  
        
        
        // Ecrire dans FF04 pour définir le fichier AA10
        vect_command = new byte[11];   
        vect_command[0] = (byte) 0x80;
        vect_command[1] = (byte)0xD2;
        vect_command[2] = 0x00; //1ér enregistrement
        vect_command[3] = 0x00;
        vect_command[4] = 0x06;
        vect_command[5] = 0x20;  // taille d'enregistrements :32 octets
        vect_command[6] = 0x04;  // 4 enregistrements
        vect_command[7] = 0x00;  // fichier libre en lecture
        vect_command[8] = (byte)0x80; //IC code pour ecriture
        vect_command[9] = (byte)0xAA;  
        vect_command[10] = 0x10;
        
        SubmitAPDU = new CommandAPDU(vect_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, Ecriture dans FF04 pour définir le fichier AA10!");            
        }
        else
            System.out.println("Erreur, Ecriture dans FF04 pour définir le fichier AA10! ( 0x"+Integer.toHexString(rep.getSW())+")");    
         
         

        // Ecriture dans FF04 pour définir le fichier AA11 
        vect_command = new byte[11];   
        vect_command[0] = (byte) 0x80;
        vect_command[1] = (byte)0xD2;
        vect_command[2] = 0x01; //2éme enregistrement
        vect_command[3] = 0x00;
        vect_command[4] = 0x06;
        vect_command[5] = (byte)0x40;  //taille de la signature 64 octets
        vect_command[6] = 0x01;  // 1 enregistrement
        vect_command[7] = 0x00;  // fichier libre en lecture
        vect_command[8] = (byte)0x80; //IC code pour ecriture
        vect_command[9] = (byte)0xAA;  
        vect_command[10] = 0x11;
        
        SubmitAPDU = new CommandAPDU(vect_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, Ecriture dans FF04 pour définir le fichier AA11!");            
        }
        else
            System.out.println("Erreur, Ecriture dans FF04 pour définir le fichier AA11! ( 0x"+Integer.toHexString(rep.getSW())+")"); 
             

         
        // Ecriture dans FF04 pour définir le fichier AA12 
        vect_command = new byte[11];   
        vect_command[0] = (byte) 0x80;
        vect_command[1] = (byte)0xD2;
        vect_command[2] = 0x02; //3éme enregistrement
        vect_command[3] = 0x00;
        vect_command[4] = 0x06;
        vect_command[5] = (byte)0x80;  // taille d'enregistrements :128 octets
        vect_command[6] = 0x01;  // 1 enregistrement
        vect_command[7] = 0x40;  // Code PIN pour lecture
        vect_command[8] = (byte)0x80; //IC code pour ecriture
        vect_command[9] = (byte)0xAA;  
        vect_command[10] = 0x12;
        
        SubmitAPDU = new CommandAPDU(vect_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, Ecriture dans FF04 pour définir le fichier AA12!");            
        }
        else
            System.out.println("Erreur, Ecriture dans FF04 pour définir le fichier AA12! ( 0x"+Integer.toHexString(rep.getSW())+")"); 

         
        // selectionner AA10 pour y écrire les infos du propriétaire
        select_command[5] = (byte)0xAA;
        select_command[6] = 0x10;
        SubmitAPDU = new CommandAPDU(select_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9100){
            System.out.println("Ok, User File 0xAA10 selectionné !");            
        }
        else
            System.out.println("Erreur, 0xAA10 non selectionné! ( 0x"+Integer.toHexString(rep.getSW())+")");          
         
        
               
         
         
        //envoyer le IC Code pour écrire dans le fichier AA10
         SubmitAPDU = new CommandAPDU(APDU_IC_CODE);
        rep = canal.transmit(SubmitAPDU);
        
        if (rep.getSW() == 0x9000){
            System.out.println("Ok, IC code verifié !");            
        }
        else
            System.out.println("Erreur, IC code non verifié! ( 0x"+Integer.toHexString(rep.getSW())+")");         
        
        
        // écriture des 4 records (infos propriétaire) dans AA10
        for(int i=0;i<info_proprietaire.length;i++) {  
            String str = info_proprietaire[i];
            vect_command = new byte[5+str.length()];
            byte[] str_byte = str.getBytes();
            vect_command[0] = (byte) 0x80;
            vect_command[1] = (byte)0xD2; 
            vect_command[2] = (byte)i; 
            vect_command[3] = 0x00;        
            vect_command[4] = (byte) str.length();
            for (int j=0;j<str.length();j++){
            vect_command[5+j] = str_byte[j];
            }
            SubmitAPDU = new CommandAPDU(vect_command);
            rep = canal.transmit(SubmitAPDU);
            if (rep.getSW() == 0x9000){
                System.out.println("Ok, record "+ (i+1) +" enregistré dans le fichier AA10  !");            
            }
            else
                System.out.println("Erreur, en ecriture du record "+ (i+1)+" dans le fichier AA10! ( 0x"+Integer.toHexString(rep.getSW())+")");              
            
        }
        
        
        //Lecture des records écrits dans AA10 pour vérification
        vect_command = new byte[5];
        for(int i=0;i<4;i++) {
            vect_command[0] = (byte) 0x80;
            vect_command[1] = (byte)0xB2;
            vect_command[2] = (byte)i; // enregistrement numéro (i+1)
            vect_command[3] = 0x00;        
            vect_command[4] = (byte) (info_proprietaire[i]).length();  
            SubmitAPDU = new CommandAPDU(vect_command);
            rep = canal.transmit(SubmitAPDU);
             if (rep.getSW() == 0x9000){
                System.out.println("Ok, Lecture du record "+(i+1)+" du fichier 0xAA10 ... !");
                //System.out.print(byteArrayToHexString(rep.getData()));
                System.out.println(":" + new String(rep.getData()));
            }
            else
                System.out.println("Erreur, en lecture du record "+(i+1)+" du fichier 0xAA10! ( 0x"+Integer.toHexString(rep.getSW())+")");      
                      
            
        }
         

        // selectionner AA11 pour y écrire la signature
        select_command[5] = (byte)0xAA;
        select_command[6] = 0x11;
        SubmitAPDU = new CommandAPDU(select_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9101){
            System.out.println("Ok, User File 0xAA11 selectionné !");            
        }
        else
            System.out.println("Erreur, 0xAA11 non selectionné! ( 0x"+Integer.toHexString(rep.getSW())+")");          
         
                         
         
        //envoyer le IC Code pour écrire dans le fichier AA11
         SubmitAPDU = new CommandAPDU(APDU_IC_CODE);
        rep = canal.transmit(SubmitAPDU);
        
        if (rep.getSW() == 0x9000){
            System.out.println("Ok, IC code verifié !");            
        }
        else
            System.out.println("Erreur, IC code non verifié! ( 0x"+Integer.toHexString(rep.getSW())+")");        
        
        
        // écriture de la signature dans AA11
        vect_command = new byte[5+Signature.length];
        vect_command[0] = (byte) 0x80;
        vect_command[1] = (byte)0xD2;
        vect_command[2] = 0x00; //1er enregistrement
        vect_command[3] = 0x00;        
        vect_command[4] = (byte)Signature.length;
        for (int i=0;i<Signature.length;i++){
            vect_command[5+i] = Signature[i];
        }
        SubmitAPDU = new CommandAPDU(vect_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, Ecriture de la signature dans 0xAA11  !");        

        }
        else
            System.out.println("Erreur, Ecriture de la signature dans 0xAA11 ! ( 0x"+Integer.toHexString(rep.getSW())+")");        
        
        
        //Lecture de la signature pour vérification
        vect_command = new byte[5]; 
        vect_command[0] = (byte) 0x80;
        vect_command[1] = (byte)0xB2;
        vect_command[2] = 0x00; //premier enregistrement
        vect_command[3] = 0x00;        
        vect_command[4] = (byte) Signature.length;  
        SubmitAPDU = new CommandAPDU(vect_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, Lecture de la signature dans AA11 ... !");
           //System.out.print(byteArrayToHexString(rep.getData()));
            System.out.println(":" + byteArrayToHexString(rep.getData()));
        }
        else
            System.out.println("Erreur, Lecture de la signature dans AA11! ( 0x"+Integer.toHexString(rep.getSW())+")");        
        
        
             
        
        
        // selectionner AA12 pour y écrire la clé publique
        select_command[5] = (byte)0xAA;
        select_command[6] = 0x12;
        SubmitAPDU = new CommandAPDU(select_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9102){
            System.out.println("Ok, User File 0xAA12 selectionné !");            
        }
        else
            System.out.println("Erreur, 0xAA12 non selectionné! ( 0x"+Integer.toHexString(rep.getSW())+")");          
        
 
         
         
         
         
    
         
         
         
        //envoyer le IC Code pour écrire dans le fichier AA12
         SubmitAPDU = new CommandAPDU(APDU_IC_CODE);
        rep = canal.transmit(SubmitAPDU);
        
        if (rep.getSW() == 0x9000){
            System.out.println("Ok, IC code verifié !");            
        }
        else
            System.out.println("Erreur, IC code non verifié! ( 0x"+Integer.toHexString(rep.getSW())+")");         
         
        
        // écriture de la clé publique dans AA12
        byte[] str_pubKey = Public_Key.getBytes();
        vect_command = new byte[5+Public_Key.length()];
        vect_command[0] = (byte) 0x80;
        vect_command[1] = (byte)0xD2;
        vect_command[2] = 0x00; //1er enregistrement
        vect_command[3] = 0x00;        
        vect_command[4] = (byte)Public_Key.length();
        for (int i=0;i<Public_Key.length();i++){
            vect_command[5+i] = str_pubKey[i];
        }
        SubmitAPDU = new CommandAPDU(vect_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, Ecriture de la clé publique dans 0xAA12  !");            
        }
        else
            System.out.println("Erreur, Ecriture de la clé publique dans 0xAA12 ! ( 0x"+Integer.toHexString(rep.getSW())+")");
         
         
        //envoyer le code PIN pour la lecture de la clé publique 
        SubmitAPDU = new CommandAPDU(APDU_PIN_CODE);
        rep = canal.transmit(SubmitAPDU);
        
        if (rep.getSW() == 0x9000){
            System.out.println("Ok, code PIN verifié !");            
        }
        else
            System.out.println("Erreur, code PIN non verifié! ( 0x"+Integer.toHexString(rep.getSW())+")"); 
         
        //Lecture de la clé publique pour vérification
        vect_command = new byte[5]; 
        vect_command[0] = (byte) 0x80;
        vect_command[1] = (byte)0xB2;
        vect_command[2] = 0x00; //premier enregistrement
        vect_command[3] = 0x00;        
        vect_command[4] = (byte) Public_Key.length();  
        SubmitAPDU = new CommandAPDU(vect_command);
        rep = canal.transmit(SubmitAPDU);
         if (rep.getSW() == 0x9000){
            System.out.println("Ok, Lecture de la clé publique dans AA12 ... !");
           //System.out.print(byteArrayToHexString(rep.getData()));
            System.out.println(":" + new String(rep.getData()));
        }
        else
            System.out.println("Erreur, Lecture de la clé publique dans AA12! ( 0x"+Integer.toHexString(rep.getSW())+")");          
         
         
         
         
         
         
    }
    

    
   
    
    public static String byteArrayToHexString(byte[] b){
        String result = "";
        for ( int i =0; i < b.length ; i ++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100 , 16).substring (1);
        }
        return result ;
    }    
    
    
}




