// All calls to the two backend services live here.
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const TRENDING_URL = import.meta.env.VITE_TRENDING_URL || 'http://localhost:8081'

async function request(url, { method = 'GET', body, token } = {}) {
  const headers = {}
  if (body !== undefined) headers['Content-Type'] = 'application/json'
  if (token) headers['Authorization'] = `Bearer ${token}`

  const response = await fetch(url, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  })

  if (!response.ok) {
    let message = `Request failed (${response.status})`
    try {
      const data = await response.json()
      if (data && data.message) message = data.message
    } catch {
      // no JSON body
    }
    const error = new Error(message)
    error.status = response.status
    throw error
  }

  if (response.status === 204) return null
  return response.json()
}

function query(params) {
  const search = new URLSearchParams()
  Object.entries(params || {}).forEach(([key, value]) => {
    if (value) search.set(key, value)
  })
  const text = search.toString()
  return text ? `?${text}` : ''
}

export const api = {
  // Movies (public)
  getMovies: (params) => request(`${API_URL}/api/movies${query(params)}`),
  getMovie: (id) => request(`${API_URL}/api/movies/${id}`),
  getGenres: () => request(`${API_URL}/api/movies/genres`),

  // Login
  register: (username, password) =>
    request(`${API_URL}/api/auth/register`, { method: 'POST', body: { username, password } }),
  login: (username, password) =>
    request(`${API_URL}/api/auth/login`, { method: 'POST', body: { username, password } }),

  // My List (login required)
  getMyList: (token) => request(`${API_URL}/api/my-list`, { token }),
  addToMyList: (id, token) => request(`${API_URL}/api/my-list/${id}`, { method: 'POST', token }),
  removeFromMyList: (id, token) => request(`${API_URL}/api/my-list/${id}`, { method: 'DELETE', token }),

  // Watching (login required)
  startWatching: (id, token) => request(`${API_URL}/api/watch/${id}/start`, { method: 'POST', token }),
  saveProgress: (id, progressPercent, token) =>
    request(`${API_URL}/api/watch/${id}`, { method: 'PUT', body: { progressPercent }, token }),
  getContinueWatching: (token) => request(`${API_URL}/api/watch/continue`, { token }),

  // Trending service (the Kafka consumer)
  getTrending: () => request(`${TRENDING_URL}/api/trending`),
}
