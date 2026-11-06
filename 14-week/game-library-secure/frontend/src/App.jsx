
import { useCallback, useEffect, useState } from 'react'
import './App.css'

const API_URL = 'http://localhost:8080/api'

const formularioVacio = {
  titulo: '',
  genero: '',
  estado: 'Pendiente',
  progreso: 0,
}

async function solicitar(ruta, opciones = {}, token = null) {
  let respuesta

  try {
    respuesta = await fetch(`${API_URL}${ruta}`, {
      ...opciones,
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...opciones.headers,
      },
    })
  } catch {
    throw new Error(
      'No se pudo conectar con el servidor. Verifica que Spring Boot este funcionando.'
    )
  }

  if (!respuesta.ok) {
    if (respuesta.status === 401) {
      throw new Error('Tu sesion no es valida o ha expirado.')
    }

    if (respuesta.status === 403) {
      throw new Error('No tienes permiso para realizar esta accion.')
    }

    if (respuesta.status === 404) {
      throw new Error('No se encontro el videojuego solicitado.')
    }

    if (respuesta.status === 409) {
      throw new Error('El correo ya esta registrado.')
    }

    if (respuesta.status === 400) {
      throw new Error('Revisa los datos ingresados.')
    }

    throw new Error(`Error del servidor: HTTP ${respuesta.status}`)
  }

  if (respuesta.status === 204) return null

  return respuesta.json()
}

function App() {
  const [token, setToken] = useState(
    () => sessionStorage.getItem('gameLibraryToken') || ''
  )
  const [pantalla, setPantalla] = useState('login')
  const [credenciales, setCredenciales] = useState({
    nombre: '',
    email: '',
    password: '',
  })
  const [juegos, setJuegos] = useState([])
  const [formulario, setFormulario] = useState(formularioVacio)
  const [editandoId, setEditandoId] = useState(null)
  const [cargando, setCargando] = useState(false)
  const [guardando, setGuardando] = useState(false)
  const [error, setError] = useState('')
  const [mensaje, setMensaje] = useState('')

  const cerrarSesion = useCallback(() => {
    sessionStorage.removeItem('gameLibraryToken')
    setToken('')
    setJuegos([])
    setFormulario({ ...formularioVacio })
    setEditandoId(null)
    setPantalla('login')
    setCredenciales({ nombre: '', email: '', password: '' })
    setError('')
    setMensaje('')
  }, [])

  const cargarJuegos = useCallback(async () => {
    if (!token) return

    setCargando(true)
    setError('')

    try {
      const datos = await solicitar('/juegos', {}, token)
      setJuegos(datos)
    } catch (err) {
      if (err.message.includes('sesion')) {
        cerrarSesion()
        setError('Tu sesion expiro. Inicia sesion nuevamente.')
      } else {
        setError(err.message)
      }
    } finally {
      setCargando(false)
    }
  }, [token, cerrarSesion])

  useEffect(() => {
    cargarJuegos()
  }, [cargarJuegos])

  async function autenticar(evento) {
    evento.preventDefault()
    setError('')
    setMensaje('')
    setGuardando(true)

    try {
      if (pantalla === 'registro') {
        await solicitar('/auth/register', {
          method: 'POST',
          body: JSON.stringify(credenciales),
        })

        setPantalla('login')
        setCredenciales((actual) => ({
          ...actual,
          password: '',
        }))
        setMensaje('Cuenta creada. Ahora inicia sesion.')
      } else {
        const datos = await solicitar('/auth/login', {
          method: 'POST',
          body: JSON.stringify({
            email: credenciales.email,
            password: credenciales.password,
          }),
        })

        sessionStorage.setItem('gameLibraryToken', datos.token)
        setToken(datos.token)
        setCredenciales((actual) => ({
          ...actual,
          password: '',
        }))
      }
    } catch (err) {
      setError(err.message)
    } finally {
      setGuardando(false)
    }
  }

  function editarJuego(juego) {
    setEditandoId(juego.id)
    setFormulario({
      titulo: juego.titulo,
      genero: juego.genero,
      estado: juego.estado,
      progreso: juego.progreso,
    })
    setError('')
    setMensaje('')
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  function cancelarEdicion() {
    setEditandoId(null)
    setFormulario({ ...formularioVacio })
  }

  async function guardarJuego(evento) {
    evento.preventDefault()
    setError('')
    setMensaje('')
    setGuardando(true)

    try {
      const editando = editandoId !== null

      await solicitar(
        editando ? `/juegos/${editandoId}` : '/juegos',
        {
          method: editando ? 'PUT' : 'POST',
          body: JSON.stringify({
            ...formulario,
            progreso: Number(formulario.progreso),
          }),
        },
        token
      )

      cancelarEdicion()
      setMensaje(
        editando
          ? 'Videojuego actualizado correctamente.'
          : 'Videojuego agregado a tu biblioteca.'
      )
      await cargarJuegos()
    } catch (err) {
      setError(err.message)
    } finally {
      setGuardando(false)
    }
  }

  async function eliminarJuego(id) {
    if (!window.confirm('¿Quieres eliminar este videojuego?')) {
      return
    }

    setError('')
    setMensaje('')

    try {
      await solicitar(`/juegos/${id}`, { method: 'DELETE' }, token)
      setJuegos((actuales) =>
        actuales.filter((juego) => juego.id !== id)
      )

      if (editandoId === id) cancelarEdicion()
      setMensaje('Videojuego eliminado correctamente.')
    } catch (err) {
      setError(err.message)
    }
  }

  const completados = juegos.filter(
    (juego) => juego.estado === 'Completado'
  ).length

  const jugando = juegos.filter(
    (juego) => juego.estado === 'Jugando'
  ).length

  if (!token) {
    return (
      <div className="app auth-layout">
        <main className="auth-card">
          <div className="logo">🎮</div>
          <h1>Game Library</h1>
          <p className="subtitulo">
            Tu biblioteca personal de videojuegos
          </p>

          <div className="auth-tabs">
            <button
              type="button"
              className={pantalla === 'login' ? 'activo' : ''}
              onClick={() => {
                setPantalla('login')
                setError('')
                setMensaje('')
              }}
            >
              Iniciar sesión
            </button>
            <button
              type="button"
              className={pantalla === 'registro' ? 'activo' : ''}
              onClick={() => {
                setPantalla('registro')
                setError('')
                setMensaje('')
              }}
            >
              Registrarse
            </button>
          </div>

          <form onSubmit={autenticar} className="formulario">
            {pantalla === 'registro' && (
              <label>
                Nombre
                <input
                  type="text"
                  required
                  maxLength={100}
                  value={credenciales.nombre}
                  onChange={(e) =>
                    setCredenciales({
                      ...credenciales,
                      nombre: e.target.value,
                    })
                  }
                  placeholder="Tu nombre"
                />
              </label>
            )}

            <label>
              Correo electrónico
              <input
                type="email"
                required
                value={credenciales.email}
                onChange={(e) =>
                  setCredenciales({
                    ...credenciales,
                    email: e.target.value,
                  })
                }
                placeholder="correo@ejemplo.com"
              />
            </label>

            <label>
              Contraseña
              <input
                type="password"
                required
                minLength={pantalla === 'registro' ? 8 : undefined}
                value={credenciales.password}
                onChange={(e) =>
                  setCredenciales({
                    ...credenciales,
                    password: e.target.value,
                  })
                }
                placeholder="Tu contraseña"
              />
            </label>

            {error && <p className="alerta error" role="alert">{error}</p>}
            {mensaje && (
              <p className="alerta exito" role="status">{mensaje}</p>
            )}

            <button
              className="btn principal"
              type="submit"
              disabled={guardando}
            >
              {guardando
                ? 'Procesando...'
                : pantalla === 'registro'
                  ? 'Crear cuenta'
                  : 'Entrar a mi biblioteca'}
            </button>
          </form>
        </main>
      </div>
    )
  }

  return (
    <div className="app">
      <header className="header">
        <div className="header-contenido">
          <div>
            <h1>🎮 Game Library</h1>
            <p>Administra tus videojuegos y tu progreso</p>
          </div>
          <button
            className="btn secundario"
            onClick={cerrarSesion}
          >
            Cerrar sesión
          </button>
        </div>
      </header>

      <main className="contenido">
        <section className="estadisticas">
          <div className="stat">
            <span>Total de juegos</span>
            <strong>{juegos.length}</strong>
          </div>
          <div className="stat">
            <span>Jugando</span>
            <strong>{jugando}</strong>
          </div>
          <div className="stat">
            <span>Completados</span>
            <strong>{completados}</strong>
          </div>
        </section>

        <section className="panel">
          <h2>
            {editandoId !== null
              ? 'Editar videojuego'
              : 'Agregar videojuego'}
          </h2>

          <form onSubmit={guardarJuego} className="formulario">
            <div className="form-grid">
              <label>
                Título
                <input
                  required
                  maxLength={120}
                  value={formulario.titulo}
                  onChange={(e) =>
                    setFormulario({
                      ...formulario,
                      titulo: e.target.value,
                    })
                  }
                  placeholder="Ej. Minecraft"
                />
              </label>

              <label>
                Género
                <input
                  required
                  value={formulario.genero}
                  onChange={(e) =>
                    setFormulario({
                      ...formulario,
                      genero: e.target.value,
                    })
                  }
                  placeholder="Ej. Aventura"
                />
              </label>

              <label>
                Estado
                <select
                  value={formulario.estado}
                  onChange={(e) =>
                    setFormulario({
                      ...formulario,
                      estado: e.target.value,
                    })
                  }
                >
                  <option>Pendiente</option>
                  <option>Jugando</option>
                  <option>Completado</option>
                </select>
              </label>

              <label>
                Progreso (%)
                <input
                  type="number"
                  required
                  min="0"
                  max="100"
                  value={formulario.progreso}
                  onChange={(e) =>
                    setFormulario({
                      ...formulario,
                      progreso: e.target.value,
                    })
                  }
                />
              </label>
            </div>

            <div className="acciones">
              <button
                className="btn principal"
                type="submit"
                disabled={guardando}
              >
                {guardando
                  ? 'Guardando...'
                  : editandoId !== null
                    ? 'Guardar cambios'
                    : 'Agregar juego'}
              </button>

              {editandoId !== null && (
                <button
                  type="button"
                  className="btn secundario"
                  onClick={cancelarEdicion}
                >
                  Cancelar
                </button>
              )}
            </div>
          </form>
        </section>

        {error && <p className="alerta error" role="alert">{error}</p>}
        {mensaje && (
          <p className="alerta exito" role="status">{mensaje}</p>
        )}

        <section className="biblioteca">
          <div className="titulo-seccion">
            <h2>Mis videojuegos</h2>
            <button
              className="btn secundario"
              onClick={cargarJuegos}
              disabled={cargando}
            >
              Actualizar
            </button>
          </div>

          {cargando ? (
            <div className="estado">Cargando biblioteca...</div>
          ) : juegos.length === 0 ? (
            <div className="estado">
              Aún no tienes videojuegos. ¡Agrega el primero!
            </div>
          ) : (
            <div className="grid">
              {juegos.map((juego) => (
                <article className="card" key={juego.id}>
                  <div className="card-top">
                    <span className="game-icon">🎮</span>
                    <span className="etiqueta">{juego.estado}</span>
                  </div>

                  <h3>{juego.titulo}</h3>
                  <p>Género: {juego.genero}</p>

                  <div className="progreso-info">
                    <span>Progreso</span>
                    <strong>{juego.progreso}%</strong>
                  </div>

                  <div
                    className="barra-progreso"
                    role="progressbar"
                    aria-label={`Progreso de ${juego.titulo}`}
                    aria-valuemin={0}
                    aria-valuemax={100}
                    aria-valuenow={juego.progreso}
                  >
                    <div
                      style={{
                        width: `${juego.progreso}%`,
                      }}
                    />
                  </div>

                  <div className="card-acciones">
                    <button
                      className="btn secundario"
                      onClick={() => editarJuego(juego)}
                    >
                      Editar
                    </button>
                    <button
                      className="btn peligro"
                      onClick={() => eliminarJuego(juego.id)}
                    >
                      Eliminar
                    </button>
                  </div>
                </article>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  )
}

export default App
