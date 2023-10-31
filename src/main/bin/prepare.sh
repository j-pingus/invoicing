echo invoicing version ${project.version}
"$JAVA_HOME/bin/java" -jar invoicing-${project.version}.jar -excel excel/Exemple.xlsx -prepare
