@echo off
echo ========================================
echo      TSLping 插件构建脚本
echo ========================================

echo 正在清理旧文件...
if exist target rmdir /s /q target

echo 正在编译项目...
mvn clean compile

if %ERRORLEVEL% NEQ 0 (
    echo 编译失败！请检查代码错误。
    pause
    exit /b 1
)

echo 正在打包插件...
mvn package

if %ERRORLEVEL% NEQ 0 (
    echo 打包失败！
    pause
    exit /b 1
)

echo ========================================
echo 构建完成！
echo 插件文件位置: target\TSLping-1.0.jar
echo ========================================

if exist target\TSLping-1.0.jar (
    echo 插件大小:
    for %%A in (target\TSLping-1.0.jar) do echo %%~zA bytes
    echo.
    echo 现在可以将 TSLping-1.0.jar 复制到服务器的 plugins 目录了！
) else (
    echo 警告：找不到生成的jar文件！
)

pause
