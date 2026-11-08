# Game Library Frontend

Mini-frontend desarrollado con React y Vite para mostrar información de videojuegos obtenida desde una API pública.

Este proyecto corresponde a la actividad opcional de la Semana 5 de Desarrollo Fullstack.

## Tecnologías

* React
* Vite
* JavaScript
* CSS
* Fetch API

## Funcionalidad

La aplicación consume una API pública de videojuegos y muestra los datos en una interfaz organizada mediante tarjetas.

La aplicación maneja tres estados principales:

* **Cargando:** se muestra un mensaje mientras se consultan los datos.
* **Datos:** se muestran los videojuegos obtenidos desde la API.
* **Error:** se muestra un mensaje si ocurre un problema al realizar la petición.

## API utilizada

La aplicación consume la siguiente API:

```text
https://api.sampleapis.com/switch/games
```

La petición se realiza utilizando `fetch` dentro de `useEffect`.

## Estructura principal

```text
src
├── App.jsx
├── App.css
├── index.css
└── main.jsx
```

## Ejecutar el proyecto

Instalar las dependencias:

```bash
npm install
```

Iniciar el servidor de desarrollo:

```bash
npm run dev
```

Abrir en el navegador:

```text
http://localhost:5173
```

## Diseño

La interfaz utiliza un diseño oscuro con tarjetas responsivas para presentar los videojuegos de forma clara.

Cada tarjeta muestra:

* Nombre del videojuego.
* ID.
* Precio disponible en la API.

El diseño se adapta a diferentes tamaños de pantalla mediante **CSS Grid** y **media queries**.
