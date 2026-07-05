@echo off
echo ============================================
echo   学生管理系统 - 数据库初始化
echo ============================================
echo.

set MYSQL="D:\MySQL\MySQL Server 8.0\bin\mysql.exe"
set PASSWORD=123456

echo [1/3] 创建数据库...
%MYSQL% -u root -p%PASSWORD% -e "CREATE DATABASE IF NOT EXISTS student_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>nul
if %errorlevel% neq 0 (
    echo 数据库创建失败！请检查MySQL密码是否正确。
    pause
    exit /b
)
echo 数据库创建成功！

echo [2/3] 创建数据表...
%MYSQL% -u root -p%PASSWORD% student_management < "%~dp0db\student_management.sql" 2>nul
if %errorlevel% neq 0 (
    echo 数据表创建失败！
    pause
    exit /b
)
echo 数据表创建成功！

echo [3/3] 验证...
%MYSQL% -u root -p%PASSWORD% student_management -e "SHOW TABLES;" 2>nul
echo.
echo ============================================
echo   初始化完成！
echo   数据库：student_management
echo   登录账号：admin / admin123
echo ============================================
pause
