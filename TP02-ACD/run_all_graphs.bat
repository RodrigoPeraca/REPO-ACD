@echo off
cls
echo ================================================
echo Compilando projeto Java...
echo ================================================
javac graph\*.java
if %errorlevel% neq 0 (
    echo ERRO: Falha ao compilar o código Java.
    pause
    exit /b
)

echo.
echo ================================================
echo Executando todos os grafos do ZIP
echo ================================================
echo.

set GRAPHS_DIR="graphs"
set LOGFILE=execucao_grafos.log

echo Limpando log anterior...
del %LOGFILE% >nul 2>&1

echo Iniciando execuções... > %LOGFILE%

for %%F in (%GRAPHS_DIR%\*.gr) do (
    echo -------------------------------------------------- >> %LOGFILE%
    echo Executando arquivo: %%F >> %LOGFILE%
    echo -------------------------------------------------- >> %LOGFILE%

    echo Rodando %%F ...
    
    REM EXECUTA O MAIN DE FORMA AUTOMÁTICA
    REM Entrada simulada: 1 (carregar grafo) depois o caminho do arquivo,
    REM depois "true" para direcionado, "true" para ponderado, depois "0" para sair

    echo 1> temp_input.txt
    echo %%F>> temp_input.txt
    echo true>> temp_input.txt
    echo true>> temp_input.txt
    echo 0>> temp_input.txt

    java graph.Main < temp_input.txt >> %LOGFILE%

    del temp_input.txt
)

echo.
echo ================================================
echo Execução concluída!
echo Resultados salvos em: %LOGFILE%
echo ================================================
pause
