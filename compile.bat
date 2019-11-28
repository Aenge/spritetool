rmdir "out/mods" /s /q
dir /s /b src\*.java > sources.txt & javac --module-path "%PATH_TO_FX_MODS%;lib" -d out/mods/spritetool @sources.txt & del sources.txt
rmdir "out/OpenRSCSpriteTool" /s /q
jlink --module-path "%PATH_TO_FX_MODS%;lib;out/mods" --add-modules spritetool --output out/OpenRSCSpriteTool
copy "properscript" "out/OpenRSCSpriteTool/RUN.bat"
xcopy /E /I /S "src" "out/OpenRSCSpriteTool/src" /EXCLUDE:excluded.txt
xcopy /E /I /S "resource" "out/OpenRSCSpriteTool/resource"
cd out/OpenRSCSpriteTool/src
for /f "delims=" %%d in ('dir /s /b /ad ^| sort /r') do rd "%%d"