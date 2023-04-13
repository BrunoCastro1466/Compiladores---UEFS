/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores.problema;

/**
 *
 * @author Bruno Castro e Dermeval Neves
 */
public class Escritor {
    
    String saida;
    String dados;
    String regex;
    String token;
    String linha;
    int numeroDeLinha;
    
    
    
    Escritor(){
        
    }
    
    
    public String gerarSaida(String dados, String token, String tokenErro, String regex, int numeroDeLinha){     
     if(dados.matches(regex)){
         saida = numeroDeLinha + " " + token + " " + dados + "\n";
     }else{
         saida = "a" + numeroDeLinha + " " + tokenErro + " " + dados + "\n";
     }        
        return saida;
    }
    
    public String gerarComentario(String dados, String tokenErro, String regex, int numeroDeLinha){
        if(dados.matches(regex)){
            saida = "";
        }else{
            saida = numeroDeLinha + " " + tokenErro + " " + linha + "\n";
        }                
        return saida;
    }
    
    public String atualizarLinha(String dados, String linha){        
        linha = linha.substring(dados.length());
        
        return linha;
    }
    
    public String atualizarLinhaParaBlocos(String dados, String linha){        
        linha = linha.substring(dados.length()+2);
        
        return linha;
    }
    
    public String getSaida(){
        return saida;
    }
    
     public String getDados(){
        return dados;
    }
     
      public String getregex(){
        return regex;
    }
      
       public int getNumeroDeLinha(){
        return numeroDeLinha;
    }
                   
    public void setSaida(String saida){
        this.saida = saida;
    }
    
    public void setDados(String dados){
        this.dados = dados;
    }
    
    public void setregex(String regex){
        this.regex = regex;
    }
    
    public void setNumeroDeLinha(int numeroDeLinha){
        this.numeroDeLinha = numeroDeLinha;
    }
        
}
