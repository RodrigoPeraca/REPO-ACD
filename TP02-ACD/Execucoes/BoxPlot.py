import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import re
import os

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
                
                # Juntar as partes restantes (pode ter vírgula no tempo)
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

print("\n=== AMOSTRA DOS DADOS CARREGADOS ===")
print("Dijkstra1 - Primeiras 3 linhas:")
print(df_dijkstra1.head(3))
print(f"\nFormato: {df_dijkstra1.shape}")

print("\nBellman - Primeiras 3 linhas:")
print(df_bellman.head(3))
print(f"Formato: {df_bellman.shape}")

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
print("\n=== PROCESSANDO DADOS ===")

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

print("\n=== DADOS PROCESSADOS ===")
print(f"Total de registros: {len(df_all)}")
print(f"Algoritmos: {df_all['algoritmo'].unique()}")
print(f"Tamanhos de amostra encontrados: {sorted(df_all['sample_size'].dropna().unique())}")

# Exemplo de dados convertidos
print("\nExemplo de dados convertidos (primeiras 3 linhas):")
print(df_all[['arquivo', 'execucao', 'tempo_ms', 'sample_size', 'algoritmo']].head(3))

# ========== FILTRAR PARA OS TAMANHOS DESEJADOS ==========
# Filtrar apenas tamanhos 100, 200, 500
df_filtrado = df_all[df_all['sample_size'].isin([100, 200, 500])].copy()

print(f"\n=== DADOS FILTRADOS (100, 200, 500) ===")
print(f"Registros após filtrar: {len(df_filtrado)}")

# Verificar se temos dados para cada tamanho
for size in [100, 200, 500]:
    for algo in ['Dijkstra', 'Bellman-Ford']:
        count = len(df_filtrado[(df_filtrado['sample_size'] == size) & (df_filtrado['algoritmo'] == algo)])
        print(f"Tamanho {size} - {algo}: {count} execuções")

# ========== CRIAR OS BOXPLOTS ==========
print("\n=== CRIANDO GRÁFICOS ===")

# Configurar estilo
plt.style.use('seaborn-v0_8-whitegrid')
fig, axes = plt.subplots(1, 3, figsize=(20, 7), sharey=True)

# Cores para cada algoritmo
cores = {'Dijkstra': '#1f77b4', 'Bellman-Ford': '#ff7f0e'}

# Ordenar por tamanho
sample_sizes = [100, 200, 500]

for idx, size in enumerate(sample_sizes):
    df_subset = df_filtrado[df_filtrado['sample_size'] == size]
    
    if len(df_subset) == 0:
        axes[idx].text(0.5, 0.5, f'Sem dados\npara tamanho {size}', 
                      ha='center', va='center', fontsize=12)
        axes[idx].set_title(f'Tamanho = {size}')
        continue
    
    # Criar boxplot
    boxplot = sns.boxplot(
        data=df_subset,
        x='algoritmo',
        y='tempo_ms',
        ax=axes[idx],
        palette=[cores['Dijkstra'], cores['Bellman-Ford']],
        width=0.6
    )
    
    # Adicionar pontos individuais (swarm plot) para ver distribuição
    sns.stripplot(
        data=df_subset,
        x='algoritmo',
        y='tempo_ms',
        ax=axes[idx],
        color='black',
        alpha=0.5,
        size=4,
        jitter=True
    )
    
    # Configurar eixos
    axes[idx].set_title(f'Tamanho da Amostra = {size}', fontsize=14, fontweight='bold')
    axes[idx].set_xlabel('Algoritmo', fontsize=12)
    
    if idx == 0:
        axes[idx].set_ylabel('Tempo de Execução (ms)', fontsize=12)
    else:
        axes[idx].set_ylabel('')
    
    # Melhorar grid
    axes[idx].grid(True, axis='y', linestyle='--', alpha=0.7)
    axes[idx].grid(True, axis='x', linestyle=':', alpha=0.3)
    
    # Adicionar valores médios
    for i, algo in enumerate(['Dijkstra', 'Bellman-Ford']):
        media = df_subset[df_subset['algoritmo'] == algo]['tempo_ms'].mean()
        axes[idx].text(i, df_subset['tempo_ms'].max() * 1.05, 
                      f'Média: {media:.1f} ms', 
                      ha='center', fontsize=10, color='darkred')

plt.suptitle('Comparação de Desempenho: Dijkstra vs Bellman-Ford', 
             fontsize=16, fontweight='bold', y=1.02)
plt.tight_layout()

# Salvar o gráfico
plt.savefig('comparacao_algoritmos_detalhada.png', dpi=300, bbox_inches='tight', facecolor='white')
print("\n✓ Gráfico salvo como 'comparacao_algoritmos_detalhada.png'")

plt.show()

# ========== ESTATÍSTICAS DETALHADAS ==========
print("\n" + "="*70)
print("ESTATÍSTICAS DETALHADAS (tempo em milissegundos)")
print("="*70)

for size in sample_sizes:
    print(f"\n{'='*50}")
    print(f"ANÁLISE PARA TAMANHO DA AMOSTRA: {size}")
    print(f"{'='*50}")
    
    for algo in ['Dijkstra', 'Bellman-Ford']:
        subset = df_filtrado[(df_filtrado['sample_size'] == size) & (df_filtrado['algoritmo'] == algo)]
        
        if len(subset) > 0:
            tempos = subset['tempo_ms']
            
            print(f"\n{algo}:")
            print(f"  {'─' * 40}")
            print(f"  • Execuções analisadas: {len(subset)}")
            print(f"  • Média: {tempos.mean():.2f} ms")
            print(f"  • Mediana: {tempos.median():.2f} ms")
            print(f"  • Desvio padrão: {tempos.std():.2f} ms")
            print(f"  • Coef. variação: {(tempos.std()/tempos.mean()*100):.1f}%")
            print(f"  • Mínimo: {tempos.min():.2f} ms")
            print(f"  • Máximo: {tempos.max():.2f} ms")
            print(f"  • Amplitude: {tempos.max() - tempos.min():.2f} ms")
            
            # Quartis
            q1 = tempos.quantile(0.25)
            q3 = tempos.quantile(0.75)
            print(f"  • Q1 (25%): {q1:.2f} ms")
            print(f"  • Q3 (75%): {q3:.2f} ms")
            print(f"  • IQR: {q3 - q1:.2f} ms")
        else:
            print(f"\n{algo}: Nenhum dado encontrado")

# ========== RESUMO COMPARATIVO ==========
print("\n" + "="*70)
print("RESUMO COMPARATIVO ENTRE ALGORITMOS")
print("="*70)

for size in sample_sizes:
    print(f"\nTamanho {size}:")
    
    dijkstra_data = df_filtrado[(df_filtrado['sample_size'] == size) & 
                                (df_filtrado['algoritmo'] == 'Dijkstra')]['tempo_ms']
    bellman_data = df_filtrado[(df_filtrado['sample_size'] == size) & 
                               (df_filtrado['algoritmo'] == 'Bellman-Ford')]['tempo_ms']
    
    if len(dijkstra_data) > 0 and len(bellman_data) > 0:
        media_dijkstra = dijkstra_data.mean()
        media_bellman = bellman_data.mean()
        
        diferenca = media_bellman - media_dijkstra
        percentual = (diferenca / media_dijkstra) * 100 if media_dijkstra > 0 else 0
        
        if diferenca > 0:
            mais_rapido = "Dijkstra"
            mais_lento = "Bellman-Ford"
        else:
            mais_rapido = "Bellman-Ford"
            mais_lento = "Dijkstra"
            diferenca = abs(diferenca)
            percentual = abs(percentual)
        
        print(f"  • {mais_rapido} é {diferenca:.1f} ms mais rápido que {mais_lento}")
        print(f"  • Diferença percentual: {percentual:.1f}%")
        
        # Teste de diferença (simplificado)
        if diferenca > (dijkstra_data.std() + bellman_data.std()):
            print(f"  • Diferença estatisticamente significativa: SIM")
        else:
            print(f"  • Diferença estatisticamente significativa: NÃO")
    else:
        print(f"  • Dados insuficientes para comparação")

print("\n" + "="*70)
print("ANÁLISE CONCLUÍDA")
print("="*70)