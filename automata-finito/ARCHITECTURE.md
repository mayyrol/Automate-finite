# Arquitectura Modelo–Vista–Controlador

## Modelo (`model`)

Contiene los datos y reglas del AFD:

- `Automaton`, `State`, `Transition` y `TransitionKey`.
- `AutomatonValidator` y `AutomatonSimulator`.
- `SimulationResult` y cada paso del recorrido.
- Apariencia y posición de estados mediante `StateVisual`.

El Modelo impone la regla determinista. La llave de una transición es
`(estado origen, símbolo)`, por lo que un símbolo solo puede tener un destino desde
cada estado. Sí se permiten diferentes símbolos hacia diferentes destinos.

## Controlador (`controller`)

`AutomatonController` recibe las operaciones de la interfaz, modifica el Modelo y
solicita simulaciones. `SymbolParser` traduce notaciones como `ab`, `a,b` y `{a,b}`
al mismo conjunto de caracteres.

## Vista (`view`)

Contiene únicamente la interacción Swing:

- ventana principal y tema visual;
- tablero y herramientas;
- paneles de alfabeto, propiedades y validación;
- selector visual de símbolos para transiciones;
- animación del recorrido.

La Vista consulta al Controlador y repinta el resultado. No escribe directamente
en las colecciones del autómata.

## Flujo de una validación

```text
Vista → Controlador → AutomatonSimulator → Modelo
  ↑                                      │
  └──── pasos, explicación y resultado ──┘
```

El simulador comienza en el estado inicial y consulta una transición por cada
carácter. Solo acepta cuando consumió toda la cadena y el último estado es final.

