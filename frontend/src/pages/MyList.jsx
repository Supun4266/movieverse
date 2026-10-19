import { useEffect, useState } from 'react'
import { Link, Navigate } from 'react-router-dom'
import { api } from '../api.js'
import { useAuth } from '../AuthContext.jsx'
import MovieCard from '../components/MovieCard.jsx'
import Message from '../components/Message.jsx'

export default function MyList() {
  const { token, isLoggedIn } = useAuth()
  const [movies, setMovies] = useState(null)

  useEffect(() => {
    if (token) api.getMyList(token).then(setMovies).catch(() => setMovies([]))
  }, [token])

  if (!isLoggedIn) return <Navigate to="/login?next=/my-list" replace />
  if (!movies) return <div className="page"><div className="spinner" aria-label="Loading" /></div>

  return (
    <div className="page">
      <h1 className="page-title">My List</h1>
      {movies.length === 0 ? (
        <Message>Your list is empty. Open a movie and press <strong>+ My List</strong>. <Link to="/">Browse movies</Link></Message>
      ) : (
        <div className="grid">
          {movies.map((m) => <MovieCard key={m.id} movie={m} />)}
        </div>
      )}
    </div>
  )
}
