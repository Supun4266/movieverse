import { Link } from 'react-router-dom'
import { posterStyle } from '../poster.js'

export default function MovieCard({ movie, progress, rank }) {
  return (
    <Link to={`/movies/${movie.id}`} className="card" title={movie.title}>
      <div className="poster" style={posterStyle(movie)}>
        {rank && <span className="rank">{rank}</span>}
        <span className="poster-genre">{movie.genre}</span>
        <span className="poster-title">{movie.title}</span>
        {progress !== undefined && (
          <span className="progress" aria-label={`${progress}% watched`}>
            <span style={{ width: `${progress}%` }} />
          </span>
        )}
      </div>
      <div className="card-meta">
        <span>{movie.releaseYear}</span>
        <span className="rating">★ {movie.rating.toFixed(1)}</span>
      </div>
    </Link>
  )
}
