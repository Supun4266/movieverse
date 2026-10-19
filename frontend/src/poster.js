// We don't use real poster images (they are copyrighted), so each movie gets
// a colourful gradient "poster" based on its genre.
const GENRE_HUES = {
  'Sci-Fi': 200,
  Action: 12,
  Animation: 42,
  Drama: 268,
  Comedy: 150,
  Thriller: 330,
}

export function posterStyle(movie) {
  const base = GENRE_HUES[movie.genre] ?? 230
  const shift = ((movie.id * 37) % 40) - 20
  const hue = base + shift
  return {
    background: `radial-gradient(circle at 30% 20%, hsl(${hue} 85% 62% / 0.9), transparent 55%),
      linear-gradient(160deg, hsl(${hue} 70% 42%), hsl(${hue + 40} 65% 12%))`,
  }
}

export function formatDuration(minutes) {
  const h = Math.floor(minutes / 60)
  const m = minutes % 60
  return h > 0 ? `${h}h ${m}m` : `${m}m`
}
