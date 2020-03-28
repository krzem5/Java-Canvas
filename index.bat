echo off
echo NUL>_.class&&del /s /f /q *.class
cls
javac com/krzem/canvas/Canvas.java&&java com/krzem/canvas/Canvas
start /min cmd /c "echo NUL>_.class&&del /s /f /q *.class"