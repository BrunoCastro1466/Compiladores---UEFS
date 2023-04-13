/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores.problema;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author bruno
 */
public class CriadorTabela {
    Map<String,String> tabela = new HashMap<String,String>();
    String palavrasReservadas = new String("[v][a][r]|[c][o][n][s][t]|[t][y][p][e][d][e][f]|[s][t][r][u][c][t]|[e][x][t][e][n][d][s]|[p][r][o][c][e][d][u][r][e]|[f][u][n][c][t][i][o][n]|[s][t][a][r][t]|[r][e][t][u][r][n]|[i][f]|[e][l][s][e]|[t][h][e][n]|[w][h][i][l][e]|[r][e][a][d]|[p][r][i][n][t]|[i][n][t]|[r][e][a][l]|[b][o][o][l][e][a][n]|[s][t][r][i][n][g]|[t][r][u][e]|[f][a][l][s][e]|[g][l][o][b][a][l]|[l][o][c][a][l]");
    String identificadores = new String("([a-z]|[A-Z])([a-z]+|[A-Z]+|[0-9]+|[_]+)");
    String tipos = new String("[b][o][o][l][e][a][n]|[i][n][t]|[r][e][a][l]|[s][t][r][i][n][g]");
    String[] lexema;
    String[] lookBack;
    String recebedor;
    String controle = new String();
    String varConst;
    String informacao;
    int flagSubstituição = 0;
    int atualizador;
    int escopo = 0;
    int aux = 0;
    ModeloTabela tabelaAtual = new ModeloTabela();
    LinkedList<ModeloTabela> tabelaSimbolo = new LinkedList<ModeloTabela>();
    LinkedList<String> errosDuplicidade = new LinkedList<String>();
    
    public LinkedList gerarTabela(LinkedList dados){
        tabelaAtual = new ModeloTabela();
        tabelaSimbolo.clear();
        errosDuplicidade.clear();
        dados.removeLast();
        escopo = 0;
        tabelaAtual.setNumeroTabela(escopo);
        tabelaSimbolo.addFirst(tabelaAtual);        
        controle = "0";
        escopo ++;
        for(int i = 3; i < dados.size(); i++){            
            aux = i;
            recebedor = (String)dados.get(i);
            lexema = recebedor.split(" ", 3);           
            if(lexema[2].trim().equals("{")){
                recebedor = (String)dados.get(i-1);                
                lookBack = recebedor.split(" ", 3);
                if((!lookBack[2].trim().equals("struct")) && (!lookBack[2].trim().equals("var")) && (!lookBack[2].trim().equals("const"))){
                    ModeloTabela nova = new ModeloTabela();
                    nova.setPais(controle);
                    nova.setNumeroTabela(escopo);                    
                    controle = controle.concat(", " + escopo);
                    escopo++;
                    if(tabelaSimbolo.getFirst().getNumeroTabela() != tabelaAtual.getNumeroTabela()){
                        tabelaSimbolo.add(tabelaAtual);
                    }else{
                        tabelaSimbolo.removeFirst();
                        tabelaSimbolo.addFirst(tabelaAtual);
                    }
                    tabelaAtual = nova;
                }                                               
            }else if(lexema[2].trim().equals("}")){
                for(int k = 0; k < tabelaSimbolo.size(); k++){
                    if(tabelaSimbolo.get(k).getNumeroTabela() == tabelaAtual.getNumeroTabela()){                        
                        tabelaSimbolo.remove(k);
                        tabelaSimbolo.add(k, tabelaAtual);
                        flagSubstituição = 1;
                    }                   
                }
                if(flagSubstituição == 0){
                    tabelaSimbolo.add(tabelaAtual);
                }
                flagSubstituição = 0;                
                if(controle.length() > 3){                    
                controle = controle.substring(0, controle.length() - 3);
                }
                atualizador = Integer.parseInt(controle.substring(controle.length() - 1, controle.length()));                
                tabelaAtual = tabelaSimbolo.get(atualizador);                        
            }else if(lexema[1].equals("IDE")){
                if(!lexema[2].matches(palavrasReservadas)){                    
                    int aux2 = aux-1;
                    int aux3 = aux+1;
                    int aux4 = aux-3;
                    int aux5 = aux-2;
                    int aux6 = aux-3;
                    int aux7 = aux+2;
                    String check = (String)dados.get(aux2);
                    String[] checker = check.split(" ", 3);
                    String check2 = (String)dados.get(aux4);
                    String[] checker2 = check2.split(" ", 3);
                    String check3 = (String)dados.get(aux5);
                    String[] checker3 = check3.split(" ", 3);
                    String check4 = (String)dados.get(aux6);
                    String[] checker4 = check4.split(" ", 3);
                    String[] checker5 = null;                   
                    if((dados.size()-aux) > 2){                        
                    String check5 = (String)dados.get(aux7);
                    checker5 = check5.split(" ", 3);
                    }
                    if(checker[2].trim().matches(tipos)){
                        if(checker3[2].trim().equals("function")){
                            String auxFunction = (String)dados.get(aux3);
                            String[] recebedorAuxFunction = auxFunction.split(" ", 3);
                            String argumentos = new String("ARGS ");
                            int contador = 0;
                            for(contador = aux3+1; !recebedorAuxFunction[2].trim().equals(")"); contador++){
                                if(recebedorAuxFunction[1].trim().equals("IDE")){
                                    String auxFunction2 = (String)dados.get(contador-2);
                                    String[] recebedorAuxFunction2 = auxFunction2.split(" ", 3);
                                    if(recebedorAuxFunction2[2].trim().matches(tipos)){
                                        String data = recebedorAuxFunction2[2].trim() + " " + recebedorAuxFunction[2].trim() + "; ";
                                        argumentos = argumentos.concat(data);                                        
                                    }
                                }                                                              
                                auxFunction = (String)dados.get(contador);
                                recebedorAuxFunction = auxFunction.split(" ", 3);
                            }
                            i = contador-1;                           
                            String informacao = checker[2].trim() + "; " + checker3[2].trim() + "; " + lexema[0].trim() + "; " + argumentos;
                            if(!tabelaAtual.getTabela().containsKey(lexema[2].trim())){
                            tabelaAtual.addSymbol(lexema[2].trim(), informacao);
                            }else if(tabelaAtual.getTabela().containsKey(lexema[2].trim())){
                                errosDuplicidade.addLast(lexema[2].trim() + " " + "linha:" + lexema[0].trim() + "; " + "ERRO SEMANTICO, FUNÇÃO JÀ EXISTENTE");
                            }
                        }else{
                            
                            if(checker4[2].trim().equals("var") || checker4[2].trim().equals("const")){
                               varConst = checker4[2].trim();
                            }
                            
                            String auxVerificador = (String)dados.get(aux3);
                            String[] verificadorFor = auxVerificador.split(" ", 3);
                            if(varConst.trim().equals("var")){
                            informacao = checker[2].trim() + "; " + varConst + "; " + lexema[0].trim();
                            }else if(varConst.trim().equals("const")){
                            informacao = checker[2].trim() + "; " + varConst + "; " + checker5[2].trim() + "; " + lexema[0].trim();
                            }   
                            if(!tabelaAtual.getTabela().containsKey(lexema[2].trim())){
                            tabelaAtual.addSymbol(lexema[2].trim(), informacao);
                            }else if(tabelaAtual.getTabela().containsKey(lexema[2].trim())){
                                errosDuplicidade.addLast(lexema[2].trim() + " " + "linha:" + lexema[0].trim() + "; " + "ERRO SEMANTICO,IDENTIFICADOR JÁ DECLARADO");
                            }       
                            for(aux3 = aux+1; !verificadorFor[2].trim().equals(";"); aux3++){                            
                                if(verificadorFor[2].trim().equals(",")){
                                    auxVerificador = (String)dados.get(aux3+1);
                                    verificadorFor = auxVerificador.split(" ", 3);
                                    if(verificadorFor[1].trim().equals("IDE")){
                                        if(varConst.trim().equals("var")){
                                            if(!tabelaAtual.getTabela().containsKey(verificadorFor[2].trim())){
                                            tabelaAtual.addSymbol(verificadorFor[2].trim(), informacao);
                                            }else if(tabelaAtual.getTabela().containsKey(verificadorFor[2].trim())){
                                                errosDuplicidade.addLast(verificadorFor[2].trim() + " " + "linha:" + lexema[0].trim() +"; " + "ERRO SEMANTICO,IDENTIFICADOR JÁ DECLARADO");
                                            }
                                        }else if(varConst.trim().equals("const")){
                                            String pegaValor = (String)dados.get(aux3+3);                                            
                                            String[] valor = pegaValor.split(" ", 3);                                           
                                            String[] modificador = informacao.split(";", 4);                                            
                                            modificador[2] = valor[2];
                                            informacao = modificador[0] + ";" + modificador[1] + "; " + modificador[2].trim() + "; " + modificador[3];
                                            if(!tabelaAtual.getTabela().containsKey(verificadorFor[2].trim())){
                                            tabelaAtual.addSymbol(verificadorFor[2].trim(), informacao);
                                            }else if(tabelaAtual.getTabela().containsKey(verificadorFor[2].trim())){
                                                errosDuplicidade.addLast(verificadorFor[2].trim() + " " + "linha:" + lexema[0].trim() +"; " + "ERRO SEMANTICO,IDENTIFICADOR JÁ DECLARADO");
                                            }
                                        }
                                    }
                                    
                                }
                                auxVerificador = (String)dados.get(aux3+1);
                                verificadorFor = auxVerificador.split(" ", 3);
                            }
                        }
                        
                        
                    }else if(checker[2].trim().equals("procedure")){
                        int contador = 0;
                        String auxFunction = (String)dados.get(aux3);
                        String[] recebedorAuxFunction = auxFunction.split(" ", 3);
                        String argumentos = new String("ARGS ");
                        for(contador = aux3+1; !recebedorAuxFunction[2].trim().equals(")"); contador++){
                            if(recebedorAuxFunction[1].trim().equals("IDE")){
                                String auxFunction2 = (String)dados.get(contador-2);
                                String[] recebedorAuxFunction2 = auxFunction2.split(" ", 3);
                                if(recebedorAuxFunction2[2].trim().matches(tipos)){
                                        String data = recebedorAuxFunction2[2].trim() + " " + recebedorAuxFunction[2].trim() + "; ";
                                        argumentos = argumentos.concat(data);                                        
                                }                                
                            }
                            auxFunction = (String)dados.get(contador);
                            recebedorAuxFunction = auxFunction.split(" ", 3);
                        }
                        i = contador-1;
                        String informacao = "null " + checker[2].trim() + "; " + lexema[0].trim() + "; " + argumentos;
                        if(!tabelaAtual.getTabela().containsKey(lexema[2].trim())){
                            tabelaAtual.addSymbol(lexema[2].trim(), informacao);
                        }else if(tabelaAtual.getTabela().containsKey(lexema[2].trim())){
                            errosDuplicidade.addLast(lexema[2].trim() + " " + "linha:" + lexema[0].trim() + "; " + "ERRO SEMANTICO, PROCEDIMENTO JÀ EXISTENTE");
                        }
                    }
                }
            }                        
        }
        
        
        return tabelaSimbolo;
    }
    
    public LinkedList errosDuplicidade(){        
        return errosDuplicidade;
    }
}
