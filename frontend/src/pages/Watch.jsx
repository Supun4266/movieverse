import { useCallback, useEffect, useRef, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { api } from '../api.js'
import { useAuth } from '../AuthContext.jsx'
import { posterStyle } from '../poster.js'
import Message from '../components/Message.jsx'

// A pretend video player. MovieVerse does not stream real movies, so the
// progress bar just moves forward. Progress is saved to the backend so the
// movie appears in "Continue Watching".
const STEP_PERCENT = 2
const STEP_MS = 1000

export default function Watch() {
  const { id } = useParams()
  const { token, isLoggedIn } = useAuth()
  const navigate = useNavigate()
  const [movie, setMovie] = useState(null)
  const [progress, setProgress] = useState(0)
  const [playing, setPlaying] = useState(false)
  const [error, setError] = useState(null)
  const progressRef = useRef(0)
  const lastSavedRef = useRef(0)
  const startedRef = useRef(null)

  // Must be logged in to watch
  useEffect(() => {
    if (!isLoggedIn) navigate(`/login?next=/watch/${id}`, { replace: true })
  }, [isLoggedIn, id, navigate])

  // Pressing Play: tell the backend (which sends the Kafka event)
  useEffect(() => {
    // The ref stops React's development double-run from sending two events
    if (!token || startedRef.current === id) return
    startedRef.current = id
    api.startWatching(id, token)
      .then((status) => {
        setMovie(status.movie)
        setProgress(status.progressPercent)
        progressRef.current = status.progressPercent
        lastSavedRef.current = status.progressPercent
        setPlaying(true)
      })
      .catch((e) => setError(e.status === 404 ? 'This movie does not exist.' : e.message))
  }, [id, token])

  const save = useCallback(() => {
    if (!token || progressRef.current === lastSavedRef.current) return
    lastSavedRef.current = progressRef.current
    api.saveProgress(id, progressRef.current, token).catch(() => {})
  }, [id, token])

  // Move the progress bar while playing
  useEffect(() => {
    if (!playing) return
    const timer = setInterval(() => {
      const next = Math.min(100, progressRef.current + STEP_PERCENT)
      progressRef.current = next
      setProgress(next)
      if (next - lastSavedRef.current >= 10 || next === 100) save()
      if (next === 100) setPlaying(false)
    }, STEP_MS)
    return () => clearInterval(timer)
  }, [playing, save])

  // Save when leaving the page
  useEffect(() => () => save(), [save])

  function togglePlay() {
    if (playing) save()
    if (!playing && progressRef.current >= 100) {
      progressRef.current = 0
      setProgress(0)
    }
    setPlaying(!playing)
  }

  if (error) return <div className="page"><Message tone="error">{error} <Link to="/">Go home</Link></Message></div>
  if (!movie) return <div className="page"><div className="spinner" aria-label="Loading" /></div>

  return (
    <div className="player">
      <div className="player-screen" style={posterStyle(movie)}>
        <div className="player-overlay">
          <Link to={`/movies/${movie.id}`} className="player-back">← Back</Link>
          <h1>{movie.title}</h1>
          <button className="player-play" onClick={togglePlay} aria-label={playing ? 'Pause' : 'Play'}>
            {playing ? '❚❚' : '▶'}
          </button>
          <div className="player-bar">
            <div className="progress progress-large"><span style={{ width: `${progress}%` }} /></div>
            <span>{progress}%</span>
          </div>
        </div>
      </div>
      <p className="player-note">
        Demo player: MovieVerse does not stream real movies. Your progress is saved for
        <strong> Continue Watching</strong>, and pressing Play sent a <strong>movie-watched</strong> event to Kafka
        for the <strong>Trending Now</strong> row.
      </p>
    </div>
  )
}
