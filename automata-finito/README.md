# Autómata Finito

Aplicación de escritorio en Java para diseñar y simular autómatas finitos
deterministas (AFD).

El proyecto se desarrollará mediante **Spec-Driven Development**. Cada cambio
debe seguir este orden:

1. Especificación funcional y criterios de aceptación.
2. Plan técnico y decisiones de arquitectura.
3. Descomposición en tareas implementables.
4. Implementación y pruebas.
5. Validación contra la especificación.

## Requisitos

- Java 21 o posterior.
- Conexión a Internet solamente durante la primera compilación, para que Maven
  Wrapper descargue Maven.

No es necesario instalar Maven globalmente.

## Ejecución rápida

La aplicación requiere Java 21 o posterior. Compruébelo con:

```text
java -version
```

Si la carpeta entregada conserva `target/automata-finito-1.0.0-SNAPSHOT.jar`, no
se necesita Maven ni conexión a Internet.

### Windows

Desde PowerShell o CMD, dentro de la carpeta del proyecto:

```powershell
run.cmd
```

También se puede ejecutar directamente:

```powershell
java -jar target\automata-finito-1.0.0-SNAPSHOT.jar
```

### Linux

La máquina debe tener un entorno gráfico y Java 21 o posterior. Desde una terminal,
dentro de la carpeta del proyecto:

```bash
sh run.sh
```

También se puede ejecutar directamente:

```bash
java -jar target/automata-finito-1.0.0-SNAPSHOT.jar
```

## Compilar desde el código

La primera compilación necesita Internet para que Maven Wrapper descargue Maven.

### Windows

```powershell
.\mvnw.cmd clean package
java -jar target\automata-finito-1.0.0-SNAPSHOT.jar
```

### Linux

```bash
sh mvnw clean package
java -jar target/automata-finito-1.0.0-SNAPSHOT.jar
```

Opcionalmente, se pueden habilitar los scripts como ejecutables:

```bash
chmod +x mvnw run.sh
./run.sh
```

## Qué se debe compartir

Para entregar el código fuente, comparta la carpeta completa `automata-finito`,
incluidos `.mvn`, `src`, `target`, `mvnw`, `mvnw.cmd`, `pom.xml`, `run.sh` y
`run.cmd`. No debe compartir `node_modules` ni instalar Maven manualmente.

La carpeta externa `specs-automata-finito` contiene la documentación del desarrollo
y puede entregarse por separado si el profesor la solicita.

## Uso

1. Escriba el alfabeto como `ab`, `a,b`, `{a,b}` o `(a,b)`. Todas esas formas
   representan símbolos individuales y se mostrarán como `{a, b}`.
2. Seleccione **Estado** y haga clic en el tablero para crear estados.
3. Seleccione un estado y configure su nombre, si es inicial, si es final y su color.
4. Seleccione **Transición**, haga clic en el origen y después en el destino.
5. Seleccione uno o más símbolos en la ventana de transición. Los símbolos que ya
   tengan otro destino desde el origen aparecerán como ocupados.
6. Ingrese una cadena y seleccione **Validar cadena** para ver el recorrido.

Con **Seleccionar** puede arrastrar estados. Con **Eliminar** o la tecla Supr puede
borrar el elemento seleccionado. La cadena vacía representa ε.

## Documentación del desarrollo

- [Especificación funcional](../specs-automata-finito/001-editor-afd/spec.md)
- [Plan técnico](../specs-automata-finito/001-editor-afd/plan.md)
- [Tareas](../specs-automata-finito/001-editor-afd/tasks.md)
- [Verificación manual](../specs-automata-finito/001-editor-afd/manual-qa.md)
- [Trazabilidad](../specs-automata-finito/001-editor-afd/traceability.md)
- [Arquitectura MVC](ARCHITECTURE.md)
- [Corrección de transiciones y rediseño](../specs-automata-finito/002-mvc-transiciones-ui/spec.md)

La aplicación simula directamente la función de transición del AFD, carácter por
carácter. No utiliza expresiones regulares para determinar la aceptación.
