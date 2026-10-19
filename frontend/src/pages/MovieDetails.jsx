import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { api } from '../api.js'
import { useAuth } from '../AuthContext.jsx'
import { posterStyle, formatDuration } from '../poster.js'
import MovieRow from '../components/MovieRow.jsx'
import Message from '../components/Message.jsx'

export default function MovieDetails() {
  const { id } = useParams()
  const { token, isLoggedIn } = useAuth()
  const navigate = useNavigate()
  const [movie, setMovie] = useState(null)
  const [similar, setSimilar] = useState([])
  const [inList, setInList] = useState(false)
  const [error, setError] = useState(null)
  const [busy, setBusy] = useState(false)

  useEffect(() => {
    setMovie(null)
    setError(null)
    api.getMovie(id)
      .then((m) => {
        setMovie(m)
        return api.getMovies({ genre: m.genre })
      })
      .then((list) => setSimilar(list.filter((m) => String(m.id) !== String(id))))
      .catch((e) => setError(e.status === 404 ? 'This movie does not exist.' : 'Could not load this movie.'))
    window.scrollTo(0, 0)
  }, [id])

  useEffect(() => {
    if (!token) return
    api.getMyList(token)
      .then((list) => setInList(list.some((m) => String(m.id) === String(id))))
      .catch(() => {})
  }, [id, token])

  async function toggleList() {
    if (!isLoggedIn) {
      navigate(`/login?next=/movies/${id}`)
      return
    }
    setBusy(true)
    try {
      const list = inList ? await api.removeFromMyList(id, token) : await api.addToMyList(id, token)
      setInList(list.some((m) => String(m.id) === String(id)))
    } finally {
      setBusy(false)
    }
  }

  if (error) return <div className="page"><Message tone="error">{error} <Link to="/">Go home</Link></Message></div>
  if (!movie) return <div className="page"><div className="spinner" aria-label="Loading" /></div>

  return (
    <div className="details">
      <div className="details-top">
        <div className="details-poster poster" style={posterStyle(movie)}>
          <span className="poster-genre">{movie.genre}</span>
          <span className="poster-title">{movie.title}</span>
        </div>
        <div className="details-info">
          <h1>{movie.title}</h1>
          <p className="hero-meta">
            <span className="rating">★ {movie.rating.toFixed(1)}</span>
            <span>{movie.releaseYear}</span>
            <span>{formatDuration(movie.durationMinutes)}</span>
            <span className="pill">{movie.genre}</span>
          </p>
          <p className="details-text">{movie.description}</p>
          <div className="actions">
            <Link to={`/watch/${movie.id}`} className="btn btn-primary">▶ Play</Link>
            <button className="btn btn-ghost" onClick={toggleList} disabled={busy}>
              {inList ? '✓ In My List' : '+ My List'}
            </button>
            <a className="btn btn-ghost" href={movie.trailerUrl} target="_blank" rel="noreferrer">
              Watch trailer ↗
            </a>
          </div>
        </div>
      </div>

      <MovieRow title="More like this" items={similar.map((m) => ({ movie: m }))} />
    </div>
  )
}
