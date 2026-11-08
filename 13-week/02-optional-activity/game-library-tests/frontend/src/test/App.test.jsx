
import {
  beforeEach,
  afterEach,
  describe,
  expect,
  it,
  vi,
} from 'vitest'

import {
  cleanup,
  fireEvent,
  render,
  screen,
  waitFor,
} from '@testing-library/react'

import App from '../App.jsx'

describe('Game Library - Frontend Tests', () => {

  beforeEach(() => {
    sessionStorage.clear()
    vi.restoreAllMocks()
    vi.unstubAllGlobals()
  })

  afterEach(() => {
    cleanup()
    vi.unstubAllGlobals()
  })

  it('displays the login form', () => {
    render(<App />)

    expect(
      screen.getByRole('heading', {
        name: /game library/i,
      })
    ).toBeInTheDocument()

    expect(
      screen.getByLabelText(/correo electrónico/i)
    ).toBeInTheDocument()

    expect(
      screen.getByLabelText(/contraseña/i)
    ).toBeInTheDocument()
  })

  it('displays an error when login credentials are invalid', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue({
        ok: false,
        status: 401,
      })
    )

    render(<App />)

    fireEvent.change(
      screen.getByLabelText(/correo electrónico/i),
      {
        target: {
          value: 'incorrecto@example.com',
        },
      }
    )

    fireEvent.change(
      screen.getByLabelText(/contraseña/i),
      {
        target: {
          value: 'Incorrecta123!',
        },
      }
    )

    fireEvent.click(
      screen.getByRole('button', {
        name: /entrar a mi biblioteca/i,
      })
    )

    expect(
      await screen.findByRole('alert')
    ).toHaveTextContent(/sesion no es valida/i)
  })

  it('loads the user library after successful login', async () => {
    const fetchMock = vi.fn()
      .mockResolvedValueOnce({
        ok: true,
        status: 200,
        json: async () => ({
          token: 'test-jwt-token',
          tipo: 'Bearer',
        }),
      })
      .mockResolvedValue({
        ok: true,
        status: 200,
        json: async () => [
          {
            id: 1,
            titulo: 'Minecraft',
            genero: 'Sandbox',
            estado: 'Jugando',
            progreso: 45,
          },
        ],
      })

    vi.stubGlobal('fetch', fetchMock)

    render(<App />)

    fireEvent.change(
      screen.getByLabelText(/correo electrónico/i),
      {
        target: {
          value: 'usuarioa@example.com',
        },
      }
    )

    fireEvent.change(
      screen.getByLabelText(/contraseña/i),
      {
        target: {
          value: 'Prueba12345!',
        },
      }
    )

    fireEvent.click(
      screen.getByRole('button', {
        name: /entrar a mi biblioteca/i,
      })
    )

    expect(
      await screen.findByText('Minecraft')
    ).toBeInTheDocument()

    expect(
      screen.getByText('45%')
    ).toBeInTheDocument()

    await waitFor(() => {
      expect(fetchMock).toHaveBeenCalledWith(
        'http://localhost:8080/api/juegos',
        expect.objectContaining({
          headers: expect.objectContaining({
            Authorization: 'Bearer test-jwt-token',
          }),
        })
      )
    })
  })
})
