import { useEffect, useMemo, useState } from 'react'
import { api } from '../api.js'
import { useAuth } from '../AuthContext.jsx'
import Hero from '../components/Hero.jsx'
import MovieRow from '../components/MovieRow.jsx'
import Message from '../components/Message.jsx'

export default function Home() {
  const { token, signOut } = useAuth()
  const [movies, setMovies] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [trending, setTrending] = useState([])
  const [trendingOffline, setTrendingOffline] = useState(false)
  const [continueWatching, setContinueWatching] = useState([])
  const [myList, setMyList] = useState([])

  // Public data: all movies + trending
  useEffect(() => {
    api.getMovies()
      .then(setMovies)
      .catch(() => setError('Cannot reach the MovieVerse API on localhost:8080. Is the backend running?'))
      .finally(() => setLoading(false))

    api.getTrending()
      .then(setTrending)
      .catch(() => setTrendingOffline(true))
  }, [])

  // Personal data: only when logged in
  useEffect(() => {
    if (!token) {
      setContinueWatching([])
      setMyList([])
      return
    }
    Promise.all([api.getContinueWatching(token), api.getMyList(token)])
      .then(([cw, list]) => {
        setContinueWatching(cw)
        setMyList(list)
      })
      .catch((e) => {
        if (e.status === 401) signOut() // token expired
      })
  }, [token, signOut])

  const moviesById = useMemo(() => new Map(movies.map((m) => [m.id, m])), [movies])

  const trendingItems = trending
    .map((t, index) => ({ movie: moviesById.get(t.movieId), rank: index + 1 }))
    .filter((item) => item.movie)

  const genres = [...new Set(movies.map((m) => m.genre))]
  const featured = trendingItems[0]?.movie || movies[0]

  if (loading) return <div className="page"><div className="spinner" aria-label="Loading" /></div>
  if (error) return <div className="page"><Message tone="error">{error}</Message></div>

  return (
    <div className="home">
      <Hero movie={featured} label={trendingItems.length ? '#1 Trending in MovieVerse' : 'Top rated'} />

      <div className="rows">
        <MovieRow
          title="Continue Watching"
          items={continueWatching.map((c) => ({ movie: c.movie, progress: c.progressPercent }))}
        />

        {trendingItems.length > 0 ? (
          <MovieRow title="Trending Now" subtitle="Live from Kafka watch events" items={trendingItems} />
        ) : (
          <section className="row">
            <div className="row-head"><h2>Trending Now</h2></div>
            <Message>
              {trendingOffline
                ? 'The trending service (localhost:8081) is not running, so there is no Trending row yet.'
                : 'Nothing is trending yet. Press Play on a movie and it will show up here.'}
            </Message>
          </section>
        )}

        <MovieRow title="My List" items={myList.map((movie) => ({ movie }))} />

        {genres.map((genre) => (
          <MovieRow
            key={genre}
            title={genre}
            items={movies.filter((m) => m.genre === genre).map((movie) => ({ movie }))}
          />
        ))}
      </div>
    </div>
  )
}
