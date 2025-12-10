@echo off
cls

REM ==============================================
REM  Execução automática N vezes (CSV)
REM ==============================================

set GRAPHS_DIR=graphs
set OUTPUT_CSV=resultados.csv

echo Arquivos disponíveis em %GRAPHS_DIR%:
echo ------------------------------------------------
for %%A in (%GRAPHS_DIR%\*.gr) do (
    echo   %%~nxA
)
echo ------------------------------------------------

echo.
set /p SELECTED="Digite os grafos desejados (separados por espaço): "

echo.
set /p TIMES="Executar quantas vezes cada grafo? "

echo.
echo Escolha a representacao do grafo:
echo 1 - Matriz de Adjacencia
echo 2 - Lista de Adjacencia
set /p REPRESENT="Digite o numero da representacao: "

echo.
echo Escolha o algoritmo a executar:
echo 0 - Dijkstra
echo 1 - BFS
echo 2 - DFS
echo 3 - Bellman-Ford
echo.
set /p ALGO="Digite o numero do algoritmo: "

echo.
echo Compilando Java...
javac graph\*.java
if %errorlevel% neq 0 (
    echo ERRO ao compilar codigo Java.
    pause
    exit /b
)

echo Limpando resultados anteriores...
del %OUTPUT_CSV% >nul 2>&1
echo arquivo,execucao,tempo_ms > %OUTPUT_CSV%

echo.
echo ==============================================
echo Iniciando execucoes...
echo Algoritmo selecionado: %ALGO%
echo Representacao: %REPRESENT%
echo ==============================================
echo.

for %%G in (%SELECTED%) do (
    echo Rodando %%G...

    for /l %%i in (1,1,%TIMES%) do (

        REM Entrada simulada para o programa
        echo %REPRESENT%> temp_input.txt
        echo %GRAPHS_DIR%\%%G>> temp_input.txt
        echo %ALGO%>> temp_input.txt

        REM Cria script de execucao temporario
        echo @echo off > run_once.bat
        echo java graph.Main ^< temp_input.txt >> run_once.bat

        REM Mede tempo via PowerShell
        for /f "tokens=*" %%T in ('powershell -command "(Measure-Command { cmd /c run_once.bat }).TotalMilliseconds"') do (
            echo %%G,%%i,%%T >> %OUTPUT_CSV%
        )

        del temp_input.txt
        del run_once.bat
    )

    echo.
)

echo ==============================================
echo Finalizado.
echo Resultados salvos em: %OUTPUT_CSV%
echo ==============================================
pause
