/**
 * Gerenciador de Algoritmos de Substituição de Páginas (FIFO, LFU, MFU, LRU, MRU)
 * @author Fernando Wanderley
 * @version 1.0 (2024)
 */

import java.util.*;

class Processo {
    private int codigo = -1;
    private int registro;
    private int frequencia = 0;

    public void setCodigo(int c)   { codigo = c;      } 
    public void setRegistro(int r) { registro = r;    }
    public void incrementar()      { frequencia++;    }
    public void zerar()            { frequencia = 0;  }

    public int getCodigo()         { return codigo;     }
    public int getRegistro()       { return registro;   }
    public int getFrequencia()     { return frequencia; }
}

class Memoria {
    private Processo p;
    private String historico;

    Memoria() { 
        p = new Processo(); 
        historico = "";
    }

    public void incrementarHistorico() { 
        if (p.getCodigo() == -1) {
            historico += "     "; 
        } else {
            historico += String.format("%-5d", p.getCodigo()); 
        }
    }

    public void iniciarHistorico() { historico = ""; }

    public Processo getProcesso() { return p;         }
    public String getHistorico()  { return historico; }
}

class Gerenciamento {
    private Memoria m[] = new Memoria[50];
    private int qtd;
    private String sequencia;
    private int erros;
    private int acertos;
    private String modoGerenciamento;
    
    private int obterIndice(int cod) {
        for(int i = 0; i < qtd; i++)
            if(m[i].getProcesso().getCodigo() == cod)
                return i;
        return -1;
    }

    private int indiceRegistroMaisAntigo() {
        int indice = 0;
        int menorRegistro = m[0].getProcesso().getRegistro();
        
        for(int i = 1; i < qtd; i++) {
            int registroAtual = m[i].getProcesso().getRegistro();
            if(registroAtual < menorRegistro) {
                menorRegistro = registroAtual;
                indice = i;
            }
        }
        return indice;
    }

    Gerenciamento() {
        for(int i = 0; i < 3; i++) 
            m[i] = new Memoria();
        qtd = 3;
    }

    public void ler(Scanner sc) {
        sequencia = sc.nextLine();
    }

    public void atualizarUnidades(int n) {
        if(n < 3) {
            System.out.println("O numero minimo de unidades de memoria eh 3.");
            return;
        }
        else if(n > 50) {
            System.out.println("O numero maximo de unidades de memoria eh 50.");
            return;
        }
        else if(n > qtd) {
            for(; qtd < n; qtd++) {
                m[qtd] = new Memoria();
            }
        }
        else {
            qtd = n;
        }
        System.out.println("Total de unidades atualizado com sucesso!");
    }
    
    public void gerenciar(String tipo) {
        if(sequencia == null) {
            System.out.println("Não há sequencia registrada para gerenciamento!");
            return;
        }

        tipo = tipo.toUpperCase();
        if(!tipo.equals("FIFO") && !tipo.equals("LFU") && !tipo.equals("MFU") && !tipo.equals("LRU") && !tipo.equals("MRU")) {
        System.out.println("Tipo " + tipo + " desconhecido.");
        return;
    }

        modoGerenciamento = tipo;
        acertos = 0;
        erros = 0;
        int numeroAtual = 0;
        boolean achouNumero = false;
        int indiceSequencia = -1;

        for(int i = 0; i < qtd; i++) {
            m[i].iniciarHistorico();
            m[i].getProcesso().zerar();
            Processo p = m[i].getProcesso();
            p.zerar();
            p.setCodigo(-1);
            p.setRegistro(0); 
        }

        for(int i = 0; i < sequencia.length(); i++) {
            char c = sequencia.charAt(i);
            if(Character.isDigit(c)) {
                numeroAtual = numeroAtual * 10 + (c - '0');
                achouNumero = true;
            }
            else if(achouNumero) {
                indiceSequencia++;
                processarNumero(numeroAtual, tipo, indiceSequencia);
                numeroAtual = 0;
                achouNumero = false;
            }
        }

        if(achouNumero) {
            indiceSequencia++;
            processarNumero(numeroAtual, tipo, indiceSequencia);
        }
    }

    private void processarNumero(int numero, String tipo, int indiceSequencia) {
        int indiceExistente = obterIndice(numero);
        
        if(indiceExistente >= 0) { 
            acertos++;
            m[indiceExistente].getProcesso().incrementar();
            if(tipo.equals("LRU") || tipo.equals("MRU")) {
                m[indiceExistente].getProcesso().setRegistro(indiceSequencia);
            }
        }
        else { 
            if(indiceSequencia < qtd) { 
                m[indiceSequencia].getProcesso().setCodigo(numero);
                m[indiceSequencia].getProcesso().setRegistro(indiceSequencia);
                m[indiceSequencia].getProcesso().incrementar();
                erros++;
            }
            else { 
                switch(tipo) {
                    case "FIFO":
                        substituirFIFO(numero, indiceSequencia);
                        break;
                    case "LFU":
                        substituirLFU(numero, indiceSequencia);
                        break;
                    case "MFU":
                        substituirMFU(numero, indiceSequencia);
                        break;
                    case "LRU":
                        substituirLRU(numero, indiceSequencia);
                        break;
                    case "MRU":
                        substituirMRU(numero, indiceSequencia);
                        break;
                }
            }
        }

        for(int j = 0; j < qtd; j++) {
            m[j].incrementarHistorico();
        }
    }

    private void substituirFIFO(int numero, int indiceSequencia) {
        int indice = indiceRegistroMaisAntigo();
        m[indice].getProcesso().setCodigo(numero);
        m[indice].getProcesso().setRegistro(indiceSequencia); 
        m[indice].getProcesso().incrementar();
        erros++;
    }

    private void substituirLFU(int numero, int indiceSequencia) {
        int indice = 0;
        int menorFreq = m[0].getProcesso().getFrequencia();
        int menorRegistro = m[0].getProcesso().getRegistro();

        for(int i = 1; i < qtd; i++) {
            if(m[i].getProcesso().getFrequencia() < menorFreq || 
              (m[i].getProcesso().getFrequencia() == menorFreq && 
               m[i].getProcesso().getRegistro() < menorRegistro)) {
                indice = i;
                menorFreq = m[i].getProcesso().getFrequencia();
                menorRegistro = m[i].getProcesso().getRegistro();
            }
        }

        m[indice].getProcesso().setCodigo(numero);
        m[indice].getProcesso().setRegistro(indiceSequencia);
        m[indice].getProcesso().zerar();
        m[indice].getProcesso().incrementar();
        erros++;
    }

    private void substituirMFU(int numero, int indiceSequencia) {
        int indice = 0;
        int maiorFreq = m[0].getProcesso().getFrequencia();
        int menorRegistro = m[0].getProcesso().getRegistro();

        for(int i = 1; i < qtd; i++) {
            if(m[i].getProcesso().getFrequencia() > maiorFreq || 
              (m[i].getProcesso().getFrequencia() == maiorFreq && 
               m[i].getProcesso().getRegistro() < menorRegistro)) {
                indice = i;
                maiorFreq = m[i].getProcesso().getFrequencia();
                menorRegistro = m[i].getProcesso().getRegistro();
            }
        }

        m[indice].getProcesso().setCodigo(numero);
        m[indice].getProcesso().setRegistro(indiceSequencia);
        m[indice].getProcesso().zerar();
        m[indice].getProcesso().incrementar();
        erros++;
    }

    private void substituirLRU(int numero, int indiceSequencia) {
        int indice = indiceRegistroMaisAntigo();
        m[indice].getProcesso().setCodigo(numero);
        m[indice].getProcesso().setRegistro(indiceSequencia);
        m[indice].getProcesso().zerar();
        m[indice].getProcesso().incrementar();
        erros++;
    }
    
    private void substituirMRU(int numero, int indiceSequencia) {
        int indice = 0;
        int maiorRegistro = m[0].getProcesso().getRegistro();
    
        for(int i = 1; i < qtd; i++) {
            int registroAtual = m[i].getProcesso().getRegistro();
            if(registroAtual > maiorRegistro) {
                maiorRegistro = registroAtual;
                indice = i;
            }
        }
    
        m[indice].getProcesso().setCodigo(numero);
        m[indice].getProcesso().setRegistro(indiceSequencia);
        m[indice].getProcesso().zerar();
        m[indice].getProcesso().incrementar();
        erros++;
}

    public void mostrar() {
        System.out.printf("Gerenciador %s\n", modoGerenciamento);
        for(int i = 0; i < qtd; i++) {
            System.out.printf("M%-2d %s\n", i + 1, m[i].getHistorico());
        }
        System.out.printf("Acertos: %d\nErros: %d\n", acertos, erros);
    }
}

public class GerenciadorDeAlgoritmos {
    private static Scanner sc = new Scanner(System.in);
    
    private static int menu() {
        int opc;
        
        do {
            System.out.println("Entre com a sua opcao:");
            System.out.println("1 - Inserir sequencia de processos");
            System.out.println("2 - Atualizar quantidade de unidades de memoria");
            System.out.println("3 - Gerenciar com FIFO");
            System.out.println("4 - Gerenciar com LFU");
            System.out.println("5 - Gerenciar com MFU");
            System.out.println("6 - Gerenciar com LRU");
            System.out.println("7 - Gerenciar com MRU"); 
            System.out.println("8 - Listar registros");
            System.out.println("9 - Sair");

            opc = sc.nextInt();
            sc.nextLine();
        } while(opc < 1 || opc > 9); 
        return opc;
    }

    private static void esperarOk() {
        String aux;
        do {
            aux = sc.nextLine().toUpperCase();
        } while (!aux.equals("OK"));
    }
    
    public static void main(String[] args) {
        Gerenciamento g = new Gerenciamento();
        boolean escolha = true;

        do {
            switch(menu()) {
                case 1: 
                    System.out.println("Digite a sequencia de codigos usando um separador nao numerico:");
                    g.ler(sc);
                    System.out.println("Sequencia inserida com sucesso!");
                    break;
                
                case 2: 
                    System.out.println("Digite a nova quantidade de unidades de memoria (minimo 3, maximo 50):");
                    int aux = sc.nextInt(); 
                    sc.nextLine();
                    g.atualizarUnidades(aux);
                    break;
                
                case 3: g.gerenciar("FIFO"); break;
                case 4: g.gerenciar("LFU"); break;
                case 5: g.gerenciar("MFU"); break;
                case 6: g.gerenciar("LRU"); break;
                case 7: g.gerenciar("MRU"); break;
                case 8: 
                    g.mostrar();
                    System.out.println("Digite 'ok' para voltar ao menu.");
                    esperarOk();
                    break;
                
                case 9: 
                    escolha = false;
                    System.out.println("Tenha um bom dia e volte sempre!");
                    break;
            }
        } while(escolha);
        sc.close();
    }
}