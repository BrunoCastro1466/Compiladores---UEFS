/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores.problema;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author bruno
 */
public class analisadorSintatico {
    
    
    LinkedList<String> saidaSintatico = new LinkedList<String>();
    LinkedList<String> dadosLexicos = new LinkedList<String>();
    
    String erro = new String();
    int contadorLista = 0;
    String[] tokenDado;    
    String[] lookAhead1;    
    String[] lookAhead2;
    String[] lookAhead3;
    String[] lookAhead4;
    String[] errorCheck;
    
    public LinkedList analise(LinkedList aLexica){        
    dadosLexicos = aLexica;    
    if(!dadosLexicos.isEmpty()){
    procedimentoINIT();  
    }
    if(!dadosLexicos.isEmpty()){
        System.out.println("FALHA NA VALIDAÇÃO SINTATICA.");
    }
    dadosLexicos.clear();
    return saidaSintatico;
    }

    
    public void procedimentoINIT(){
        
        tokenDado = dadosLexicos.getFirst().split(" ", 3);        
        if(tokenDado[2].trim().matches("procedure")){            
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'procedure'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
        tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().matches("start")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'start'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){            
        tokenDado = dadosLexicos.getFirst().split(" ", 3);       
        }else{
            return;
        }                
        if(tokenDado[2].trim().equals("{")){            
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
           erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '{'" + "\n";
           saidaSintatico.addLast(erro); 
        }
        if(!dadosLexicos.isEmpty()){
        tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoSTART();                
        if(tokenDado[2].trim().matches("}")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '}'" + "\n";
            saidaSintatico.addLast(erro); 
        }
        
    }
    
    public void procedimentoSTART(){
        if(!tokenDado[2].trim().equals("}")){           
            procedimentoPROGRAM();            
            if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }            
            procedimentoSTART();            
        }else{            
            return;
        }
    }
    
    public void procedimentoPROGRAM(){
        procedimentoSTATEMENT();
    }

    public void procedimentoSTATEMENT() {
        if(tokenDado[2].trim().equals("{")){//Verifica se o Statement começa com "{"
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("}")){//Verifica se logo após o "{" há um "}"
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
            }else{//Caso em que o statement começa com "{" mas não é logo seguido de um "}"
                procedimentoSTATEMENTSLIST();                 
                if(tokenDado[2].trim().equals("}")){                                       
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                }else{
                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '}'";
                    saidaSintatico.addLast(erro);
                }                
            }
        }else{//Caso o statement não comece com "{", o caso é o do SIMPLESTATAMENT
            procedimentoSIMPLESTATAMENT();            
        }        
    }

    public void procedimentoSTATEMENTSLIST() {
        if(tokenDado[2].trim().equals("{")){
            
            procedimentoSTATEMENT();
        }else{
            procedimentoSIMPLESTATAMENT();
        }                
    }
    
    public void procedimentoSIMPLESTATAMENT() {        
        if(tokenDado[2].trim().equals("read")){//OK
            procedimentoREADSTATEMENT();
        }else if(tokenDado[2].trim().equals("print")){//OK
            procedimentoPRINTSTATEMENT();
        }else if(tokenDado[2].trim().equals("function")||tokenDado[2].trim().equals("procedure")){//OK                        
            procedimentoDECL();
        }else if(tokenDado[2].trim().equals("typedef")){//OK
            procedimentoSTRUCTDECL();
        }else if(tokenDado[2].trim().equals("var")||tokenDado[2].trim().equals("const")){//OK
            procedimentoVARDECL();
        }else if(tokenDado[2].trim().equals("if")){//OK
            procedimentoIF();
        }else if(tokenDado[2].trim().equals("while")){//OK
            procedimentoWHILE();
        }else if(tokenDado[1].trim().equals("IDE")||tokenDado[2].trim().equals("local")||tokenDado[2].trim().equals("global")){
            if(dadosLexicos.size()>1){//LookAHead1
                lookAhead1 = dadosLexicos.get(1).split(" ", 3);
            }else{
                return;
            }                        
            if(lookAhead1[2].trim().equals("(")){
                procedimentoCALLFUNC();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                if(tokenDado[2].trim().equals(";")){
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                }else{
                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                    saidaSintatico.addLast(erro);
                }
            }else if(tokenDado[2].trim().equals("local")||tokenDado[2].trim().equals("global")||lookAhead1[2].trim().equals(".")||lookAhead1[2].trim().equals("[")||lookAhead1[2].trim().equals(";")||lookAhead1[2].trim().equals("=")){
                procedimentoVARIABLESCOPEANDUSAGE();
            }         
        }else{
          errorCheck = dadosLexicos.getFirst().split(" ", 3);                   
          erro = tokenDado[0] + "OCORREU UM ERRO, A ENTRADA DO PROCEDIMENTO NÃO ERA VALIDA" + "\n";
          saidaSintatico.addLast(erro);
          
          while(!errorCheck[2].trim().equals(";") && !errorCheck[2].trim().equals("}")){
              
              dadosLexicos.removeFirst();
              if(!dadosLexicos.isEmpty()){
              errorCheck = dadosLexicos.getFirst().split(" ", 3);
              }else{
                  break;
              }
          }
          dadosLexicos.removeFirst();
          System.out.println("OCORREU UM ERRO, A ENTRADA DO PROCEDIMENTO NÃO ERA VALIDA");
          if(!dadosLexicos.isEmpty()){
              tokenDado = dadosLexicos.getFirst().split(" ", 3);
              procedimentoSTART();
          }else{
              return;
          }
        }
        
    }
                  
    public void procedimentoREADSTATEMENT(){
        if(tokenDado[2].trim().equals("read")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();            
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'read'" + "\n";
            saidaSintatico.addLast(erro);
        }        
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }        
        if(tokenDado[2].trim().equals("(")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '('" + "\n";
            saidaSintatico.addLast(erro);
        }        
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }        
        procedimentoEXPREAD();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        
        if(tokenDado[2].trim().equals(")")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ')'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals(";")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
            saidaSintatico.addLast(erro);
        }
        
    }
    
    public void procedimentoPRINTSTATEMENT(){
        if(tokenDado[2].trim().equals("print")){            
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'print'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("(")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '('" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoEXPPRINT();        
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }    
        if(tokenDado[2].trim().equals(")")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ')'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals(";")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ');'" + "\n";
            saidaSintatico.addLast(erro);
        }        
    }

    private void procedimentoVARIABLESCOPEANDUSAGE() {
        if(tokenDado[2].trim().equals("local")||tokenDado[2].trim().equals("global")){            
            procedimentoVARIABLESCOPETYPE();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(".")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '.'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[1].trim().equals("IDE")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("=")){               
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                if(tokenDado[1].trim().equals("IDE")){ 
                    if(dadosLexicos.size()>1){
                        lookAhead1 = dadosLexicos.get(1).split(" ", 3);
                    }else{
                        return;
                    }
                    if(dadosLexicos.size()>2){
                        lookAhead2 = dadosLexicos.get(2).split(" ", 3);
                    }
                    if(dadosLexicos.size()>3){
                        lookAhead3 = dadosLexicos.get(3).split(" ", 3);
                    }
                   if(lookAhead1[2].trim().equals(";")){                       
                       saidaSintatico.addLast(dadosLexicos.getFirst());
                       dadosLexicos.removeFirst();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }
                   }else if(lookAhead1[2].trim().equals(".")){
                       procedimentoSTRUCTUSAGE();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                   }else if(lookAhead1[2].trim().equals("[")){
                       procedimentoARRAYUSAGE();
                       if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                   }else if((lookAhead1[2].trim().equals("!")||lookAhead1[1].trim().equals("IDE")||lookAhead1[2].trim().equals("true")||lookAhead1[2].trim().equals("false")||lookAhead1[2].trim().matches("[0-9]+")||lookAhead1[2].trim().matches("[0-9]+[.][0-9]+")||lookAhead1[1].trim().equals("CAD")) && (lookAhead2[2].trim().equals("!")||lookAhead2[1].trim().equals("IDE")||lookAhead2[2].trim().equals("true")||lookAhead2[2].trim().equals("false")||lookAhead2[2].trim().matches("[0-9]+")||lookAhead2[2].trim().matches("[0-9]+[.][0-9]+")||lookAhead2[1].trim().equals("CAD")||lookAhead2[1].trim().equals("LOG")||lookAhead2[1].trim().equals("REL")||lookAhead2[1].trim().equals("ART")) && (lookAhead3[2].trim().equals("!")||lookAhead3[1].trim().equals("IDE")||lookAhead3[2].trim().equals("true")||lookAhead3[2].trim().equals("false")||lookAhead3[2].trim().matches("[0-9]+")||lookAhead3[2].trim().matches("[0-9]+[.][0-9]+")||lookAhead3[1].trim().equals("CAD")||lookAhead3[1].trim().equals("LOG")||lookAhead3[1].trim().equals("REL")||lookAhead3[1].trim().equals("ART"))){
                       procedimentoVARIABLEINIT();
                       if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                       if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                   }else if(lookAhead1[2].trim().equals("(")){
                       procedimentoCALLFUNC();
                       if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                       if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                   }
                }else if(tokenDado[2].trim().equals("local")||tokenDado[2].trim().equals("global")){                    
                    procedimentoVARIABLESCOPETYPE();
                    if(!dadosLexicos.isEmpty()){
                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                    }else{
                        return;
                    }
                    if(tokenDado[2].trim().equals(".")){
                        saidaSintatico.addLast(dadosLexicos.getFirst());
                        dadosLexicos.removeFirst();
                    }else{
                        erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '.'" + "\n";
                        saidaSintatico.addLast(erro);
                    }
                    if(!dadosLexicos.isEmpty()){
                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                    }else{
                        return;
                    }
                    if(dadosLexicos.size()>1){
                        lookAhead1 = dadosLexicos.get(1).split(" ", 3);
                    }else{
                        return;
                    }
                    if(lookAhead1[2].trim().equals(";")){
                        saidaSintatico.addLast(dadosLexicos.getFirst());
                        dadosLexicos.removeFirst();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                    }else if(lookAhead1[2].trim().equals("[")){
                        procedimentoARRAYUSAGE();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                    }
                    
                }else if(tokenDado[2].trim().equals("!") || tokenDado[2].trim().equals("true") || tokenDado[2].trim().equals("false") || tokenDado[2].trim().matches("[0-9]+") || tokenDado[2].trim().matches("[0-9]+[.][0-9]+") || tokenDado[1].trim().equals("CAD")){
                    procedimentoVARIABLEINIT();
                    if(!dadosLexicos.isEmpty()){
                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                    }else{
                        return;
                    }
                    if(tokenDado[2].trim().equals(";")){
                        saidaSintatico.addLast(dadosLexicos.getFirst());
                        dadosLexicos.removeFirst();
                    }else{
                        erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                        saidaSintatico.addLast(erro);
                    }            
                }
            }else if(tokenDado[2].trim().equals("[")){                
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                if(tokenDado[1].trim().equals("IDE")||tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+")||tokenDado[1].trim().equals("CAD")){
                    procedimentoPRIMARY();
                    if(!dadosLexicos.isEmpty()){
                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                    }else{
                        return;
                    }
                    if(tokenDado[2].trim().equals("]")){
                        saidaSintatico.addLast(dadosLexicos.getFirst());
                        dadosLexicos.removeFirst(); 
                    }else{
                       erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ']'" + "\n";
                       saidaSintatico.addLast(erro);
                    }
                    if(!dadosLexicos.isEmpty()){
                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                    }else{
                        return;
                    }
                    if(tokenDado[2].trim().equals("=")){
                        saidaSintatico.addLast(dadosLexicos.getFirst());
                        dadosLexicos.removeFirst();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals("local")||tokenDado[2].trim().equals("global")){
                            procedimentoVARIABLESCOPETYPE();
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals(".")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '.'" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[1].trim().equals("IDE")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals(";")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                        }else if(tokenDado[1].trim().equals("IDE")){
                            if(dadosLexicos.size()>1){
                                lookAhead1 = dadosLexicos.get(1).split(" ", 3);
                            }else{
                                return;
                            }
                            if(lookAhead1[2].trim().equals(";")){
                                procedimentoPRIMARY();
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(tokenDado[2].trim().equals(";")){
                                    saidaSintatico.addLast(dadosLexicos.getFirst());
                                    dadosLexicos.removeFirst();
                                }else{
                                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                    saidaSintatico.addLast(erro);
                                }
                            }else if(lookAhead1[2].trim().equals(".")){
                                procedimentoSTRUCTUSAGE();
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(tokenDado[2].trim().equals(";")){
                                    saidaSintatico.addLast(dadosLexicos.getFirst());
                                    dadosLexicos.removeFirst();
                                }else{
                                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                    saidaSintatico.addLast(erro);
                                }
                            }else if(lookAhead1[2].trim().equals("[")){
                                procedimentoARRAYUSAGE();
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(tokenDado[2].trim().equals(";")){
                                    saidaSintatico.addLast(dadosLexicos.getFirst());
                                    dadosLexicos.removeFirst();
                                }else{
                                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                    saidaSintatico.addLast(erro);
                                }
                            }
                        }else if(tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")||tokenDado[1].trim().equals("CAD")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+")){
                                procedimentoPRIMARY();
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(tokenDado[2].trim().equals(";")){
                                    saidaSintatico.addLast(dadosLexicos.getFirst());
                                    dadosLexicos.removeFirst();
                                }else{
                                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                    saidaSintatico.addLast(erro);
                                }
                        }else if(tokenDado[2].trim().equals("{")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                            if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            procedimentoVARARGS();
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals("}")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM '}'" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals(";")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                        }
                    }else if(tokenDado[2].trim().equals("[")){
                        saidaSintatico.addLast(dadosLexicos.getFirst());
                        dadosLexicos.removeFirst();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[1].trim().equals("IDE")||tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+")||tokenDado[1].trim().equals("CAD")){
                            procedimentoPRIMARY();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE OU UM LITERAL" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals("]")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();  
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ']'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals("=")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals("local")||tokenDado[2].trim().equals("global")){
                                procedimentoVARIABLESCOPETYPE();
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(tokenDado[2].trim().equals(".")){
                                    saidaSintatico.addLast(dadosLexicos.getFirst());
                                    dadosLexicos.removeFirst(); 
                                }else{
                                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '.'" + "\n";
                                    saidaSintatico.addLast(erro);  
                                }
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(tokenDado[1].trim().equals("IDE")){
                                    saidaSintatico.addLast(dadosLexicos.getFirst());
                                    dadosLexicos.removeFirst();
                                }else{
                                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                                    saidaSintatico.addLast(erro);
                                }
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(tokenDado[2].trim().equals(";")){
                                    saidaSintatico.addLast(dadosLexicos.getFirst());
                                    dadosLexicos.removeFirst();
                                }else{
                                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                                    saidaSintatico.addLast(erro);
                                }                            
                            }else if(tokenDado[1].trim().equals("IDE")){
                                if(dadosLexicos.size()>1){
                                    lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(lookAhead1[2].trim().equals(";")){
                                    procedimentoPRIMARY();
                                    if(!dadosLexicos.isEmpty()){
                                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                    }else{
                                        return;
                                    }
                                    if(tokenDado[2].trim().equals(";")){
                                        saidaSintatico.addLast(dadosLexicos.getFirst());
                                        dadosLexicos.removeFirst();
                                    }else{
                                        erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                        saidaSintatico.addLast(erro);
                                    }
                                }else if(lookAhead1[2].trim().equals(".")){
                                    procedimentoSTRUCTUSAGE();
                                    if(!dadosLexicos.isEmpty()){
                                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                    }else{
                                        return;
                                    }
                                    if(tokenDado[2].trim().equals(";")){
                                        saidaSintatico.addLast(dadosLexicos.getFirst());
                                        dadosLexicos.removeFirst();
                                    }else{
                                        erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                        saidaSintatico.addLast(erro);
                                    }
                                }else if(lookAhead1[2].trim().equals("[")){
                                    procedimentoARRAYUSAGE();
                                    if(!dadosLexicos.isEmpty()){
                                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                    }else{
                                        return;
                                    }
                                    if(tokenDado[2].trim().equals(";")){
                                        saidaSintatico.addLast(dadosLexicos.getFirst());
                                        dadosLexicos.removeFirst();
                                    }else{
                                        erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                        saidaSintatico.addLast(erro);
                                    }
                                }
                            }else if(tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")||tokenDado[1].trim().equals("CAD")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+")){
                                procedimentoPRIMARY();
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(tokenDado[2].trim().equals(";")){
                                    saidaSintatico.addLast(dadosLexicos.getFirst());
                                    dadosLexicos.removeFirst();
                                }else{
                                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                    saidaSintatico.addLast(erro);
                                }
                            }else if(tokenDado[2].trim().equals("{")){                                
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                procedimentoVARARGS();
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(tokenDado[2].trim().equals("}")){
                                    saidaSintatico.addLast(dadosLexicos.getFirst());
                                    dadosLexicos.removeFirst(); 
                                }else{
                                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM '}'" + "\n";
                                    saidaSintatico.addLast(erro);
                                }
                                if(!dadosLexicos.isEmpty()){
                                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                                }else{
                                    return;
                                }
                                if(tokenDado[2].trim().equals(";")){
                                    saidaSintatico.addLast(dadosLexicos.getFirst());
                                    dadosLexicos.removeFirst(); 
                                }else{
                                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ';'" + "\n";
                                    saidaSintatico.addLast(erro);
                                }
                            }
                        }else if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '=' OU ';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }                                                
                    }else if(tokenDado[2].trim().equals(";")){
                        saidaSintatico.addLast(dadosLexicos.getFirst());
                        dadosLexicos.removeFirst();
                    }else{
                        erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '=' OU '[' OU ';'" + "\n";
                        saidaSintatico.addLast(erro);
                    }
                }
                
            }else if(tokenDado[2].trim().equals(".")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                if(tokenDado[1].trim().equals("IDE")){
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                }else{
                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                    saidaSintatico.addLast(erro);
                }
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                if(tokenDado[2].trim().equals("=")){
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                    if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                    }else{
                        return;
                    }
                    if(tokenDado[1].trim().equals("IDE")){                        
                        if(dadosLexicos.size()>1){
                            lookAhead1 = dadosLexicos.get(1).split(" ", 3);
                        }else{
                            return;
                        }
                        if(dadosLexicos.size()>2){
                            lookAhead2 = dadosLexicos.get(2).split(" ", 3);
                        }else{
                            return;
                        }
                        if(dadosLexicos.size()>3){
                            lookAhead3 = dadosLexicos.get(3).split(" ", 3);
                        }else{
                            return;
                        }
                        if(lookAhead1[2].trim().equals(".")){
                            procedimentoSTRUCTUSAGE();
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals(";")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                        }else if(lookAhead1[2].trim().equals("[")){
                            procedimentoARRAYUSAGE();
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals(";")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                        }else if(lookAhead1[2].trim().equals("(")){
                            procedimentoCALLFUNC();
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals(";")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                                saidaSintatico.addLast(erro);
                            }                        
                        }else if((lookAhead1[2].trim().equals("!")||lookAhead1[1].trim().equals("IDE")||lookAhead1[2].trim().equals("true")||lookAhead1[2].trim().equals("false")||lookAhead1[2].trim().matches("[0-9]+")||lookAhead1[2].trim().matches("[0-9]+[.][0-9]+")||lookAhead1[1].trim().equals("CAD")) && (lookAhead2[2].trim().equals("!")||lookAhead2[1].trim().equals("IDE")||lookAhead2[2].trim().equals("true")||lookAhead2[2].trim().equals("false")||lookAhead2[2].trim().matches("[0-9]+")||lookAhead2[2].trim().matches("[0-9]+[.][0-9]+")||lookAhead2[1].trim().equals("CAD")||lookAhead2[1].trim().equals("LOG")||lookAhead2[1].trim().equals("REL")||lookAhead2[1].trim().equals("ART")) && (lookAhead3[2].trim().equals("!")||lookAhead3[1].trim().equals("IDE")||lookAhead3[2].trim().equals("true")||lookAhead3[2].trim().equals("false")||lookAhead3[2].trim().matches("[0-9]+")||lookAhead3[2].trim().matches("[0-9]+[.][0-9]+")||lookAhead3[1].trim().equals("CAD")||lookAhead3[1].trim().equals("LOG")||lookAhead3[1].trim().equals("REL")||lookAhead3[1].trim().equals("ART"))){
                            procedimentoVARIABLEINIT();
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals(";")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                        }                                              
                    }else if(tokenDado[2].trim().equals("!") || tokenDado[2].trim().equals("true") || tokenDado[2].trim().equals("false") || tokenDado[2].trim().matches("[0-9]+") || tokenDado[2].trim().matches("[0-9]+[.][0-9]+") || tokenDado[1].trim().equals("CAD")){
                        procedimentoVARIABLEINIT();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }                        
                    }                    
                }else if(tokenDado[2].trim().equals(";")){
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                }                                
            }else if(tokenDado[2].trim().equals(";")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '=' OU '[' OU '.' OU ';'" + "\n";
                saidaSintatico.addLast(erro);
            }
        }else if(tokenDado[1].trim().equals("IDE")){//OS CASOS DE VARIABLE SCOPE QUE NÂO COMEÇAM COM VARIABLESCOPETYPE
            if(dadosLexicos.size()>1){
                lookAhead1 = dadosLexicos.get(1).split(" ", 3);               
            }else{
                return;
            }
            if(lookAhead1[2].trim().equals(".")){
                procedimentoSTRUCTUSAGE();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);               
                }else{
                    return;
                }
                if(tokenDado[2].trim().equals("=")){    
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                    if(!dadosLexicos.isEmpty()){
                        tokenDado = dadosLexicos.getFirst().split(" ", 3);               
                    }else{
                        return;
                    }
                    if(dadosLexicos.size()>1){
                        lookAhead1 = dadosLexicos.get(1).split(" ", 3);               
                    }else{
                        return;
                    }
                    if(dadosLexicos.size()>2){
                        lookAhead2 = dadosLexicos.get(2).split(" ", 3);               
                    }else{
                        return;
                    }                    
                    if(tokenDado[2].trim().equals("local")||tokenDado[2].trim().equals("global")){
                        procedimentoVARIABLESCOPETYPE();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);               
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(".")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '.'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);               
                        }else{
                            return;
                        }
                        if(tokenDado[1].trim().equals("IDE")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);               
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                    }else if(tokenDado[1].trim().equals("IDE") && lookAhead1[2].trim().equals(".")){
                        procedimentoSTRUCTUSAGE();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);               
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                    }else if(tokenDado[1].trim().equals("IDE") && lookAhead1[2].trim().equals("[")){
                        procedimentoARRAYUSAGE();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);               
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                    }else if(tokenDado[1].trim().equals("IDE")||tokenDado[2].trim().equals("!")||tokenDado[1].trim().equals("CAD")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+") && (lookAhead1[2].trim().equals("!")||lookAhead1[2].trim().equals("(")||lookAhead1[1].trim().equals("IDE")||lookAhead1[2].trim().matches("[0-9]+")||lookAhead1[2].trim().matches("[0-9]+[.][0-9]+")||lookAhead1[1].trim().equals("CAD")||lookAhead1[1].trim().equals("LOG")||lookAhead1[1].trim().equals("REL")||lookAhead1[1].trim().equals("ART")) && (lookAhead2[2].trim().equals("!")||lookAhead2[2].trim().equals("(")||lookAhead2[1].trim().equals("IDE")||lookAhead2[2].trim().matches("[0-9]+")||lookAhead2[2].trim().matches("[0-9]+[.][0-9]+")||lookAhead2[1].trim().equals("CAD")||lookAhead2[1].trim().equals("LOG")||lookAhead2[1].trim().equals("REL")||lookAhead2[1].trim().equals("ART")) ){
                        procedimentoVARIABLEINIT();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);               
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                    }else if(tokenDado[1].trim().equals("IDE") && lookAhead1[2].trim().equals("(")){
                        procedimentoCALLFUNC();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);               
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                    }                                                                                                                      
                }else if(tokenDado[2].trim().equals(";")){
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                }                                                                
            }else if(lookAhead1[2].trim().equals("[")){
                if(dadosLexicos.size()>4){
                    lookAhead1 = dadosLexicos.get(4).split(" ", 3);
                }else{
                    return;
                }
                if(dadosLexicos.size()>7){
                    lookAhead2 = dadosLexicos.get(7).split(" ", 3);
                }else{
                    return;
                }
                if(dadosLexicos.size()>8){
                    lookAhead3 = dadosLexicos.get(8).split(" ", 3);
                }else{
                    return;
                }
                if((lookAhead1[2].trim().equals("=") || lookAhead2[2].trim().equals("=")) && !lookAhead3[2].trim().equals("{")){
                    procedimentoARRAYUSAGE();
                    if(!dadosLexicos.isEmpty()){
                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                    }else{
                        return;
                    }
                    if(tokenDado[2].trim().equals("=")){
                        saidaSintatico.addLast(dadosLexicos.getFirst());
                        dadosLexicos.removeFirst();
                    }
                    if(!dadosLexicos.isEmpty()){
                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                    }else{
                        return;
                    }
                    if(dadosLexicos.size()>1){
                        lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
                    }else{
                        return;
                    }
                    if(tokenDado[2].trim().equals("local") || tokenDado[2].trim().equals("global")){
                        procedimentoVARIABLESCOPETYPE();
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(".")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '.'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[1].trim().equals("IDE")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                        if(!dadosLexicos.isEmpty()){
                            tokenDado = dadosLexicos.getFirst().split(" ", 3);
                        }else{
                            return;
                        }
                        if(tokenDado[2].trim().equals(";")){
                            saidaSintatico.addLast(dadosLexicos.getFirst());
                            dadosLexicos.removeFirst();
                        }else{
                            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                            saidaSintatico.addLast(erro);
                        }
                        
                    }else if(tokenDado[1].trim().equals("IDE")){
                        if(lookAhead1[2].trim().equals(";")){
                            procedimentoPRIMARY();
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals(";")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                        }else if(lookAhead1[2].trim().equals(".")){
                            procedimentoSTRUCTUSAGE();
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals(";")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                        }else if(lookAhead1[2].trim().equals("[")){
                            procedimentoARRAYUSAGE();
                            if(!dadosLexicos.isEmpty()){
                                tokenDado = dadosLexicos.getFirst().split(" ", 3);
                            }else{
                                return;
                            }
                            if(tokenDado[2].trim().equals(";")){
                                saidaSintatico.addLast(dadosLexicos.getFirst());
                                dadosLexicos.removeFirst();
                            }else{
                                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                                saidaSintatico.addLast(erro);
                            }
                        }
                    }                   
                }else{
                    procedimentoVARIABLEDECLARATOR();
                    if(!dadosLexicos.isEmpty()){
                        tokenDado = dadosLexicos.getFirst().split(" ", 3);
                    }else{
                        return;
                    }
                    if(tokenDado[2].trim().equals(";")){
                        saidaSintatico.addLast(dadosLexicos.getFirst());
                        dadosLexicos.removeFirst();
                    }else{
                        erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                        saidaSintatico.addLast(erro);
                    }
                }
                
            }
        }
    }

    public void procedimentoDECL() {
         if(tokenDado[2].trim().equals("function")){
             procedimentoFUNCTIONDECL();
         }else if(tokenDado[2].trim().equals("procedure")){
             procedimentoPROCEDUREDECL();
         }else{
             erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'function' ou 'procedure'" + "\n";
             saidaSintatico.addLast(erro);
         }
    }
    
    public void procedimentoPROCEDUREDECL(){
        if(tokenDado[2].trim().equals("procedure")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'procedure'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[1].trim().equals("IDE")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA um 'IDE'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("(")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '('" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoPARAMS();
        if(tokenDado[2].trim().equals(")")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ')'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoBLOCKPROC();
            
    }
    
    public void procedimentoBLOCKPROC(){
        if(tokenDado[2].trim().equals("{")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '{'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoSTATEMENTSLIST();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("}")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '}'" + "\n";
            saidaSintatico.addLast(erro);
        }
        
    }
    
    public void procedimentoFUNCTIONDECL(){
        if(tokenDado[2].trim().equals("function")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'function'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoTYPE();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }        
        if(tokenDado[1].trim().equals("IDE")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM 'IDE'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("(")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '('" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoPARAMS();
        if(tokenDado[2].trim().equals(")")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ')'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoBLOCKFUNC();
    }
    
    public void procedimentoPARAMS(){
        if(tokenDado[2].trim().equals(")")){
            return;
        }else if(tokenDado[2].trim().equals("boolean")||tokenDado[2].trim().equals("string")||tokenDado[2].trim().equals("int")||tokenDado[2].trim().equals("real")){
            procedimentoTYPE();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[1].trim().equals("IDE")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM 'IDE'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(",")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                procedimentoPARAMS();
            }else if(tokenDado[2].trim().equals(")")){
                return;
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ')' ou ','" + "\n";
                saidaSintatico.addLast(erro);
            }            
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ')' OU 'boolean' OU 'string' OU 'INT' OU 'REAL'" + "\n";
            saidaSintatico.addLast(erro);               
        }
    }
    
    public void procedimentoBLOCKFUNC(){
        if(tokenDado[2].trim().equals("{")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '{'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoSTATEMENTSLIST();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoRETURN();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("}")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '}'" + "\n";
            saidaSintatico.addLast(erro);
        }        
    }
    
    public void procedimentoRETURN(){
        if(tokenDado[2].trim().equals("return")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'return'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals(";")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else if(tokenDado[1].trim().equals("IDE")){
            if(!dadosLexicos.isEmpty()){
                lookAhead1 = dadosLexicos.get(1).split(" ", 3);
            }else{
                return;
            }
            if(lookAhead1[2].trim().equals(";")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else if(lookAhead1[2].trim().equals("(")){
                procedimentoCALLFUNC();
                
            }else{
                erro = lookAhead1[0] + "ERRO SINTATICO, ESPERAVA ';' OU '('" + "\n";
                saidaSintatico.addLast(erro);
            }
        }else if(tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(";")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                saidaSintatico.addLast(erro);
            }
        }else if(tokenDado[2].trim().matches("[0-9]+")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(";")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst(); 
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                saidaSintatico.addLast(erro);
            }
        }else if(tokenDado[2].trim().matches("[0-9]+[.][0-9]+")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(";")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst(); 
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                saidaSintatico.addLast(erro);
            }            
        }else if(tokenDado[1].trim().equals("CAD")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(";")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst(); 
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                saidaSintatico.addLast(erro);
            }            
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';' OU 'IDE' OU 'true' OU 'false' OU 'DecimalLiteral' OU 'RealLiteral' OU 'StringLiteral'" + "\n";
            saidaSintatico.addLast(erro);
        } 
    }
    
    public void procedimentoTYPE(){
        if(tokenDado[2].trim().equals("boolean")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else if(tokenDado[2].trim().equals("string")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else if(tokenDado[2].trim().equals("int")||tokenDado[2].trim().equals("real")){
            procedimentoSCALAR();
        }else{            
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'boolean' ou 'string' ou 'int' ou 'real'" + "\n";
            saidaSintatico.addLast(erro);
        }        
    }

    public void procedimentoSCALAR(){
        if(tokenDado[2].trim().equals("int")||tokenDado[2].trim().equals("real")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'int' ou 'real'" + "\n";
            saidaSintatico.addLast(erro);
        }
    }
    
    public void procedimentoSTRUCTDECL() {
        if(tokenDado[2].trim().equals("typedef")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'typedef'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("struct")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'struct'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("{")){            
            if(tokenDado[2].trim().equals("{")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();           
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '{'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoSTRUCTDEF();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("}")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '}'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[1].trim().equals("IDE")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA um IDE" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(";")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                saidaSintatico.addLast(erro);
            }
        }else if(tokenDado[2].trim().equals("extends")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[1].trim().equals("IDE")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("{")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '{'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoSTRUCTDEF();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("}")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '}'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[1].trim().equals("IDE")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(";")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
                saidaSintatico.addLast(erro);
            }
        }
    }
    
    public void procedimentoSTRUCTDEF(){
        procedimentoVARDECL();
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(lookAhead1[2].trim().equals("var")||lookAhead1[2].trim().equals("const")){
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoSTRUCTDEF();
        }
        
    }
    
    public void procedimentoVARDECL() {
        if(tokenDado[2].trim().equals("var")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("{")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '{'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoTYPEDVARIABLE();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("}")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '}'" + "\n";
                saidaSintatico.addLast(erro);
            }            
        }else if(tokenDado[2].trim().equals("const")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("{")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '{'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoTYPEDCONST();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("}")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '}'" + "\n";
                saidaSintatico.addLast(erro);
            }           
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'var' ou 'const'" + "\n";
            saidaSintatico.addLast(erro);
        }
    }
    
    public void procedimentoTYPEDVARIABLE(){
        procedimentoTYPE();        
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoVARIABLES();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals(";")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(lookAhead1[2].trim().equals("boolean")||lookAhead1[2].trim().equals("int")||lookAhead1[2].trim().equals("real")||lookAhead1[2].trim().equals("string")){
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoTYPEDVARIABLE();
        }
    }
    
    public void procedimentoTYPEDCONST(){
        procedimentoTYPE();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoCONSTANTS();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals(";")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ';'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(lookAhead1[2].trim().equals("boolean")||lookAhead1[2].trim().equals("string")||lookAhead1[2].trim().equals("int")||lookAhead1[2].trim().equals("real")){
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoTYPEDCONST();            
        }        
    }
    
    public void procedimentoCONSTANTS(){
        procedimentoCONSTANTDECLARATOR();
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(lookAhead1[2].trim().equals(",")){
           if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
           }else{
                return;
           }
           if(tokenDado[2].trim().equals(",")){
               saidaSintatico.addLast(dadosLexicos.getFirst());
               dadosLexicos.removeFirst();
           }else{
               erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ','" + "\n";
               saidaSintatico.addLast(erro);
           }
           if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
           }else{
                return;
           }
           procedimentoCONSTANTS();           
        }
    }
    
    public void procedimentoCONSTANTDECLARATOR(){
        if(tokenDado[1].trim().equals("IDE")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
            saidaSintatico.addLast(dadosLexicos.getFirst());
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("=")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM '='" + "\n";
            saidaSintatico.addLast(dadosLexicos.getFirst());
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+")||tokenDado[1].trim().equals("CAD")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM 'true' OU 'false' OU UM DecimalLiteral OU UM RealLiteral OU UM STRINGLITERAL" + "\n";
            saidaSintatico.addLast(dadosLexicos.getFirst());
        }        
    }
    
    public void procedimentoVARIABLES(){
        procedimentoVARIABLEDECLARATOR();
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(lookAhead1[2].trim().equals(",")){
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(",")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ','" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoVARIABLES();
        }
        
    }
    
    public void procedimentoVARIABLEDECLARATOR(){
        lookAhead1 = dadosLexicos.get(1).split(" ", 3);
        if(lookAhead1[2].trim().equals(",")||lookAhead1[2].trim().equals(";")){
            if(tokenDado[1].trim().equals("IDE")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                saidaSintatico.addLast(erro);
            }
        }else if(lookAhead1[2].trim().equals("=")){
            if(tokenDado[1].trim().equals("IDE")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("=")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '='" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(!dadosLexicos.isEmpty()){
                lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(dadosLexicos.size()>2){
                lookAhead2 = dadosLexicos.get(1).split(" ", 3);
            }else{
                return;
            }
            
            if(lookAhead1[2].trim().equals("!")||lookAhead1[2].trim().equals("(")||lookAhead1[2].trim().equals("true")||lookAhead1[2].trim().equals("false")||lookAhead1[2].trim().matches("[0-9]+")||lookAhead1[2].trim().matches("[0-9]+[.][0-9]+")||lookAhead1[1].trim().equals("CAD")||(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals("*"))||(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals("/"))||(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals("<"))||(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals(">"))||(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals("+"))||(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals("-"))||(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals("="))||(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals("!"))||(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals("&"))||(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals("|"))){//DETERMINAR VARIABLE INIT
                procedimentoVARIABLEINIT();
            }else if(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals("(")){//DETERMINAR CALL FUNC
                procedimentoCALLFUNC();
            }else if(lookAhead1[1].trim().equals("IDE") && lookAhead2[2].trim().equals(".")){//DETERMINAR STRUCT USAGE
                procedimentoSTRUCTUSAGE();
            }else if(lookAhead1[2].trim().equals("local")||lookAhead1[2].trim().equals("global")){//DETERMINAR VARIABLE SCOPE TYPE
                procedimentoVARIABLESCOPETYPE();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                if(tokenDado[2].trim().equals(".")){
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                }else{
                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '.'" + "\n";
                    saidaSintatico.addLast(erro);
                }
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                if(tokenDado[1].trim().equals("IDE")){
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                }else{
                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE" + "\n";
                    saidaSintatico.addLast(erro);
                }
            }
            
        }else if(lookAhead1[2].trim().equals("[")){
            procedimentoARRAYUSAGE();
            if(!dadosLexicos.isEmpty()){
                lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(lookAhead1[2].trim().equals("=")){
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                if(tokenDado[2].trim().equals("=")){
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                }else{
                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '='" + "\n";
                    saidaSintatico.addLast(erro);
                }
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                if(tokenDado[2].trim().equals("{")){
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                }else{
                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '{'" + "\n";
                    saidaSintatico.addLast(erro);
                }
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                procedimentoVARARGS();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                if(tokenDado[2].trim().equals("}")){
                    saidaSintatico.addLast(dadosLexicos.getFirst());
                    dadosLexicos.removeFirst();
                }else{
                    erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '}'" + "\n";
                    saidaSintatico.addLast(erro);
                }
            }else if(lookAhead1[2].trim().equals(",")||lookAhead1[2].trim().equals(";")){
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                return;
            }
        }
    }
    
    public void procedimentoVARIABLEINIT(){
        procedimentoEXPRESSION();
    }
    
    public void procedimentoEXPRESSION(){
        procedimentoOREXPRESSION();
    }
    
    public void procedimentoOREXPRESSION(){
        procedimentoANDEXPRESSION();
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }  
        if(lookAhead1[2].trim().equals("||")){
           if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
           if(tokenDado[2].trim().equals("||")){
               saidaSintatico.addLast(dadosLexicos.getFirst());
               dadosLexicos.removeFirst();
           }else{
               erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '|'" + "\n";
               saidaSintatico.addLast(erro);
           }
           if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }          
           procedimentoOREXPRESSION();           
        }
    }
    
    public void procedimentoANDEXPRESSION(){
        procedimentoEQUALITYEXPRESSION();
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }        
        if(lookAhead1[2].trim().equals("&&")){
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("&&")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '&'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }            
            procedimentoANDEXPRESSION();                        
        }
    }
    
    public void procedimentoEQUALITYEXPRESSION(){
        procedimentoCOMPAREEXPRESSION();
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }             
        if(lookAhead1[2].trim().equals("==")||lookAhead1[2].trim().equals("!=")){
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }  
            if(tokenDado[2].trim().equals("==")||tokenDado[2].trim().equals("!=")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '=' OU '!'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }                        
            procedimentoEQUALITYEXPRESSION();            
        }
    }
    
    public void procedimentoCOMPAREEXPRESSION(){
        procedimentoADDEXPRESSION();
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }               
        if(lookAhead1[2].trim().equals("<")||lookAhead1[2].trim().equals(">")){
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("<")||tokenDado[2].trim().equals(">")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '<' OU '>'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoCOMPAREEXPRESSION();                       
        }else if(lookAhead1[2].trim().equals("<=")||lookAhead1[2].trim().equals(">=")){
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("<=")||tokenDado[2].trim().equals(">=")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '<' OU '>'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoCOMPAREEXPRESSION();
        }       
    }
    
    public void procedimentoADDEXPRESSION(){
        procedimentoMULTIPLICATIONEXPRESSION();
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(lookAhead1[2].trim().equals("+")||lookAhead1[2].trim().equals("-")){
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("+")||tokenDado[2].trim().equals("-")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                procedimentoADDEXPRESSION();
            }
        }
    }
    
    public void procedimentoMULTIPLICATIONEXPRESSION(){
        procedimentoUNARYEXPRESSION();
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(lookAhead1[2].trim().equals("*")||lookAhead1[2].trim().equals("/")){
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("*")||tokenDado[2].trim().equals("/")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '*' OU '/'" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoMULTIPLICATIONEXPRESSION();            
        }
    }
    
    public void procedimentoUNARYEXPRESSION(){
        if(tokenDado[2].trim().equals("!")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoUNARYEXPRESSION();
        }else if(tokenDado[1].trim().equals("IDE")||tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+")||tokenDado[1].trim().equals("CAD")||tokenDado[2].trim().equals("(")){
            procedimentoOBJECTEXPRESSION();
        }
    }
    
    public void procedimentoOBJECTEXPRESSION(){
        procedimentoMETHODEXPRESSION();
    }
    
    public void procedimentoMETHODEXPRESSION(){
        procedimentoPRIMARYEXPRESSION();
    }
    
    public void procedimentoPRIMARYEXPRESSION(){
        if(tokenDado[1].trim().equals("IDE")||tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+")||tokenDado[1].trim().equals("CAD")){
            procedimentoPRIMARY();
        }else if(tokenDado[2].trim().equals("(")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoEXPRESSION();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(")")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ')'" + "\n";
                saidaSintatico.addLast(erro);
            }
            
        }
    }
    
    public void procedimentoVARIABLESCOPETYPE(){
        if(tokenDado[2].trim().equals("local")||tokenDado[2].trim().equals("global")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'local' OU 'global'" + "\n";
            saidaSintatico.addLast(erro);
        }
    }
    
    public void procedimentoVARARGS(){        
        if(tokenDado[2].trim().equals("}")){
            return;
        }else if(tokenDado[1].trim().equals("IDE")||tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+")||tokenDado[1].trim().equals("CAD")){
            procedimentoVARARG();
            if(!dadosLexicos.isEmpty()){
                lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(lookAhead1[2].trim().equals(",")){
               if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
               }else{
                    return;
               }
               if(tokenDado[2].trim().equals(",")){
                   saidaSintatico.addLast(dadosLexicos.getFirst());
                   dadosLexicos.removeFirst();
               }else{
                   erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ','" + "\n";
                   saidaSintatico.addLast(erro);
               }
               if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
               }else{
                    return;
               }
               procedimentoVARARGS();
            }            
        }        
    }
    
    public void procedimentoVARARG(){
        if(tokenDado[1].trim().equals("IDE")||tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+")||tokenDado[1].trim().equals("CAD")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDE OU UM LITERAL" + "\n";
            saidaSintatico.addLast(erro);
        }   
    }


    public void procedimentoCALLFUNC() {
        if(tokenDado[1].trim().equals("IDE")){  
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'IDE'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("(")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '('" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        procedimentoARGS();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);
        }else{
            return;
        }
        if(tokenDado[2].trim().equals(")")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ')'" + "\n";
        }
    }
    
    public void procedimentoARGS(){
        if(tokenDado[2].trim().equals(")")){
            return;
        }else if(tokenDado[1].trim().equals("IDE")||tokenDado[2].trim().equals("true")||tokenDado[2].trim().equals("false")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9]+[.][0-9]+")||tokenDado[1].trim().equals("CAD")){
            procedimentoARG();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            if(tokenDado[2].trim().equals(",")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                procedimentoARGS();
            }
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'IDE' OU 'true' OU 'false' OU DecimalLiteral OU RealLiteral OU StringLiteral" + "\n";
            saidaSintatico.addLast(erro);
        }
    }
    
    public void procedimentoARG(){
        if(tokenDado[1].trim().equals("IDE")){ 
            if(!dadosLexicos.isEmpty()){
                lookAhead1 = dadosLexicos.get(1).split(" ", 3);
            }else{
                return;
            }
            if(lookAhead1[2].trim().equals("(")){
                procedimentoCALLFUNC();
            }else{
                procedimentoPRIMARY();
            }
        }else{
            procedimentoPRIMARY();
        }
    }
    
    public void procedimentoIF() {
        if(tokenDado[2].trim().equals("if")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM 'if'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("(")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM '('" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
        }else{
            return;
        }
        procedimentoEXPRESSION();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
        }else{
            return;
        }
        if(tokenDado[2].trim().equals(")")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM ')'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
        }else{
            return;
        }
        procedimentoSTATEMENT();
        if(!dadosLexicos.isEmpty()){
            lookAhead1 = dadosLexicos.getFirst().split(" ", 3);                    
        }else{
            return;
        }
        if(lookAhead1[2].trim().equals("else")){
           if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
           }else{
                return;
           }
           if(tokenDado[2].trim().equals("else")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            }else{
               erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'else'" + "\n";
               saidaSintatico.addLast(erro);
           }
           if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
           }else{
                return;
           }
           procedimentoSTATEMENT();           
        }
        
    }
    
    public void procedimentoWHILE() {
        if(tokenDado[2].trim().equals("while")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'while'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
        }else{
            return;
        }
        if(tokenDado[2].trim().equals("(")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '('" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
        }else{
            return;
        }
        procedimentoEXPRESSION();
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
        }else{
            return;
        }
        if(tokenDado[2].trim().equals(")")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ')'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
        }else{
            return;
        }
        procedimentoSTATEMENT();     
    }

    private void procedimentoEXPREAD() {        
        if(tokenDado[1].trim().equals("IDE")){
            lookAhead1 = dadosLexicos.get(1).split(" ", 3);
            if(lookAhead1[2].trim().equals(",")){//Identifier -> More Readings
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
                }else{
                    return;
                }
                procedimentoMOREREADINGS();
            }else if(lookAhead1[2].trim().equals("[")){//Array Usage
                procedimentoARRAYUSAGE();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);                   
                }else{
                    return;
                }
                procedimentoMOREREADINGS();
            }else if(lookAhead1[2].trim().equals(".")){//Struct Usage
                procedimentoSTRUCTUSAGE();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
                }else{
                    return;
                }
                procedimentoMOREREADINGS();
            }else{
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                return;
            }            
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDENTIFICADOR" + "\n";
            saidaSintatico.addLast(erro);
        }
    }

    private void procedimentoMOREREADINGS() {
        if(tokenDado[2].trim().matches(",")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3); 
                }else{
                    return;
                }
            procedimentoEXPREAD();
        }
    }

    private void procedimentoARRAYUSAGE() {
        if(tokenDado[1].trim().matches("IDE")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();            
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDENTIFICADOR" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3); 
            }else{
                return;
            }
        if(tokenDado[2].trim().equals("[")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst(); 
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '['" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3); 
            }else{
                return;
            }
        procedimentoPRIMARY();   
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3); 
            }else{
                return;
            }        
        if(tokenDado[2].trim().equals("]")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();            
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ']'" + "\n";
            saidaSintatico.addLast(erro);
        }   
        lookAhead1 = dadosLexicos.getFirst().split(" ", 3);
        if(lookAhead1[2].trim().equals("[")){
            if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3);           
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("[")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[1] + "ERRO SINTATICO, ESPERAVA '['" + "\n";
                saidaSintatico.addLast(erro);
            }
            if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3); 
            }else{
                return;
            }
            procedimentoPRIMARY();
            if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3); 
            }else{
                return;
            }
            if(tokenDado[2].trim().equals("]")){
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
            }else{
                erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA ']'" + "\n";
                saidaSintatico.addLast(erro);
            }            
        }
        
    }
    
    public void procedimentoPRIMARY(){
        if(tokenDado[1].trim().matches("IDE")||tokenDado[2].trim().matches("true")||tokenDado[2].trim().matches("false")||tokenDado[2].trim().matches("[0-9]+")||tokenDado[2].trim().matches("[0-9][.][0-9]+")||tokenDado[2].trim().matches("([a-z]|[A-Z]|[0-9]|[ ]|[!]|[#]|[$]|[%]|[&]|[']|[(]|[)]|[*]|[+]|[,]|[-]|[.]|[/]|[:]|[;]|[<]|[=]|[>]|[?]|[@]|[\\[]|[\\\\]|[\\]]|[\\^]|[_]|[`]|[{]|[|]|[}]|[~]|[\\\\\"])*")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, NÂO RECEBEU O CARACTER ESPERADO" + "\n";
            saidaSintatico.addLast(erro);
        }
    }
    
    private void procedimentoSTRUCTUSAGE() {
       if(tokenDado[1].trim().matches("IDE")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();            
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA UM IDENTIFICADOR" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3); 
        }else{
            return;
        }
        if(tokenDado[2].trim().matches(".")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA '.'" + "\n";
            saidaSintatico.addLast(erro);
        }
        if(!dadosLexicos.isEmpty()){
            tokenDado = dadosLexicos.getFirst().split(" ", 3); 
        }else{
            return;
        }
        if(tokenDado[1].trim().matches("IDE")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
        }else{
            erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'IDE'" + "\n";
            saidaSintatico.addLast(erro);
        }
    }

    private void procedimentoEXPPRINT() {
        if(tokenDado[1].trim().equals("IDE")){
            lookAhead1 = dadosLexicos.get(1).split(" ", 3);
            if(lookAhead1[2].trim().equals(",")){//Identifier -> More EXpressions
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);                    
                }else{
                    return;
                }
                procedimentoMOREEXPRESSIONS();
            }else if(lookAhead1[2].trim().equals("[")){//Array Usage
                procedimentoARRAYUSAGE();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);                   
                }else{
                    return;
                }
                procedimentoMOREEXPRESSIONS();
            }else if(lookAhead1[2].trim().equals(".")){//Struct Usage
                procedimentoSTRUCTUSAGE();
                if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3);
                }else{
                    return;
                }
                procedimentoMOREEXPRESSIONS();
            }else{
                saidaSintatico.addLast(dadosLexicos.getFirst());
                dadosLexicos.removeFirst();
                return;
            }            
        }else if(tokenDado[1].trim().equals("CAD")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                tokenDado = dadosLexicos.getFirst().split(" ", 3);
            }else{
                return;
            }
            procedimentoMOREEXPRESSIONS();
        }else{           
           erro = tokenDado[0] + "ERRO SINTATICO, ESPERAVA 'IDE' ou 'CAD'" + "\n";
           saidaSintatico.addLast(erro);
        }
    }

    public void procedimentoMOREEXPRESSIONS() {
        if(tokenDado[2].trim().matches(",")){
            saidaSintatico.addLast(dadosLexicos.getFirst());
            dadosLexicos.removeFirst();
            if(!dadosLexicos.isEmpty()){
                    tokenDado = dadosLexicos.getFirst().split(" ", 3); 
                }else{
                    return;
                }
            procedimentoEXPPRINT();
        }
    }    
}

