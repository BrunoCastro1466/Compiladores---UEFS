/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores.problema;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bruno
 */
public class ModeloTabela {
    int numeroTabela;
    String pais = new String();
    Map<String,String> tabela = new HashMap<String,String>();
    
    public Map getTabela(){
        return tabela;
    }
    
    public void addSymbol(String key, String value){
        tabela.put(key, value);
    }
    
    public String getPais(){
        return pais;
    }
    
    public void setPais(String pais){
        this.pais = pais;
    }
    
    public int getNumeroTabela(){
        return numeroTabela;
    }
    
    public void setNumeroTabela(int numeroTabela){
        this.numeroTabela = numeroTabela;
    }
}
