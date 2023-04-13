/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores.problema;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author bruno
 */
public class analisadorSemantico {
    LinkedList<String> saidaSemantico = new LinkedList<String>();
    LinkedList<String> dados = new LinkedList<String>();
    LinkedList<ModeloTabela> tabela = new LinkedList<ModeloTabela>();
    LinkedList<String> errosDaTabela = new LinkedList<String>();
    LinkedList<String> errosDeNaoDeclaracao = new LinkedList<String>();
    LinkedList<String> errosDeAtribuicaoConstante = new LinkedList<String>();    
    
    public LinkedList analiseSemantica(LinkedList aSintatica, LinkedList tabelaDeSimbolos, LinkedList errosDuplicidade){        
        dados = aSintatica;
        dados.removeLast();
        tabela = tabelaDeSimbolos;
        errosDaTabela = errosDuplicidade;
        saidaSemantico = dados;
        ideNaoDeclarado();
        atribuicaoConstante();
        
        if(errosDeNaoDeclaracao.size()>0){
            saidaSemantico.addLast("------------------------------------------------------" + "\n");
            saidaSemantico.addLast("                                                     " + "\n");
            saidaSemantico.addLast("ERROS SEMANTICOS DE NAO DECLARACAO DE IDENTIFICADORES" + "\n");
            saidaSemantico.addLast("                                                     " + "\n");
            saidaSemantico.addLast("------------------------------------------------------" + "\n");
            for(int errorCounterNaoDeclaracao = 0; errorCounterNaoDeclaracao < errosDeNaoDeclaracao.size(); errorCounterNaoDeclaracao++){
                String msgErroNaoDeclaracao = errosDeNaoDeclaracao.get(errorCounterNaoDeclaracao);
                saidaSemantico.addLast(msgErroNaoDeclaracao);             
            }        
        }
        
        if(errosDeAtribuicaoConstante.size()>0){
            saidaSemantico.addLast("------------------------------------------------------" + "\n");
            saidaSemantico.addLast("                                                     " + "\n");
            saidaSemantico.addLast("ERROS SEMANTICOS DE ATRIBUICAO DE CONSTANTES JA DECLARADAS" + "\n");
            saidaSemantico.addLast("                                                     " + "\n");
            saidaSemantico.addLast("------------------------------------------------------" + "\n");
            for(int errorCounterAtribuicaoConstante = 0; errorCounterAtribuicaoConstante < errosDeAtribuicaoConstante.size(); errorCounterAtribuicaoConstante++){
                String msgErroAtribuicaoConstante = errosDeAtribuicaoConstante.get(errorCounterAtribuicaoConstante);
                saidaSemantico.addLast(msgErroAtribuicaoConstante);
            }
        }
        
        
        
        duplicidade();
         
        
        errosDeNaoDeclaracao.clear();
        errosDeAtribuicaoConstante.clear();
        return saidaSemantico;
    }
    
    
    public void duplicidade(){
        if(errosDaTabela.size()>0){
            String divisorErro = "------------------------------------------------------";
            String anuncioErro = "ERROS SEMANTICOS DE DUPLICIDADE";
            saidaSemantico.addLast(divisorErro + "\n");
            saidaSemantico.addLast("                                                     " + "\n");
            saidaSemantico.addLast(anuncioErro + "\n");
            saidaSemantico.addLast("                                                     " + "\n");
            saidaSemantico.addLast(divisorErro + "\n");
            for(int errorCounter = 0; errorCounter<errosDaTabela.size(); errorCounter++){
                String msgErro = errosDaTabela.get(errorCounter);                            
            
                saidaSemantico.addLast(msgErro + "\n");
            
            }
        }
    }
    
    public void ideNaoDeclarado(){
        String paterna = new String();
        Map<String,String> auxiliar = new HashMap<String,String>(); 
        String paisDaTabela = new String();
        String aux1 = new String();
        String aux2 = new String();
        String aux3 = new String();
        String aux4 = new String();
        String[] atual = null;
        String[] lookAhead = null;
        String[] lookAhead2 = null;
        String[] lookBack = null;
        int controladorTabela = 0;
        int contadorTabela = 0;
        int flag = 0;
        int flag2 = 0;
        
        lookAhead = null;
        lookAhead2 = null;
        
        for(int i=3; i<dados.size(); i++){
            aux1 = dados.get(i);
            aux2 = dados.get(i-1);
            atual = aux1.split(" ", 3);
            lookBack = aux2.split(" ", 3);
            if((dados.size()-i) > 2 ){
                aux3 = dados.get(i+1);
                aux4 = dados.get(i+2);
                lookAhead = aux3.split(" ", 3);
                lookAhead2 = aux4.split(" ", 3);
            }
            
            if(atual[2].trim().equals("{")){
                if((lookBack[2].trim().equals("struct")) || (lookBack[2].trim().equals("var")) || (lookBack[2].trim().equals("const"))){
                    flag2 = 1;
                }
                if((!lookBack[2].trim().equals("struct")) && (!lookBack[2].trim().equals("var")) && (!lookBack[2].trim().equals("const"))){                                        
                    contadorTabela = contadorTabela+1;
                    controladorTabela = contadorTabela;
                    flag = 1;
                }                                                
            }else if(atual[2].trim().equals("}")){
                if(flag2 == 0){
                    if(flag == 1){
                        for(int j = 0; j<tabela.size(); j++ ){
                            if(tabela.get(j).getNumeroTabela() == controladorTabela){                                
                                paisDaTabela = tabela.get(j).getPais();
                                j = tabela.size();
                            }                    
                        }
                        if(controladorTabela != 0){                            
                        controladorTabela = Integer.parseInt(paisDaTabela.substring(paisDaTabela.length()-1, paisDaTabela.length()));
                        flag = 0;
                        }   
                    }
                }else if(flag2 == 1){
                    flag2 = 0;
                }
            }
            
            
            if((atual[1].trim().equals("IDE")) && (lookAhead[2].trim().equals("=")) && (lookAhead2[1].trim().equals("IDE")) ){
                for(int contador = 0; contador<tabela.size(); contador++ ){
                    if(tabela.get(contador).getNumeroTabela() == controladorTabela){
                        auxiliar = tabela.get(contador).getTabela();
                        paterna = tabela.get(contador).getPais();
                        contador = tabela.size();
                    }                    
                }
                while(paterna.length() > -1){
                    if((!auxiliar.containsKey(lookAhead2[2].trim())) && (paterna.length() == 0)){
                        errosDeNaoDeclaracao.addLast(lookAhead2[2].trim() + " " + "linha:" + lookAhead2[0].trim() + "; ERRO SEMANTICO, IDENTIFICADOR NÃO DECLARADO" + "\n");
                        break;
                    }else if(auxiliar.containsKey(lookAhead2[2].trim())){
                        break;
                    }
                    if(paterna.length() == 1){
                        auxiliar = tabela.get(0).getTabela();
                        paterna = new String();
                    }else if(paterna.length() > 1){                    
                        auxiliar = tabela.get(Integer.parseInt(paterna.substring(paterna.length()-1, paterna.length()))).getTabela();
                        paterna = paterna.substring(0 , paterna.length()-3);
                    }
                    
                }
            }                        
        }                         
    }
    
    public void atribuicaoConstante(){
        String paterna = new String();
        Map<String,String> auxiliar = new HashMap<String,String>(); 
        String paisDaTabela = new String();
        String dadosConst = new String();
        String aux1 = new String();
        String aux2 = new String();
        String aux3 = new String();
        String aux4 = new String();
        String aux5 = new String();
        String[] atual;
        String[] lookAhead;
        String[] lookAhead2;
        String[] lookBack;
        String[] lookBack2;
        String[] verificadorConst;
        int controladorTabela = 0;
        int contadorTabela = 0;
        int flag = 0;
        int flag2 = 0;
        int flagConst = 0;
        int contadorLoop = 0;
        
        verificadorConst = null;
        lookAhead = null;
        lookAhead2 = null;
        
        for(int i=3; i<dados.size(); i++){
            aux1 = dados.get(i);
            aux2 = dados.get(i-1);
            atual = aux1.split(" ", 3);
            lookBack = aux2.split(" ", 3);
            if((dados.size()-i) > 2 ){
                aux3 = dados.get(i+1);
                aux4 = dados.get(i+2);
                lookAhead = aux3.split(" ", 3);
                lookAhead2 = aux4.split(" ", 3);
            }
            
            if(atual[2].trim().equals("{")){
                if((lookBack[2].trim().equals("struct")) || (lookBack[2].trim().equals("var")) || (lookBack[2].trim().equals("const"))){
                    flag2 = 1;
                }
                if((!lookBack[2].trim().equals("struct")) && (!lookBack[2].trim().equals("var")) && (!lookBack[2].trim().equals("const"))){                                        
                    contadorTabela = contadorTabela+1;
                    controladorTabela = contadorTabela;
                    flag = 1;
                }                                                
            }else if(atual[2].trim().equals("}")){
                if(flag2 == 0){
                    if(flag == 1){
                        for(int j = 0; j<tabela.size(); j++ ){
                            if(tabela.get(j).getNumeroTabela() == controladorTabela){
                                paisDaTabela = tabela.get(j).getPais();
                                j = tabela.size();
                            }                    
                        }
                        if(controladorTabela != 0){
                        controladorTabela = Integer.parseInt(paisDaTabela.substring(paisDaTabela.length()-1, paisDaTabela.length()));
                        flag = 0;
                        }   
                    }
                }else if(flag2 == 1){
                    flag2 = 0;
                }
            }
            
            if((atual[1].trim().equals("IDE")) && (lookAhead[2].trim().equals("=")) && (lookAhead2[1].trim().equals("NRO") || lookAhead2[1].trim().equals("CAD"))){
                aux5 = dados.get(i-2);
                lookBack2 = aux5.split(" ", 3);
                contadorLoop = i;
                while(!lookBack[2].trim().equals("}")){
                    contadorLoop = contadorLoop - 1;
                    if(lookBack[2].trim().equals("{") && lookBack2[2].trim().equals("const")){
                    flagConst = 1;
                    break;
                    }
                    aux2 = dados.get(contadorLoop-1);
                    lookBack = aux2.split(" ", 3);
                    aux5 = dados.get(contadorLoop-2);
                    lookBack2 = aux5.split(" ", 3);
                    
                }
                
                
                if((atual[1].trim().equals("IDE")) && (lookAhead[2].trim().equals("=")) && (lookAhead2[1].trim().equals("NRO") || lookAhead2[1].trim().equals("CAD")) && flagConst == 0){
                    for(int contador = 0; contador < tabela.size(); contador++ ){
                        if(tabela.get(contador).getNumeroTabela() == controladorTabela){
                            auxiliar = tabela.get(contador).getTabela();
                            paterna = tabela.get(contador).getPais();
                            contador = tabela.size();
                        }                    
                    }
                
                    while(paterna.length() > -1){
                        if(auxiliar.containsKey(atual[2].trim())){
                            dadosConst = auxiliar.get(atual[2].trim());
                            break;
                        }
                    
                        if(paterna.length() == 1){
                            auxiliar = tabela.get(0).getTabela();
                            paterna = new String();
                        }else if(paterna.length() > 1){                    
                            auxiliar = tabela.get(Integer.parseInt(paterna.substring(paterna.length()-1, paterna.length()))).getTabela();
                            paterna = paterna.substring(0 , paterna.length()-3);
                        }
                    
                    }
                    if(dadosConst.length() > 0){
                    verificadorConst = dadosConst.split(";", 4);                
                    }
                
                    if(verificadorConst.length > 0){
                        if(verificadorConst[1].trim().equals("const")){
                        errosDeAtribuicaoConstante.addLast(atual[2].trim() + " " + "linha:" + atual[0].trim() + "; ERRO SEMANTICO, ATRIBUICAO DE VALOR A UMA CONSTANTE" + "\n");
                        }
                    }                
                }
                
                flagConst = 0;
            }
            
        }
    }
}
