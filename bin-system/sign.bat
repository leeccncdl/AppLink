@echo off

set FILE_NAME=AppLink.apk

if not exist ..\bin\%FILE_NAME% (
  echo [ERROR] %FILE_NAME% missing.
  goto end
)

if exist ..\bin\_%FILE_NAME% (
  echo Deleting _%FILE_NAME% ...
  del ..\bin\_%FILE_NAME%
)
if exist ..\bin\_%FILE_NAME% (
  echo [ERROR] File _%FILE_NAME% delete failed.
  goto end
)

echo Renaming file ...
rename ..\bin\%FILE_NAME% _%FILE_NAME%
if not exist ..\bin\_%FILE_NAME% (
  echo [ERROR] Rename file failed.
  goto end
)


echo Signing APK ...
java -jar src/signapk.rar src/platform.x509.pem src/platform.pk8 ..\bin\_%FILE_NAME% ..\bin\%FILE_NAME%
echo Deleting _%FILE_NAME% ...
del ..\bin\_%FILE_NAME%
if exist ..\bin\_%FILE_NAME% (
  echo [WARNING] File _%FILE_NAME% delete failed.
)
echo [SUCCESS]

:end
::pause