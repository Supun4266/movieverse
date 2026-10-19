import { Link } from 'react-router-dom'
import { posterStyle, formatDuration } from '../poster.js'

export default function Hero({ movie, label }) {
  if (!movie) return null
  return (
    <section className="hero">
      <div className="hero-bg" style={posterStyle(movie)} />
      <div className="hero-content">
        <span className="hero-label">{label}</span>
        <h1>{movie.title}</h1>
        <p className="hero-meta">
          <span className="rating">★ {movie.rating.toFixed(1)}</span>
          <span>{movie.releaseYear}</span>
          <span>{formatDuration(movie.durationMinutes)}</span>
          <span className="pill">{movie.genre}</span>
        </p>
        <p className="hero-text">{movie.description}</p>
        <div className="actions">
          <Link to={`/watch/${movie.id}`} className="btn btn-primary">▶ Play</Link>
          <Link to={`/movies/${movie.id}`} className="btn btn-ghost">More info</Link>
        </div>
      </div>
    </section>
  )
}
