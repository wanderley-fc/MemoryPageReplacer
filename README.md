# Gerenciador de Algoritmos de Substituição de Páginas

*Simulador comparativo dos algoritmos FIFO, LFU, MFU, LRU e MRU para gerenciamento de memória em sistemas operacionais.*

## 📌 Funcionalidades

- Implementação dos 5 algoritmos clássicos:
  - **FIFO** *(First-In, First-Out)*
  - **LFU** *(Least Frequently Used)*
  - **MFU** *(Most Frequently Used)*
  - **LRU** *(Least Recently Used)*
  - **MRU** *(Most Recently Used)*
- Inserção de sequência de páginas (representadas por códigos)
- Configuração flexível (3-50 unidades de memória)
- Visualização gráfica do histórico de alocação
- Estatísticas de acertos/erros (page faults)



## 📋 Requisitos 

- **Java JDK 24 (ou alguma outra versão)**  
  [Download oficial - Adoptium](https://adoptium.net/en-GB/download/)  
  [Download direto (Windows)](https://github.com/adoptium/temurin24-binaries/releases/download/jdk-24.0.1%2B9/OpenJDK24U-jdk_x64_windows_hotspot_24.0.1_9.zip)



## ⚙️ Configuração

### Variáveis de ambiente (Windows)

```cmd
set JAVA_HOME=<caminho_para_o_jdk>
set PATH=%JAVA_HOME%\bin;%PATH%
```

### Variáveis de ambiente (Linux/macOS)

```bash
export JAVA_HOME=/caminho/para/o/jdk
export PATH=$JAVA_HOME/bin:$PATH
```


## 🚀 Como Executar

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/MemoryPageReplacer.git
cd MemoryPageReplacer
```

2. Compile e execute:
```bash
javac GerenciadorDeAlgoritmos.java
java GerenciadorDeAlgoritmos
```


## 🖥️ Interface do Programa
```
1 - Inserir sequência de processos
2 - Atualizar unidades de memória 
3 - Gerenciar com FIFO
4 - Gerenciar com LFU
5 - Gerenciar com MFU
6 - Gerenciar com LRU
7 - Gerenciar com MRU
8 - Listar registros
9 - Sair
```


## 📊 Exemplo de Saída
```
Gerenciador FIFO
M1  1    1    1    1    1    1    1    1    1    1    1    1
M2       2    2    2    2    2    2    2    2    2    2    2
M3            3    3    3    3    3    3    3    3    3    3
Acertos: 9
Erros: 3
```


## 📘 Licença

Este projeto é de uso **livre**, inclusive para fins comerciais ou acadêmicos, **desde que seja feita a devida atribuição ao autor**.

> Autor: **Fernando Cardoso Wanderley**  
> Referência: **Projeto Gerenciador de Algoritmos de Substituição de Páginas**
