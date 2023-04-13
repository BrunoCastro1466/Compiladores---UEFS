/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores.problema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
/**
 *
 * @author Bruno Castro
 * 
 * Este código faz a análise léxica de uma entrada que consiste em um ou mais arquivos de texto.
 * Estes arquivos devem ser colocados na pasta "input", já existente, localizada na raíz do projeto.
 * Os arquivos de saída serão criados em uma pasta "output", também já existente, localizada
 * na raíz do projeto, para que os arquivos de saída sejam criados corretamente é necessário 
 * que não haja nenhum arquivo.txt na pasta cujo nome siga o padrão "outputx", sendo x a numeração do arquivo.
 */

public class CompiladoresProblema4 {
    
    public static void main(String[] args) {       
        int lineCounter = 1;
        int flagComentBloco = 0;
        int estado = 0;
        int tamanhoDosLexicos;
        String checkReservadas = new String("^([v][a][r]|[c][o][n][s][t]|[t][y][p][e][d][e][f]|[s][t][r][u][c][t]|[e][x][t][e][n][d][s]|[p][r][o][c][e][d][u][r][e]|[f][u][n][c][t][i][o][n]|[s][t][a][r][t]|[r][e][t][u][r][n]|[i][f]|[e][l][s][e]|[t][h][e][n]|[w][h][i][l][e]|[r][e][a][d]|[p][r][i][n][t]|[i][n][t]|[r][e][a][l]|[b][o][o][l][e][a][n]|[s][t][r][i][n][g]|[t][r][u][e]|[f][a][l][s][e]|[g][l][o][b][a][l]|[l][o][c][a][l])(([\\]]|[\\[]|[,]|[ ]|[;]|[(]|[)]|[.])[^A-Za-z]*.*)*");
        String reservadas = new String("[v][a][r]|[c][o][n][s][t]|[t][y][p][e][d][e][f]|[s][t][r][u][c][t]|[e][x][t][e][n][d][s]|[p][r][o][c][e][d][u][r][e]|[f][u][n][c][t][i][o][n]|[s][t][a][r][t]|[r][e][t][u][r][n]|[i][f]|[e][l][s][e]|[t][h][e][n]|[w][h][i][l][e]|[r][e][a][d]|[p][r][i][n][t]|[i][n][t]|[r][e][a][l]|[b][o][o][l][e][a][n]|[s][t][r][i][n][g]|[t][r][u][e]|[f][a][l][s][e]|[g][l][o][b][a][l]|[l][o][c][a][l]");
        Escritor escritor = new Escritor();                             
        String erros = new String();
        String guardaComentarios = new String();
        String caminhoInput = new String();
        String caminhoOutput = new String();
        File arquivos[];
        LinkedList<ModeloTabela> tabelaDeSimbolos = new LinkedList<ModeloTabela>();
        LinkedList<String> lexicos = new LinkedList<String>();
        LinkedList<String> dadosTabela = new LinkedList<String>();
        LinkedList<String> saidaSintatico = new LinkedList<String>();
        LinkedList<String> saidaSemantico = new LinkedList<String>();
        LinkedList<String> errosTabela = new LinkedList<String>();
        analisadorSintatico analiseSintatica = new analisadorSintatico();
        analisadorSemantico analiseSemantica = new analisadorSemantico();
        CriadorTabela gerador = new CriadorTabela();
        
        String path = new String();
        path = System.getProperty("user.dir");       
        caminhoInput = path + "\\input";
        File diretorio = new File(caminhoInput);        
        arquivos = diretorio.listFiles();
        String saida = new String();
       
        for(int i = 0; i < arquivos.length; i++){
            int j = i+1;
            erros = "";
            guardaComentarios = "";
        try{
            FileReader arq = new FileReader(arquivos[i]);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();            
            
            caminhoOutput = path + "\\output\\output"+j+".txt";
            FileWriter arqSaida = new FileWriter(caminhoOutput);          
            PrintWriter gravarSaida = new PrintWriter(arqSaida);
             
            while(linha != null){
                linha = linha.trim();
                while(linha != ""){
             
                    switch (estado){
                        case 0://Verificação do inicio da string
                                                      
                            if(linha.matches("[0-9]+[.]?([0-9]+)*.*")){//Verifica se a String começa com um digito
                                estado = 1;
                                break;                                                           
                            }else if(linha.matches(checkReservadas)){//Verifica se a String começa com uma palavra reservada válida                                                                                             
                                estado = 2;
                                break;
                            }else if(linha.matches("([a-z]|[A-Z])([a-z]+|[A-Z]+|[0-9]+|[_]+)*.*")){//Verifica se a String começa com uma letra                                                             
                                estado = 3;
                                break;
                            }
                                
                            else if(linha.matches("[/]{2}.*")){//Verifica se a String começa com um comentario de linha
                                estado = 4;
                                break;
                            }else if(linha.matches("[/][*].*")){//Verifica se a String começa com um comentario de bloco
                                estado = 5;
                                break;
                                    }                           
                            else if(linha.matches("([=]|[=]{2}|[!][=]|[>]|[>][=]|[<]|[<][=]).*")){//Verifica se a String começa com um operador relacional
                                estado = 8;
                                break;
                            }else if(linha.matches("([+]|[-]|[*]|[/]|[+]{2}|[-]{2}).*")){//Verifica se a String começa com um operador aritmetico
                                estado = 7;
                                break;
                            }else if(linha.matches("([&]{2}|[|]{2}|[!]).*")){//Verifica se a String começa com um operador lógico
                                estado = 6;
                                break;
                            }else if(linha.matches("([.]|[;]|[,]|[(]|[)]|[{]|[}]|[\\[]|[\\]]).*")){// Verifica se a String começa com um delimitador
                                estado = 9;
                                break;
                            }else if(linha.matches("[\"]([a-z]|[A-Z]|[0-9]|[ ]|[!]|[#]|[$]|[%]|[&]|[']|[(]|[)]|[*]|[+]|[,]|[-]|[.]|[/]|[:]|[;]|[<]|[=]|[>]|[?]|[@]|[\\[]|[\\\\]|[\\]]|[\\^]|[_]|[`]|[{]|[|]|[}]|[~]|[\\\\\"])*[\"].*")){//Verifica se a string começa com um indicador de cadeia de caracteres
                                estado = 10;
                                break;
                            }else if(linha.matches("[\"].*")){
                                estado = 11;
                                break;
                            }else if(linha.matches("([#]|[$]|[%]|[']|[:]|[@]|[\\^]|[_]|[`]|[~]|[?]|[&]|[!]|[\"]|[(]|[)]|[|]).*")){
                                estado = 12;
                                break;
                            }else{
                                linha = "";
                                break;
                            }
                            
                            case 1://Numeros
                                linha = linha.replaceAll(" ", "   ");
                                String[] dadosNumeros = linha.split("[^0-9.]", 2);
                                saida = escritor.gerarSaida(dadosNumeros[0],"NRO","NMF","[0-9]+[.]?([0-9]+)*",lineCounter);
                                if(!saida.startsWith("a")){
                                 lexicos.add(saida);   
                                }else{
                                    saida = saida.substring(1);
                                    erros = erros.concat(saida);
                                }                                
                                linha = escritor.atualizarLinha(dadosNumeros[0], linha);
                                linha = linha.trim();
                                estado = 0;
                                break;
                                
                            case 2://Reservadas
                                String[] dadosReservadas = linha.split("[^A-Za-z]", 2);
                                saida = escritor.gerarSaida(dadosReservadas[0],"PRE","CMF",reservadas, lineCounter);                                
                                
                                if(!saida.startsWith("a")){
                                 lexicos.add(saida);   
                                }else{
                                    saida = saida.substring(1);
                                    erros = erros.concat(saida);
                                }
                                linha = escritor.atualizarLinha(dadosReservadas[0], linha);
                                linha = linha.trim();
                                estado = 0;
                                break;
                                
                            case 3://Identificadores
                                String[] dadosIdentificadores = linha.split("[^0-9|^a-z|^A-Z|^_|^ç|^ã|^Ã|^õ|^Õ|^á|^é|^í|^ó|^ú|^Á|^É|^Í|^Ó|^Ú|^à|^è|^ì|^ò|^ù||^À|^È|^Ì|^Ò|^Ù|^â|^ê|^î|^ô|^û|^Â|^Ê|^Î|^Ô|^Û]", 2);
                                saida = escritor.gerarSaida(dadosIdentificadores[0],"IDE","CMF", "([a-z]|[A-Z])([a-z]+|[A-Z]+|[0-9]+|[_]+)*", lineCounter);                                
                                
                                if(!saida.startsWith("a")){
                                 lexicos.add(saida);  
                                }else{
                                    saida = saida.substring(1);
                                    erros = erros.concat(saida);
                                }
                                linha = escritor.atualizarLinha(dadosIdentificadores[0], linha);
                                linha = linha.trim();
                                estado = 0;
                                break;
                                
                            case 4://Comentarios de linha
                                saida = escritor.gerarComentario(linha,"CoMF","[/]{2}.+",lineCounter);
                                linha = "";
                                estado = 0;
                                break;
                                
                            case 5://Comentarios de bloco
                                int numeroDoErro = lineCounter;
                                guardaComentarios = (numeroDoErro + " " + "CoMF");
                                while(!linha.matches(".*[*][/].*")){                                    
                                    if(linha != null){
                                    guardaComentarios = guardaComentarios.concat(" " + linha);
                                    }
                                    linha = lerArq.readLine();
                                    
                                    lineCounter = lineCounter+1;
                                        if(linha == null){
                                            flagComentBloco = 1;
                                            break;
                                        }
                                    }
                                if(linha != null){
                                guardaComentarios = ("");
                                System.out.println(guardaComentarios);
                                String dadosComentBloco[] = linha.split( "[*][/]", 2);
                                linha = linha.substring(dadosComentBloco[0].length()+2);
                                linha = linha.trim();
                                estado = 0;
                                break;
                                }
                                break;
                                
                            case 6://Operadores Lógicos
                                if(linha.charAt(0) == '!'){
                                    String[] dadosOpLogicos = linha.split("[^!]", 2);
                                    saida = escritor.gerarSaida(dadosOpLogicos[0],"LOG","OpMF","[&]{2}|[|]{2}|[!]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosOpLogicos[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '&'){
                                    String[] dadosOpLogicos = linha.split("[^&]", 2);
                                    saida = escritor.gerarSaida(dadosOpLogicos[0],"LOG","OpMF","[&]{2}|[|]{2}|[!]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosOpLogicos[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '|'){
                                    String[] dadosOpLogicos = linha.split("[^|]", 2);
                                    saida = escritor.gerarSaida(dadosOpLogicos[0],"LOG","OpMF","[&]{2}|[|]{2}|[!]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosOpLogicos[0], linha);
                                    linha = linha.trim();                                   
                                }                                                               
                                
                                if(!saida.startsWith("a")){
                                lexicos.add(saida);    
                                }else{
                                    saida = saida.substring(1);
                                    erros = erros.concat(saida);
                                }
                                estado = 0;
                                break;
                                
                            case 7://Operadores Aritmeticos
                                if(linha.charAt(0) == '+'){
                                    String[] dadosOpAritmeticos = linha.split("[^+]", 2);
                                    saida = escritor.gerarSaida(dadosOpAritmeticos[0],"ART","OpMF","[+]|[-]|[*]|[/]|[+]{2}|[-]{2}", lineCounter);
                                    linha = escritor.atualizarLinha(dadosOpAritmeticos[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '-'){
                                    String[] dadosOpAritmeticos = linha.split("[^-]", 2);
                                    saida = escritor.gerarSaida(dadosOpAritmeticos[0],"ART","OpMF","[+]|[-]|[*]|[/]|[+]{2}|[-]{2}", lineCounter);
                                    linha = escritor.atualizarLinha(dadosOpAritmeticos[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '*'){
                                    String[] dadosOpAritmeticos = linha.split("[^*]", 2);
                                    saida = escritor.gerarSaida(dadosOpAritmeticos[0],"ART","OpMF","[+]|[-]|[*]|[/]|[+]{2}|[-]{2}", lineCounter);
                                    linha = escritor.atualizarLinha(dadosOpAritmeticos[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '/'){
                                    String[] dadosOpAritmeticos = linha.split("[^/]", 2);
                                    saida = escritor.gerarSaida(dadosOpAritmeticos[0],"ART","OpMF","[+]|[-]|[*]|[/]|[+]{2}|[-]{2}", lineCounter);
                                    linha = escritor.atualizarLinha(dadosOpAritmeticos[0], linha);
                                    linha = linha.trim();
                                }                                
                                
                                if(!saida.startsWith("a")){
                                    lexicos.add(saida);   
                                }else{
                                    saida = saida.substring(1);
                                    erros = erros.concat(saida);
                                }
                                estado = 0;
                                break;
                                
                            case 8://Operadores Relacionais
                                 if(linha.charAt(0) == '='){
                                    String[] dadosOpRelacionais = linha.split("[^=]", 2);                                    
                                    saida = escritor.gerarSaida(dadosOpRelacionais[0],"REL","OpMF","[=]|[=]{2}|[!][=]|[>]|[>][=]|[<]|[<][=]", lineCounter);                                    
                                    linha = escritor.atualizarLinha(dadosOpRelacionais[0], linha);
                                    linha = linha.trim();                                  
                                }else if(linha.charAt(0) == '!'){
                                    String[] dadosOpRelacionais = linha.split("[^!=]", 2);
                                    saida = escritor.gerarSaida(dadosOpRelacionais[0],"REL","OpMF","[=]|[=]{2}|[!][=]|[>]|[>][=]|[<]|[<][=]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosOpRelacionais[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '>'){
                                    String[] dadosOpRelacionais = linha.split("[^>=]", 2);
                                    saida = escritor.gerarSaida(dadosOpRelacionais[0],"REL","OpMF","[=]|[=]{2}|[!][=]|[>]|[>][=]|[<]|[<][=]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosOpRelacionais[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '<'){
                                    String[] dadosOpRelacionais = linha.split("[^<=]", 2);                                    
                                    saida = escritor.gerarSaida(dadosOpRelacionais[0],"REL","OpMF","[=]|[=]{2}|[!][=]|[>]|[>][=]|[<]|[<][=]", lineCounter);                                    
                                    linha = escritor.atualizarLinha(dadosOpRelacionais[0], linha);
                                    linha = linha.trim();
                                }                                
                                
                                if(!saida.startsWith("a")){
                                 lexicos.add(saida);   
                                }else{
                                    saida = saida.substring(1);
                                    erros = erros.concat(saida);
                                }
                                estado = 0;
                                break;
                                
                            case 9://Delimitadores
                                if(linha.charAt(0) == ';'){
                                    String[] dadosDelimitadores = linha.split("[^;]", 2);
                                    saida = escritor.gerarSaida(dadosDelimitadores[0],"DEL","CMF","[.]|[;]|[,]|[(][)]|[{][}]|[\\[]|[\\]]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosDelimitadores[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == ','){
                                    String[] dadosDelimitadores = linha.split("[^,]", 2);
                                    saida = escritor.gerarSaida(dadosDelimitadores[0],"DEL","CMF","[.]|[;]|[,]|[(]|[)]|[{]|[}]|[\\[]|[\\]]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosDelimitadores[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '('){
                                    String[] dadosDelimitadores = linha.split("[^(]", 2);
                                    saida = escritor.gerarSaida(dadosDelimitadores[0],"DEL","CMF","[.]|[;]|[,]|[(]|[)]|[{]|[}]|[\\[]|[\\]]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosDelimitadores[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == ')'){
                                    String[] dadosDelimitadores = linha.split("[^)]", 2);
                                    saida = escritor.gerarSaida(dadosDelimitadores[0],"DEL","CMF","[.]|[;]|[,]|[(]|[)]|[{]|[}]|[\\[]|[\\]]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosDelimitadores[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '{'){
                                    String[] dadosDelimitadores = linha.split("[^{]", 2);
                                    saida = escritor.gerarSaida(dadosDelimitadores[0],"DEL","CMF","[.]|[;]|[,]|[(]|[)]|[{]|[}]|[\\[]|[\\]]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosDelimitadores[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '}'){
                                    String[] dadosDelimitadores = linha.split("[^}]", 2);
                                    saida = escritor.gerarSaida(dadosDelimitadores[0],"DEL","CMF","[.]|[;]|[,]|[(]|[)]|[{]|[}]|[\\[]|[\\]]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosDelimitadores[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '['){
                                    String[] dadosDelimitadores = linha.split("[^\\[]", 2);
                                    saida = escritor.gerarSaida(dadosDelimitadores[0],"DEL","CMF","[.]|[;]|[,]|[(]|[)]|[{]|[}]|[\\[]|[\\]]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosDelimitadores[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == ']'){
                                    String[] dadosDelimitadores = linha.split("[^]]", 2);
                                    saida = escritor.gerarSaida(dadosDelimitadores[0],"DEL","CMF","[.]|[;]|[,]|[(]|[)]|[{]|[}]|[\\[]|[\\]]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosDelimitadores[0], linha);
                                    linha = linha.trim();
                                }else if(linha.charAt(0) == '.'){
                                    String[] dadosDelimitadores = linha.split("[^.]", 2);
                                    saida = escritor.gerarSaida(dadosDelimitadores[0],"DEL","CMF","[.]|[;]|[,]|[(]|[)]|[{]|[}]|[\\[]|[\\]]", lineCounter);
                                    linha = escritor.atualizarLinha(dadosDelimitadores[0], linha);
                                    linha = linha.trim();
                                }
                                
                                if(!saida.startsWith("a")){
                                 lexicos.add(saida);   
                                }else{
                                    saida = saida.substring(1);
                                    erros = erros.concat(saida);
                                }
                                estado = 0;
                                break;
                                
                            case 10://Cadeia de caracteres
                                int index = linha.lastIndexOf("\"");
                                String dadosCadeia = linha.substring(0, index)+"\"";
                                saida = escritor.gerarSaida(dadosCadeia,"CAD","CMF","[\"]([a-z]|[A-Z]|[0-9]|[ ]|[!]|[#]|[$]|[%]|[&]|[']|[(]|[)]|[*]|[+]|[,]|[-]|[.]|[/]|[:]|[;]|[<]|[=]|[>]|[?]|[@]|[\\[]|[\\\\]|[\\]]|[\\^]|[_]|[`]|[{]|[|]|[}]|[~]|[\\\\\"])*[\"]", lineCounter);
                                linha = escritor.atualizarLinha(dadosCadeia, linha);
                                linha = linha.trim();

                                if(!saida.startsWith("a")){
                                 lexicos.add(saida);                                    
                                }else{
                                    saida = saida.substring(1);
                                    erros = erros.concat(saida);
                                }
                                estado = 0;
                                break;
                                
                            case 11://Aspas abertas
                                erros = erros.concat(lineCounter + " " + "CMF" + " " + linha + "\n");
                                linha = linha.substring(linha.length());
                                linha = linha.trim();
                                estado = 0;
                                break;
                                
                            case 12://Simbolo invalido
                                erros = erros.concat(lineCounter + " " + "SIB" + " " + linha.charAt(0) + "\n");
                                linha = linha.substring(1);
                                linha = linha.trim();
                                estado = 0;
                                break;
                                
                    }//Fim Do Switch Case 
                    if(linha == null){
                        estado = 0;
                        break;
                    }
                }//Fim primeiro While                
                saida = null;                
                linha = lerArq.readLine();                               
                lineCounter++;
            }//Fim segundo While
            
            if(flagComentBloco == 0 && erros == ""){
                System.out.println("SUCESSO NO ARQUIVO" + j);
            }  
              if(!erros.isEmpty()){
              lexicos.add("\n\n");             
              lexicos.add(erros);
                }
              tamanhoDosLexicos = lexicos.size();
              dadosTabela = (LinkedList)lexicos.clone();
              /////////////////////////////////////////////////////////////////////////////////
              //saidaSintatico = analiseSintatica.analise(lexicos);//Inicio da analise sintatica
              /////////////////////////////////////////////////////////////////////////////////
              //if(saidaSintatico.size() != tamanhoDosLexicos){                  
                  //System.out.println("OCORRERAM ERROS NA VALIDAÇÃO SINTATICA");
              //}else{
                  //System.out.println("VALIDAÇÂO SINTATICA COM SUCESSO");
              //}
              
              tabelaDeSimbolos = gerador.gerarTabela(dadosTabela);
              errosTabela = gerador.errosDuplicidade();
              //TESTE
              
              //System.out.println(errosTabela.get(0));
              System.out.println("-----------------------------------------");
              
                            
              
              saidaSemantico = analiseSemantica.analiseSemantica(lexicos, tabelaDeSimbolos, errosTabela);
              
              for(int counter = 0; counter < saidaSemantico.size(); counter++){
              String impressao = saidaSemantico.get(counter);
              gravarSaida.printf(impressao);
              }
              
              saidaSemantico.clear();
              
                                                        
             if(flagComentBloco == 1){
                    gravarSaida.printf(guardaComentarios);
                    flagComentBloco = 0;
                }
             lineCounter = 1;
             arq.close();
             arqSaida.close();             
        }catch(IOException e){
             System.err.printf("Erro na abertura do arquivo: %s.\n",
            e.getMessage());
        }                
    }//Fim do FOR
    
}
}
