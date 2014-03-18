/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmanTree;

import java.io.IOException;

import core.BitOutputStream;

/**
 * 
 * @author Steph
 */
public class AHAC extends AHA {

	public AHAC() {

	}
	
	public void encode(char c, BitOutputStream ecriture) throws IOException {
		// BONUX: passer écritude au dessus: loin d'etre évident en pratique
		
		//Si le caractere est déja present
		if (this.charIsPresent(c)) {
			//On ecrit le prends le code du caractere et l'on l'ecrit
			String buff = this.getCodeChar(c);
			this.ecrireBitSetFlux(buff, ecriture);
		} else {
			//Sinon on prends le code du caractere special et on l'écrit
			String buff = this.getCodeSpecialChar();
			this.ecrireBitSetFlux(buff, ecriture);
			//On ecrit le code ascii du nouveau caractere lu
			this.ecrireCharFlux(c, ecriture);
		}
		//On modifie la structure 
		this.modificationAHA(c);
	}

	public void ecrireBitSetFlux(String s, BitOutputStream flux)
			throws IOException {
		//§DISStats.printCharIOC("Ecriture des bits : '" + s + "'");
		//§DISlong tstart = System.currentTimeMillis();
		
		for (int i = 0; i < s.length(); i++) {
			flux.writeBit(Character.getNumericValue(s.charAt(i)));
		}
		//§DISStats.addTimeBinaryWrite(System.currentTimeMillis() - tstart);
	}

	public void ecrireCharFlux(char c, BitOutputStream flux) throws IOException {
		//§DISlong tstart = System.currentTimeMillis();
		
		String buff = Integer.toBinaryString((int) c);
		//§DISStats.printCharIOC("Ecriture du caractère ascii : '" + c + "'");
//		int tmp = 0;
		//Si le code ascii est plus petit que 8 on rajoute des bit 0 jusqu'a 8
		//Sinon on supprime les bits en trop :'( 
		
		//TODO HERE REFACTOR: utiliser plutot un break si 8
		
//		if(buff.length()>8){
//			tmp = 8;
//		}else{
//			tmp = buff.length();
//		}
//		
		int buflen =buff.length();
		for(int j = buflen ; j< 8; j++){ //HERE
			flux.writeBit(0);
		}
		
		//int j =  (buff.length()> 8) ? buff.length() :8;
				
				
		for (int i = 0; (i < buflen) && (i < 8) ; i++) { 
			flux.writeBit(Character.getNumericValue(buff.charAt(i)));
		}
		//§DISStats.addTimeBinaryWrite(System.currentTimeMillis() - tstart);
		
	}
}
