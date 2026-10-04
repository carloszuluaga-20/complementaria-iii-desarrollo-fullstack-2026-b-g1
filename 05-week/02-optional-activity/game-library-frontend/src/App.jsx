import { useEffect, useState } from 'react'
import './App.css'

function App() {
  const [juegos, setJuegos] = useState([])
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetch('https://api.sampleapis.com/switch/games')
      .then((response) => {
        if (!response.ok) {
          throw new Error('No fue posible cargar los videojuegos')
        }

        return response.json()
      })
      .then((data) => {
        setJuegos(data.slice(0, 12))
        setCargando(false)
      })
      .catch(() => {
        setError('Ocurrió un error al consultar la API')
        setCargando(false)
      })
  }, [])

  return (
    <div className="app">
      <header className="header">
        <h1>Game Library</h1>
        <p>Biblioteca de videojuegos consumida desde una API pública</p>
      </header>

      <main className="contenido">
        {cargando && (
          <div className="estado">
            <h2>Cargando videojuegos...</h2>
            <p>Por favor espera mientras consultamos la API.</p>
          </div>
        )}

        {error && (
          <div className="estado error">
            <h2>Error</h2>
            <p>{error}</p>
          </div>
        )}

        {!cargando && !error && (
          <>
            <h2>Videojuegos disponibles</h2>

            <div className="grid">
              {juegos.map((juego) => (
                <article className="card" key={juego.id}>
                  <h3>{juego.name || 'Videojuego'}</h3>

                  <p>
                    <strong>ID:</strong> {juego.id}
                  </p>

                  <p>
                    <strong>Precio:</strong>{' '}
                    {juego.price ? `$${juego.price}` : 'No disponible'}
                  </p>
                </article>
              ))}
            </div>
          </>
        )}
      </main>
    </div>
  )
}

export default App