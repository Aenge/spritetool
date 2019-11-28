rmdir "out/mods" /s /q
dir /s /b src\*.java > sources.txt & javac --module-path "%PATH_TO_FX_MODS%;lib" -d out/mods/spritetool @sources.txt & del sources.txt
rmdir "out/OpenRSCSpriteTool" /s /q
jlink --module-path "%PATH_TO_FX_MODS%;lib;out/mods" --add-modules spritetool --output out/OpenRSCSpriteTool
copy "properscript" "out/OpenRSCSpriteTool/RUN.bat"
xcopy /E /I "src" "out/OpenRSCSpriteTool/src"
xcopy /E /I "resource" "out/OpenRSCSpriteTool/resource"