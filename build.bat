@echo off
cls
if exist build rmdir /s /q build
mkdir build
cd src
javac -d ../build com/krzem/canvas/Main.java&&jar cvmf ../manifest.mf ../build/canvas.jar -C ../build *&&goto run
cd ..
goto end
:run
cd ..
pushd "build"
for /D %%D in ("*") do (
	rd /S /Q "%%~D"
)
for %%F in ("*") do (
	if /I not "%%~nxF"=="canvas.jar" del "%%~F"
)
popd
cls
java -jar build/canvas.jar
:end
