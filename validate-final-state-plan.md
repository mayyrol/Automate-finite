# Plan: Validar si un estado es final

## Resumen

Agregar un método en `model/` que valide si un estado es final (accepting), junto con su prueba unitaria. La clase [`State`](automata-finito/src/main/java/com/compiladores/automata/model/State.java) ya tiene el campo `boolean accepting`; el método nuevo lo expondrá de forma semánticamente explícita. Se añadirá JUnit 5 al `pom.xml` con aprobación del usuario, y se creará la clase de prueba desde cero.

---

## Sub-tarea 1 — Agregar dependencia de JUnit 5 en `pom.xml`

**Intent**  
Habilitar la compilación y ejecución de pruebas unitarias con JUnit 5 (Jupiter). Sin esta dependencia, el código de prueba no puede compilarse.

**Expected Outcomes**  
- `pom.xml` contiene la dependencia `junit-jupiter` con `scope=test`.
- El proyecto sigue compilando sin errores (`mvn compile`).

**Todo List**  
- [ ] Agregar bloque `<dependencies>` con `org.junit.jupiter:junit-jupiter:5.11.0` en `scope=test`.
- [ ] Agregar el plugin `maven-surefire-plugin` (versión ≥ 3.2) para que Maven ejecute tests de JUnit 5.

**Relevant Context**  
- Archivo: [`pom.xml`](automata-finito/pom.xml)
- Modificación aprobada explícitamente por el usuario.

**Status**: [ ] pending

---

## Sub-tarea 2 — Agregar método `isFinalState()` en `State`

**Intent**  
Exponer de forma explícita y semánticamente clara si un estado es final/aceptador. El campo `accepting` ya existe en el record, pero un método con nombre de dominio (`isFinalState`) hace el código más legible en el contexto del autómata.

**Expected Outcomes**  
- [`State.java`](automata-finito/src/main/java/com/compiladores/automata/model/State.java) tiene un nuevo método público `isFinalState()` que devuelve el valor del campo `accepting`.
- El método incluye un comentario Javadoc breve (regla del proyecto).
- No se alteran los métodos ni campos existentes.

**Todo List**  
- [ ] Agregar el método `public boolean isFinalState()` con Javadoc en la clase `State`.

**Relevant Context**  
- Archivo: [`State.java`](automata-finito/src/main/java/com/compiladores/automata/model/State.java)
- La clase es un Java record; los métodos de instancia adicionales se agregan dentro del cuerpo del record sin problema.
- Convención: nombres en inglés, consistente con el resto de la clase (`renamedTo`, `withAccepting`).

**Status**: [ ] pending

---

## Sub-tarea 3 — Crear clase de prueba `StateTest`

**Intent**  
Cubrir el nuevo método `isFinalState()` con al menos una prueba unitaria, cumpliendo la regla: *"Cualquier nueva funcionalidad en `model/` debe incluir al menos una prueba unitaria."*

**Expected Outcomes**  
- Existe el archivo `src/test/java/com/compiladores/automata/model/StateTest.java`.
- La clase contiene pruebas que verifican:
  - `isFinalState()` devuelve `true` cuando `accepting=true`.
  - `isFinalState()` devuelve `false` cuando `accepting=false`.
- Las pruebas pasan con `mvn test`.

**Todo List**  
- [ ] Crear el directorio `src/test/java/com/compiladores/automata/model/` si no existe.
- [ ] Crear `StateTest.java` en el paquete `com.compiladores.automata.model` con anotaciones JUnit 5 (`@Test`, `Assertions`).
- [ ] Agregar dos casos de prueba: estado final y estado no final.

**Relevant Context**  
- Paquete destino: `com.compiladores.automata.model`
- Framework: JUnit 5 Jupiter (instalado en sub-tarea 1).
- Patrón de inmutabilidad: crear instancias de `State` directamente (record público).
- Idioma de mensajes/comentarios: español, consistente con el resto del proyecto.

**Status**: [ ] pending
