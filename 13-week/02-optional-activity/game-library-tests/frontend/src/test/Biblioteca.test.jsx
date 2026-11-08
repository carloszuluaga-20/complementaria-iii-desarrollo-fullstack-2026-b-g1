import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { cleanup, fireEvent, render, screen } from '@testing-library/react'

import App from '../App.jsx'

// Simula que el usuario ya inició sesión (el token vive en sessionStorage).
function iniciarSesionConToken() {
  sessionStorage.setItem('gameLibraryToken', 'token-de-prueba')
}

// Respuesta falsa de fetch con estado OK y un cuerpo JSON.
function respuestaOk(datos) {
  return { ok: true, status: 200, json: async () => datos }
}

const juegosDeEjemplo = [
  { id: 1, titulo: 'Minecraft', genero: 'Sandbox', estado: 'Jugando', progreso: 45 },
  { id: 2, titulo: 'Hades', genero: 'Roguelike', estado: 'Completado', progreso: 100 },
  { id: 3, titulo: 'Celeste', genero: 'Plataformas', estado: 'Pendiente', progreso: 0 },
]

describe('Game Library - componente App (biblioteca)', () => {
  beforeEach(() => {
    sessionStorage.clear()
  })

  afterEach(() => {
    cleanup()
    vi.unstubAllGlobals()
  })

  // ---------- Camino feliz ----------

  it('muestra las estadísticas calculadas a partir de los juegos', async () => {
    iniciarSesionConToken()
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(respuestaOk(juegosDeEjemplo)))

    render(<App />)

    await screen.findByText('Minecraft')

    expect(screen.getByText('Total de juegos').closest('.stat')).toHaveTextContent('3')
    expect(screen.getByText('Jugando', { selector: 'span:not(.etiqueta)' })
      .closest('.stat')).toHaveTextContent('1')
    expect(screen.getByText('Completados').closest('.stat')).toHaveTextContent('1')
  })

  it('cierra la sesión: borra el token y vuelve al login', async () => {
    iniciarSesionConToken()
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(respuestaOk(juegosDeEjemplo)))

    render(<App />)
    await screen.findByText('Minecraft')

    fireEvent.click(screen.getByRole('button', { name: /cerrar sesión/i }))

    expect(sessionStorage.getItem('gameLibraryToken')).toBeNull()
    expect(screen.getByRole('button', { name: /entrar a mi biblioteca/i }))
      .toBeInTheDocument()
  })

  // ---------- Casos de error / límite ----------

  it('muestra el estado vacío cuando el usuario no tiene juegos', async () => {
    iniciarSesionConToken()
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(respuestaOk([])))

    render(<App />)

    expect(await screen.findByText(/aún no tienes videojuegos/i)).toBeInTheDocument()
  })

  it('muestra un error de conexión cuando el servidor no responde', async () => {
    iniciarSesionConToken()
    // fetch rechaza la promesa, como pasa cuando Spring Boot está apagado.
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new TypeError('Failed to fetch')))

    render(<App />)

    expect(await screen.findByRole('alert'))
      .toHaveTextContent(/no se pudo conectar con el servidor/i)
  })

  it('cierra la sesión automáticamente si el token expiró (401)', async () => {
    iniciarSesionConToken()
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ ok: false, status: 401 }))

    render(<App />)

    expect(await screen.findByRole('alert'))
      .toHaveTextContent(/tu sesion expiro/i)
    expect(sessionStorage.getItem('gameLibraryToken')).toBeNull()
  })

  it('no elimina el juego si el usuario cancela la confirmación', async () => {
    iniciarSesionConToken()
    const fetchMock = vi.fn().mockResolvedValue(respuestaOk(juegosDeEjemplo))
    vi.stubGlobal('fetch', fetchMock)
    vi.spyOn(window, 'confirm').mockReturnValue(false)

    render(<App />)
    await screen.findByText('Minecraft')

    fireEvent.click(screen.getAllByRole('button', { name: /eliminar/i })[0])

    // Solo debe existir la llamada inicial GET; ninguna llamada DELETE.
    const llamadasDelete = fetchMock.mock.calls.filter(
      ([, opciones]) => opciones?.method === 'DELETE'
    )
    expect(llamadasDelete).toHaveLength(0)
    expect(screen.getByText('Minecraft')).toBeInTheDocument()
  })

  it('informa que el correo ya está registrado (409) al crear la cuenta', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ ok: false, status: 409 }))

    render(<App />)

    fireEvent.click(screen.getByRole('button', { name: /^registrarse$/i }))
    fireEvent.change(screen.getByLabelText(/nombre/i), { target: { value: 'Ana' } })
    fireEvent.change(screen.getByLabelText(/correo electrónico/i), {
      target: { value: 'ana@example.com' },
    })
    fireEvent.change(screen.getByLabelText(/contraseña/i), {
      target: { value: 'Prueba12345!' },
    })
    fireEvent.click(screen.getByRole('button', { name: /crear cuenta/i }))

    expect(await screen.findByRole('alert'))
      .toHaveTextContent(/el correo ya esta registrado/i)
  })
})
