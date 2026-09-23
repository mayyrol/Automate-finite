# Reglas del proyecto 

## Arquitectura
- Este proyecto sigue el patrón MVC: `controller/`, `model/`, `view/`.
- No mezclar lógica de negocio (model) con lógica de presentación (view).

## Estándares de código
- Todo método público nuevo debe incluir un comentario Javadoc breve.
- Usar nombres descriptivos en español o inglés, pero consistentes con el resto del proyecto.

## Pruebas
- Cualquier nueva funcionalidad en `model/` debe incluir al menos una prueba unitaria.

## Cambios sensibles
- No modificar `pom.xml`, `.mvn/` ni los scripts `run.sh` / `run.cmd` sin aprobación explícita.