echo [INFO] Copy dependencies to web/WEB-INF/lib.
cd..
call mvn dependency:copy-dependencies -DoutputDirectory=lib -DexcludeScope=runtime
call mvn dependency:copy-dependencies -DoutputDirectory=web/WEB-INF/lib  -DincludeScope=runtime

pause