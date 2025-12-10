import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import re
import os


# ========== VERIFICAR SE OS ARQUIVOS EXISTEM ==========
import os

print("Verificando arquivos...")
arquivos_necessarios = ['resultados1.csv', 'resultados2.csv', 'resultados3.csv', 'resultados.csv']

for arquivo in arquivos_necessarios:
    if os.path.exists(arquivo):
        print(f"✓ {arquivo} - ENCONTRADO")
    else:
        print(f"✗ {arquivo} - NÃO ENCONTRADO")
        print(f"  Caminho completo testado: {os.path.abspath(arquivo)}")

# Verificar se todos existem
if all(os.path.exists(f) for f in arquivos_necessarios):
    print("\n✓ Todos os arquivos encontrados! Carregando dados...")
else:
    print("\n✗ Alguns arquivos não foram encontrados!")
    print("\nArquivos na pasta atual:")
    for f in os.listdir('.'):
        print(f"  - {f}")
    exit(1)
# ========== CARREGAR OS DADOS CORRETAMENTE ==========
# Função para ler os arquivos CSV com formato especial
def ler_csv_especial(caminho_arquivo):
    """Lê arquivos CSV onde tempo_ms tem vírgula decimal"""
    dados = []
    with open(caminho_arquivo, 'r', encoding='utf-8') as f:
        # Pular cabeçalho
        cabecalho = f.readline().strip()
        
        for linha in f:
            linha = linha.strip()
            if linha:
                # Separar por vírgula
                partes = linha.split(',')
                
                # As primeiras 2 partes são arquivo e execucao
                arquivo = partes[0]
                execucao = partes[1]
                
                # Juntar as partes restantes (paga ter vírgula no tempo)
                tempo = ','.join(partes[2:])
                
                dados.append([arquivo, execucao, tempo])
    
    # Criar DataFrame
    df = pd.DataFrame(dados, columns=['arquivo', 'execucao', 'tempo_ms'])
    return df

# Carregar os dados
print("Carregando dados...")
df_dijkstra1 = ler_csv_especial('resultados1.csv')
df_dijkstra2 = ler_csv_especial('resultados2.csv')
df_dijkstra3 = ler_csv_especial('resultados3.csv')
df_bellman = ler_csv_especial('resultados.csv')

# ========== FUNÇÕES AUXILIARES ==========
def extract_sample_size(filename):
    """Extrai o tamanho da amostra do nome do arquivo"""
    if isinstance(filename, str):
        match = re.match(r'sample(\d+)', filename)
        if match:
            return int(match.group(1))
    return None

def convert_time(time_str):
    """Converte string com vírgula decimal para float"""
    if isinstance(time_str, str):
        # Remover possíveis espaços
        time_str = time_str.strip()
        # Substituir vírgula por ponto para converter para float
        return float(time_str.replace(',', '.'))
    return float(time_str)

# ========== PROCESSAR OS DADOS ==========
print("Processando dados...")

# Converter tempo para float
df_dijkstra1['tempo_ms'] = df_dijkstra1['tempo_ms'].apply(convert_time)
df_dijkstra2['tempo_ms'] = df_dijkstra2['tempo_ms'].apply(convert_time)
df_dijkstra3['tempo_ms'] = df_dijkstra3['tempo_ms'].apply(convert_time)
df_bellman['tempo_ms'] = df_bellman['tempo_ms'].apply(convert_time)

# Extrair tamanho da amostra
df_dijkstra1['sample_size'] = df_dijkstra1['arquivo'].apply(extract_sample_size)
df_dijkstra2['sample_size'] = df_dijkstra2['arquivo'].apply(extract_sample_size)
df_dijkstra3['sample_size'] = df_dijkstra3['arquivo'].apply(extract_sample_size)
df_bellman['sample_size'] = df_bellman['arquivo'].apply(extract_sample_size)

# Adicionar coluna de algoritmo
df_dijkstra1['algoritmo'] = 'Dijkstra'
df_dijkstra2['algoritmo'] = 'Dijkstra'
df_dijkstra3['algoritmo'] = 'Dijkstra'
df_bellman['algoritmo'] = 'Bellman-Ford'

# Concatenar todos os dados Dijkstra
df_dijkstra = pd.concat([df_dijkstra1, df_dijkstra2, df_dijkstra3], ignore_index=True)

# Concatenar todos os dados
df_all = pd.concat([df_dijkstra, df_bellman], ignore_index=True)

# Converter execucao para int
df_all['execucao'] = df_all['execucao'].astype(int)

print(f"Total de registros: {len(df_all)}")

# ========== FILTRAR PARA OS TAMANHOS DESEJADOS ==========
# Filtrar apenas tamanhos 100, 200, 500
df_filtrado = df_all[df_all['sample_size'].isin([100, 200, 500])].copy()

print(f"Registros após filtrar (100, 200, 500): {len(df_filtrado)}")

# ========== CRIAR BOXPLOTS SEPARADOS ==========
print("\nCriando boxplots individuais...")

# Configurar estilo
plt.style.use('seaborn-v0_8-whitegrid')

# Cores para cada algoritmo
cores = {'Dijkstra': '#1f77b4', 'Bellman-Ford': '#ff7f0e'}

# Tamanhos de amostra
sample_sizes = [100, 200, 500]
nomes_tamanhos = {100: 'Cem', 200: 'Duzentas', 500: 'Quinhentas'}

for size in sample_sizes:
    print(f"\nCriando boxplot para tamanho {size}...")
    
    # Filtrar dados para este tamanho
    df_subset = df_filtrado[df_filtrado['sample_size'] == size]
    
    if len(df_subset) == 0:
        print(f"  Nenhum dado encontrado para tamanho {size}")
        continue
    
    # Criar figura individual
    fig, ax = plt.subplots(figsize=(10, 7))
    
    # Criar boxplot
    boxplot = sns.boxplot(
        data=df_subset,
        x='algoritmo',
        y='tempo_ms',
        ax=ax,
        palette=[cores['Dijkstra'], cores['Bellman-Ford']],
        width=0.5
    )
    
    # Renomear os labels no gráfico
    ax.set_xticklabels(['Dijkstra', 'Bellman-Ford'])
    
    # Adicionar pontos individuais para ver distribuição
    sns.stripplot(
        data=df_subset,
        x='algoritmo',
        y='tempo_ms',
        ax=ax,
        color='black',
        alpha=0.6,
        size=5,
        jitter=True
    )
    
    # Configurar título e labels
    ax.set_title(f'Comparação de Desempenho - {nomes_tamanhos[size]} Vertices', 
                 fontsize=16, fontweight='bold', pad=20)
    ax.set_xlabel('Algoritmo', fontsize=13)
    ax.set_ylabel('Tempo de Execução (ms)', fontsize=13)
    
    # Melhorar grid
    ax.grid(True, axis='y', linestyle='--', alpha=0.7)
    ax.grid(True, axis='x', linestyle=':', alpha=0.3)
    
    # Adicionar valores médios acima das caixas
    for i, algo in enumerate(['Dijstra', 'Bellman-Ford']):
        # Mapear o novo nome para o nome original nos dados
        nome_original = 'Dijkstra' if i == 0 else 'Bellman-Ford'
        subset_algo = df_subset[df_subset['algoritmo'] == nome_original]
        if len(subset_algo) > 0:
            media = subset_algo['tempo_ms'].mean()
            mediana = subset_algo['tempo_ms'].median()
            desvio = subset_algo['tempo_ms'].std()
            
            # Adicionar texto com estatísticas
            ax.text(i, df_subset['tempo_ms'].max() * 1.08, 
                   f'Média: {media:.1f} ms\nMediana: {mediana:.1f} ms', 
                   ha='center', fontsize=11, 
                   bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.8))
    
    # Adicionar contagem de execuções
    for i, algo in enumerate(['Dijstra', 'Bellman-Ford']):
        # Mapear o novo nome para o nome original nos dados
        nome_original = 'Dijkstra' if i == 0 else 'Bellman-Ford'
        count = len(df_subset[df_subset['algoritmo'] == nome_original])
        ax.text(i, ax.get_ylim()[0] - (ax.get_ylim()[1] - ax.get_ylim()[0]) * 0.05,
               f'n = {count}', 
               ha='center', fontsize=11, fontweight='bold')
    
    # Ajustar layout
    plt.tight_layout()
    
    # Salvar figura individual
    nome_arquivo = f'boxplot_{size}_vertices.png'
    plt.savefig(nome_arquivo, dpi=300, bbox_inches='tight', facecolor='white')
    print(f"  ✓ Salvo como: {nome_arquivo}")
    
    # Fechar a figura para liberar memória
    plt.close(fig)

# ========== CRIAR UM GRÁFICO COMPOSTO TAMBÉM (OPCIONAL) ==========
print("\nCriando gráfico composto (opcional)...")

fig, axes = plt.subplots(1, 3, figsize=(18, 6), sharey=True)

for idx, size in enumerate(sample_sizes):
    df_subset = df_filtrado[df_filtrado['sample_size'] == size]
    
    if len(df_subset) == 0:
        axes[idx].text(0.5, 0.5, f'Sem dados\n{size} Vertices', 
                      ha='center', va='center', fontsize=12)
        axes[idx].set_title(f'{size} Vertices')
        continue
    
    sns.boxplot(
        data=df_subset,
        x='algoritmo',
        y='tempo_ms',
        ax=axes[idx],
        palette=[cores['Dijkstra'], cores['Bellman-Ford']],
        width=0.6
    )
    
    # Renomear os labels no gráfico composto
    axes[idx].set_xticklabels(['Dijkstra', 'Bellman-Ford'])
    
    axes[idx].set_title(f'{size} Vertices', fontsize=14)
    axes[idx].set_xlabel('Algoritmo', fontsize=12)
    
    if idx == 0:
        axes[idx].set_ylabel('Tempo (ms)', fontsize=12)
    else:
        axes[idx].set_ylabel('')
    
    axes[idx].grid(True, axis='y', linestyle='--', alpha=0.7)

plt.suptitle('Comparação Dijkstra vs Bellman-Ford por Tamanho da Amostra', 
             fontsize=16, fontweight='bold', y=1.02)
plt.tight_layout()

# Salvar gráfico composto
plt.savefig('comparacao_todos_tamanhos.png', dpi=300, bbox_inches='tight', facecolor='white')
print("  ✓ Gráfico composto salvo como: comparacao_todos_tamanhos.png")

plt.show()

# ========== ESTATÍSTICAS DETALHADAS ==========
print("\n" + "="*60)
print("ESTATÍSTICAS DETALHADAS (tempo em ms)")
print("="*60)

for size in sample_sizes:
    print(f"\n{'='*40}")
    print(f"TAMANHO DA AMOSTRA: {size} Vertices")
    print(f"{'='*40}")
    
    for algo_novo, algo_original in [('Dijkstra', 'Dijkstra'), ('Bellman-Ford', 'Bellman-Ford')]:
        subset = df_filtrado[(df_filtrado['sample_size'] == size) & 
                            (df_filtrado['algoritmo'] == algo_original)]
        
        if len(subset) > 0:
            tempos = subset['tempo_ms']
            
            print(f"\n{algo_novo}:")
            print(f"  Execuções: {len(subset)}")
            print(f"  Média: {tempos.mean():.2f} ms")
            print(f"  Mediana: {tempos.median():.2f} ms")
            print(f"  Desvio padrão: {tempos.std():.2f} ms")
            print(f"  Mínimo: {tempos.min():.2f} ms")
            print(f"  Máximo: {tempos.max():.2f} ms")
            
            # Comparação percentual
            if algo_novo == 'Bellman Matrix':
                dijkstra_subset = df_filtrado[(df_filtrado['sample_size'] == size) & 
                                             (df_filtrado['algoritmo'] == 'Dijkstra')]
                if len(dijkstra_subset) > 0:
                    media_dijkstra = dijkstra_subset['tempo_ms'].mean()
                    media_bellman = tempos.mean()
                    diferenca = ((media_bellman - media_dijkstra) / media_dijkstra) * 100
                    print(f"  Diferença em relação a Bellman LDA: {diferenca:+.1f}%")
        else:
            print(f"\n{algo_novo}: Nenhum dado encontrado")

print("\n" + "="*60)
print("ANÁLISE CONCLUÍDA")
print("="*60)
print("\nArquivos gerados:")
print("1. boxplot_100_vertices.png")
print("2. boxplot_200_vertices.png")
print("3. boxplot_500_vertices.png")
print("4. comparacao_todos_tamanhos.png (opcional)")