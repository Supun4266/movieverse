import MovieCard from './MovieCard.jsx'

// A horizontal, scrollable row of movie cards.
// items: [{ movie, progress?, rank? }]
export default function MovieRow({ title, subtitle, items }) {
  if (!items || items.length === 0) return null
  return (
    <section className="row">
      <div className="row-head">
        <h2>{title}</h2>
        {subtitle && <span className="row-sub">{subtitle}</span>}
      </div>
      <div className="row-scroll">
        {items.map((item) => (
          <MovieCard key={item.movie.id} movie={item.movie} progress={item.progress} rank={item.rank} />
        ))}
      </div>
    </section>
  )
}
