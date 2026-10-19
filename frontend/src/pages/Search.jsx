import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { api } from '../api.js'
import MovieCard from '../components/MovieCard.jsx'
import Message from '../components/Message.jsx'

export default function Search() {
  const [params] = useSearchParams()
  const q = params.get('q') || ''
  const [results, setResults] = useState(null)

  useEffect(() => {
    setResults(null)
    api.getMovies({ search: q }).then(setResults).catch(() => setResults([]))
  }, [q])

  return (
    <div className="page">
      <h1 className="page-title">Results for “{q}”</h1>
      {!results && <div className="spinner" aria-label="Loading" />}
      {results && results.length === 0 && <Message>No movies match “{q}”.</Message>}
      {results && results.length > 0 && (
        <div className="grid">
          {results.map((m) => <MovieCard key={m.id} movie={m} />)}
        </div>
      )}
    </div>
  )
}
